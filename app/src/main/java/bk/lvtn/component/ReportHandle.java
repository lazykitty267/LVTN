package bk.lvtn.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import bk.lvtn.data.DataRow;
import bk.lvtn.fragment_adapter.Report;

/**
 * Created by Phupc on 10/05/17.
 */

public class ReportHandle {
    private List<DataRow> listValue;
    public ReportHandle(){
        listValue = new ArrayList<DataRow>();
    }
    public List<DataRow> getListValue() {
        return listValue;
    }

    public void setListValue(List<DataRow> listValue) {
        this.listValue = listValue;
    }

    public void addValue(String key,Object value){
        DataRow a = new DataRow(key,value);
        listValue.add(a);
    }
}
