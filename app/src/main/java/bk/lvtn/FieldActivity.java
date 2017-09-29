package bk.lvtn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;

public class FieldActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template1_layout);
        EditText a = (EditText) findViewById(R.id.company_name_input);
        Animation scaleAnimation = new ScaleAnimation(0, 1, 1, 1);
        scaleAnimation.setDuration(750);
        a.startAnimation(scaleAnimation);
    }
}
