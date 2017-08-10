package bk.lvtn.fragment_adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bk.lvtn.R;
import bk.lvtn.fragment_adapter.Report;
/**
 * Created by Phupc on 08/10/17.
 */

public class ReportAdapter extends ArrayAdapter<Report> {
    Context context;
    ArrayList<Report> listReport;
    int resId;
    public ReportAdapter(Context context, ArrayList<Report> listReport, int resId){
        super(context,resId,listReport);
        this.context = context;
        this.listReport = listReport;
        this.resId = resId;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resId,null);
//        TextView totalTime = (TextView)convertView.findViewById(R.id);
//        TextView currentTime = (TextView)convertView.findViewById(R.id.currentTime);
//        TextView noId = (TextView)convertView.findViewById(R.id.noId);
        TextView reportName = (TextView)convertView.findViewById(R.id.rpName);
        TextView reportDetail = (TextView) convertView.findViewById(R.id.rpDetail);
        Report report = listReport.get(position);
        reportName.setText(report.getRp_name());
        reportDetail.setText(report.getRp_detail());

//        Lap lap = lstLap.get(position);
//        totalTime.setText(lap.getTime());
//        currentTime.setText(lap.getTimeSpace());
//        noId.setText(lap.getId());

        return convertView;

    }
}
