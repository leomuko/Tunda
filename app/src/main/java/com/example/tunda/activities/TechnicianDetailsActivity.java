package com.example.tunda.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tunda.R;
import com.example.tunda.helpers.Constants;
import com.example.tunda.models.ProductModel;
import com.example.tunda.models.TechnicianModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class TechnicianDetailsActivity extends AppCompatActivity {

    ImageView mImageView;
    private TechnicianModel mCurrentProduct;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth firebaseAuth;
    private Button deleteButton;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        mCurrentProduct = intent.getParcelableExtra("product");
        mImageView = findViewById(R.id.imageSlider);

        getSupportActionBar().setTitle(mCurrentProduct.getTechnicianName());
        deleteButton = (Button) findViewById(R.id.deleteButton);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = firebaseAuth.getCurrentUser();

        if (mFirebaseUser != null && Objects.equals(mFirebaseUser.getEmail(), "admin@tunda.com")) {
            deleteButton.setVisibility(View.VISIBLE);
        }


        initialiseViews();
    }

    private void deleteFromDb() {
        DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Technician_table).child(mCurrentProduct.getTcnId());
        dbRef.removeValue();
       startActivity(new Intent(this, TechnicianActivity.class));
       finish();
    }

    private void initialiseViews() {
        CircleImageView circleImageView = findViewById(R.id.seller_profile_image);
        TextView userNameTv = findViewById(R.id.detail_userName);
        TextView locationTv = findViewById(R.id.detail_userLocation);
        Button button = findViewById(R.id.detail_userPhoneNumber);

        TextView productPrice = findViewById(R.id.productPage_price);
        TextView productDetails = findViewById(R.id.productPage_details);

        productPrice.setText(addCommasToNumericString(mCurrentProduct.getTcnBasePrice()));
        productDetails.setText(mCurrentProduct.getTcnDetails());
        Picasso.get().load(mCurrentProduct.getTcnCoverPic())
                .fit()
                .centerCrop()
                .into(mImageView);

        Picasso.get().load(mCurrentProduct.getTcnCoverPic())
                .placeholder(R.drawable.ic_account_1).into(circleImageView);
        userNameTv.setText(mCurrentProduct.getTechnicianName());
        locationTv.setText(mCurrentProduct.getTcnLocation());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+256"+ mCurrentProduct.getTcnContact()));
                startActivity(callIntent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDb();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, TechnicianActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public String addCommasToNumericString(String digits) {
        String result = "";
        for (int i=1; i <= digits.length(); ++i) {
            char ch = digits.charAt(digits.length() - i);
            if (i % 3 == 1 && i > 1) {
                result = "," + result;
            }
            result = ch + result;
        }

        return result;
    }
}