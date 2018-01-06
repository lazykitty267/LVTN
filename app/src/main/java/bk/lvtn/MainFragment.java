package bk.lvtn;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bk.lvtn.data.EventFromServer;
import bk.lvtn.fragment_adapter.EventAdapter;
import bk.lvtn.fragment_adapter.ReportAdapter;
import bk.lvtn.fragment_adapter.Template;
import bk.lvtn.fragment_adapter.TemplateAdapter;
import bk.lvtn.fragment_adapter.WrapContentListview;
import dataService.DataService;
import dataService.DatabaseConnection;
import entity.Note;
import entity.Report;
import entity.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    String text_notify ="";
    ArrayList<Note> arrEvent ;
    EventAdapter adapter;
    com.getbase.floatingactionbutton.FloatingActionButton add_b;
    ArrayList<Report> arrRp = new ArrayList<Report>();
    ReportAdapter adapterRp;
    WrapContentListview listRp;
    Dialog dialog;
    TextView thongbao;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("EZ Report");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        arrEvent = new ArrayList<Note>();
        WrapContentListview listView = (WrapContentListview)view.findViewById(R.id.list_event_inweek);
        thongbao = (TextView)view.findViewById(R.id.text_notify);
        adapter = new EventAdapter(getActivity(), arrEvent,R.layout.item_inlist_events);
        listRp = (WrapContentListview)view.findViewById(R.id.list_report_inweek);
        add_b = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.fab_rp);
        adapterRp = new ReportAdapter(getActivity(),arrRp,R.layout.item_inlist_report);
        listRp.setAdapter(adapterRp);
        listView.setAdapter(adapter);
        //connect to note db
        DatabaseConnection databaseConnection = new DatabaseConnection();
        DatabaseReference databaseReference = databaseConnection.connectNoteDatabase();
        //30p break
        DataService dataService = new DataService();
        User user = dataService.getCurrentUser(getActivity());
        databaseReference.child(user.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Note event = new Note();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    event = postSnapshot.getValue(Note.class);
                    arrEvent.add(event);
                }
                thongbao.setText("Chào Thanh, bạn có "+String.valueOf(arrEvent.size()) +" sự kiện mới trong tuần này!");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference = databaseConnection.connectReportDatabase();
        databaseReference.child(user.getManagerName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    arrRp.add(report);
                    adapterRp.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.list_template_dialog);
                GridView lv = (GridView ) dialog.findViewById(R.id.list_template_d);
                ArrayList<Template> arrTp = new ArrayList<Template>();

                TemplateAdapter adapter = new TemplateAdapter(getActivity(),arrTp,R.layout.item_inlist_template);
                lv.setAdapter(adapter);

                Template test1 = new Template();
                test1.setTp_name("Báo cáo 1");
                test1.setImag_src(R.drawable.report_thumbnai);
                Template test2 = new Template();
                test2.setTp_name("Báo cáo 2");
                test2.setImag_src(R.drawable.rp_cover1);
                arrTp.add(test1);
                arrTp.add(test2);
                adapter.notifyDataSetChanged();

                dialog.setCancelable(true);
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
