package dataService;

import android.app.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import entity.Contants;

/**
 * Created by lazyk on 10/3/2017.
 */

public class DatabaseConnection {

    public DatabaseReference connectReportDatabase () {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Contants.REPORT_PATH);
        return  databaseReference;
    }
    public StorageReference connectPdfFileDatabase () {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(Contants.PDF_FILE_PATH);
        return storageReference;
    }
    public DatabaseReference connectPdfDatabase () {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Contants.PDF_PATH);
        return databaseReference;
    }
    public DatabaseReference connectUserDatabase () {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Contants.USERS_PATH);
        return  databaseReference;
    }
    public DatabaseReference connectAttachDatabase () {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Contants.IMAGE_PATH);
        return databaseReference;
    }
    public StorageReference connectAttachFileDatabase () {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(Contants.IMAGE_FILE_PATH
        );
        return storageReference;
    }
}
