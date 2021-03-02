package com.example.tunda.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tunda.R;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    private TextView mToLoginText;
    private TextInputEditText mUserNameInput, mEmailInput, mPhoneInput, mPasswordInput, mConfirmPasswordInput;
    private Button mCreateAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Account");

        mToLoginText = findViewById(R.id.toLoginText);
        mUserNameInput = findViewById(R.id.userNameInput);
        mEmailInput = findViewById(R.id.emailInput);
        mPhoneInput = findViewById(R.id.phoneInput);
        mPasswordInput = findViewById(R.id.passwordInput);
        mConfirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        mCreateAccountButton = findViewById(R.id.buttonLogin);
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