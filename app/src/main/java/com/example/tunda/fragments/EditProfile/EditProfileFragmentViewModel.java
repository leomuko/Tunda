package com.example.tunda.fragments.EditProfile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tunda.helpers.FirebaseRepo;
import com.example.tunda.models.UserModel;

public class EditProfileFragmentViewModel extends AndroidViewModel {
    private FirebaseRepo mFirebaseRepo;
    public LiveData<UserModel> userLiveData;

    public EditProfileFragmentViewModel(@NonNull Application application) {
        super(application);
        mFirebaseRepo = new FirebaseRepo();
    }
    public void getUserData(){
        userLiveData = mFirebaseRepo.getUserData();
    }

    public void editUserDetails(String name, String phone, UserModel user){
        mFirebaseRepo.editUserDetails(name, phone, user);
    }
}
