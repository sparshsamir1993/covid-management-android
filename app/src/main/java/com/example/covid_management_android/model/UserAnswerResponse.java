package com.example.covid_management_android.model;
import java.util.HashMap;

public class UserAnswerResponse {
    private Integer userId;  public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    private HashMap<Integer,Integer> userAnswers;

    public HashMap<Integer,Integer> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(HashMap<Integer,Integer> userAnswers) {
        this.userAnswers = userAnswers;
    }
}
