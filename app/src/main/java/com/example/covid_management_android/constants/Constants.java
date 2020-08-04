package com.example.covid_management_android.constants;

public final class Constants {
    private Constants(){
    }

    public static final String BASE_URL = "http://10.0.2.2:5050/api/v1/user";
    public static final String SHARED_PREF_MAIN_NAME = "covidManagement";


    //////////////////////    Shared Preferences    //////////////////////

    public static final String USER_SURVEY_COMPLETED = "userSurveyCompleted";
    public static final String TOKEN = "token";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_NAME = "userName";
    public static final String USER_ID = "userId";


    //////////////////////    APPOINTMENT CONSTANTS    //////////////////////

    public static final String SLOT_AVAILABLE = "AVAILABLE";
    public static final String SLOT_NOT_AVAILABLE = "NOT AVAILABLE";


    //////////////////////    INTENT CONSTANTS    //////////////////////

    public static final String HOSPITAL_DATA = "hospitalData";
}
