package entity;

import java.util.Date;

/**
 * Created by lazyk on 10/31/2017.
 */

public class AttachImage {
    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String reportId;
    private String id;
    private String name;
    private String url;

    public  AttachImage() {
    }

    public  AttachImage(String reportId, String id, String name) {
        this.id = id;
        this.name = name;
        this.reportId = reportId;
    }

}
