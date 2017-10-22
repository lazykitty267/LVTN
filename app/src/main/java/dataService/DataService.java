package dataService;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import entity.PdfFile;
import entity.Report;

/**
 * Created by lazyk on 10/3/2017.
 */

public class DataService {
    private DatabaseConnection databaseConnection;

    /**
     * @param data
     * @param pdfFile có id băng với id của Report
     */
    public void uploadFile(Uri data, final PdfFile pdfFile) {
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

    public Uri downloadFile(PdfFile pdfFile) {
        return Uri.parse(pdfFile.getUrl());
    }

    public boolean saveReport(Report report) {
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        String id = databaseReference.push().getKey();
        report.setId(id);
        return databaseReference.child(id).setValue(report).isSuccessful();
    }

    public boolean updateReport(Report report) {
        PdfFile file = getPdf(report.getId());
        deletePdf(file);
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        return databaseReference.child(report.getId()).setValue(report).isSuccessful();
    }

    private boolean savePdf(final PdfFile pdfFile) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        pdfFile.setId(pdfFile.getId());
        return databaseReference.child(pdfFile.getId()).setValue(pdfFile).isSuccessful();
    }

    private boolean updatePdf(PdfFile pdfFile) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        return databaseReference.child(pdfFile.getId()).setValue(pdfFile).isSuccessful();
    }

    public List<Report> getAllReport() {
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        final List<Report> reportList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
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

    public boolean deleteReport(String id) {
        PdfFile file = getPdf(id);
        deletePdf(file);
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        databaseReference.child(id).removeValue();
        return true;
    }

    public List<PdfFile> getAllPdf() {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        final List<PdfFile> pdfList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
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

    public PdfFile getPdf(@NonNull final String id) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        final List<PdfFile> pdfFileList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PdfFile pdfFile = dataSnapshot.child(id).getValue(PdfFile.class);
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

    public boolean deletePdf(PdfFile pdfFile) {
        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
        databaseReference.child(pdfFile.getId()).removeValue();
        StorageReference storageReference = databaseConnection.connectPdfFileDatabase();
        storageReference.child(pdfFile.getId() + "_" + pdfFile.getName() + ".pdf").delete();
        return true;
    }
}
