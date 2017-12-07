package bk.lvtn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;


import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bk.lvtn.Signature.DigitalSignature;
import bk.lvtn.data.DataRow;
import bk.lvtn.form.Form;
import bk.lvtn.fragment_adapter.AttachImages;
import bk.lvtn.fragment_adapter.AttachImgAdapter;
import bk.lvtn.fragment_adapter.Field;
import bk.lvtn.fragment_adapter.FieldAdapter;
import bk.lvtn.fragment_adapter.Template;
import bk.lvtn.fragment_adapter.TemplateAdapter;
import dataService.DataService;
import dataService.DatabaseConnection;
import dataService.OfflineDataService;
import entity.AttachImage;
import entity.PdfFile;
import entity.Report;


public class ModifyReportActivity extends AppCompatActivity {
    Bitmap theBitmap;
    FieldActivityAsyncTask fieldActivityAsyncTask;
    ListView listField;
    ArrayList<Field> arrField = new ArrayList<Field>();
    FieldAdapter adapter, adapterAdd = null;
    String excel_name = "";
    Form form;
    String[] fileList = null;
    RecyclerView mRecyclerView;
    AttachImgAdapter mRcvAdapter;
    Dialog dialog;

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_report_detail);
        Intent intent = getIntent();
        final Report curreport = (Report) intent.getExtras().getSerializable("CURREPORT");
        ArrayList<DataRow> fieldsData = new ArrayList<DataRow>(curreport.getFieldList());
        DataService dataService = new DataService();
        // listImgAttached trả về null
        DatabaseConnection databaseConnection = new DatabaseConnection();
        DatabaseReference databaseReference = databaseConnection.connectAttachDatabase();
//

        // todo : convert AttachImage to AttachImages
        Button saveForm = (Button) findViewById(R.id.save_button);
        Button addField = (Button) findViewById(R.id.add_button);
        Button attachFile = (Button) findViewById(R.id.add_attachimg_button);
        listField = (ListView) findViewById(R.id.list_field);
        adapter = new FieldAdapter(this, arrField, R.layout.item_inlist_field);
        listField.setAdapter(adapter);
        for (DataRow item : fieldsData) {
            Field tmp = new Field(item.getKey(), item.getValue().get(0));
            arrField.add(tmp);
        }
        adapter.notifyDataSetChanged();

        final DigitalSignature digi = new DigitalSignature();
//        try {
//
//            digi.generateKey(this);
//
//        } catch (Exception e1) {
//
//            // TODO Auto-generated catch block
//
//            e1.printStackTrace();
//
//        }

        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ModifyReportActivity.this);
                dialog.setContentView(R.layout.list_template_dialog);
                GridView lv = (GridView) dialog.findViewById(R.id.list_template_d);
                ArrayList<Field> arrTp = new ArrayList<Field>();

                adapterAdd = new FieldAdapter(ModifyReportActivity.this, arrTp, R.layout.item_inlist_field);
                lv.setAdapter(adapterAdd);

                final Field field1 = new Field("Tên Field");
                arrTp.add(field1);
                adapterAdd.notifyDataSetChanged();

                dialog.setCancelable(true);
                dialog.setTitle("ListView");
                dialog.show();
                Button rp_select = (Button) dialog.findViewById(R.id.rp_select);
                rp_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (field1.getValue_field().equals("")) {
                            Toast.makeText(ModifyReportActivity.this, "Mời nhập field name!!", Toast.LENGTH_LONG);
                            return;
                        } else {
                            Field f = new Field(field1.getValue_field());
                            arrField.add(f);
                            adapterAdd.notifyDataSetChanged();
                            adapterAdd = null;
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
//        attachFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ContextCompat.checkSelfPermission(ModifyReportActivity.this, android.Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_GRANTED) {
//                    //TODO: Do somethings
//                } else {
//                    //Request camera permission
//                    ActivityCompat.requestPermissions(ModifyReportActivity.this, new String[]{android.Manifest.permission.CAMERA},
//                            1);
//                }
////                getAttachFile();
//                AttachImageService a = new AttachImageService(ModifyReportActivity.this, getApplication());
//                a.takePicture();
//            }
//        });
        // // TODO: 11/20/17 update curreport on database
        saveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    curreport.getFieldList().clear();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        final String s = adapter.getItem(i).getValue_field();
                        curreport.addValue(adapter.getItem(i).getKey_field(), new ArrayList<String>() {{
                            add(s);
                        }});
                    }
                    form = new Form(curreport);
                    try {

                        ActivityCompat.requestPermissions(ModifyReportActivity.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        InputStream is = getAssets().open("vuArial.ttf");

                        PdfFile pdfFile = new PdfFile();

                        File file = form.createForm1(Environment.getExternalStorageDirectory().getAbsolutePath().toString(), is, pdfFile);
                        if (file == null) {
                            return;
                        }
//                        if (!isOnline()) {
//                            OfflineDataService offData = new OfflineDataService();
//                            offData.doCreateDb(ModifyReportActivity.this);
//                            offData.doInsertReport(curreport);
//
//                            byte[] b = new byte[(int) file.length()];
//                            FileInputStream fileInputStream = new FileInputStream(file);
//                            fileInputStream.read(b);
//
//                            DataOutputStream dos = new DataOutputStream(openFileOutput(curreport.getReportName(), ModifyReportActivity.this.MODE_PRIVATE));
//                            dos.write(b);
//                            dos.flush();
//                            dos.close();
//                            Toast.makeText(ModifyReportActivity.this, "OFFLINE MODE", Toast.LENGTH_SHORT);
//                        } else {

                        DataService dataService = new DataService();
                        dataService.updateReport(curreport);

                        showSuccessDialog();
//                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ;
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int position, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && null != data) {

            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            try {
                Field field = adapter.getItem(position);
                field.setValue_field(field.getValue_field() + result.get(0) + ".");
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "null cmnr", Toast.LENGTH_SHORT).show();
        }
    }


    private void showSuccessDialog() {
        AlertDialog alertbox = new AlertDialog.Builder(ModifyReportActivity.this).setTitle("Success")
                .setMessage("Báo cáo đã được tạo")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                }).show();
    }

    private void getExcel() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        //final EditText valueField = (EditText) findViewById(R.id.company_name_input);
//        excelfile = null;
        //ExcelHandle excelfile = null;

        FilePickerDialog dialog = new FilePickerDialog(ModifyReportActivity.this, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {

                excel_name = files[0].toString();
                fieldActivityAsyncTask = new FieldActivityAsyncTask(ModifyReportActivity.this, excel_name, adapter);
                fieldActivityAsyncTask.execute();
            }
        });
        dialog.show();
    }

    private boolean isConnectInternet() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else return false;
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
