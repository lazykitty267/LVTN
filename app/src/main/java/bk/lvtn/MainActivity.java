package bk.lvtn;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import bk.lvtn.component.ExcelHandle;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pre = getSharedPreferences
                ("my_data", MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor = pre.edit();
        boolean isLogin = true;
        //lưu vào editor
        editor.putBoolean("isLogin", isLogin);
        //chấp nhận lưu xuống file
        editor.commit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if ((ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
//        Button aa = (Button) findViewById(R.id.button);
//        aa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent=new Intent(MainActivity.this, FieldActivity.class);
//                startActivity(myIntent);
//            }
//        });
//        Button bb = (Button) findViewById(R.id.read_excel);
//        bb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                test();
//            }
//        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment1 = new MainFragment();
        fragmentTransaction.replace(R.id.content,fragment1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Bạn có muốn thoát?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_report) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment1 = new MainFragment();
            fragmentTransaction.replace(R.id.content,fragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_template) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment1 = new TemplateActivity();
            fragmentTransaction.replace(R.id.content,fragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_note) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment1 = new NoteActivity();
            fragmentTransaction.replace(R.id.content,fragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_manager) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment1 = new ManagerActivity();
            fragmentTransaction.replace(R.id.content,fragment1);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setMessage("Bạn có muốn thoát?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Có",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences sharedpreferences = getSharedPreferences("my_data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent( MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                            dialog.cancel();
                        }
                    });

            builder1.setNegativeButton(
                    "Không",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
