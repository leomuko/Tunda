package com.example.tunda.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tunda.helpers.FirebaseRepo;
import com.example.tunda.models.ProductModel;
import com.example.tunda.models.TechnicianModel;

import java.util.List;

public class TechnicianViewModel extends AndroidViewModel {

    public MutableLiveData<List<TechnicianModel>> mProductLiveData;
    private FirebaseRepo mFirebaseRepo;

    public TechnicianViewModel(@NonNull Application application) {
        super(application);
        mFirebaseRepo = new FirebaseRepo();
    }

    public void loadingProducts() {
        mProductLiveData = mFirebaseRepo.loadingTechnicians();

    }
}
