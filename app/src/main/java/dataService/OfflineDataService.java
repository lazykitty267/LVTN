package dataService;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import bk.lvtn.data.DataRow;
import entity.Report;

/**
 * Created by Long on 20/11/2017.
 */

public class OfflineDataService {
    private SQLiteDatabase database;

    public OfflineDataService() {
    }

    public void doCreateDb(Context context){
        database = context.openOrCreateDatabase("offlineReport.db",Context.MODE_PRIVATE,null);
        doCreateReportTable();
        doCreateFieldTable();
    }
    public boolean doDeleteDB(Context context){
        return context.deleteDatabase("offlineReport.db");
    }

    public void doCreateReportTable(){
        String sql = "CREATE TABLE report (Id TEXT primary key, reportName TEXT, userName TEXT, templateId TEXT" +
                ", createDate TEXT, updateDate TEXT, note TEXT, managerId TEXT)";
        database.execSQL(sql);
    }
    public void doCreateFieldTable(){
        String sql = "CREATE TABLE field (fieldID INTEGER primary key, fieldName TEXT, fieldValue TEXT, Id TEXT NOT NULL" +
                "CONSTRAINT Id REFERENCES report(Id) ON DELETE CASCADE)";
        database.execSQL(sql);
    }

    public void doInsertReport(Report report){
        ContentValues values = new ContentValues();
        report.setId(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
        values.put("Id",report.getId());
        values.put("reportName",report.getReportName());
        values.put("userName",report.getUserName());
        values.put("templateId",report.getTemplateId());
        values.put("createDate",report.getCreateDate());
        values.put("updateDate",report.getUpdateDate());
        values.put("note",report.getNote());
        values.put("managerId",report.getManagerId());
        database.insert("report",null,values);
        doInsertField(report.getFieldList(),report.getId());

    }
    private void doInsertField(List<DataRow> fieldList,String reportId){
        for (int i = 0;i<fieldList.size();i++){
            ContentValues values = new ContentValues();
            values.put("fieldID",i+1);
            values.put("fieldName",fieldList.get(i).getKey());
            values.put("fieldValue",changeToString(fieldList.get(i).getValue()));
            values.put("Id",reportId);
            database.insert("field",null,values);
        }
    }
    private String changeToString(List<String> sList) {
        String s = new String();
        for (int i = 0; i < sList.size(); i++) {
            if(sList.size() > 1){
                s = s + (i+1)+ ". ";
            }
            s = s + sList.get(i) + "\n";
        }
        return s;
    }

    public List<Report> loadReport(){
        List<Report> list = new ArrayList<Report>();
        Cursor reportCursor=database.query("report",null,null,null,null,null,null);
        reportCursor.moveToFirst();
        while(!reportCursor.isAfterLast()){
            Report r = new Report();
            r.setId(reportCursor.getString(0));
            r.setReportName(reportCursor.getString(1));
            r.setUserName(reportCursor.getString(2));
            r.setTemplateId(reportCursor.getString(3));
            r.setCreateDate(reportCursor.getString(4));
            r.setUpdateDate(reportCursor.getString(5));
            r.setNote(reportCursor.getString(6));
            r.setManagerId(reportCursor.getString(7));
            final Cursor fieldCursor=database.query("field",null,"Id=?",new String[]{r.getId()},null,null,null);
            fieldCursor.moveToFirst();
            while(!fieldCursor.isAfterLast()){
                r.addValue(fieldCursor.getString(1),new ArrayList<String>(){{add(fieldCursor.getString(2));}});
                fieldCursor.moveToNext();
            }
            list.add(r);
            reportCursor.moveToNext();
        }
        return list;
    }
}
