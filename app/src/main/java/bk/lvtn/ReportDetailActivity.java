package bk.lvtn;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import bk.lvtn.R;
import bk.lvtn.fragment_adapter.Field;
import bk.lvtn.fragment_adapter.FieldAdapter;
import bk.lvtn.fragment_adapter.Report;
import bk.lvtn.fragment_adapter.ReportAdapter;

public class ReportDetailActivity extends AppCompatActivity {
    ListView listField;
    ArrayList<Field> arrField = new ArrayList<Field>();
    FieldAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        listField = (ListView) findViewById(R.id.list_field);
        adapter = new FieldAdapter(this,arrField,R.layout.item_inlist_field);

        listField.setAdapter(adapter);
        // Test listview
        Field test = new Field();
        test.setKey_field("Địa điểm");
        test.setValue_field("Báo cáo kết quả thường kỳ");
        arrField.add(test);
        arrField.add(test);
        adapter.notifyDataSetChanged();
    }
}
