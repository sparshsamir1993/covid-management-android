package com.example.covid_management_android.model;


import java.util.List;


public class Question {

    private Integer id;
    private String question;
    private String createdAt;
    private String updatedAt;
    private List<QAnswerOption> qAnswerOptions = null;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getQuestion() {
        return question;
    }
    public List<QAnswerOption> getQAnswerOptions() {
        return qAnswerOptions;
    }
    public void setQAnswerOptions(List<QAnswerOption> qAnswerOptions) {
        this.qAnswerOptions = qAnswerOptions;
    }

}
