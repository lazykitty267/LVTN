package bk.lvtn;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import bk.lvtn.fragment_adapter.Report;
import bk.lvtn.fragment_adapter.ReportAdapter;
import bk.lvtn.fragment_adapter.Template;
import bk.lvtn.fragment_adapter.TemplateAdapter;

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
                ArrayList<Template> arrTp = new ArrayList<Template>();

                TemplateAdapter adapter = new TemplateAdapter(getActivity(),arrTp,R.layout.item_inlist_template);
                lv.setAdapter(adapter);

                Template test = new Template();
                test.setTp_name("Báo cáo kết quả");
                test.setImag_src(R.drawable.ic_note_black_24dp);
                arrTp.add(test);
                arrTp.add(test);
                adapter.notifyDataSetChanged();

                dialog.setCancelable(true);
                dialog.setTitle("ListView");
                dialog.show();
                Button rp_select = (Button) dialog.findViewById(R.id.rp_select);
                rp_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent myIntent = new Intent(getActivity(), FieldActivity.class);
                        final Bundle bundle = new Bundle();
                        AlertDialog alertbox = new AlertDialog.Builder(getActivity())
                                .setMessage("Bạn có muốn thêm dữ liệu kèm theo?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        bundle.putBoolean("isAdd",true);
                                        myIntent.putExtra("Stream", bundle);
                                        startActivity(myIntent);
                                        //close();


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        bundle.putBoolean("isAdd",false);
                                        myIntent.putExtra("Stream", bundle);
                                        startActivity(myIntent);
                                    }
                                })
                                .show();


                    }
                });
            }
        });

        return view;
    }

}
