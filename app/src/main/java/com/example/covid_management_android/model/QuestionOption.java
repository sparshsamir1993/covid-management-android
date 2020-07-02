package com.example.covid_management_android.model;

public class QuestionOption {
    private Integer id;

    private String option1;

    private String option2;

    private String createdAt;

    private String updatedAt;

    private Integer questionId;

    private Question question;

    //private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getOption1() {
        return option1;
    }


    public void setOption1(String option1) {
        this.option1 = option1;
    }


    public String getOption2() {
        return option2;
    }


    public void setOption2(String option2) {
        this.option2 = option2;
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




}
