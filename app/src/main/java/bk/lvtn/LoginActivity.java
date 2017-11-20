package bk.lvtn;

/**
 * Created by Long on 31/10/2017.
 */
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.itextpdf.text.pdf.StringUtils;

import org.apache.poi.util.StringUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import bk.lvtn.Signature.DigitalSignature;
import dataService.DataService;
import dataService.DatabaseConnection;
import entity.User;


public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

//    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

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
            final File keyFileDirectory = new File(getFilesDir(), "rsa/");
            final File privateKeyFile = new File(keyFileDirectory, "sikkr_priv_key");
            if (privateKeyFile.exists()) {
                try {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.d("aaa", e.toString());
                }
            } else {
                try {
                    DigitalSignature digi = new DigitalSignature();
                    digi.generateKey(LoginActivity.this);
//                                        editor.putString("private key", digi.rk.toString());
//
//                                        File secondFile = new File(getFilesDir().getAbsolutePath() + "/", "privatekey");
//                                        secondFile.createNewFile();
//                                        FileOutputStream fos = new FileOutputStream(secondFile);
//                                        fos.write(digi.rk.toString().getBytes());
//                                        fos.flush();
//                                        fos.close();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    Log.d("aaa", e.toString());
                }
            }
        }
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