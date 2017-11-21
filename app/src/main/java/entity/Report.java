package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bk.lvtn.data.DataRow;

/**
 * Created by lazyk on 7/24/2017.
 */

public class Report implements Serializable {

    private String id;

    private String reportName;

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    private String templateId;

    private String createDate;

    private String updateDate;

    private List<DataRow> fieldList;

    private String note;

    private String managerId;

    public String getId() {
        return id;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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


    public void addValue(String key,List<String> value){
        DataRow a = new DataRow(key,value);
        fieldList.add(a);
    }

    public Report(){
        fieldList = new ArrayList<DataRow>();
    }

}
