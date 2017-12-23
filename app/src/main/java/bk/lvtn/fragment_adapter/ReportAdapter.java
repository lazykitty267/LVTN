package bk.lvtn.fragment_adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import at.markushi.ui.CircleButton;
import bk.lvtn.ModifyReportActivity;
import bk.lvtn.R;
import bk.lvtn.ReportDetailActivity;
import entity.Report;

/**
 * Created by Phupc on 08/10/17.
 */

public class ReportAdapter extends ArrayAdapter<Report> {
    Activity context;
    ArrayList<Report> listReport;
    ArrayList<Report> listReportTmp;
    int resId;

    public ReportAdapter(Activity context, ArrayList<Report> listReport, int resId){
        super(context,resId,listReport);
        this.context = context;
        this.listReport = listReport;
        this.resId = resId;
        listReportTmp = new ArrayList<Report>();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resId,null);
        TextView reportName = (TextView)convertView.findViewById(R.id.report_name);
        TextView reportCreateDate = (TextView) convertView.findViewById(R.id.report_create_date);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_rp_thumbnai) ;
        Random rand = new Random();
        if((position%2)==1){
            imageView.setBackgroundResource(R.drawable.report_thumbnai);
        }
        else {
            imageView.setBackgroundResource(R.drawable.rp_cover1);
        }
        CircleButton mod_button = (CircleButton) convertView.findViewById(R.id.modify_report_button);
        final Report report = listReport.get(position);
        reportName.setText(report.getReportName());
        reportCreateDate.setText(report.getCreateDate());
        mod_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pop up dialog
                final CharSequence actions[] = new CharSequence[] {"Tải xuống","Chỉnh sửa"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setTitle("Pick a color");
                builder.setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if(which==1){
                            Intent intent = new Intent(context, ModifyReportActivity.class);
                            intent.putExtra("CURREPORT",report );
                            ((Activity)context).startActivity(intent);
                        }
                        else {
                        }
                    }
                });
                builder.show();

            }
        });
        return convertView;

    }
    public void copyList(){
        listReportTmp.addAll(listReport);
    }
    // Filter Class
    public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            listReport.clear();
            if (charText.length() == 0) {
                listReport.addAll(listReportTmp);
            } else {
                for (Report rp : listReportTmp) {
                    if (rp.getReportName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        listReport.add(rp);
                    }
                }
            }
            notifyDataSetChanged();
        }
}
