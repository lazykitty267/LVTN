package bk.lvtn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.util.TempFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bk.lvtn.Signature.DigitalSignature;
import bk.lvtn.form.Form;
import bk.lvtn.fragment_adapter.Field;
import bk.lvtn.fragment_adapter.FieldAdapter;
import bk.lvtn.fragment_adapter.Template;
import bk.lvtn.fragment_adapter.TemplateAdapter;
import dataService.DataService;
import entity.AttachImage;
import entity.PdfFile;
import entity.Report;


public class ReportDetailActivity extends AppCompatActivity {
    FieldActivityAsyncTask fieldActivityAsyncTask;
    ListView listField;
    ArrayList<Field> arrField = new ArrayList<Field>();
    FieldAdapter adapter,adapterAdd=null;
    String excel_name = "";
    Form form;
    String[] fileList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra("Stream");

        Button saveForm = (Button) findViewById(R.id.save_button);
        Button addField = (Button) findViewById(R.id.add_button);
        Button attachFile = (Button) findViewById(R.id.attach_file_button);
        listField = (ListView) findViewById(R.id.list_field);
        adapter = new FieldAdapter(this, arrField, R.layout.item_inlist_field);

        listField.setAdapter(adapter);
        // Test listview
        Field field1 = new Field("Tên cơ quan");
        Field field2 = new Field("Thời gian bắt đầu");
        Field field3 = new Field("Địa điểm");
        Field field4 = new Field("Thành phần tham dự");
        Field field5 = new Field("Nội dung");
        arrField.add(field1);
        arrField.add(field2);
        arrField.add(field3);
        arrField.add(field4);
        arrField.add(field5);
        adapter.notifyDataSetChanged();

        final DigitalSignature digi = new DigitalSignature();
        try {

            digi.generateKey("Son Long");

        } catch (Exception e1) {

            // TODO Auto-generated catch block

            e1.printStackTrace();

        }

        if (bundle.getBoolean("isAdd")) {
            getExcel();

        }
        addField.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ReportDetailActivity.this);
                dialog.setContentView(R.layout.list_template_dialog);
                GridView lv = (GridView ) dialog.findViewById(R.id.list_template_d);
                ArrayList<Field> arrTp = new ArrayList<Field>();

                adapterAdd = new FieldAdapter(ReportDetailActivity.this,arrTp,R.layout.item_inlist_field);
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
                        if(field1.getValue_field().equals("")){
                            Toast.makeText(ReportDetailActivity.this,"Mời nhập field name!!",Toast.LENGTH_LONG);
                            return;
                        }
                        else {
                            Field f = new Field(field1.getValue_field());
                            arrField.add(f);
                            adapterAdd.notifyDataSetChanged();
                            adapterAdd=null;
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        attachFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getAttachFile();
            }
        });

        saveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Report report = new Report();
                for (int i = 0; i < adapter.getCount(); i++) {
                    final String s = adapter.getItem(i).getValue_field();
                    report.addValue(adapter.getItem(i).getKey_field(), new ArrayList<String>() {{
                        add(s);
                    }});
                }
                form = new Form(report);
                try {

                    ActivityCompat.requestPermissions(ReportDetailActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);

                    InputStream is = getAssets().open("vuArial.ttf");

                    PdfFile pdfFile = new PdfFile();

                    File file = form.createForm1(Environment.getExternalStorageDirectory().getAbsolutePath().toString(), is, pdfFile);
                    if (file == null) {
                        return;
                    }
                    DataService dataService = new DataService();
                    dataService.saveReport(report);
                    if (fileList!=null) {
                        for (int index = 0; index < fileList.length; index++) {
                            File f = new File(fileList[index]);
                            AttachImage attachImage = new AttachImage();
                            attachImage.setReportId(report.getId());
                            attachImage.setName(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                            dataService.uploadAttachFile(f, attachImage);
                        }
                    }

////                    byte[] s = encrypt(DigitalSignature.myhash(FileUtils.readFileToByteArray(file)));
//                    DigitalSignature dSig = new DigitalSignature();
//                    dSig.generateKey();
                    byte[] b = new byte[(int) file.length()];
                    FileInputStream fileInputStream = new FileInputStream(file);
                    fileInputStream.read(b);
                    byte[] s = digi.encrypt(digi.myhash(b),digi.rk.toString());
                    File tempFile = File.createTempFile("prefix", "suffix", null);
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(s);
                    AttachImage attachImage = new AttachImage();
                    attachImage.setReportId(report.getId());
                    attachImage.setName( new  SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                    dataService.uploadAttachFile(tempFile, attachImage);

                    s = digi.decrypt(tempFile,digi.uk.toString());
                    File tempFile1 = File.createTempFile("prefix", "suffix", null);
                    FileOutputStream fos1 = new FileOutputStream(tempFile1);
                    fos1.write(s);


                    pdfFile.setReportId(report.getId());
                    dataService.uploadFile(tempFile1, pdfFile);


//                    Toast.makeText(ReportDetailActivity.this,s,Toast.LENGTH_SHORT);


                } catch (Exception e) {
                    Log.d("aaa", e.toString());
                }
                showSuccessDialog();
            }
        });

    }



    @Override
    protected void onActivityResult(int position, int resultCode, Intent data) {


        if (resultCode == RESULT_OK && null != data) {

            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            try {
                if (adapterAdd!=null){
                    Field field = adapterAdd.getItem(position);
                    field.setValue_field(field.getValue_field() + result.get(0) + ".");
                    adapterAdd.notifyDataSetChanged();
                }
                else {
                    Field field = adapter.getItem(position);
                    field.setValue_field(field.getValue_field() + result.get(0) + ".");
                    adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "null cmnr", Toast.LENGTH_SHORT).show();
        }

    }

    private void showSuccessDialog() {
        AlertDialog alertbox = new AlertDialog.Builder(ReportDetailActivity.this).setTitle("Success")
                .setMessage("Báo cáo đã được tạo")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
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

        FilePickerDialog dialog = new FilePickerDialog(ReportDetailActivity.this, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {

                excel_name = files[0].toString();
                fieldActivityAsyncTask = new FieldActivityAsyncTask(ReportDetailActivity.this,excel_name,adapter);
                fieldActivityAsyncTask.execute();
            }
        });
        dialog.show();
    }

    private void getAttachFile() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.MULTI_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        //final EditText valueField = (EditText) findViewById(R.id.company_name_input);
//        excelfile = null;
        //ExcelHandle excelfile = null;

        FilePickerDialog dialog = new FilePickerDialog(ReportDetailActivity.this, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                fileList = files;
            }
        });
        dialog.show();
    }
}
