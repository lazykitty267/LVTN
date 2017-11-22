package bk.lvtn;

/**
 * Created by Long on 31/10/2017.
 */

import android.*;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bk.lvtn.Signature.DigitalSignature;
import dataService.DataService;
import dataService.DatabaseConnection;
import dataService.OfflineDataService;
import entity.AttachImage;
import entity.PdfFile;
import entity.Report;
import entity.User;


public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if ((ContextCompat.checkSelfPermission(LoginActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(LoginActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = (Button) findViewById(R.id.btn_login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                DatabaseConnection databaseConnection = new DatabaseConnection();
                //authenticate user
                progressBar.setVisibility(View.GONE);
                DatabaseReference databaseReference = databaseConnection.connectUserDatabase();
                databaseReference.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User tempuser = dataSnapshot.getValue(User.class);
                        checkLogin(password, tempuser);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void checkLogin(String password, User user) {
        if (user == null) {
            // there was an error
            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

        } else {
            if (!password.equals(user.getPassword())) {
                if (password.length() < 6) {
                    inputPassword.setError(getString(R.string.minimum_password));
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                }
            } else {
                SharedPreferences loginInfo = getSharedPreferences("Login", 0);
                final SharedPreferences.Editor editor = loginInfo.edit();
                editor.putString("managername", user.getManagerName());
                editor.putString("username", user.getUsername());
                editor.putString("password", user.getPassword());
                editor.putString("publickey", user.getPublicKey());
                editor.putString("name", user.getName());
                editor.commit();
                final File keyFileDirectory = new File(getFilesDir(), "rsa/");
                final File privateKeyFile = new File(keyFileDirectory, user.getUsername() + "_priv_key");
                if (privateKeyFile.exists()) {
                    try {
                        OfflineDataService offData = new OfflineDataService();
                        offData.doOpenDb(LoginActivity.this);
                        AlertDialog uploadAlert = new AlertDialog.Builder(LoginActivity.this).setTitle("Success")
                                .setMessage("Upload báo cáo được tạo lúc bạn ngoại tuyến!!")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        try {
                                            uploadOfffile();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } catch (Exception e) {
                                            Log.d("aaa", e.toString());
                                        }
                                    }
                                }).show();
                    } catch (Exception e) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Log.d("aaa", e.toString());
                    }
                } else {
                    final String userName = user.getUsername();
                    AlertDialog alertbox = new AlertDialog.Builder(LoginActivity.this).setTitle("Success")
                            .setMessage("Private key đã được tạo")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    try {
                                        DigitalSignature digi = new DigitalSignature();
                                        digi.generateKey(LoginActivity.this,userName);
//                                        editor.putString("private key", digi.rk.toString());
//
//                                        File secondFile = new File(getFilesDir().getAbsolutePath() + "/", "privatekey");
//                                        secondFile.createNewFile();
//                                        FileOutputStream fos = new FileOutputStream(secondFile);
//                                        fos.write(digi.rk.toString().getBytes());
//                                        fos.flush();
//                                        fos.close();
                                        final File publicKeyFile = new File(keyFileDirectory, "sikkr_pub_key");
                                        DataService dataService = new DataService();
                                        dataService.savePublicKey(publicKeyFile,userName);

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } catch (Exception e) {
                                        Log.d("aaa", e.toString());
                                    }
                                }
                            }).show();
                }
            }
        }
    }

    public void uploadOfffile() throws Exception{
        OfflineDataService offData = new OfflineDataService();
        offData.doOpenDb(LoginActivity.this);
        List<Report> listReport = offData.loadReport();
        DataService dataService = new DataService();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        for (Report report : listReport) {
            File userDir = new File(path,report.getId());
            PdfFile pdfFile = new PdfFile();
            pdfFile.setName(report.getId());
            File file = new File(userDir,report.getId()+".pdf");
            File attachDir = new File(userDir,report.getId());
//            User user = dataService.getCurrentUser(LoginActivity.this);
//            report.setUserName(user.getUsername());
            if(report.getNote().equals(OfflineDataService.CREATE_MODE))
                dataService.saveReport(report);
            else dataService.updateReport(report);
            if (attachDir.listFiles() != null) {
                for (File f : attachDir.listFiles()) {
                    AttachImage attachImage = new AttachImage();
                    attachImage.setReportId(report.getId());
                    attachImage.setName(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                    dataService.uploadAttachFile(f, attachImage);
                }
            }
            final DigitalSignature digi = new DigitalSignature();
            final File keyFileDirectory = new File(getFilesDir(), "rsa/");
            final File privateKeyFile = new File(keyFileDirectory, report.getUserName() + "_priv_key");
            final File publicKeyFile = new File(keyFileDirectory, "sikkr_pub_key");
            byte[] b = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            String k = digi.myhash(b);
            byte[] s = digi.rsaSign(k.getBytes(), digi.getPrivateKey(privateKeyFile));
            final File signal = new File(getFilesDir(), "signal");
            signal.createNewFile();
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(signal));
            dos.write(s);
            dos.flush();
            dos.close();
            dataService.saveSignature(signal,report.getId());
            signal.delete();

            pdfFile.setReportId(report.getId());
            dataService.uploadFile(file, pdfFile);
            
            offData.doDeleteDB(LoginActivity.this);
//            Toast.makeText(LoginActivity.this, "OFFLINE MODE", Toast.LENGTH_SHORT);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                break;
        }
    }
//    public boolean isFilePresent(String fileName) {
//        String path = getFilesDir().getAbsolutePath() + "/" + fileName;
//        File file = new File(path);
//        return file.exists();
//    }
//    public String getvk(String filename) throws Exception{
//        File secondInputFile = new File(getFilesDir().getAbsolutePath() + "/", filename);
//        InputStream secondInputStream = new BufferedInputStream(new FileInputStream(secondInputFile));
//        BufferedReader r = new BufferedReader(new InputStreamReader(secondInputStream));
//        StringBuilder total = new StringBuilder();
//        String line;
//        while ((line = r.readLine()) != null) {
//            total.append(line);
//        }
//        r.close();
//        secondInputStream.close();
//        return total.toString();
//    }
}