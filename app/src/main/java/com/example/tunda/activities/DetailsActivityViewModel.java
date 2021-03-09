package com.example.tunda.activities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tunda.helpers.FirebaseRepo;
import com.example.tunda.models.UserModel;

public class DetailsActivityViewModel extends AndroidViewModel {
    private FirebaseRepo mFirebaseRepo;
    public LiveData<UserModel> userLiveData;
    public DetailsActivityViewModel(@NonNull Application application) {
        super(application);
        mFirebaseRepo = new FirebaseRepo();
    }

    public void getUserData(String uid){
        userLiveData = mFirebaseRepo.getSellerData(uid);
    }
}
