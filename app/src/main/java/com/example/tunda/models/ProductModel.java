package com.example.tunda.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ProductModel implements Parcelable {

    private String productID, sellerID, pdtName, pdtPrice,  pdtDescription, coverPic;

    public ProductModel() {
    }

    public ProductModel(String productID, String sellerID, String pdtName, String pdtPrice, String pdtDescription) {
        this.productID = productID;
        this.sellerID = sellerID;
        this.pdtName = pdtName;
        this.pdtPrice = pdtPrice;
        this.pdtDescription = pdtDescription;
    }
    protected ProductModel(Parcel in) {
        productID = in.readString();
        sellerID = in.readString();
        pdtName = in.readString();
        pdtPrice = in.readString();
        pdtDescription = in.readString();
        coverPic = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productID);
        dest.writeString(sellerID);
        dest.writeString(pdtName);
        dest.writeString(pdtPrice);
        dest.writeString(pdtDescription);
        dest.writeString(coverPic);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductModel> CREATOR = new Creator<ProductModel>() {
        @Override
        public ProductModel createFromParcel(Parcel in) {
            return new ProductModel(in);
        }

        @Override
        public ProductModel[] newArray(int size) {
            return new ProductModel[size];
        }
    };

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getPdtName() {
        return pdtName;
    }

    public void setPdtName(String pdtName) {
        this.pdtName = pdtName;
    }

    public String getPdtPrice() {
        return pdtPrice;
    }

    public void setPdtPrice(String pdtPrice) {
        this.pdtPrice = pdtPrice;
    }

    public String getPdtDescription() {
        return pdtDescription;
    }

    public void setPdtDescription(String pdtDescription) {
        this.pdtDescription = pdtDescription;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }
}
