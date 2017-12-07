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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    private String createDate;
    private String updateDate;

    public  AttachImage() {
    }

    public  AttachImage(String id, String name, String reportId,String updateDate, String url) {
        this.id = id;
        this.name = name;
        this.reportId = reportId;
        this.url = url;
        this.updateDate= updateDate;
    }
    public  AttachImage(String reportId, String id, String name) {
        this.id = id;
        this.name = name;
        this.reportId = reportId;
    }

}
