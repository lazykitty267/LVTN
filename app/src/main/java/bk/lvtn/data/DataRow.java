package bk.lvtn.data;

import java.security.Key;
import java.util.Date;

/**
 * Created by Phupc on 10/05/17.
 */

public class DataRow {
    private String key;

    private String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object value;
    public DataRow(String key,Object value){
        this.key = key;
        this.value = value;
    }
}
