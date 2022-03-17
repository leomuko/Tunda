package com.example.tunda.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.tunda.MainActivity;
import com.example.tunda.R;
import com.example.tunda.helpers.Constants;
import com.example.tunda.models.ProductModel;
import com.example.tunda.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity {

    ImageView mImageView;
    private ProductModel mCurrentProduct;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth firebaseAuth;
    private Button deleteButton;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        mCurrentProduct = intent.getParcelableExtra("product");
        mImageView = findViewById(R.id.imageSlider);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = firebaseAuth.getCurrentUser();

        if (mFirebaseUser != null && mCurrentProduct.getSellerID().equals(mFirebaseUser.getUid())) {
            deleteButton.setVisibility(View.VISIBLE);
        }
        if (mFirebaseUser != null && Objects.equals(mFirebaseUser.getEmail(), "admin@tunda.com")) {
            deleteButton.setVisibility(View.VISIBLE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProductFromDb();
            }
        });


        initialiseViews();
        initialiseViewModel();
    }

    private void deleteProductFromDb() {
        DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Products_table).child(mCurrentProduct.getProductID());
        dbRef.removeValue();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void initialiseViewModel() {
        DetailsActivityViewModel detailsActivityViewModel = new ViewModelProvider(this).get(DetailsActivityViewModel.class);
        detailsActivityViewModel.getUserData(mCurrentProduct.getSellerID());
        detailsActivityViewModel.userLiveData.observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                initialiseUserViews(userModel);
            }
        });
    }

    private void initialiseUserViews(UserModel userModel) {
        CircleImageView circleImageView = findViewById(R.id.seller_profile_image);
        TextView userNameTv = findViewById(R.id.detail_userName);
        TextView locationTv = findViewById(R.id.detail_userLocation);
        Button button = findViewById(R.id.detail_userPhoneNumber);

        Picasso.get().load(userModel.getProfilePic())
                .placeholder(R.drawable.ic_account_1).into(circleImageView);
        userNameTv.setText(userModel.getName());
        locationTv.setText(userModel.getLocation());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+256"+ userModel.getPhone().substring(1)));
                startActivity(callIntent);
            }
        });
    }

    private void initialiseViews() {
        TextView productPrice = findViewById(R.id.productPage_price);
        TextView productDetails = findViewById(R.id.productPage_details);

        productPrice.setText(addCommasToNumericString(mCurrentProduct.getPdtPrice()));
        productDetails.setText(mCurrentProduct.getPdtDescription());
        Picasso.get().load(mCurrentProduct.getCoverPic())
                .fit()
                .centerCrop()
                .into(mImageView);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
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