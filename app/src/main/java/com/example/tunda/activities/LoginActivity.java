package com.example.tunda.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tunda.MainActivity;
import com.example.tunda.R;
import com.example.tunda.helpers.Loading;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextView mGoToRegisterPage;
    private TextInputEditText mEmailInput, mPasswordInput;
    private TextInputLayout mEmailInputLayout, mPasswordInputLayout;
    Button mloginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        mAuth = FirebaseAuth.getInstance();

        mGoToRegisterPage = findViewById(R.id.toRegisterText);
        mGoToRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mEmailInput = findViewById(R.id.emailInput);
        mPasswordInput = findViewById(R.id.passwordInput);
        mloginButton = findViewById(R.id.buttonLogin);
        mEmailInputLayout = findViewById(R.id.emailInputLayout);
        mPasswordInputLayout = findViewById(R.id.passwordInputLayout);

        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUserLogin();
            }
        });


    }

    private void doUserLogin() {
        String email = mEmailInput.getText().toString();
        String password = mPasswordInput.getText().toString();
        if(TextUtils.isEmpty(email)){
            mEmailInputLayout.setError("Fill in Email Field");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailInputLayout.setError("Email address not valid");
        }
        else if(TextUtils.isEmpty(password)){
            mPasswordInputLayout.setError("Fill in password");
        }else {
            final Loading progressBar = new Loading();
            progressBar.startLoading(this);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressBar.endLoading();
                                Toast.makeText(LoginActivity.this, "User Logged in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this, "User Login failed "+
                                        Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.endLoading();
                            }
                        }
                    });
        }

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