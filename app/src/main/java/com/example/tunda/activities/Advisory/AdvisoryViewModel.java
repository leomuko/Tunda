package com.example.tunda.activities.Advisory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tunda.helpers.FirebaseRepo;
import com.example.tunda.models.AdvisoryModel;
import java.util.List;

public class AdvisoryViewModel extends AndroidViewModel {

    public MutableLiveData<List<AdvisoryModel>> mProductLiveData;
    private FirebaseRepo mFirebaseRepo;
    public AdvisoryViewModel(@NonNull Application application) {
        super(application);
        mFirebaseRepo = new FirebaseRepo();
    }

    public void loadingAdvisories(){
        mProductLiveData = mFirebaseRepo.loadingAdvisories();
    }
}
