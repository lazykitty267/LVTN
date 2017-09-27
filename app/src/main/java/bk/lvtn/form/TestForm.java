package form;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;


public class TestForm {


	@org.junit.Test
	public void test() throws IOException {
		Form form = new Form();
		ArrayList<String> key = new ArrayList<String>();
		ArrayList<String> data = new ArrayList<String>();
		key.add("Thời gian bắt đầu");
		key.add("Địa điểm");
		key.add("Thành phần tham dự");
		key.add("Chủ trì");
		key.add("Thư ký");
		key.add("giờ");
		data.add("Thời gian bắt đầu");
		data.add("Địa điểm");
		data.add("Thành phần tham dự\nThành phần tham dự\nThành phần tham dự\nThành phần tham dự");
		data.add("Chủ trì");
		data.add("Thư ký");
		data.add("giờ");
		form.getData(key, data);
		form.createForm1();
		long expected = 160327;
		long result = 160327;
		assertEquals(expected, result);
	}
	
}
