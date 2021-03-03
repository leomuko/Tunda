package com.example.tunda;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tunda.helpers.FirebaseRepo;
import com.example.tunda.models.UserModel;

public class MainActivityViewModel extends AndroidViewModel {
    private FirebaseRepo mFirebaseRepo;
    public LiveData<UserModel> userLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mFirebaseRepo = new FirebaseRepo();
    }

    public void getUserData(){
        userLiveData = mFirebaseRepo.getUserData();
    }

    public void logOut(){
        mFirebaseRepo.logUserOut();
    }
}
