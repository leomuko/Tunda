package com.example.tunda.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {

    private String userID, email, name, phone, profilePic;

    public UserModel() {
    }

    public UserModel(String userID, String email, String name, String phone) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    protected UserModel(Parcel in) {
        userID = in.readString();
        email = in.readString();
        name = in.readString();
        phone = in.readString();
        profilePic = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(profilePic);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
