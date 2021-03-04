package com.example.tunda.fragments.Seller;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tunda.helpers.FirebaseRepo;
import com.example.tunda.models.ProductModel;

import java.util.List;

public class SellerViewModel extends AndroidViewModel {
    MutableLiveData<List<ProductModel>> productHashMap;
    private FirebaseRepo mFirebaseRepo;
    public SellerViewModel(@NonNull Application application) {
        super(application);
        mFirebaseRepo = new FirebaseRepo();
    }
    public void loadingProducts() {
        productHashMap = mFirebaseRepo.loadingAllProducts();

    }
}
