package bk.lvtn.component;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by Phupc on 09/29/17.
 */

public class ExcelHandle {
    private FileInputStream file;
    public ExcelHandle(String path){

        try {
            this.file = new FileInputStream(new File(path));
            HSSFWorkbook a = new HSSFWorkbook(file);
            HSSFSheet b= a.getSheet("sheet1");
        }
        catch (Exception e){
            Log.d("ReadfileError","Read file exception: "+e.toString());
        }
    }
}
