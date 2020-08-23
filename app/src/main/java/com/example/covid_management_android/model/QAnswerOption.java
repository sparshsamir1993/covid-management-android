
package com.example.covid_management_android.model;

import java.util.HashMap;
import java.util.Map;

public class QAnswerOption {

    private Integer id;
    private String optionContent;
    public String getOptionContent() {
        return optionContent;
    }
    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
    private String createdAt;
    private String updatedAt;
    private Integer questionId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



}
