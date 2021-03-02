package com.example.tunda.fragments;

import androidx.appcompat.app.ActionBar;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tunda.R;
import com.example.tunda.helpers.drawerToggleHelper;

public class EditProfileFragment extends Fragment {

    drawerToggleHelper mDrawerToggleHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mDrawerToggleHelper = (drawerToggleHelper) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ActionBar FragmentActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        FragmentActionBar.setTitle("Edit Profile");
        mDrawerToggleHelper.setDrawerState(false);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mDrawerToggleHelper.setDrawerState(true);
        super.onDestroyView();
    }
}