package dataService;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bk.lvtn.Signature.DigitalSignature;
import entity.AttachImage;
import entity.Note;
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
     *
     * @param file    file được upload lên server
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
                //noinspection VisibleForTests
                pdfFile.setUrl(taskSnapshot.getDownloadUrl().toString());
                updatePdf(pdfFile);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /**
     * download file pdf từ server
     *
     * @param pdfFile Thông tin file được download
     * @return file pdf
     */
    public File downloadFile(@NonNull final PdfFile pdfFile,String reportName) {
        try {
            StorageReference storageReference = databaseConnection.connectPdfFileDatabase();
            File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString(), "pdf_download");
            if (!storageDir.exists()) {
                storageDir.mkdir();
            }
            File localFIle = File.createTempFile(reportName+"_"+getCurdateTimeDifType(), ".pdf",storageDir);
            storageReference.child(pdfFile.getId() + "_" + pdfFile.getName() + ".pdf").getFile(localFIle);
            return localFIle;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lưu thông tin report lên server
     *
     * @param report Thông tin report
     * @return Lưu có thành công hay không
     */
    public boolean saveReport(@NonNull final Report report) {
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        String id = databaseReference.push().getKey();
        report.setId(id);
        report.setCreateDate(getCurdateTime());
        return databaseReference.child(report.getUserName()).child(id).setValue(report).isSuccessful();
    }

    /**
     * Cập nhật thông tin report
     *
     * @param report thông tin report sau khi sửa
     * @return lưu có thành công hay không
     */
    public boolean updateReport(Report report) {
        PdfFile file = getPdf(report.getId());
        deletePdf(file);
        report.setUpdateDate(getCurdateTime());
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        return databaseReference.child(report.getUserName()).child(report.getId()).setValue(report).isSuccessful();
    }

    /**
     * Lưu thông tin file pdf được tạo ra
     *
     * @param pdfFile thông tin file pdf
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
     *
     * @param pdfFile Thông tin file pdf cần cập nhật
     * @return cập nhật thành công hay không
     */
    public boolean updatePdf(@NonNull final PdfFile pdfFile) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        pdfFile.setUpdateDate(getCurdateTime());
        return databaseReference.child(pdfFile.getReportId()).setValue(pdfFile).isSuccessful();
    }

//    /**
//     * lấy thông tin tất cả report của user
//     *
//     * @param userId id của user
//     * @return List các thông tin report
//     */
//    public List<Report> getAllReport(@NonNull final String userId) {
//        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
//        final List<Report> reportList = new ArrayList<>();
//        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Report report = postSnapshot.getValue(Report.class);
//                    reportList.add(report);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return reportList;
//    }

    /**
     * Lấy thông tin của report trên server theo id
     *
     * @param id id của report
     * @return Thông tin report cần tìm
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
     *
     * @param id id report cần xóa
     * @return Xóa thành công hay không
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
     *
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
     *
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
     *
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
     *
     * @param file       file ảnh được upload lên server
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
                //noinspection VisibleForTests
                attachFile.setUrl(taskSnapshot.getDownloadUrl().toString());
                updateAttachFile(attachFile);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void savePublicKey(@NonNull final File file, @NonNull final String username, @NonNull final Context context) {
        Uri data = Uri.fromFile(file);
        StorageReference storageReference = databaseConnection.connectPublicKeyDatabase();
        final StorageReference sRef = storageReference.child(username + ".publicKey");
        sRef.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisitableForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = taskSnapshot.getDownloadUrl().toString();
                //TODO: Lưu vào file
                final File keyFileParent = new File(context.getFilesDir(),  "rsa/");
                final File keyFileDirectory = new File(keyFileParent,  username + "/");
                try {
                    keyFileDirectory.mkdir();
                    final File URL = new File(keyFileDirectory, url);
                    URL.mkdirs();
                    // Luu pubkey dang hex
//                    DigitalSignature digitalSignature = new DigitalSignature();
//                    String hexName = digitalSignature.fileToHex(file);
//                    final File hexPubkey = new File(keyFileDirectory, hexName);
//                    hexPubkey.mkdirs();
                }
                catch (Exception e){

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }



    public void saveSignature(@NonNull final File file, @NonNull final PdfFile pdfFile) {
        Uri data = Uri.fromFile(file);
        StorageReference storageReference = databaseConnection.connectSignatureDatabase();
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
        final StorageReference sRef = storageReference.child(timeStamp + ".signature");
        sRef.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisitableForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pdfFile.setSignUrl(taskSnapshot.getDownloadUrl().toString());
//                DigitalSignature digitalSignature = new DigitalSignature();
//                try {
//                    String hexName = digitalSignature.fileToHex(file);
//                    pdfFile.setSignUrl(hexName);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
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
        String id = databaseReference.push().getKey();
        attachImage.setId(id);
        return databaseReference.child(attachImage.getReportId()).child(id).setValue(attachImage).isSuccessful();
    }

    public boolean updateAttachFile(AttachImage attachImage) {
        DatabaseReference databaseReference = databaseConnection.connectAttachDatabase();
        attachImage.setUpdateDate(getCurdateTime());
        return databaseReference.child(attachImage.getReportId()).child(attachImage.getId()).setValue(attachImage).isSuccessful();
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
     *
     * @param reportId
     * @return List thông tin file đính kèm
     */
    public List<AttachImage> getAllAttach(String reportId) {
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
     *
     * @param username username
     * @return thông tin user
     */
    public User getUser(final String username) {
        DatabaseReference databaseReference = databaseConnection.connectUserDatabase();
        final User[] user = new User[1];
        databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User tempuser = dataSnapshot.getValue(User.class);
                user[0] = tempuser;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return user[0];
    }

    /**
     * lấy thông tin user đang đăng nhập
     *
     * @param context
     * @return thông tin user
     */
    public User getCurrentUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Login", 0);
        User user = new User();
        user.setName(sharedPreferences.getString("name", null));
        user.setManagerName(sharedPreferences.getString("managername", null));
        user.setPassword(sharedPreferences.getString("password", null));
        user.setUsername(sharedPreferences.getString("username", null));
        user.setPublicKey(sharedPreferences.getString("publickey", null));
        return user;
    }

    public String getCurdateTime() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }
    public String getCurdateTimeDifType() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * Lưu thông tin note lên server
     *
     * @param note Thông tin note
     * @return Lưu có thành công hay không
     */

    public boolean saveNote(@NonNull final Note note) {
        DatabaseReference databaseReference = databaseConnection.connectNoteDatabase();
        String id = databaseReference.push().getKey();
        note.setId(id);
        return databaseReference.child(note.getUserName()).child(id).setValue(note).isSuccessful();
    }

    /**
     * Cập nhật thông tin note
     *
     * @param note thông tin note sau khi sửa
     * @return lưu có thành công hay không
     */
    public boolean updateNote(Note note) {
        DatabaseReference databaseReference = databaseConnection.connectNoteDatabase();
        return databaseReference.child(note.getUserName()).child(note.getId()).setValue(note).isSuccessful();
    }
}
