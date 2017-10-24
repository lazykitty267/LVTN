package bk.lvtn;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import bk.lvtn.component.ExcelHandle;
import bk.lvtn.form.Form;
import bk.lvtn.fragment_adapter.FieldAdapter;
import entity.Report;

/**
 * Created by Phupc on 10/24/17.
 */

public class FieldActivityAsyncTask extends AsyncTask<Void, Integer, Void> {
    Activity contextParent;
    String excel_path ;
    FieldAdapter adapter;
    Report report;
    ExcelHandle excelfile;
    public FieldActivityAsyncTask(Activity contextParent, String excel_path, FieldAdapter adapter) {
        this.contextParent = contextParent;
        this.excel_path= excel_path;
        this.adapter = adapter;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Hàm này sẽ chạy đầu tiên khi AsyncTask này được gọi
        //Ở đây mình sẽ thông báo quá trình load bắt đâu "Start"


    }

    @Override
    protected Void doInBackground(Void... params) {
        //Hàm được được hiện tiếp sau hàm onPreExecute()
        //Hàm này thực hiện các tác vụ chạy ngầm
        //Tuyệt đối k vẽ giao diện trong hàm này
        try {
            FileInputStream f = new FileInputStream(new File(excel_path));
            excelfile = new ExcelHandle(f);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


            publishProgress();
        return null;
    }
    private Report getReport(ExcelHandle excelfile){
        Report report = new Report();
        if (excelfile == null) return null;
        HSSFSheet sheet = excelfile.getSheet();
        if (sheet == null) return null;
//        for (int i =0; i<sheet.getPhysicalNumberOfRows(); i++){
//            HSSFRow row = sheet.getRow(i);
            ArrayList<String> arr =new ArrayList<String>();arr.add("");
//            for (int j =1; j<row.getPhysicalNumberOfCells(); j++){
//                arr.add(excelfile.getCellData(i,j));
//            }
            report.addValue(excelfile.getCellData(0,0),arr) ;
//        }
        Toast.makeText(contextParent, sheet.getPhysicalNumberOfRows(), Toast.LENGTH_SHORT).show();
        return report;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //Hàm thực hiện update giao diện khi có dữ liệu từ hàm doInBackground gửi xuống
        super.onProgressUpdate(values);
        report = getReport(excelfile);
        //Toast.makeText(contextParent, report.getFieldList().get(0).getKey(), Toast.LENGTH_SHORT).show();
        Report r = new Report();
        for (int i = 0; i<adapter.getCount();i++){
            r.addValue(adapter.getItem(i).getKey_field(),new ArrayList<String>(){{add("");}});
        }
        Form form = new Form(r);
        form.getData(report);
        for (int i = 0; i<adapter.getCount();i++){
            adapter.getItem(i).setValue_field(form.dataForm.get(i));
        }
        //adapter.getItem(1).setValue_field("m laf con chos");
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Hàm này được thực hiện khi tiến trình kết thúc
        //Ở đây mình thông báo là đã "Finshed" để người dùng biết
        Toast.makeText(contextParent, excel_path, Toast.LENGTH_SHORT).show();
    }
}
