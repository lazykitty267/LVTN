package form;



import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.TabSettings;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.events.PdfPCellEventForwarder;


public class Form {
	
	ArrayList<String> keyForm = new ArrayList<String>();
	ArrayList<String> dataForm = new ArrayList<String>();
	Form(){
		this.keyForm.add("Thời gian bắt đầu".toLowerCase());
		this.keyForm.add("Địa điểm".toLowerCase());
		this.keyForm.add("Thành phần tham dự".toLowerCase());
		this.keyForm.add("Chủ trì".toLowerCase());
		this.keyForm.add("Thư ký".toLowerCase());
		this.keyForm.add("Nội dung".toLowerCase());
		this.keyForm.add("giờ".toLowerCase());
		this.keyForm.add("ngày".toLowerCase());
		this.keyForm.add("tháng".toLowerCase());
		this.keyForm.add("năm".toLowerCase());
		this.dataForm.add("");
		this.dataForm.add("");
		this.dataForm.add("");
		this.dataForm.add("");
		this.dataForm.add("");
		this.dataForm.add("");
		this.dataForm.add("");
		this.dataForm.add("");
		this.dataForm.add("");
		this.dataForm.add("");
	}
	
	public void getData(ArrayList<String> key, ArrayList<String> data){
		for (int i = 0;i<key.size();i++){
			String str = key.get(i).toLowerCase();
			if (this.keyForm.contains(str)){
				int j = this.keyForm.indexOf(str);
				this.dataForm.set(j, data.get(i));
			}
		}
		
		//System.out.print(this.dataForm);
		
	}
	
	
	
	
	
	
    public void createForm1() throws UnsupportedEncodingException {

        // Tạo đối tượng tài liệu
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {

            // Tạo đối tượng PdfWriter
        	
        	FileOutputStream fos = new FileOutputStream("D:/TTTN/LVTN/test.pdf");
            
        	try {
				OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	
            PdfWriter.getInstance(document, fos);

            // Mở file để thực hiện ghi
            document.open();
            // Thêm nội dung sử dụng add function
            String base = "form/vuArial.ttf";
            BaseFont courier = null;
			try {
				courier = BaseFont.createFont(base, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            PdfPTable t = new PdfPTable(2);
            t.setWidthPercentage(95);
            t.setWidths(new int[]{1,2});
            Paragraph title1 = new Paragraph("\n\nCỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM \n\nĐộc lập – Tự do – Hạnh phúc \n\n- - - - - - - o0o- - - - - - -",
            		new Font(courier,13,Font.BOLD,BaseColor.BLACK));

            Paragraph title2 = new Paragraph("TÊN CƠ QUAN, TC CHỦ QUẢN (1)……………… \n\n\n\nTÊN CƠ QUAN, TC CHỦ QUẢN (2)……………… \n\n\n\n\nSố:       /BB- … (3)….",
            		new Font(courier,13,Font.BOLD,BaseColor.BLACK));
            
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
            for (int i = 0;i<6;i++){
            	//t.getRow(0).getCells()[1].disableBorderSide(i);
            	//t.getRow(0).getCells()[0].disableBorderSide(i);
            }

            document.add(t);

            String s = "\n\n\nBIÊN BẢN CUỘC HỌP GIAO BAN\n\n\n";
            Paragraph title3 = new Paragraph(s,
            		new Font(courier,16,Font.BOLD,BaseColor.BLACK));

            title3.setAlignment(Element.ALIGN_CENTER);
            

            
            
            //document.add(title1);
            document.add(title3);
            
            Font font = new Font(courier,12,Font.NORMAL,BaseColor.BLACK);

    		System.out.print(this.dataForm);
            
            Paragraph line1 = new Paragraph("Thời gian bắt đầu: " + this.dataForm.get(0),font);
            line1.setFirstLineIndent((float) 30.30);
            document.add(line1);
            Paragraph line2 = new Paragraph("Địa điểm: " + this.dataForm.get(1),font);
            line2.setFirstLineIndent((float) 30.30);
            document.add(line2);
            Paragraph line3 = new Paragraph("Thành phần tham dự gồm:",font);
            line3.setFirstLineIndent((float) 30.30);
            document.add(line3);
	        Paragraph line4 = new Paragraph(this.dataForm.get(2),font);
	        line4.setIndentationLeft((float) 30.30);
	        document.add(line4);
            Paragraph line5 = new Paragraph("Chủ trì (chủ tọa): " + this.dataForm.get(3),font);
            line5.setFirstLineIndent((float) 30.30);
            document.add(line5);
            Paragraph line6 = new Paragraph("Thư ký (người ghi biên bản): " + this.dataForm.get(4),font);
            line6.setFirstLineIndent((float) 30.30);
            document.add(line6);
            Paragraph line7 = new Paragraph("Nội dung (theo diễn biến cuộc họp/hội nghị/hội thảo):",font);
            line7.setFirstLineIndent((float) 30.30);
            document.add(line7);
            for (int i = 1;i<5;i++){
	            Paragraph nd = new Paragraph(i +". "+ this.dataForm.get(5) + "\t",font);
	            nd.setFirstLineIndent((float) 30.30);
	            document.add(nd);
            }
            Paragraph line8 = new Paragraph("Cuộc họp (hội nghị, hội thảo) kết thúc vào "+this.dataForm.get(6)+", ngày "+this.dataForm.get(7)+ " tháng "+this.dataForm.get(8)+ " năm " + this.dataForm.get(9) ,font);
            line8.setFirstLineIndent((float) 30.30);
            document.add(line8);
            
            
            // Đóng File
            document.close();
        } catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }


}

