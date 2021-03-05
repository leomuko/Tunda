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

import com.example.tunda.R;
import com.example.tunda.models.ProductModel;
import com.example.tunda.models.UserModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity {

    ImageView mImageView;
    private ProductModel mCurrentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        mCurrentProduct = intent.getParcelableExtra("product");
        mImageView = findViewById(R.id.imageSlider);


        initialiseViews();
        initialiseViewModel();
    }

    private void initialiseViewModel() {
        DetailsActivityViewModel detailsActivityViewModel = new ViewModelProvider(this).get(DetailsActivityViewModel.class);
        detailsActivityViewModel.getUserData();
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
        Button button = findViewById(R.id.detail_userPhoneNumber);

        Picasso.get().load(userModel.getProfilePic())
                .placeholder(R.drawable.ic_account_1).into(circleImageView);
        userNameTv.setText(userModel.getName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:+256"+ userModel.getPhone()));
                startActivity(callIntent);
            }
        });
    }

    private void initialiseViews() {
        TextView productPrice = findViewById(R.id.productPage_price);
        TextView productDetails = findViewById(R.id.productPage_details);

        productPrice.setText("UGX " +mCurrentProduct.getPdtPrice());
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
}