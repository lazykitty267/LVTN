package bk.lvtn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bk.lvtn.component.ExcelHandle;
import bk.lvtn.form.Form;
import dataService.DataService;
import entity.PdfFile;
import entity.Report;

public class FieldActivity extends AppCompatActivity {
    Button saveForm;
    ExcelHandle excelfile = null;
    String excel_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Stream");

        if (bundle.getBoolean("isAdd")){
            getExcel();
            FileInputStream f = null;
            try {
                f = new FileInputStream(new File (excel_name));
                excelfile = new ExcelHandle(f,FieldActivity.this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        Report report = getReport();
        if(report == null) {
            report = new Report();
            List<String> l = new ArrayList<>();
            l.add("");
            report.addValue("Địa điểm", new ArrayList<String>() {{add("");}});
            report.addValue("Thời gian bắt đầu", new ArrayList<String>() {{add("");}});
            report.addValue("Thành phần tham dự", new ArrayList<String>() {{add("");}});
            report.addValue("Chủ trì", new ArrayList<String>() {{add("");}});
        }
        final Form form = new Form(report);

        setContentView(R.layout.template1_layout);
        EditText a = (EditText) findViewById(R.id.company_name_input);
        saveForm = (Button) findViewById(R.id.save_report_button);
        saveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Report report = new Report();
                report.addValue("Địa điểm",new ArrayList<String>() {{add("Viettel");}});
                report.addValue("Thời gian bắt đầu",new ArrayList<String>() {{add(new Date().toString());}});
                report.addValue("Thành phần tham dự",new ArrayList<String>() {{add("phu");add("nghia");add("long");}});
                report.addValue("Chủ trì",new ArrayList<String>() {{add("Viettel");}});

                form.getData(report);
                try {
                    // lưu form thành pdf
                    // cần test file pdf có lưu trong dir : getFilesDir().getAbsolutePath()
                    // ko
//                    form.createForm1(getFilesDir().getAbsolutePath().toString());
                    ActivityCompat.requestPermissions(FieldActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);

                    InputStream is = getAssets().open("vuArial.ttf");

                    PdfFile pdfFile = new PdfFile();
                    File file = form.createForm1(Environment.getExternalStorageDirectory().getAbsolutePath().toString(),is, pdfFile);
                    if (file == null) {
                        return;
                    }
                    DataService dataService = new DataService();
                    dataService.saveReport(report);
                    pdfFile.setId(report.getId());
                    dataService.uploadFile(file, pdfFile);
                }
                catch (Exception e){
                    Log.d("aaa",e.toString());


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

    private void getExcel(){
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        //final EditText valueField = (EditText) findViewById(R.id.company_name_input);
        excelfile = null;
        //ExcelHandle excelfile = null;

        FilePickerDialog dialog = new FilePickerDialog(FieldActivity.this,properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                //files is the array of the paths of files selected by the Application User.
                //Log.d("path",files[0].toString());
                //File file = new File(files[0].toString());
                excel_name = files[0].toString();
                //boolean aaa=file.isFile();

                //boolean b=file.isFile();
                //valueField.setText(files[0].toString());
//                FileInputStream f = null;
//                try {
//                    f = new FileInputStream(file);
//                    excelfile = new ExcelHandle(f,FieldActivity.this);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                //excelfile = new ExcelHandle(f,FieldActivity.this);
            }
        });
        dialog.show();
        //excelfile = new ExcelHandle(valueField.getText().toString());

    }

    @Nullable
    private Report getReport(){
        Report report = new Report();
        if (excelfile == null) return null;
        HSSFSheet sheet = excelfile.getSheet();
        if (sheet == null) return null;
        for (int i =0; i<sheet.getPhysicalNumberOfRows(); i++){
            report.addValue(excelfile.getCellData(i,0),new ArrayList<String>() {{add("");}}) ;
        }
        return report;
    }

}
