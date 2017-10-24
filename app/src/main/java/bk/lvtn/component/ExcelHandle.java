package bk.lvtn.component;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by Phupc on 09/29/17.
 */

public class ExcelHandle {
    private FileInputStream file;
    private HSSFSheet sheet = null;
    public ExcelHandle(FileInputStream file, Context c){
        //File f = new File(path);
        //FileInputStream file = null;
        try {
            //file = new FileInputStream(f);
            //POIFSFileSystem myFileSystem = new POIFSFileSystem(file);
            HSSFWorkbook a = new HSSFWorkbook(file);
            setSheet(a.getSheetAt(0));
        }
        catch (IOException e) {
            setSheet(null);
            e.printStackTrace();
            Toast.makeText(c, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public String getCellData(int colNum, int rowNum){
        //for (char s = 'a';s<'d';s++){String k = String.valueOf(s);}
        //String cell_name = col_name + row_name;
        //CellReference cellReference = new CellReference(cell_name);
        HSSFRow row = getSheet().getRow(rowNum);
        HSSFCell cell = row.getCell(colNum);
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

    public HSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }
}
