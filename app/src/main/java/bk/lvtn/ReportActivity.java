package bk.lvtn;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

import bk.lvtn.fragment_adapter.Report;
import bk.lvtn.fragment_adapter.ReportAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportActivity extends Fragment {

    ListView listRp;


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
        //
        return view;
    }

}
