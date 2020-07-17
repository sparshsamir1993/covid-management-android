
package com.example.covid_management_android.model.UserSubmission;

import com.google.gson.JsonElement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class UserSubmittedAnswers implements Serializable {


    private Integer id;

    public Integer getId() {
        return id;
    }

    private Integer userId;

    public Integer getOptionId() {
        return optionId;
    }

    private Integer optionId;

    private String createdAt;

    private String updatedAt;

    private Boolean isOptionCorrect;
        private Option option;
      private Map<String, Object> additionalProperties = new HashMap<String, Object>();




        public void setId(Integer id) {
        this.id = id;
    }

        public Integer getUserId() {
        return userId;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }





    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
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


    public Boolean getIsOptionCorrect() {
        return isOptionCorrect;
    }


    public void setIsOptionCorrect(Boolean isOptionCorrect) {
        this.isOptionCorrect = isOptionCorrect;
    }


    public Option getOption() {
        return option;
    }


    public void setOption(Option option) {
        this.option = option;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
