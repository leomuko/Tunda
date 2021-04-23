package com.example.tunda.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AdvisoryModel implements Parcelable{

    String advisoryId, advisoryTitle, advisoryDetails;

    public AdvisoryModel() {
    }

    public AdvisoryModel(String advisoryId, String advisoryTitle, String advisoryDetails) {
        this.advisoryId = advisoryId;
        this.advisoryTitle = advisoryTitle;
        this.advisoryDetails = advisoryDetails;
    }

    protected AdvisoryModel(Parcel in) {
        advisoryId = in.readString();
        advisoryTitle = in.readString();
        advisoryDetails = in.readString();
    }

    public static final Creator<AdvisoryModel> CREATOR = new Creator<AdvisoryModel>() {
        @Override
        public AdvisoryModel createFromParcel(Parcel in) {
            return new AdvisoryModel(in);
        }

        @Override
        public AdvisoryModel[] newArray(int size) {
            return new AdvisoryModel[size];
        }
    };

    public String getAdvisoryId() {
        return advisoryId;
    }

    public void setAdvisoryId(String advisoryId) {
        this.advisoryId = advisoryId;
    }

    public String getAdvisoryTitle() {
        return advisoryTitle;
    }

    public void setAdvisoryTitle(String advisoryTitle) {
        this.advisoryTitle = advisoryTitle;
    }

    public String getAdvisoryDetails() {
        return advisoryDetails;
    }

    public void setAdvisoryDetails(String advisoryDetails) {
        this.advisoryDetails = advisoryDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(advisoryId);
        dest.writeString(advisoryTitle);
        dest.writeString(advisoryDetails);
    }
}
