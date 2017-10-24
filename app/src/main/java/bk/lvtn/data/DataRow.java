package bk.lvtn.data;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * Created by Phupc on 10/05/17.
 */

public class DataRow {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public List<String> value;
    public DataRow(String key,List<String> value){
        this.key = key;
        this.value = value;
    }
}
