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
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import at.markushi.ui.CircleButton;
import bk.lvtn.ModifyReportActivity;
import bk.lvtn.R;
import bk.lvtn.ReportDetailActivity;
import dataService.DataService;
import dataService.DatabaseConnection;
import entity.PdfFile;
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
                final CharSequence actions[] = new CharSequence[] {"Xem","Chỉnh sửa"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setItems(actions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==1){
                            Intent intent = new Intent(context, ModifyReportActivity.class);
                            intent.putExtra("CURREPORT",report );
                            ((Activity)context).startActivity(intent);
                        }
                        else {
                            DatabaseConnection databaseConnection = new DatabaseConnection();
                            DatabaseReference databaseReference = databaseConnection.connectPdfDatabase();
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    PdfFile pdfFile = dataSnapshot.child(report.getId()).getValue(PdfFile.class);
                                    DataService dataService = new DataService();
                                    File pdffile =dataService.downloadFile(pdfFile,report.getReportName());

                                    if (pdffile.exists()) {
                                        try {
                                            if (Build.VERSION.SDK_INT >= 24) {

                                                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                                m.invoke(null);

                                            }
                                            Thread.sleep(1000);
                                            Uri path = Uri.fromFile(pdffile);
                                            Intent objIntent = new Intent(Intent.ACTION_VIEW);
                                            objIntent.setDataAndType(path, "application/pdf");
                                            objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            ((Activity) context).startActivity(objIntent);
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
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
