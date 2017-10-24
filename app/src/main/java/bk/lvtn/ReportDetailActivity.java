package bk.lvtn;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.util.ArrayList;

import bk.lvtn.fragment_adapter.Field;
import bk.lvtn.fragment_adapter.FieldAdapter;

public class ReportDetailActivity extends AppCompatActivity {
    FieldActivityAsyncTask fieldActivityAsyncTask;
    ListView listField;
    ArrayList<Field> arrField = new ArrayList<Field>();
    FieldAdapter adapter;
    String excel_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Stream");

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
        if (bundle.getBoolean("isAdd")) {
            getExcel();

        }
    }

    @Override
    protected void onActivityResult(int position, int resultCode, Intent data) {


        if (resultCode == RESULT_OK && null != data) {

            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            try {
                Toast.makeText(this, "aaaaa" + position,
                        Toast.LENGTH_SHORT).show();
                Field field = adapter.getItem(position);
                field.setValue_field(result.get(0));
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "null cmnr", Toast.LENGTH_SHORT).show();
        }

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
}
