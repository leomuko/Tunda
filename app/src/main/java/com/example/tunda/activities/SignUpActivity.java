package com.example.tunda.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tunda.MainActivity;
import com.example.tunda.R;
import com.example.tunda.helpers.Constants;
import com.example.tunda.helpers.Loading;
import com.example.tunda.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private TextView mToLoginText;
    private TextInputEditText mUserNameInput, mEmailInput, mPhoneInput, mPasswordInput,
            mConfirmPasswordInput, mLocationInput;
    private TextInputLayout mUserNameInputLayout, mEmailInputLayout, mPhoneInputLayout,
            mPasswordInputLayout, mConfirmPasswordInputLayout, mLocationInputLayout;
    private Button mCreateAccountButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Account");

        mAuth = FirebaseAuth.getInstance();

        mToLoginText = findViewById(R.id.toLoginText);
        mUserNameInput = findViewById(R.id.userNameInput);
        mEmailInput = findViewById(R.id.emailInput);
        mPhoneInput = findViewById(R.id.phoneInput);
        mPasswordInput = findViewById(R.id.passwordInput);
        mConfirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        mCreateAccountButton = findViewById(R.id.buttonSignIn);
        mUserNameInputLayout = findViewById(R.id.userNameInputLayout);
        mEmailInputLayout = findViewById(R.id.emailInputLayout);
        mPhoneInputLayout = findViewById(R.id.phoneInputLayout);
        mPasswordInputLayout = findViewById(R.id.passwordInputLayout);
        mConfirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        mLocationInput = findViewById(R.id.locationInput);
        mLocationInputLayout = findViewById(R.id.locationInputLayout);
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserProfile();

            }
        });
        mToLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createUserProfile() {
        String email = mEmailInput.getText().toString();
        String password = mPasswordInput.getText().toString();
        String confirmedPassword = mConfirmPasswordInput.getText().toString();
        String userName = mUserNameInput.getText().toString();
        String phoneNumber = mPhoneInput.getText().toString();
        String location = mLocationInput.getText().toString();
        if(TextUtils.isEmpty(userName)){
            mUserNameInputLayout.setError("Fill in User Name");
        }else if(TextUtils.isEmpty(email)){
            mEmailInputLayout.setError("Fill in email");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailInputLayout.setError("Enter Valid Email");
        }else if(TextUtils.isEmpty(phoneNumber)){
            mPhoneInputLayout.setError("Fill in phone number");
        }else if(TextUtils.isEmpty(location)){
            mLocationInputLayout.setError("Location Field cannot be empty");
        } else if(TextUtils.isEmpty(password)){
            mPasswordInputLayout.setError("Fill in password");
        }else if(TextUtils.isEmpty(confirmedPassword)){
            mConfirmPasswordInputLayout.setError("Fill in field");
        }else if(!password.equals(confirmedPassword)){
            mConfirmPasswordInputLayout.setError("Passwords must match");
        }else{
            final Loading progressBar = new Loading();
            progressBar.startLoading(this);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                UserModel userModel = new UserModel(firebaseUser.getUid(), firebaseUser.getEmail(), userName, phoneNumber);
                                userModel.setLocation(location);
                                saveUserToDb(userModel);
                                progressBar.endLoading();

                                Toast.makeText(SignUpActivity.this, "User Successfully created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(SignUpActivity.this, "User Creation Failed "+
                                        Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.endLoading();
                            }
                        }
                    });
        }

    }

    private void saveUserToDb(UserModel userModel) {
        DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Users_table)
                .child(userModel.getUserID());

        dbRef.setValue(userModel).addOnCompleteListener(userCreationTask ->{
            if(userCreationTask.isSuccessful()){
                Log.d(TAG, "User Successfully Saved");
            }else {
                Log.e(TAG, Objects.requireNonNull(userCreationTask.getException().getMessage()));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}