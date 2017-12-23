package bk.lvtn.fragment_adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import at.markushi.ui.CircleButton;
import bk.lvtn.ModifyReportActivity;
import bk.lvtn.R;
import entity.Note;
import entity.Report;

/**
 * Created by Phupc on 08/10/17.
 */

public class EventAdapter extends ArrayAdapter<Note> {
    Activity context;
    ArrayList<Note> listNote;
    int resId;

    public EventAdapter(Activity context, ArrayList<Note> listNote, int resId){
        super(context,resId,listNote);
        this.context = context;
        this.listNote = listNote;
        this.resId = resId;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resId,null);
        TextView eventDes = (TextView)convertView.findViewById(R.id.event_des_inlist);
        TextView eventPlace = (TextView) convertView.findViewById(R.id.event_place_inlist);
        TextView eventTime = (TextView) convertView.findViewById(R.id.event_time_inlist);
        LinearLayout color = (LinearLayout) convertView.findViewById(R.id.event_color);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_rp_thumbnai) ;


//        CircleButton mod_button = (CircleButton) convertView.findViewById(R.id.modify_report_button);
        final Note event = listNote.get(position);
        eventDes.setText(event.getContent());
        eventPlace.setText(event.getPlace());
        eventTime.setText(event.getDate()+" "+event.getTime());
        if(event.isUserCreated()){
            color.setBackground(ContextCompat.getDrawable(context, R.drawable.borderred));
        }
        else {
            color.setBackground(ContextCompat.getDrawable(context, R.drawable.bordergreen));
        }
//        mod_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ModifyReportActivity.class);
//                intent.putExtra("CURREPORT",report );
//                ((Activity)context).startActivity(intent);
//
//            }
//        });
        return convertView;

    }

}
