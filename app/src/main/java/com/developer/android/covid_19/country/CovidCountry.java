package com.developer.android.covid_19.country;


public class CovidCountry {

    String mCountryName,mFlag;
    int mLat,mLan,mTotalCases,mTotalDeaths,mTotalRecovered,mTodayCases,mTodayDeaths;

    public CovidCountry(String mCountryName,String mFlag,int mLat, int mLan,int mTotalCases,int mTotalDeaths, int mTotalRecovered,int mTodayCases,int mTodayDeaths){
        this.mCountryName=mCountryName;
        this.mFlag=mFlag;
        this.mLat=mLat;
        this.mLan=mLan;
        this.mTotalCases=mTotalCases;
        this.mTotalDeaths=mTotalDeaths;
        this.mTotalRecovered=mTotalRecovered;
        this.mTodayCases=mTodayCases;
        this.mTodayDeaths=mTodayDeaths;
    }

    public String getmCountryName() {
        return mCountryName;
    }

    public String getmFlag() {
        return mFlag;
    }

    public int getmLat() {
        return mLat;
    }

    public int getmLan() {
        return mLan;
    }

    public int getmTotalCases() {
        return mTotalCases;
    }

    public int getmTotalDeaths() {
        return mTotalDeaths;
    }

    public int getmTotalRecovered() {
        return mTotalRecovered;
    }

    public int getmTodayCases() {
        return mTodayCases;
    }

    public int getmTodayDeaths() {
        return mTodayDeaths;
    }
}
