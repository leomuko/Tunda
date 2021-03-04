package com.example.tunda.fragments.Home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tunda.helpers.FirebaseRepo;
import com.example.tunda.models.ProductModel;

import java.util.List;

public class HomeFragmentViewModel extends AndroidViewModel {

    MutableLiveData< List<ProductModel>> mProductLiveData;
    private FirebaseRepo mFirebaseRepo;

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        mFirebaseRepo = new FirebaseRepo();
    }

    public void loadingProducts() {
        mProductLiveData = mFirebaseRepo.loadingAllProducts();

    }
}
