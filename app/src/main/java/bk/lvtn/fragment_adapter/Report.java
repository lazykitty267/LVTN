package bk.lvtn.fragment_adapter;

import java.util.Date;

/**
 * Created by Phupc on 08/10/17.
 */

public class Report {
    public String getRp_name() {
        return rp_name;
    }
    public String getRp_detail() {
        String text = "Created by "+author_name+" on "+ create_date.toString();
        return text;
    }
    public void setRp_name(String rp_name) {
        this.rp_name = rp_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    private String rp_name;
    private String author_name;
    private Date create_date;
}
