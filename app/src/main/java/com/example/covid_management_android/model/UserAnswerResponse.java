package com.example.covid_management_android.model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAnswerResponse {
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public ArrayList<AnswerData> getUserAnswers() {
        return userAnswers;
    }


    private ArrayList<AnswerData> userAnswers;

    public void setUserAnswers(ArrayList<AnswerData> userAnswers) {
        this.userAnswers = userAnswers;
    }
}
