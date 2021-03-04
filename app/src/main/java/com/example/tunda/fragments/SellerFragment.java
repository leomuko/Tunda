package com.example.tunda.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tunda.R;
import com.example.tunda.activities.LoginActivity;
import com.example.tunda.activities.NewProductActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SellerFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private Button mSignInButton;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_seller, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        mSignInButton = rootView.findViewById(R.id.signInButton);
        fab = rootView.findViewById(R.id.fab);

        if(firebaseUser == null){
            Toast.makeText(getContext(), "To Become a seller, You must login", Toast.LENGTH_LONG).show();
            mSignInButton.setVisibility(View.VISIBLE);
        }else{
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), NewProductActivity.class);
                startActivity(intent);
            }
        });


        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}