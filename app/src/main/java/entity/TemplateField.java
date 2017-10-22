package entity;

import java.util.List;

/**
 * Created by lazyk on 7/24/2017.
 */

public class TemplateField {

    private String fieldName;

    private int numberOfRules;

    private List<Rule>  ruleList;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getNumberOfRules() {
        return numberOfRules;
    }

    public void setNumberOfRules(int numberOfRules) {
        this.numberOfRules = numberOfRules;
    }

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }
}
