package dataService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bk.lvtn.LoginActivity;
import bk.lvtn.LoginActivityAsyncTask;
import bk.lvtn.R;
import bk.lvtn.fragment_adapter.OnGetDataListener;
import entity.AttachImage;
import entity.PdfFile;
import entity.Report;
import entity.User;

/**
 * Created by lazyk on 10/3/2017.
 */
public class DataService {
    private DatabaseConnection databaseConnection = new DatabaseConnection();

    /**
     * Upload pdf file lên server
     * @param file file được upload lên server
     * @param pdfFile thông tin file upload lên server
     */
    public void uploadFile(@NonNull final File file, @NonNull final PdfFile pdfFile) {
        Uri data = Uri.fromFile(file);
        StorageReference storageReference = databaseConnection.connectPdfFileDatabase();
        savePdf(pdfFile);
        final StorageReference sRef = storageReference.child(pdfFile.getId() + "_" + pdfFile.getName() + ".pdf");
        sRef.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisitableForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                PdfFile file = new PdfFile(pdfFile.getId(), pdfFile.getName());
                //noinspection VisibleForTests
                file.setUrl(taskSnapshot.getDownloadUrl().toString());
                updatePdf(file);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     *  download file pdf từ server
     * @param pdfFile   Thông tin file được download
     * @return file pdf
     */
    public File downloadFile(@NonNull final PdfFile pdfFile) {
        try {
            StorageReference storageReference = databaseConnection.connectPdfFileDatabase();
            File localFIle = File.createTempFile(pdfFile.getId()+"_"+pdfFile.getName(),"pdf");
            storageReference.child(pdfFile.getId()+"_"+pdfFile.getName()+".pdf").getFile(localFIle);
            return localFIle;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Lưu thông tin report lên server
     * @param report    Thông tin report
     * @return  Lưu có thành công hay không
     */
    public boolean saveReport(@NonNull final Report report) {
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        String id = databaseReference.push().getKey();
        report.setId(id);
        report.setCreateDate(getCurdateTime());
        return databaseReference.child(id).setValue(report).isSuccessful();
    }

    /**
     * Cập nhật thông tin report
     * @param report    thông tin report sau khi sửa
     * @return  lưu có thành công hay không
     */
    public boolean updateReport(Report report) {
        PdfFile file = getPdf(report.getId());
        deletePdf(file);
        report.setUpdateDate(getCurdateTime());
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        return databaseReference.child(report.getId()).setValue(report).isSuccessful();
    }

    /**
     *  Lưu thông tin file pdf được tạo ra
     * @param pdfFile   thông tin file pdf
     * @return lưu có thành công hay không
     */
    private boolean savePdf(@NonNull final PdfFile pdfFile) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        pdfFile.setId(databaseReference.push().getKey());
        pdfFile.setCreateDate(getCurdateTime());
        return databaseReference.child(pdfFile.getReportId()).setValue(pdfFile).isSuccessful();
    }

    /**
     * Cập nhật thông tin file pdf
     * @param pdfFile   Thông tin file pdf cần cập nhật
     * @return  cập nhật thành công hay không
     */
    public boolean updatePdf(@NonNull final PdfFile pdfFile) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        pdfFile.setUpdateDate(getCurdateTime());
        return databaseReference.child(pdfFile.getReportId()).setValue(pdfFile).isSuccessful();
    }

    /**
     * lấy thông tin tất cả report của user
     * @param userId    id của user
     * @return List các thông tin report
     */
    public List<Report> getAllReport(@NonNull final String userId) {
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        final List<Report> reportList = new ArrayList<>();
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    reportList.add(report);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return reportList;
    }

    /**
     * Lấy thông tin của report trên server theo id
     * @param id    id của report
     * @return  Thông tin report cần tìm
     */
    public Report getReport(@NonNull final String id) {
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        final List<Report> reportList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Report report = dataSnapshot.child(id).getValue(Report.class);
                reportList.add(report);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (reportList.size() == 0) {
            return null;
        }
        return reportList.get(0);
    }

    /**
     * Xóa thông tin report
     * @param id    id report cần xóa
     * @return  Xóa thành công hay không
     */
    public boolean deleteReport(@NonNull final String id) {
        PdfFile file = getPdf(id);
        List<AttachImage> attachImageList = getAllAttach(id);
        deletePdf(file);
        for (int index = 0; index < attachImageList.size(); index++) {
            deleteAttach(attachImageList.get(index));
        }
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        databaseReference.child(id).removeValue();
        return true;
    }

    /**
     * Lấy tất cả file pdf của report
     * @param reportId
     * @return lấy thành công hay không
     */
    public List<PdfFile> getAllPdf(@NonNull final String reportId) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        final List<PdfFile> pdfList = new ArrayList<>();
        databaseReference.child(reportId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PdfFile pdfFile = postSnapshot.getValue(PdfFile.class);
                    pdfList.add(pdfFile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return pdfList;
    }

    /**
     * Lấy file pdf từ server
     * @param reportId
     * @return file pdf
     */
    public PdfFile getPdf(@NonNull final String reportId) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        final List<PdfFile> pdfFileList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PdfFile pdfFile = dataSnapshot.child(reportId).getValue(PdfFile.class);
                pdfFileList.add(pdfFile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (pdfFileList.size() == 0) {
            return null;
        }
        return pdfFileList.get(0);
    }

    /**
     * Xóa file pdf trên server
     * @param pdfFile
     * @return xóa thành công hay không
     */
    public boolean deletePdf(@NonNull final PdfFile pdfFile) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        databaseReference.child(pdfFile.getReportId()).removeValue();
        StorageReference storageReference = databaseConnection.connectPdfFileDatabase();
        storageReference.child(pdfFile.getId() + "_" + pdfFile.getName() + ".pdf").delete();
        return true;
    }

    /**
     * uplaod ảnh đính kèm lên server
     * @param file file ảnh được upload lên server
     * @param attachFile thông tin file đính kèm
     */
    public void uploadAttachFile(@NonNull final File file, @NonNull final AttachImage attachFile) {
        Uri data = Uri.fromFile(file);
        StorageReference storageReference = databaseConnection.connectAttachFileDatabase();
        saveAttachFile(attachFile);
        final StorageReference sRef = storageReference.child(attachFile.getId() + "_" + attachFile.getName() + ".jpg");
        sRef.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisitableForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                AttachImage file = new AttachImage(attachFile.getReportId(), attachFile.getId(), attachFile.getName());
                //noinspection VisibleForTests
                file.setUrl(taskSnapshot.getDownloadUrl().toString());
                updateAttachFile(file);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public boolean saveAttachFile(@NonNull final AttachImage attachImage) {
        DatabaseReference databaseReference = databaseConnection.connectAttachDatabase();
        attachImage.setCreateDate(getCurdateTime());
        attachImage.setId(databaseReference.push().getKey());
        return databaseReference.child(attachImage.getReportId()).setValue(attachImage).isSuccessful();
    }

    public boolean updateAttachFile(AttachImage attachImage) {
        DatabaseReference databaseReference = databaseConnection.connectAttachDatabase();
        attachImage.setUpdateDate(getCurdateTime());
        return databaseReference.child(attachImage.getReportId()).setValue(attachImage).isSuccessful();
    }

    public boolean deleteAttach(AttachImage attachImage) {
        DatabaseReference databaseReference = databaseConnection.connectAttachDatabase();
        databaseReference.child(attachImage.getReportId()).removeValue();
        StorageReference storageReference = databaseConnection.connectPdfFileDatabase();
        storageReference.child(attachImage.getId() + "_" + attachImage.getName() + ".pdf").delete();
        return true;
    }

    /**
     * Lấy tất cả thông tin của file đính kèm theo report
     * @param reportId
     * @return  List thông tin file đính kèm
     */
    public  List<AttachImage> getAllAttach(String reportId) {
        DatabaseReference databaseReference = databaseConnection.connectAttachDatabase();
        final List<AttachImage> attachImageList = new ArrayList<>();
        databaseReference.child(reportId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AttachImage attachImage = postSnapshot.getValue(AttachImage.class);
                    attachImageList.add(attachImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return attachImageList;
    }

    /**
     * Lấy thôn gtin user trên server theo id
     * @param id    id user
     * @return thông tin user
     */
    public User getUser(final String id) {
        DatabaseReference databaseReference = databaseConnection.connectUserDatabase();
        final User[] user = new User[1];
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User tempuser = dataSnapshot.child(id).getValue(User.class);
                user[0] = tempuser;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return user[0];
    }

    /**
     * kiẻm tra đăng nhập
     * @param username
     * @param password
     * @return
     */
    public User checkLoginInfo(@NonNull final String username, @NonNull final String password) {
        DatabaseReference reference = databaseConnection.connectUserDatabase();
        DatabaseReference ref =  reference.orderByChild("username").equalTo(username).getRef();

        final User[] user = new User[1];
        if (ref.child("password").equals(password)) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User tempuser = dataSnapshot.getValue(User.class);
                    user[0] = tempuser;

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return  user[0];
    }
    public User mCheckInforInServer(String child, final Activity contextParent, final String username, final String password) {
        final User[] user = new User[1];
        user[0]= null;
        new DataService().mReadDataOnce(child, new OnGetDataListener() {
            @Override
            public void onStart() {

                //DO SOME THING WHEN START GET DATA HERE


            }

            @Override
            public void onSuccess(DataSnapshot data) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE
                User abc = data.getValue(User.class);
//                user[0] = tempuser;
                user[0]= new User();
                user[0] =abc;
                if(abc!=null) {
                    if (abc.getPassword().equals(password)) {
                        LoginActivityAsyncTask loginActivityAsyncTask;
                        loginActivityAsyncTask = new LoginActivityAsyncTask(contextParent, username, password, user[0]);
                        loginActivityAsyncTask.execute();
                    } else {
                        Toast.makeText(contextParent, contextParent.getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(contextParent, contextParent.getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });
        return user[0];
    }
    public void mReadDataOnce(String child, final OnGetDataListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference("Users/").child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }

    /**
     * lấy thông tin user đang đăng nhập
     * @param context
     * @return thông tin user
     */
    public User getCurrentUser (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login", 0);
        User user = new User("","","","");
        user.setId(sharedPreferences.getString("id", null));
        user.setName(sharedPreferences.getString("name", null));
        user.setManagerId(sharedPreferences.getString("managerid", null));
        user.setPassword(sharedPreferences.getString("password", null));
        user.setUsername(sharedPreferences.getString("username", null));
        user.setPublicKey(sharedPreferences.getString("publickey", null));
        return user;
    }

    private String getCurdateTime() {
       return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }
}
