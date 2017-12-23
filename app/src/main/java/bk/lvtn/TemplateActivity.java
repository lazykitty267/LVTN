package bk.lvtn;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

import bk.lvtn.fragment_adapter.Template;
import bk.lvtn.fragment_adapter.TemplateAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateActivity extends Fragment {


    public TemplateActivity() {
        // Required empty public constructor
    }

    ArrayList<Template> arrTp = new ArrayList<Template>();
    TemplateAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mẫu báo cáo");
        GridView gridView = (GridView)view.findViewById(R.id.gridview);
        adapter = new TemplateAdapter(getActivity(), arrTp,R.layout.item_inlist_template);
        gridView.setAdapter(adapter);

        // Test gridview
        Template test1 = new Template();
        test1.setTp_name("Báo cáo 1");
        test1.setImag_src(R.drawable.report_thumbnai);
        Template test2 = new Template();
        test2.setTp_name("Báo cáo 2");
        test2.setImag_src(R.drawable.rp_cover1);
        arrTp.add(test1);
        arrTp.add(test2);
        adapter.notifyDataSetChanged();


        return view;
    }

}
