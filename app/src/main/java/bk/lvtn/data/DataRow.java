package bk.lvtn.data;

import java.security.Key;
import java.util.Date;

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

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public String[] value;
    public DataRow(String key,String[] value){
        this.key = key;
        this.value = value;
    }
}
