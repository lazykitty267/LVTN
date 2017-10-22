package bk.lvtn;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        GridView gridView = (GridView)view.findViewById(R.id.gridview);
        adapter = new TemplateAdapter(getActivity(), arrTp,R.layout.item_inlist_template);
        gridView.setAdapter(adapter);

        // Test gridview
        Template test = new Template();
        test.setTp_name("Báo cáo kết quả");
        test.setImag_src(R.drawable.ic_note_black_24dp);
        arrTp.add(test);
        arrTp.add(test);
        adapter.notifyDataSetChanged();


        return view;
    }

}
