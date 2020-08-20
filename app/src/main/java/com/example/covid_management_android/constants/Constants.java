package com.example.covid_management_android.constants;

public final class Constants {
    private Constants(){
    }

    public static final String BASE_URL = "http://192.168.0.110:5050;/api/v1/user";
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
    public static final String APPOINTMENT_CONFIRMED = "CONFIRMED";
    public static final String TEST_RESULT_PENDING = "RESULT PENDING";

    public static final String TEST_RESULTS_DELIVERED = "RESULT AVAILABLE";
    //////////////////////    INTENT CONSTANTS    //////////////////////

    public static final String HOSPITAL_DATA = "hospitalData";
    public static final String APPOINTMENT_ID = "appointmentId";
}
