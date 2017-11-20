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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bk.lvtn.fragment_adapter.ReportAdapter;
import bk.lvtn.fragment_adapter.Template;
import bk.lvtn.fragment_adapter.TemplateAdapter;
import dataService.DataService;
import dataService.DatabaseConnection;
import entity.PdfFile;
import entity.Report;
import entity.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportActivity extends Fragment {
    Dialog dialog;
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
        final View view = inflater.inflate(R.layout.fragment_report, container, false);
        listRp = (ListView)view.findViewById(R.id.listRp);
        add_b = (FloatingActionButton)view.findViewById(R.id.fab_rp);
        adapter = new ReportAdapter(getActivity(),arrRp,R.layout.item_inlist_report);
        listRp.setAdapter(adapter);

        PdfFile pdfFile = new PdfFile();

        DataService dataService = new DataService();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        DatabaseReference databaseReference = databaseConnection.connectReportDatabase();
        User user = dataService.getCurrentUser(getActivity());
        databaseReference.child(user.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    arrRp.add(report);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Test listview
//        Report test = new Report();
//        test.setUserName("Phu");
//        test.setReportName("Báo cáo kết quả thường kỳ");
//        Date a = new Date();
////        test.setCreate_date(a);
//        arrRp.add(test);
//        arrRp.add(test);
        adapter.notifyDataSetChanged();
        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.list_template_dialog);
                GridView lv = (GridView ) dialog.findViewById(R.id.list_template_d);
                ArrayList<Template> arrTp = new ArrayList<Template>();

                TemplateAdapter adapter = new TemplateAdapter(getActivity(),arrTp,R.layout.item_inlist_template);
                lv.setAdapter(adapter);

                Template test = new Template();
                test.setTp_name("Báo cáo kết quả");
                test.setImag_src(R.drawable.report_thumbnai);
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
                        dialog.cancel();
                        final Intent myIntent = new Intent(getActivity(), ReportDetailActivity.class);
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
