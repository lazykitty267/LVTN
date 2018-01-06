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


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
import java.util.Arrays;
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
import entity.User;


public class ModifyReportActivity extends AppCompatActivity {
    Bitmap theBitmap;
    FieldActivityAsyncTask fieldActivityAsyncTask;
    ListView listField;
    ArrayList<Field> arrField = new ArrayList<Field>();
    FieldAdapter adapter, adapterAdd = null;
    String excel_name = "";
    Form form;


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
        final FloatingActionsMenu attachFile = (FloatingActionsMenu) findViewById(R.id.add_attachimg_button);
        FloatingActionButton addField1 = (FloatingActionButton) findViewById(R.id.add_ffield);
        FloatingActionButton saveForm = (FloatingActionButton) findViewById(R.id.save_rp);
        FloatingActionButton addSpecialField = (FloatingActionButton) findViewById(R.id.add_specical_field);

        listField = (ListView) findViewById(R.id.list_field);
        adapter = new FieldAdapter(this, arrField, R.layout.item_inlist_field);
        listField.setAdapter(adapter);
        for (DataRow item : fieldsData) {
            Field tmp = new Field(item.getKey(), item.getValue().get(0));
            arrField.add(tmp);
        }
        adapter.notifyDataSetChanged();

        final DigitalSignature digi = new DigitalSignature();
        addSpecialField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachFile.toggle();
                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyReportActivity.this);
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

        addField1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachFile.toggle();
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

        saveForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    attachFile.toggle();
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

                        final File file = form.createForm1(Environment.getExternalStorageDirectory().getAbsolutePath().toString(), is, pdfFile);
                        if (file == null) {
                            return;
                        }

                        final DatabaseConnection databaseConnection = new DatabaseConnection();
                        DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
                        final List<PdfFile> pdfFileList = new ArrayList<>();
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    PdfFile pdfFile = dataSnapshot.child(curreport.getId()).getValue(PdfFile.class);
                                    pdfFileList.add(pdfFile);
                                    PdfFile pdfFile1 = pdfFileList.get(0);
                                    DataService dataService = new DataService();
                                    dataService.deletePdf(pdfFile1);
                                    curreport.setUpdateDate(dataService.getCurdateTime());
                                    DatabaseReference databaseReference1 = databaseConnection.connectReportDatabase();
                                    databaseReference1.child(curreport.getUserName()).child(curreport.getId()).setValue(curreport).isSuccessful();
                                    final File keyFileDirectory = new File(getFilesDir(), "rsa/");
                                    User user = dataService.getCurrentUser(ModifyReportActivity.this);
                                    final File privateKeyFile = new File(keyFileDirectory, user.getUsername() + "_priv_key");
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
                                    dataService.saveSignature(signal, pdfFile);
                                    signal.delete();


                                    pdfFile.setReportId(curreport.getId());
                                    dataService.uploadFile(file, pdfFile);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
        }
    }


    private void showSuccessDialog() {
        AlertDialog alertbox = new AlertDialog.Builder(ModifyReportActivity.this).setTitle("Success")
                .setMessage("Sửa đổi đã được lưu")
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
