package com.example.covid_management_android.model;

public class CovidStats {
    long cases;
    long recovered;
    long critical;
    long deaths;
    long tests;
    long todayCases;
    long todayDeaths;
    long todayRecovered;

    public long getTodayRecovered() {
        return todayRecovered;
    }

    public void setTodayRecovered(long todayRecovered) {
        this.todayRecovered = todayRecovered;
    }

    public void setCases(long cases) {
        this.cases = cases;
    }

    public void setRecovered(long recovered) {
        this.recovered = recovered;
    }

    public void setCritical(long critical) {
        this.critical = critical;
    }

    public void setDeaths(long deaths) {
        this.deaths = deaths;
    }

    public void setTests(long tests) {
        this.tests = tests;
    }

    public void setTodayCases(long todayCases) {
        this.todayCases = todayCases;
    }

    public void setTodayDeaths(long todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public long getCases() {
        return cases;
    }

    public long getRecovered() {
        return recovered;
    }

    public long getCritical() {
        return critical;
    }

    public long getDeaths() {
        return deaths;
    }

    public long getTests() {
        return tests;
    }

    public long getTodayCases() {
        return todayCases;
    }

    public long getTodayDeaths() {
        return todayDeaths;
    }
}
