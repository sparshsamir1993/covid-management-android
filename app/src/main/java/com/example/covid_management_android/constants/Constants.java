package com.example.covid_management_android.constants;

public final class Constants {
    private Constants(){
    }

    public static final String BASE_URL = "http://10.0.2.2:5050/api/v1/user";

    //////////////////////    Shared Preferences    //////////////////////

    public static final String USER_SURVEY_COMPLETED = "userSurveyCompleted";
    public static final String TOKEN = "token";
    public static final String REFRESH_TOKEN = "refreshToken";


    //////////////////////    APPOINTMENT CONSTANTS    //////////////////////

    public static final String SLOT_AVAILABLE = "AVAILABLE";
    public static final String SLOT_NOT_AVAILABLE = " NOT AVAILABLE";
}
