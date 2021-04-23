package com.example.tunda.activities.Advisory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tunda.R;
import com.example.tunda.activities.Technician.TechnicianActivity;
import com.example.tunda.helpers.Constants;
import com.example.tunda.models.AdvisoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AdvisoryDetailsActivity extends AppCompatActivity {

    private AdvisoryModel mCurrentProduct;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth firebaseAuth;
    private Button deleteButton;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    TextView titleTv, detailsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisory_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        mCurrentProduct = intent.getParcelableExtra("product");

        getSupportActionBar().setTitle(mCurrentProduct.getAdvisoryTitle());

        titleTv = findViewById(R.id.detailsAdvisoryTitle);
        detailsTv = findViewById(R.id.detailsAdvisoryDetails);
        deleteButton = findViewById(R.id.deleteButton);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = firebaseAuth.getCurrentUser();

        if (mFirebaseUser != null && Objects.equals(mFirebaseUser.getEmail(), "admin@tunda.com")) {
            deleteButton.setVisibility(View.VISIBLE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAdvisory();
            }
        });


        initialiseViews();

    }

    private void deleteAdvisory() {
        DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Advisory_table).child(mCurrentProduct.getAdvisoryId());
        dbRef.removeValue();
        startActivity(new Intent(this, AdvisoryActivity.class));
        finish();
    }

    private void initialiseViews() {
        titleTv.setText(mCurrentProduct.getAdvisoryTitle());
        detailsTv.setText(mCurrentProduct.getAdvisoryDetails());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, AdvisoryActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}