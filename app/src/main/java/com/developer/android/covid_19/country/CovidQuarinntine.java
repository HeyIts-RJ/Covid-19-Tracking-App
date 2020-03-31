package com.developer.android.covid_19.country;

public class CovidQuarinntine {

    String mDistrict;
    int mCases;
    double mLat,mLan;


    public CovidQuarinntine(String mDistrict, int mCases, double mLat, double mLan) {
        this.mDistrict = mDistrict;
        this.mCases = mCases;
        this.mLat = mLat;
        this.mLan = mLan;
    }

    public String getmDistrict() {
        return mDistrict;
    }

    public int getmCases() {
        return mCases;
    }

    public double getmLat() {
        return mLat;
    }

    public double getmLan() {
        return mLan;
    }
}
