package bk.lvtn;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import bk.lvtn.fragment_adapter.FieldAdapter;

/**
 * Created by Phupc on 10/24/17.
 */

public class FieldActivityAsyncTask extends AsyncTask<Void, Integer, Void> {
    Activity contextParent;
    String excel_path ;
    FieldAdapter adapter;
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

            publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //Hàm thực hiện update giao diện khi có dữ liệu từ hàm doInBackground gửi xuống
        super.onProgressUpdate(values);
        adapter.getItem(1).setValue_field("m laf con chos");
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
