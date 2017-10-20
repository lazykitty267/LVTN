package bk.lvtn.form;


import android.Manifest;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import bk.lvtn.component.ReportHandle;
import bk.lvtn.data.DataRow;

public class Form {
    //ArrayList<Map<String, String>> = new ArrayList<Map<String, String>>();
    ArrayList<String> keyForm = new ArrayList<String>();
    ArrayList<String> dataForm = new ArrayList<String>();

    private ReportHandle report = new ReportHandle();

    public Form() {
        this.report.addValue("Thời gian bắt đầu", new String[]{""});
        this.report.addValue("Địa điểm", new String[]{""});
        this.report.addValue("Thành phần tham dự", new String[]{""});
        this.report.addValue("Chủ trì (chủ tọa)", new String[]{""});
        this.report.addValue("Thư ký (người ghi biên bản)", new String[]{""});
        this.report.addValue("Nội dung (theo diễn biến cuộc họp/hội nghị/hội thảo)", new String[]{""});
        this.report.addValue("giờ", new String[]{""});
        this.report.addValue("ngày", new String[]{""});
        this.report.addValue("tháng", new String[]{""});
        this.report.addValue("năm", new String[]{""});
        this.change();
    }

    public Form(ReportHandle report) {
        this.report = report;
        this.change();
    }

    private void change() {
        List<DataRow> list = this.report.getListValue();
        for (int i = 0; i < list.size(); i++) {
            this.keyForm.add(list.get(i).getKey());
            this.dataForm.add(changeToString(list.get(i).getValue()));
        }
    }

    private String changeToString(String[] sList) {
        String s = new String();
        for (int i = 0; i < sList.length; i++) {
            s = s + sList[i] + "\n";
        }
        return s;
    }

    public boolean isSameString(String s1, String s2) {
        int count = 0, total = 0;
        String[] lStr1 = s1.split("\\s");
        String[] lStr2 = s2.split("\\s");
        ArrayList<String> Str = new ArrayList<String>();
        if (s1.length() < s2.length()) {
            for (int i = 0; i < lStr2.length; i++) {
                Str.add(lStr2[i]);
            }
            for (int i = 0; i < lStr1.length; i++) {
                total = total + lStr1[i].length();
                if (Str.contains(lStr1[i])) {
                    count = count + lStr1[i].length();
                }
            }
            //System.out.print((float)count/(float)total);
            if ((float) count / (float) total > 0.85) {
                return true;
            }

        } else {
            for (int i = 0; i < lStr1.length; i++) {
                Str.add(lStr1[i]);
            }
            for (int i = 0; i < lStr2.length; i++) {
                total = total + lStr2[i].length();
                if (Str.contains(lStr2[i])) {
                    count = count + lStr2[i].length();
                }
            }
            //System.out.print((float)count/(float)total);
            if ((float) count / (float) total > 0.85) {
                return true;
            }
        }

        return false;
    }

    public void getData(ReportHandle report) {
        List<DataRow> list = report.getListValue();
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i).getKey().toLowerCase();
            for (int k = 0; k < this.keyForm.size(); k++) {
                if (this.isSameString(str, this.keyForm.get(k).toLowerCase())) {
                    //int j = this.keyForm.indexOf(str);
                    int a =5;
                    this.dataForm.set(k, this.changeToString(list.get(i).getValue()));

                    String as =dataForm.get(k);
                    Log.d("dataform",as);
                }
            }
        }

        //System.out.print(this.dataForm);

    }


    public void createForm1(String dir, InputStream is) throws UnsupportedEncodingException {

        // Tạo đối tượng tài liệu
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {

            // Tạo đối tượng PdfWriter
            // Dir thường là data/user/0 hình như có quyền root ms xem đc, hoặc xài root explorer
            // xài android virtual device ko xem dc
            File pdfFolder = new File(dir, "pdfdemo");

            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
            }
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
            }
//            File len =pdfFolder.listFiles()[1];
//            File le =pdfFolder.listFiles()[0];
//            Log.d("listfile", String.valueOf(len));
            //Create time stamp

            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            // Test file pdf có đc tạo ko
            File myFile = new File(pdfFolder, timeStamp + ".pdf");
            try {
                myFile.createNewFile();
            } catch (Exception e) {
                Log.d("create new file", e.toString());
            }
            Boolean a = myFile.isFile();
            long bbb = myFile.getUsableSpace();
            OutputStream fos = new FileOutputStream(myFile);

			/*FileOutputStream fos = new FileOutputStream("test.pdf");
            
        	try {
				OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/

            PdfWriter.getInstance(document, fos);

            // Mở file để thực hiện ghi
            document.open();
            // Thêm nội dung sử dụng add function
//            String base = "/res/vuArial.ttf";
            BaseFont courier = null;
            try {
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);

//                courier = BaseFont.createFont(base, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                courier = BaseFont.createFont("vuArial.ttf", BaseFont.IDENTITY_H, true, false, buffer, null);
            } catch (IOException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("aaaaaa", e.toString());
            }

            PdfPTable t = new PdfPTable(2);
            t.setWidthPercentage(95);
            t.setWidths(new int[]{1, 2});
            Paragraph title1 = new Paragraph("\n\nCỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM \n\nĐộc lập – Tự do – Hạnh phúc \n\n- - - - - - - o0o- - - - - - -",
                    new Font(courier, 13, Font.BOLD, BaseColor.BLACK));

            Paragraph title2 = new Paragraph("TÊN CƠ QUAN, TC CHỦ QUẢN (1)……………… \n\n\n\nTÊN CƠ QUAN, TC CHỦ QUẢN (2)……………… \n\n\n\n\nSố:       /BB- … (3)….",
                    new Font(courier, 13, Font.BOLD, BaseColor.BLACK));

            t.addCell(title2);
            t.addCell(title1);
            t.getRow(0).getCells()[1].setHorizontalAlignment(Element.ALIGN_CENTER);
            t.getRow(0).getCells()[0].setHorizontalAlignment(Element.ALIGN_CENTER);
            t.getRow(0).getCells()[1].disableBorderSide(8);
            t.getRow(0).getCells()[1].disableBorderSide(3);
            t.getRow(0).getCells()[1].disableBorderSide(7);
            t.getRow(0).getCells()[0].disableBorderSide(7);
            t.getRow(0).getCells()[0].disableBorderSide(8);
            t.getRow(0).getCells()[0].disableBorderSide(3);
            for (int i = 0; i < 6; i++) {
                //t.getRow(0).getCells()[1].disableBorderSide(i);
                //t.getRow(0).getCells()[0].disableBorderSide(i);
            }

            Log.d("aaaaaaa", "aaaaaaaaaaaaaaaa");
            document.add(t);

            String s = "\n\n\nBIÊN BẢN CUỘC HỌP GIAO BAN\n\n\n";
            Paragraph title3 = new Paragraph(s,
                    new Font(courier, 16, Font.BOLD, BaseColor.BLACK));

            title3.setAlignment(Element.ALIGN_CENTER);


            //document.add(title1);
            document.add(title3);

            Font font = new Font(courier, 12, Font.NORMAL, BaseColor.BLACK);

            System.out.print(this.dataForm);

            Paragraph line1 = new Paragraph(this.keyForm.get(0) + ": " + this.dataForm.get(0), font);
            line1.setFirstLineIndent((float) 30.30);
            document.add(line1);
            Paragraph line2 = new Paragraph(this.keyForm.get(1) + ": " + this.dataForm.get(1), font);
            line2.setFirstLineIndent((float) 30.30);
            document.add(line2);
            Paragraph line3 = new Paragraph(this.keyForm.get(2) + ": ", font);
            line3.setFirstLineIndent((float) 30.30);
            document.add(line3);
            Paragraph line4 = new Paragraph(this.dataForm.get(2), font);
            line4.setIndentationLeft((float) 30.30);
            document.add(line4);
            Paragraph line5 = new Paragraph(this.keyForm.get(3) + ": " + this.dataForm.get(3), font);
            line5.setFirstLineIndent((float) 30.30);
            document.add(line5);
            Paragraph line6 = new Paragraph(this.keyForm.get(4) + ": " + this.dataForm.get(4), font);
            line6.setFirstLineIndent((float) 30.30);
            document.add(line6);
            Paragraph line7 = new Paragraph(this.keyForm.get(5) + ": ", font);
            line7.setFirstLineIndent((float) 30.30);
            document.add(line7);
            for (int i = 1; i < 5; i++) {
                Paragraph nd = new Paragraph(i + ". " + this.dataForm.get(5) + "\t", font);
                nd.setFirstLineIndent((float) 30.30);
                document.add(nd);
            }
            Paragraph line8 = new Paragraph("Cuộc họp (hội nghị, hội thảo) kết thúc vào " +
                    this.dataForm.get(6) + " " + this.keyForm.get(6) + ", " + this.keyForm.get(7) + " " + this.dataForm.get(7) + " " +
                    this.keyForm.get(8) + " " + this.dataForm.get(8) + " " + this.keyForm.get(9) + " " + this.dataForm.get(9) + " ", font);

            line8.setFirstLineIndent((float) 30.30);
            document.add(line8);


            // Đóng File
            document.close();
            long ccc = myFile.getUsableSpace();
            long c = 1;
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }


}

