package com.example.tunda.activities.Advisory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tunda.R;
import com.example.tunda.activities.Technician.NewTechnicianActivity;
import com.example.tunda.helpers.Constants;
import com.example.tunda.models.AdvisoryModel;
import com.example.tunda.models.TechnicianModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NewAdvisoryActivity extends AppCompatActivity {

    TextInputEditText titleEt, detailsEt;
    Button submitButton;
    String title, details;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advisory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create New Advisory");

        titleEt = findViewById(R.id.advisoryTitleEt);
        detailsEt = findViewById(R.id.advisoryDetailsEt);
        submitButton = findViewById(R.id.saveAdvisoryButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        title = titleEt.getText().toString();
        details = detailsEt.getText().toString();

        if (TextUtils.isEmpty(title)){
            Toast.makeText(this, "The Title Field Must Not Be Empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(details)){
            Toast.makeText(this, "The Details Field Must Not Be Empty", Toast.LENGTH_SHORT).show();
        }else {
            DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Advisory_table);
            String advisoryId = dbRef.push().getKey();
            AdvisoryModel advisoryModel = new AdvisoryModel(advisoryId, title, details);
            dbRef.child(advisoryId).setValue(advisoryModel).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    Toast.makeText(NewAdvisoryActivity.this, "Advisory Successfully Created", Toast.LENGTH_SHORT).show();
                    goToAdvisoryActivity();
                }else {
                    Toast.makeText(NewAdvisoryActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void goToAdvisoryActivity() {
        Intent intent = new Intent(this, AdvisoryActivity.class);
        startActivity(intent);
        finish();
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