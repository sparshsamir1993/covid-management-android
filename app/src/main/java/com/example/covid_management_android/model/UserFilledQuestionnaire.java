package com.example.covid_management_android.model;

import android.os.Parcelable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class UserFilledQuestionnaire implements Serializable {

    JSONObject userFilledData;
    public JSONObject getUserFilledData() {
        return userFilledData;
    }
    public void setUserFilledData(JSONObject userFilledData) {
        this.userFilledData = userFilledData;
    }




}
