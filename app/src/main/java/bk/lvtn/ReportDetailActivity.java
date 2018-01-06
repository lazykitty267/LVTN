package bk.lvtn;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;


import org.apache.poi.util.IOUtils;
import org.apache.poi.util.StringUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreSpi;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bk.lvtn.Signature.DigitalSignature;
import bk.lvtn.data.EventFromServer;
import bk.lvtn.form.Form;
import bk.lvtn.fragment_adapter.AttachImages;
import bk.lvtn.fragment_adapter.AttachImgAdapter;
import bk.lvtn.fragment_adapter.Field;
import bk.lvtn.fragment_adapter.FieldAdapter;
import bk.lvtn.fragment_adapter.Template;
import bk.lvtn.fragment_adapter.TemplateAdapter;
import dataService.DataService;
import dataService.OfflineDataService;
import entity.AttachImage;
import entity.Note;
import entity.PdfFile;
import entity.Report;
import entity.User;


public class ReportDetailActivity extends AppCompatActivity {
    String regexPhoneNum = "^[0-9]*$";
    FieldActivityAsyncTask fieldActivityAsyncTask;
    ListView listField;
    ArrayList<Field> arrField = new ArrayList<Field>();
    FieldAdapter adapter,adapterAdd=null;
    String excel_name = "";
    Form form;
    String[] fileList = null;
    ArrayList<AttachImages > listImgAttach = new ArrayList<AttachImages>();
    RecyclerView mRecyclerView;
    AttachImgAdapter mRcvAdapter;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra("Stream");

        final FloatingActionsMenu attachFile = (FloatingActionsMenu) findViewById(R.id.add_attachimg_button);
        FloatingActionButton attachFileFromCamera = (FloatingActionButton) findViewById(R.id.add_fcamera);
        FloatingActionButton attachFileFromFile = (FloatingActionButton) findViewById(R.id.add_ffile);
        FloatingActionButton addField1 = (FloatingActionButton) findViewById(R.id.add_ffield);
        FloatingActionButton saveForm = (FloatingActionButton) findViewById(R.id.save_rp);
        FloatingActionButton addSpecialField = (FloatingActionButton) findViewById(R.id.add_specical_field);
        listField = (ListView) findViewById(R.id.list_field);
        adapter = new FieldAdapter(this, arrField, R.layout.item_inlist_field);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_attachfile);
        adapter = new FieldAdapter(this, arrField, R.layout.item_inlist_field);
        mRcvAdapter = new AttachImgAdapter(listImgAttach);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRcvAdapter);
        listField.setAdapter(adapter);
        // Test listview
        Field field1 = new Field("Tên cơ quan");
        Field field2 = new Field("Thời gian bắt đầu");
        Field field3 = new Field("Địa điểm");
        Field field4 = new Field("Thành phần tham dự");
        Field field5 = new Field("Nội dung");
        Field field6 = new Field("Số điện thoại");
        arrField.add(field1);
        arrField.add(field2);
        arrField.add(field3);
        arrField.add(field4);
        arrField.add(field5);
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

        if (bundle.getBoolean("isAdd")) {
            getExcel();

        }
//        addField.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                View dialogView = getLayoutInflater().inflate(R.layout.add_field_dialog,null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(ReportDetailActivity.this);
//                builder.setView(dialogView);
//                String titleText = "Nội dung mới";
//                // Initialize a new foreground color span instance
//                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
//
//                // Initialize a new spannable string builder instance
//                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
//
//                // Apply the text color span
//                ssBuilder.setSpan(
//                        foregroundColorSpan,
//                        0,
//                        titleText.length(),
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                );
//                builder.setTitle(ssBuilder);
//                ListView lv = (ListView ) dialogView.findViewById(R.id.add_field_d);
//                ArrayList<Field> arrTp = new ArrayList<Field>();
//
//                adapterAdd = new FieldAdapter(ReportDetailActivity.this,arrTp,R.layout.item_inlist_field);
//                lv.setAdapter(adapterAdd);
//
//                final Field field1 = new Field("Tên Field");
//                arrTp.add(field1);
//                adapterAdd.notifyDataSetChanged();
//                builder.setPositiveButton("Thêm", null);
//                final AlertDialog dialog = builder.create();
//                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//
//                    @Override
//                    public void onShow(DialogInterface dialog1) {
//                        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                        b.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                // TODO Do something
//                                if(field1.getValue_field().equals("")){
//                                    Toast.makeText(ReportDetailActivity.this,"Mời nhập field name!!",Toast.LENGTH_LONG);
//                                }
//                                else {
//                                    Field f = new Field(field1.getValue_field());
//                                    arrField.add(f);
//                                    adapter.notifyDataSetChanged();
//                                    adapterAdd=null;
//                                    dialog.cancel();
//                                }
//                            }
//                        });
//
//
//                    }
//                });
//                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                dialog.show();
////                final AlertDialog dialog = new AlertDialog(ReportDetailActivity.this);
////                dialog.setContentView(R.layout.add_field_dialog);
////                ListView lv = (ListView ) dialog.findViewById(R.id.add_field_d);
////                ArrayList<Field> arrTp = new ArrayList<Field>();
////
////                adapterAdd = new FieldAdapter(ReportDetailActivity.this,arrTp,R.layout.item_inlist_field);
////                lv.setAdapter(adapterAdd);
////
////                final Field field1 = new Field("Tên Field");
////                arrTp.add(field1);
////                adapterAdd.notifyDataSetChanged();
////
////                dialog.setCancelable(true);
////                dialog.setTitle("ListView");
////                dialog.show();
////                Button rp_select = (Button) dialog.findViewById(R.id.rp_select);
////                rp_select.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        if(field1.getValue_field().equals("")){
////                            Toast.makeText(ReportDetailActivity.this,"Mời nhập field name!!",Toast.LENGTH_LONG);
////                            return;
////                        }
////                        else {
////                            Field f = new Field(field1.getValue_field());
////                            arrField.add(f);
////                            adapter.notifyDataSetChanged();
////                            adapterAdd=null;
////                            dialog.dismiss();
////                        }
////                    }
////                });
//            }
//        });
        attachFileFromCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(ReportDetailActivity.this, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    //TODO: Do somethings
                } else {
                    //Request camera permission
                    ActivityCompat.requestPermissions(ReportDetailActivity.this, new String[] { android.Manifest.permission.CAMERA },
                            1);
                }
//                getAttachFile();
                AttachImageService a = new AttachImageService(ReportDetailActivity.this,getApplication());
                attachFile.toggle();
                a.takePicture();
            }
        });
        addField1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachFile.toggle();
                final Dialog dialog = new Dialog(ReportDetailActivity.this);
                dialog.setContentView(R.layout.add_field_dialog1);
                GridView lv = (GridView) dialog.findViewById(R.id.list_template_d);
                ArrayList<Field> arrTp = new ArrayList<Field>();

                adapterAdd = new FieldAdapter(ReportDetailActivity.this, arrTp, R.layout.item_inlist_field);
                lv.setAdapter(adapterAdd);

                final Field field1 = new Field("Tên Field");
                arrTp.add(field1);
                adapterAdd.notifyDataSetChanged();

                dialog.setCancelable(true);
                dialog.show();
                Button rp_select = (Button) dialog.findViewById(R.id.rp_select);
                rp_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (field1.getValue_field().equals("")) {
                            Toast.makeText(ReportDetailActivity.this, "Mời nhập field name!!", Toast.LENGTH_LONG);
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
        addSpecialField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachFile.toggle();
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportDetailActivity.this);
                // String array for alert dialog multi choice items
                final String[] choices = new String[]{
                        "Hashtag",
                        "Ghi chú quan trọng"
                };
                final boolean[] checked = new boolean[]{
                        false, // Red
                        false

                };
                // Convert the color array to list
                final List<String> choicesList = Arrays.asList(choices);
                builder.setMultiChoiceItems(choices, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        // Update the current focused item's checked status
                        checked[which] = isChecked;

                        // Get the current focused item
                        String currentItem = choicesList.get(which);

                    }
                });
                // Set a title for alert dialog
                builder.setTitle("Nội dung đặc biệt");
                // Set the positive/yes button click listener
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when click positive button
                        for (int i = 0; i<checked.length; i++){
                            boolean flag = checked[i];
                            if (flag) {
                                    Field f = new Field(choices[i]);
                                    arrField.add(f);
                                    adapter.notifyDataSetChanged();
                                    adapterAdd=null;
                            }
                        }

                    }
                });
                // Set the negative/no button click listener
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when click the negative button
                    }
                });
                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();

            }
        });
        attachFileFromFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachFile.toggle();
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
                dialog.setTitle("Chọn file cần đính kèm");
                dialog.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(String[] files) {
                        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_attach_file_icon);
                        AttachImages attachImages = new AttachImages(String.valueOf(files[0].toString()),image);
                        listImgAttach.add(attachImages);
                        mRcvAdapter.notifyDataSetChanged();
                        Toast.makeText(ReportDetailActivity.this,String.valueOf(listImgAttach.size()), Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.show();
            }
        });
        saveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    attachFile.toggle();
                    dialog = new Dialog(ReportDetailActivity.this);
                    dialog.setContentView(R.layout.private_key_dialog);

                    dialog.setCancelable(true);
                    dialog.show();
                    Button name_add = (Button) dialog.findViewById(R.id.pk_add);
                    final EditText report_name = (EditText) dialog.findViewById(R.id.private_key);
                    name_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (report_name.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), "Mời bạn nhập tên báo cáo", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                Report report = new Report();
                                for (int i = 0; i < adapter.getCount(); i++) {
                                    final String s = adapter.getItem(i).getValue_field();
                                    if(adapter.getItem(i).getKey_field().equals("Hashtag")){
                                        report.setHashTag(s);
                                        report.addValue(adapter.getItem(i).getKey_field(), new ArrayList<String>() {{
                                            add(s);
                                        }});
                                    }
                                    else if(adapter.getItem(i).getKey_field().equals("Ghi chú quan trọng")){
                                        report.setPrivateField(s);
                                    }
                                    else {
                                        report.addValue(adapter.getItem(i).getKey_field(), new ArrayList<String>() {{
                                            add(s);
                                        }});
                                    }
                                }
                                report.setReportName(report_name.getText().toString());
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
                                    User user = dataService.getCurrentUser(ReportDetailActivity.this);
                                    report.setUserName(user.getUsername());
                                    report.setManagerName(user.getManagerName());
                                    if (!isOnline()){
                                        report.setId(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                                        report.setNote(OfflineDataService.CREATE_MODE);
                                        OfflineDataService offData = new OfflineDataService();
                                        offData.doCreateDb(ReportDetailActivity.this);
                                        offData.doCreateReportTable();
                                        offData.doCreateFieldTable();
                                        offData.doInsertReport(report);
                                        byte[] b = new byte[(int) file.length()];
                                        FileInputStream fileInputStream = new FileInputStream(file);
                                        fileInputStream.read(b);

                                        String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                                        File userDir = new File(path,report.getId());
                                        if (!userDir.exists()) {
                                            userDir.mkdir();
                                        }
                                        File pdf = new File(userDir,report.getId()+".pdf");
                                        file.renameTo(pdf);
                                        if (listImgAttach != null) {
                                            File attachDir = new File(userDir,report.getId());
                                            if (!attachDir.exists()) {
                                                attachDir.mkdir();
                                            }
                                            for (int index = 0; index < listImgAttach.size(); index++) {
                                                File f = new File(listImgAttach.get(index).getPath());
                                                f.renameTo(new File(attachDir,f.getName()));
                                            }
                                        }
                                        showOfflineDialog();
                                        Toast.makeText(ReportDetailActivity.this,"OFFLINE MODE",Toast.LENGTH_SHORT);
                                    }
                                    else {

                                        dataService.saveReport(report);
                                        if (listImgAttach != null) {
                                            for (int index = 0; index < listImgAttach.size(); index++) {
                                                File f = new File(listImgAttach.get(index).getPath());
                                                AttachImage attachImage = new AttachImage();
                                                attachImage.setReportId(report.getId());
                                                attachImage.setName(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                                                dataService.uploadAttachFile(f, attachImage);
                                            }
                                        }

//                                        byte[] s = encrypt(DigitalSignature.myhash(FileUtils.readFileToByteArray(file)));
//                                        DigitalSignature dSig = new DigitalSignature();
//                                        dSig.generateKey();

                                        final File keyFileDirectory = new File(getFilesDir(), "rsa/");
                                        final File privateKeyFile = new File(keyFileDirectory, user.getUsername() + "_priv_key");
                                        final File publicKeyFile = new File(keyFileDirectory, "sikkr_pub_key");
//                                        pdfFile.longg = privateKeyFile.toString();
                                        byte[] b = new byte[(int) file.length()];
                                        FileInputStream fileInputStream = new FileInputStream(file);
                                        fileInputStream.read(b);
                                        String k = digi.myhash(b);
//                                        pdfFile.dongkcualong = k;
                                        byte[] s = digi.rsaSign(k.getBytes(), digi.getPrivateKey(privateKeyFile));

                                        final File signal = new File(getFilesDir(), "signal");
                                        signal.createNewFile();
                                        DataOutputStream dos = new DataOutputStream(new FileOutputStream(signal));
                                        dos.write(s);
                                        dos.flush();
                                        dos.close();
                                        dataService.saveSignature(signal,pdfFile);
//                                        DigitalSignature digitalSignature = new DigitalSignature();
//                                        String hexName = digitalSignature.fileToHex(file);
//                                        pdfFile.setSignUrl(hexName);
                                        signal.delete();


                                        pdfFile.setReportId(report.getId());
                                        //TODO: Bỏ url vào pdfFile
//                                        final File URLFileDirectory = new File(keyFileDirectory,  user.getUsername() + "/");
                                        String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                                        final File keyFileParent = new File(path,  "rsa/");
                                        if (!keyFileParent.exists()) {
                                            keyFileParent.mkdir();
                                        }
                                        final File URLFileDirectory = new File(keyFileParent,  user.getUsername() + "/");
                                        if (!URLFileDirectory.exists()) {
                                            URLFileDirectory.mkdir();
                                        }
//                                        if (URLFileDirectory.listFiles() != null) {
//                                            for (File f : URLFileDirectory.listFiles()) {
//                                                pdfFile.setPublicKeyUrl(f.getName());
//                                            }
//                                        }
                                        final File pubkeyURL = new File(URLFileDirectory, "key.txt");
//                                        DataInputStream dis = new DataInputStream(new FileInputStream(hexPubkey));
//                                        String name = dis.readUTF();
//                                        dis.close();
                                        StringBuilder text = new StringBuilder();
                                        try {
                                            BufferedReader br = new BufferedReader(new FileReader(pubkeyURL));
                                            String line;

                                           while ((line = br.readLine()) != null) {
                                                text.append(line);
                                            }
                                            br.close();
                                        }
                                        catch (IOException e) {
                                            //You'll need to add proper error handling here
                                        }
                                        pdfFile.setPublicKeyUrl(text.toString().replace("\u0000", ""));
                                        while (pdfFile.getSignUrl() != null
                                                && pdfFile.getPublicKeyUrl() != null
                                                && pdfFile.getUrl() != null) {
                                            dataService.uploadFile(file, pdfFile);
                                            Toast.makeText(ReportDetailActivity.this,"ONLINE MODE",Toast.LENGTH_SHORT);
                                            showSuccessDialog();
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(ReportDetailActivity.this,e.toString(),Toast.LENGTH_SHORT);
                                    Log.d("aaa", e.toString());
                                }
                            }
                        }
                    });
                }
            }
        });

    }



    @Override
    protected void onActivityResult (int position, int resultCode, Intent data){
        if(position==999){
            AttachImageService attachImageService = new AttachImageService(ReportDetailActivity.this,getApplication());
            File photoFile =null;
            try {
                photoFile = attachImageService.createImageFile();
                Log.d("image path",photoFile.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            AttachImages attachImages = new AttachImages(String.valueOf(photoFile.getAbsolutePath()),imageBitmap);
            listImgAttach.add(attachImages);
            mRcvAdapter.notifyDataSetChanged();
            Toast.makeText(this,String.valueOf(listImgAttach.size()), Toast.LENGTH_SHORT).show();

            OutputStream os;
            try {
                os = new FileOutputStream(photoFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
            }
        }
        else{
            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                try {
                    if (adapterAdd == null) {
                        boolean flag;
                        Field field = adapter.getItem(position);
                        switch (field.getKey_field()){
                            case "Số điện thoại":{
                                if(field.getValue_field().matches(regexPhoneNum)){
                                    flag = true;
                                }
                                else {
                                    flag = false;
                                }
                            }

                        }

                        field.setValue_field(field.getValue_field() + result.get(0) + ".");
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Field field = adapterAdd.getItem(position);
                        field.setValue_field(field.getValue_field() + result.get(0));
                        adapterAdd.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
            }
        }


    }

    private void showSuccessDialog() {
        AlertDialog alertbox = new AlertDialog.Builder(ReportDetailActivity.this).setTitle("Success")
                .setMessage("Báo cáo đã được tạo")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
                        finish();
                    }
                }).show();
    }
    private void showOfflineDialog() {
        AlertDialog alertbox = new AlertDialog.Builder(ReportDetailActivity.this).setTitle("Success")
                .setMessage("Bạn đang ngoại tuyến!! Báo cáo sẽ được upload sau khi bạn đăng nhập lại!!")
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

        FilePickerDialog dialog = new FilePickerDialog(ReportDetailActivity.this, properties);
        dialog.setTitle("Chọn file excel");
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

    private boolean isConnectInternet(){
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (this.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        else return false;
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
    @Override
    public void onBackPressed() {
            AlertDialog alertbox = new AlertDialog.Builder(this)
                    .setMessage("Báo cáo chưa được lưu, bạn có muốn thoát")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {

                            finish();
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })
                    .show();
    }

}
