package entity;

import java.util.List;

/**
 * Created by lazyk on 7/24/2017.
 */

public class Template {

    private Long id;

    private Long numberOfField;

    private List<TemplateField> templateFieldList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumberOfField() {
        return numberOfField;
    }

    public void setNumberOfField(Long numberOfField) {
        this.numberOfField = numberOfField;
    }

    public List<TemplateField> getTemplateFieldList() {
        return templateFieldList;
    }

    public void setTemplateFieldList(List<TemplateField> templateFieldList) {
        this.templateFieldList = templateFieldList;
    }
}
