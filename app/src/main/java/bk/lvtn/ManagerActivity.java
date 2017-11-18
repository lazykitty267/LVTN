package bk.lvtn;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

import bk.lvtn.fragment_adapter.ReportAdapter;
import bk.lvtn.fragment_adapter.Template;
import bk.lvtn.fragment_adapter.TemplateAdapter;
import dataService.DataService;
import entity.Report;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerActivity extends Fragment {


    public ManagerActivity() {
        // Required empty public constructor
    }
    ArrayList<Report> arrRp ;
    ReportAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager, container, false);
        //get user reports
        DataService dataService = new DataService();
        arrRp=new ArrayList<>(dataService.getAllReport("123456789"));
        ListView listView = (ListView)view.findViewById(R.id.list_user_reports);
        adapter = new ReportAdapter(getActivity(), arrRp,R.layout.item_inlist_report);
        listView.setAdapter(adapter);

        // Test listview
        Report a = new Report();
        a.setReportName("Báo cáo nhiệm kỳ ABCDEFASSDGDFGFD");
        a.setCreateDate("18/11/2017");
        arrRp.add(a);
        arrRp.add(a);
        arrRp.add(a);
        arrRp.add(a);
        arrRp.add(a);
        arrRp.add(a);
        arrRp.add(a);
        arrRp.add(a);
        arrRp.add(a);
        arrRp.add(a);
        arrRp.add(a);

        adapter.notifyDataSetChanged();
        return view;
    }

}
