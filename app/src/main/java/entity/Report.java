package entity;

import java.util.List;

import bk.lvtn.data.DataRow;

/**
 * Created by lazyk on 7/24/2017.
 */

public class Report {

    private String id;

    private String templateId;

    private String createUserId;

    private String createDate;

    private List<DataRow> fieldList;

    private String note;

    private String managerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<DataRow> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DataRow> fieldList) {
        this.fieldList = fieldList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
}
