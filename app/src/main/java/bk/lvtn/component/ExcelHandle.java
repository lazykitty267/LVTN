package bk.lvtn.component;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * Created by Phupc on 09/29/17.
 */

public class ExcelHandle {
    private FileInputStream file;
    HSSFSheet sheet;
    public ExcelHandle(String path){
        try {
            this.file = new FileInputStream(new File(path));
            HSSFWorkbook a = new HSSFWorkbook(file);
            sheet= a.getSheet("sheet1");
        }
        catch (Exception e){
            Log.d("ReadfileError","Read file exception: "+e.toString());
        }
    }


    public String getCellData(String col_name,String row_name){
        //for (char s = 'a';s<'d';s++){String k = String.valueOf(s);}
        String cell_name = col_name + row_name;
        CellReference cellReference = new CellReference(cell_name);
        HSSFRow row = sheet.getRow(cellReference.getRow());
        HSSFCell cell = row.getCell(cellReference.getCol());
        String cellValue;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;

            case HSSFCell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;

            case HSSFCell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue().toString();
                } else {
                    cellValue = Double.toString(cell.getNumericCellValue());
                }
                break;

            case HSSFCell.CELL_TYPE_BOOLEAN:
                cellValue = Boolean.toString(cell.getBooleanCellValue());
                break;

            default:
                cellValue = "";
                break;
        }
        return cellValue;
    }
}
