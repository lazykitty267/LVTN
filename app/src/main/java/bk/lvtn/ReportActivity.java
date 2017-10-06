package bk.lvtn;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import bk.lvtn.component.ReportHandle;
import bk.lvtn.data.DataRow;
import bk.lvtn.form.Form;
import bk.lvtn.fragment_adapter.Report;
import bk.lvtn.fragment_adapter.ReportAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportActivity extends Fragment {

    ListView listRp;
    FloatingActionButton add_b;

    public ReportActivity() {
        // Required empty public constructor
    }
    ArrayList<Report> arrRp = new ArrayList<Report>();
    ReportAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        listRp = (ListView)view.findViewById(R.id.listRp);
        add_b = (FloatingActionButton)view.findViewById(R.id.fab_rp);
        adapter = new ReportAdapter(getActivity(),arrRp,R.layout.item_inlist_report);
        listRp.setAdapter(adapter);
        // Test listview
        Report test = new Report();
        test.setAuthor_name("Phu");
        test.setRp_name("Báo cáo kết quả thường kỳ");
        Date a = new Date();
        test.setCreate_date(a);
        arrRp.add(test);
        arrRp.add(test);
        adapter.notifyDataSetChanged();
        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.list_template_dialog);
                GridView lv = (GridView ) dialog.findViewById(R.id.list_template_d);
                String names[] ={"A","B","C","D"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,names);
                lv.setAdapter(adapter);
                dialog.setCancelable(true);
                dialog.setTitle("ListView");
                dialog.show();
                Button rp_select = (Button) dialog.findViewById(R.id.rp_select);
                rp_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(getActivity(), FieldActivity.class);
                        startActivity(myIntent);
                    }
                });
            }
        });
        //test data
        Form form = new Form();
        ReportHandle report = new ReportHandle();
        report.addValue("Địa điểm",new String[] {"Viettel"});
        report.addValue("Thời gian bắt đầu",new String[]{new Date().toString()});
        report.addValue("Thành phần tham dự",new String[] {"phu","long","nghia"});
        report.addValue("Chủ trì",new String[] {"aaaaaaaaaabbbbbbbbbbbbbbbb"});
        form.getData(report);
        try {
            form.createForm1();
        }
        catch (Exception e){

        }
        String[] stringValues = (String[])report.getListValue().get(2).value;
        Toast.makeText(getContext(), "Path: "+stringValues,
                Toast.LENGTH_SHORT).show();
        return view;
    }

}
