package bk.lvtn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.jar.Manifest;

import bk.lvtn.component.ReportHandle;
import bk.lvtn.form.Form;

public class FieldActivity extends AppCompatActivity {
    Button saveForm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template1_layout);
        EditText a = (EditText) findViewById(R.id.company_name_input);
        saveForm = (Button) findViewById(R.id.save_report_button);
        saveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Form form = new Form();
                ReportHandle report = new ReportHandle();
                report.addValue("Địa điểm",new String[] {"Viettel"});
                report.addValue("Thời gian bắt đầu",new String[]{new Date().toString()});
                report.addValue("Thành phần tham dự",new String[] {"phu","long","nghia"});
                report.addValue("Chủ trì",new String[] {"aaaaaaaaaabbbbbbbbbbbbbbbb"});
                form.getData(report);
                try {
                    // lưu form thành pdf
                    // cần test file pdf có lưu trong dir : getFilesDir().getAbsolutePath()
                    // ko
                    form.createForm1(getFilesDir().getAbsolutePath().toString());
                }
                catch (Exception e){

                }
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnToMainAc();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void returnToMainAc(){
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Báo cáo sẽ không được lưu, bạn có muốn thoát?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }
}
