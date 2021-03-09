package com.example.tunda.helpers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.tunda.models.ProductModel;
import com.example.tunda.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    public MutableLiveData<UserModel> getSellerData(String uid){
        MutableLiveData<UserModel> userModelMutableLiveData = new MutableLiveData<>();
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

        return userModelMutableLiveData;
    }

    public void logUserOut() {
        firebaseAuth.signOut();
    }

    public MutableLiveData<List<ProductModel>> loadingAllProducts(){
        List<ProductModel> allProducstList = new ArrayList<>();
        MutableLiveData<List<ProductModel>> mutableLiveData = new MutableLiveData<>();
        DatabaseReference allProductsDbref = mFirebaseDatabase.getReference(Constants.Products_table);

        allProductsDbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    allProducstList.add(s.getValue(ProductModel.class));
                }
                mutableLiveData.postValue(allProducstList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return mutableLiveData;
    }

    public void editUserDetails(String name, String phone, UserModel user) {


        user.setName(name);
        user.setPhone(phone);
        DatabaseReference dbRef = mFirebaseDatabase
                .getReference(Constants.Users_table).child(user.getUserID());
        dbRef.setValue(user);

    }
}
