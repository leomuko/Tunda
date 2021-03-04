package com.example.tunda.helpers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.tunda.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseRepo {
    private static final String TAG = "FIREBASE";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public MutableLiveData<UserModel> getUserData(){
        MutableLiveData<UserModel> userModelMutableLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            return  userModelMutableLiveData;
        }else{
            String uid = firebaseUser.getUid();
            DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Users_table)
                    .child(uid);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userModelMutableLiveData.setValue(snapshot.getValue(UserModel.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "onCancelled: "+ error.getMessage());
                }
            });
        }
        return userModelMutableLiveData;
    }

    public void logUserOut() {
        firebaseAuth.signOut();
    }
}