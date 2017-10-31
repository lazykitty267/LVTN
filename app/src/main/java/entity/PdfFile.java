package entity;

/**
 * Created by lazyk on 10/20/2017.
 */

public class PdfFile {
    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String name;
    private String reportId;
    private String userId;
    private String id;
    private String url;

    public PdfFile(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public PdfFile(){

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
