package com.example.tunda.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TechnicianModel implements Parcelable {

    private String tcnId, technicianName,tcnLocation, tcnContact, tcnRole, tcnBasePrice, tcnDetails, tcnCoverPic;

    public TechnicianModel() {
    }

    public TechnicianModel(String tcnId, String technicianName, String tcnLocation, String tcnContact,
                           String tcnRole, String tcnBasePrice, String tcnDetails, String tcnCoverPic) {
        this.tcnId = tcnId;
        this.technicianName = technicianName;
        this.tcnLocation = tcnLocation;
        this.tcnContact = tcnContact;
        this.tcnRole = tcnRole;
        this.tcnBasePrice = tcnBasePrice;
        this.tcnDetails = tcnDetails;
        this.tcnCoverPic = tcnCoverPic;
    }

    protected TechnicianModel(Parcel in) {
        tcnId = in.readString();
        technicianName = in.readString();
        tcnLocation = in.readString();
        tcnContact = in.readString();
        tcnRole = in.readString();
        tcnBasePrice = in.readString();
        tcnDetails = in.readString();
        tcnCoverPic = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tcnId);
        dest.writeString(technicianName);
        dest.writeString(tcnLocation);
        dest.writeString(tcnContact);
        dest.writeString(tcnRole);
        dest.writeString(tcnBasePrice);
        dest.writeString(tcnDetails);
        dest.writeString(tcnCoverPic);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TechnicianModel> CREATOR = new Creator<TechnicianModel>() {
        @Override
        public TechnicianModel createFromParcel(Parcel in) {
            return new TechnicianModel(in);
        }

        @Override
        public TechnicianModel[] newArray(int size) {
            return new TechnicianModel[size];
        }
    };

    public String getTcnId() {
        return tcnId;
    }

    public void setTcnId(String tcnId) {
        this.tcnId = tcnId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getTcnLocation() {
        return tcnLocation;
    }

    public void setTcnLocation(String tcnLocation) {
        this.tcnLocation = tcnLocation;
    }

    public String getTcnContact() {
        return tcnContact;
    }

    public void setTcnContact(String tcnContact) {
        this.tcnContact = tcnContact;
    }

    public String getTcnRole() {
        return tcnRole;
    }

    public void setTcnRole(String tcnRole) {
        this.tcnRole = tcnRole;
    }

    public String getTcnBasePrice() {
        return tcnBasePrice;
    }

    public void setTcnBasePrice(String tcnBasePrice) {
        this.tcnBasePrice = tcnBasePrice;
    }

    public String getTcnDetails() {
        return tcnDetails;
    }

    public void setTcnDetails(String tcnDetails) {
        this.tcnDetails = tcnDetails;
    }

    public String getTcnCoverPic() {
        return tcnCoverPic;
    }

    public void setTcnCoverPic(String tcnCoverPic) {
        this.tcnCoverPic = tcnCoverPic;
    }
}
