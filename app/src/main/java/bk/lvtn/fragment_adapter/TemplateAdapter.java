package bk.lvtn.fragment_adapter;

/**
 * Created by Long on 21/10/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bk.lvtn.R;

public class TemplateAdapter extends ArrayAdapter<Template> {

    Context mContext;
    ArrayList<Template> listTemplate;
    int resID;

    public TemplateAdapter(Context context, ArrayList<Template> listTemplate, int resID) {
        super(context,resID,listTemplate);
        this.mContext = context;
        this.listTemplate = listTemplate;
        this.resID = resID;
    }


    // 2
    @Override
    public int getCount() {
        return listTemplate.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Template getItem(int position) {
        return null;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Template template = listTemplate.get(position);

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(resID, null);
        }

        // 3
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_favorite);

        // 4
        imageView.setImageResource(template.getImag_src());
        nameTextView.setText(template.getTp_name());

        return convertView;
    }

}
