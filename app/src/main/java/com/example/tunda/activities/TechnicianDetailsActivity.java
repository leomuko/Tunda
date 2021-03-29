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
import com.example.tunda.models.ProductModel;
import com.example.tunda.models.TechnicianModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TechnicianDetailsActivity extends AppCompatActivity {

    ImageView mImageView;
    private TechnicianModel mCurrentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_details);

        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        mCurrentProduct = intent.getParcelableExtra("product");
        mImageView = findViewById(R.id.imageSlider);

        getSupportActionBar().setTitle(mCurrentProduct.getTechnicianName());

        initialiseViews();
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