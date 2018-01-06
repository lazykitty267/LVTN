package bk.lvtn.form;


import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import bk.lvtn.data.DataRow;
import entity.PdfFile;
import entity.Report;

public class Form {
    //ArrayList<Map<String, String>> = new ArrayList<Map<String, String>>();
    public ArrayList<String> keyForm = new ArrayList<String>();
    public ArrayList<String> dataForm = new ArrayList<String>();

    private Report report = new Report();
//
//    public Form() {
//        this.report.addValue("Thời gian bắt đầu", new String[]{""});
//        this.report.addValue("Địa điểm", new String[]{""});
//        this.report.addValue("Thành phần tham dự", new String[]{""});
//        this.report.addValue("Chủ trì (chủ tọa)", new String[]{""});
//        this.report.addValue("Thư ký (người ghi biên bản)", new String[]{""});
//        this.report.addValue("Nội dung (theo diễn biến cuộc họp/hội nghị/hội thảo)", new String[]{""});
//        this.report.addValue("giờ", new String[]{""});
//        this.report.addValue("ngày", new String[]{""});
//        this.report.addValue("tháng", new String[]{""});
//        this.report.addValue("năm", new String[]{""});
//        this.change();
//    }

    public Form(Report report) {
        this.report = report;
        this.change();
    }

    private void change() {
        List<DataRow> list = this.report.getFieldList();
        for (int i = 0; i < list.size(); i++) {
            this.keyForm.add(list.get(i).getKey());
            this.dataForm.add(changeToString(list.get(i).getValue()));
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

    public void getData(Report report) {
        List<DataRow> list = report.getFieldList();
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


    public File createForm1(String dir, InputStream is, PdfFile pdfFile) throws UnsupportedEncodingException {

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

            //Create time stamp

            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            pdfFile.setName(timeStamp);
            // Test file pdf có đc tạo ko
            File myFile = new File(pdfFolder, timeStamp + ".pdf");
            try {
                myFile.createNewFile();
            } catch (Exception e) {
                Log.d("create new file", e.toString());
            }
            OutputStream fos = new FileOutputStream(myFile);

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

            Paragraph title2 = new Paragraph("TÊN CƠ QUAN, TC CHỦ QUẢN" + this.dataForm.get(0) + " \n\n\n\nSố:       /BB- … (3)….",
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

            String s = "\n\n\nBIÊN BẢN CUỘC HỌP\n\n\n";
            Paragraph title3 = new Paragraph(s,
                    new Font(courier, 22, Font.BOLD, BaseColor.BLACK));

            title3.setAlignment(Element.ALIGN_CENTER);


            //document.add(title1);
            document.add(title3);

            Font font = new Font(courier, 13, Font.NORMAL, BaseColor.BLACK);

            System.out.print(this.dataForm);
            /*
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
            */

            for (int i = 2;i<keyForm.size();i++){
                Paragraph line1 = new Paragraph(this.keyForm.get(i) + ": " + this.dataForm.get(i), font);
                line1.setIndentationLeft((float) 30.30);
                line1.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(line1);
                Paragraph line2 = new Paragraph("", font);
                line2.setIndentationLeft((float) 30.30);
                document.add(line2);
            }
            Paragraph line1 = new Paragraph(this.dataForm.get(1), font);
            line1.setAlignment(Element.ALIGN_RIGHT);
            document.add(line1);

            // Đóng File
            document.close();
//            long ccc = myFile.getUsableSpace();
            long c = 1;



            return myFile;

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


    public File createForm2(String dir, InputStream is, PdfFile pdfFile) throws UnsupportedEncodingException {

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

            //Create time stamp

            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            pdfFile.setName(timeStamp);
            // Test file pdf có đc tạo ko
            File myFile = new File(pdfFolder, timeStamp + ".pdf");
            try {
                myFile.createNewFile();
            } catch (Exception e) {
                Log.d("create new file", e.toString());
            }
            OutputStream fos = new FileOutputStream(myFile);

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
                courier = BaseFont.createFont("vuArial.ttf", BaseFont.IDENTITY_H, true, false, buffer, null);
            } catch (IOException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("aaaaaa", e.toString());
            }

            PdfPTable t = new PdfPTable(2);
            t.setWidthPercentage(95);
            t.setWidths(new int[]{3,4});
            Paragraph title1 = new Paragraph("PHIẾU ĐỀ NGHỊ ĐƯỢC TẠM DỪNG HỌC",
                    new Font(courier,13,Font.BOLD,BaseColor.BLACK));
            Paragraph title2 = new Paragraph("ĐẠI HỌC QUỐC GIA TP.HCM\nTRƯỜNG ĐẠI HỌC BÁCH KHOA\nPhòng Đào tạo\nwww.hcmut.edu.vn\n\n\n",
                    new Font(courier,13,Font.BOLD,BaseColor.BLACK));

            t.addCell(title2);
            t.addCell(title1);
            t.getRow(0).getCells()[1].setHorizontalAlignment(Element.ALIGN_CENTER);
            t.getRow(0).getCells()[1].setVerticalAlignment(Element.ALIGN_MIDDLE);
            t.getRow(0).getCells()[0].setHorizontalAlignment(Element.ALIGN_CENTER);
            t.getRow(0).getCells()[1].setBorder(Rectangle.BOTTOM);
            t.getRow(0).getCells()[1].setBorderWidthBottom(5f);
            t.getRow(0).getCells()[0].setBorder(Rectangle.BOTTOM);
            t.getRow(0).getCells()[0].setBorderWidthBottom(5f);


            for (int i = 0;i<6;i++){
                //t.getRow(0).getCells()[1].disableBorderSide(i);
                //t.getRow(0).getCells()[0].disableBorderSide(i);
            }

            document.add(t);

//            String s = "\n\n\nBIÊN BẢN CUỘC HỌP GIAO BAN\n\n\n";
//            Paragraph title3 = new Paragraph(s,
//            		new Font(courier,16,Font.BOLD,BaseColor.BLACK));
//
//            title3.setAlignment(Element.ALIGN_CENTER);




            //document.add(title1);
//            document.add(title3);

            Font font = new Font(courier,12,Font.NORMAL,BaseColor.BLACK);

            System.out.print(this.dataForm);

//            Paragraph line1 = new Paragraph(this.keyForm.get(0) +": " + this.dataForm.get(0),font);
//            line1.setFirstLineIndent((float) 30.30);
//            document.add(line1);
//            Paragraph line2 = new Paragraph(this.keyForm.get(1) +": " + this.dataForm.get(1),font);
//            line2.setFirstLineIndent((float) 30.30);
//            document.add(line2);
//            Paragraph line3 = new Paragraph(this.keyForm.get(2) +": ",font);
//            line3.setFirstLineIndent((float) 30.30);
//            document.add(line3);
//	        Paragraph line4 = new Paragraph(this.dataForm.get(2),font);
//	        line4.setIndentationLeft((float) 30.30);
//	        document.add(line4);
//            Paragraph line5 = new Paragraph(this.keyForm.get(3) +": " + this.dataForm.get(3),font);
//            line5.setFirstLineIndent((float) 30.30);
//            document.add(line5);
//            Paragraph line6 = new Paragraph(this.keyForm.get(4) +": " + this.dataForm.get(4),font);
//            line6.setFirstLineIndent((float) 30.30);
//            document.add(line6);
//            Paragraph line7 = new Paragraph(this.keyForm.get(5) +": ",font);
//            line7.setFirstLineIndent((float) 30.30);
//            document.add(line7);
//            for (int i = 1;i<5;i++){
//	            Paragraph nd = new Paragraph(i +". "+ this.dataForm.get(5) + "\t",font);
//	            nd.setFirstLineIndent((float) 30.30);
//	            document.add(nd);
//            }
//            Paragraph line8 = new Paragraph("Cuộc họp (hội nghị, hội thảo) kết thúc vào "+
//            this.dataForm.get(6)+ " " +this.keyForm.get(6) +", "+ this.keyForm.get(7)+ " " + this.dataForm.get(7)+ " "+
//            		this.keyForm.get(8)+ " "+this.dataForm.get(8)+ " "+ this.keyForm.get(9)+ " " + this.dataForm.get(9)+ " " ,font);
//
//            line8.setFirstLineIndent((float) 30.30);
//            document.add(line8);


            String text ="\n\n\n"+ this.keyForm.get(0) + ": " + this.dataForm.get(0);
            Paragraph line1 = new Paragraph(text, font);
            line1.setTabSettings(new TabSettings(56f));
            line1.add(Chunk.TABBING);
            line1.add(new Chunk(this.keyForm.get(1) + ": " + this.dataForm.get(1), font));
            line1.setIndentationLeft((float) 30.30);
            line1.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(line1);
            Paragraph line2 = new Paragraph("\n", font);
            line2.setIndentationLeft((float) 30.30);
            document.add(line2);
            text = this.keyForm.get(2) + ": " + this.dataForm.get(2);
            Paragraph line3 = new Paragraph(text, font);
            line3.setTabSettings(new TabSettings(56f));
            line3.add(Chunk.TABBING);
            line3.add(new Chunk(this.keyForm.get(3) + ": " + this.dataForm.get(3), font));
            line3.setTabSettings(new TabSettings(56f));
            line3.add(Chunk.TABBING);
            line3.add(new Chunk(this.keyForm.get(4) + ": " + this.dataForm.get(4), font));
            line3.setIndentationLeft((float) 30.30);
            line3.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(line3);
            Paragraph line4 = new Paragraph("\n", font);
            line4.setIndentationLeft((float) 30.30);
            document.add(line4);

            Paragraph line5= new Paragraph("Tình trạng hiện tại: " + this.dataForm.get(5), font);

            line5.setIndentationLeft((float) 30.30);
            line5.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(line5);
            Paragraph line6 = new Paragraph("\n", font);
            line6.setIndentationLeft((float) 30.30);
            document.add(line6);
            text = "Đề nghị được tạm dừng học kỳ " + this.dataForm.get(6) + " năm học " + this.dataForm.get(7)
                    +" - " + (Integer.parseInt(this.dataForm.get(7)) + 1);
            Paragraph line7 = new Paragraph(text, new Font(courier,12,Font.BOLD,BaseColor.BLACK));
            line7.setIndentationLeft((float) 30.30);
            line7.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(line7);
            Paragraph line8 = new Paragraph("\n", font);
            line8.setIndentationLeft((float) 30.30);
            document.add(line8);
            text = this.keyForm.get(8) + ": \n" + this.dataForm.get(8);
            Paragraph line9 = new Paragraph(text, font);
            line9.setIndentationLeft((float) 30.30);

            line9.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(line9);
            Paragraph line10 = new Paragraph("\n", font);
            line10.setIndentationLeft((float) 30.30);
            document.add(line10);


            for (int i = 9;i<keyForm.size();i++){
                Paragraph line12 = new Paragraph(this.keyForm.get(i) + ": " + this.dataForm.get(i), font);
                line12.setIndentationLeft((float) 30.30);
                line12.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(line1);
                Paragraph line13 = new Paragraph("", font);
                line13.setIndentationLeft((float) 30.30);
                document.add(line13);
            }

            Paragraph line11 = new Paragraph("Sinh viên (Họ tên, chữ ký) \n" + this.dataForm.get(0), font);
            line11.setAlignment(Element.ALIGN_RIGHT);
            line11.setIndentationLeft((float) 30.30);
            document.add(line11);

            // Đóng File
            document.close();
//            long ccc = myFile.getUsableSpace();
            long c = 1;



            return myFile;

        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }


}

