package bk.lvtn;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import bk.lvtn.fragment_adapter.Field;
import bk.lvtn.fragment_adapter.FieldAdapter;

public class ReportDetailActivity extends AppCompatActivity {
    ListView listField;
    ArrayList<Field> arrField = new ArrayList<Field>();
    FieldAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        listField = (ListView) findViewById(R.id.list_field);
        adapter = new FieldAdapter(this, arrField, R.layout.item_inlist_field);

        listField.setAdapter(adapter);
        // Test listview
        Field test = new Field();
        test.setKey_field("Địa điểm");
        test.setValue_field("Báo cáo kết quả thường kỳ");

        Field test1 = new Field();
        test1.setKey_field("Địa asdasd");
        test1.setValue_field("Báo cáo kếasdasdasdsat quả thường kỳ");
        arrField.add(test);
        arrField.add(test1);
        adapter.notifyDataSetChanged();

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


}
