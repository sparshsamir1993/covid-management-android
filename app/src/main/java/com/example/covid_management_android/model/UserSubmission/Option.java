
package com.example.covid_management_android.model.UserSubmission;

import java.util.HashMap;
import java.util.Map;

public class Option {


    private Integer id;

    private String optionContent;

    private String createdAt;

    private String updatedAt;

    private Integer questionId;

    private Question question;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getOptionContent() {
        return optionContent;
    }


    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }


    public String getCreatedAt() {
        return createdAt;
    }

     public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    public Integer getQuestionId() {
        return questionId;
    }


    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

       public Question getQuestion() {
        return question;
    }


    public void setQuestion(Question question) {
        this.question = question;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

       public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
