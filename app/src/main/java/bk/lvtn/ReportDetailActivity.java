package bk.lvtn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 100: {
//                if (resultCode == (RESULT_OK && null != data) {

//                ArrayList<String> result = data
//                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                Toast.makeText(this, result.get(0),
//                        Toast.LENGTH_SHORT).show();
//                Bundle eee = data.getBundleExtra("position");
//
//                Field field = adapter.getItem(eee.getInt("pos"));
////                    field = listField.get(pos);
//                field.setValue_field("vcvcvcv");
////                    valueField.setText("vcvcvcv");
////                    valueField.setSelection(valueField.getText().length());
//                adapter.notifyDataSetChanged();
//                }
                break;
            }

        }
    }
}
