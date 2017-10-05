package bk.lvtn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;

import bk.lvtn.component.ReportHandle;

public class FieldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template1_layout);
        EditText a = (EditText) findViewById(R.id.company_name_input);
//        Animation scaleAnimation = new ScaleAnimation(0, 1, 1, 1);
//        scaleAnimation.setDuration(750);
//        a.startAnimation(scaleAnimation);

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
