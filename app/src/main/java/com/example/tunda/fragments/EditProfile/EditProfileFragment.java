package com.example.tunda.fragments.EditProfile;

import androidx.appcompat.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tunda.MainActivity;
import com.example.tunda.R;
import com.example.tunda.helpers.drawerToggleHelper;
import com.example.tunda.models.UserModel;

public class EditProfileFragment extends Fragment {

    drawerToggleHelper mDrawerToggleHelper;
    private EditText mUserName;
    private EditText mUserPhone;
    private Button mEditButton;
    private TextView mUserEmail;
    private UserModel myUser;
    private EditProfileFragmentViewModel mEditProfileFragmentViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        initViewModel();

        mUserName = rootView.findViewById(R.id.editName);
        mUserPhone = rootView.findViewById(R.id.editPhoneNumber);
        mUserEmail = rootView.findViewById(R.id.editEmail);
        mEditButton = rootView.findViewById(R.id.edit_button);

        prepareViews();

        mEditButton.setOnClickListener(v -> {
            editUserDetails();
        });

        return rootView;
    }

    private void editUserDetails() {
        String userName = mUserName.getText().toString();
        String phoneNumber = mUserPhone.getText().toString();
        if(userName != null && phoneNumber != null){
            mEditProfileFragmentViewModel.editUserDetails(userName,phoneNumber, myUser);
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);

        }else {
            Toast.makeText(getContext(), "Please enter details", Toast.LENGTH_SHORT).show();

        }
    }


    private void prepareViews() {
        mEditProfileFragmentViewModel.getUserData();

        mEditProfileFragmentViewModel.userLiveData.observe(getViewLifecycleOwner(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel b) {
                myUser = b;
                mUserName.setText(b.getName());
                mUserEmail.setText(b.getEmail());
                if(b.getPhone() != null){
                    mUserPhone.setText(b.getPhone());
                }
            }
        });
    }

    private void initViewModel() {
        mEditProfileFragmentViewModel = new ViewModelProvider(this).get(EditProfileFragmentViewModel.class);
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