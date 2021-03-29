package com.example.tunda.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tunda.MainActivity;
import com.example.tunda.R;
import com.example.tunda.helpers.Constants;
import com.example.tunda.helpers.Loading;
import com.example.tunda.helpers.ProductImagesAdapter;
import com.example.tunda.models.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewProductActivity extends AppCompatActivity implements ProductImagesAdapter.ProductImageListener {

    TextView addCoverPic;
    ImageView mAddOtherImages, coverPicImageView;
    RecyclerView imagesRecyclerView;
    private static final int TAKE_PHOTO_CODE = 0;
    private static final int CHOOSE_PHOTO_CODE = 1;
    private static final int TAKE_COVER_CODE = 2;
    private static final int CHOOSE_COVER_CODE = 3;
    List<Bitmap> bitmaps;
    Bitmap coverBitmap;
    ProductImagesAdapter mAdapter;
    TextInputEditText nameEt, descriptionEt, priceEt;
    Button submitButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    private static final String TAG = "NewProductActivity";
    final Loading progressBar = new Loading();
    private String mProductName;
    private String mProductDetails;
    private String mPrice;
    List<Uri> mUriList;
    List<String> otherImageUploadUrlsList;
    Uri mUri;
    private List<String> mOthers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Product");
        bitmaps = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mUriList = new ArrayList<>();
        otherImageUploadUrlsList = new ArrayList<>();

        addCoverPic = findViewById(R.id.addCoverPic);
        addCoverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCoverImage();
            }
        });

        nameEt = findViewById(R.id.productNameEt);
        descriptionEt = findViewById(R.id.productDetailsEt);
        priceEt = findViewById(R.id.productPriceEt);
        submitButton = findViewById(R.id.saveProductButton);

        coverPicImageView = findViewById(R.id.pdtCoverImageView);
        mAddOtherImages = findViewById(R.id.addImagesView);
        mAddOtherImages.setVisibility(View.GONE);
        mAddOtherImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        imagesRecyclerView = findViewById(R.id.productImagesRecyclerView);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });



    }

    private void submit() {
        mProductName = nameEt.getText().toString();
        mProductDetails = descriptionEt.getText().toString();
        mPrice = priceEt.getText().toString();
        if (TextUtils.isEmpty(mProductName)){
            Toast.makeText(this, "Product name cannot be null", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(mProductDetails)){
            Toast.makeText(this, "Product Details cannot be null", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(mPrice)){
            Toast.makeText(this, "Product Price cannot be null", Toast.LENGTH_SHORT).show();
        }else if(coverBitmap == null){
            Toast.makeText(this, "Please Add Cover Pic", Toast.LENGTH_SHORT).show();
        }else{
            progressBar.startLoading(this);
           /* DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Products_table);
            String productId = dbRef.push().getKey();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String sellerId = firebaseUser.getUid();
            ProductModel productModel = new ProductModel(productId, sellerId, productName, price, productDetails);
            dbRef.child(productId).setValue(productModel).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    saveCoverPic(productId);
                    saveProductImages();
                    progressBar.endLoading();
                }else {
                    Log.e(TAG, Objects.requireNonNull(task.getException().getMessage()));
                    progressBar.endLoading();
                }
            });*/

            StorageReference storageReference = mFirebaseStorage.getReference(Constants.Cover_image_path + "/" +mUri.getLastPathSegment());
           storageReference.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           mOthers = new ArrayList<>();
                           //saveOtherImages();

                           saveProduct(uri.toString());
                       }
                   });
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                   Toast.makeText(NewProductActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show();
                    progressBar.endLoading();
               }
           });
        }
    }

    private void saveOtherImages() {

        for(Uri i : mUriList){
            StorageReference storageReference = mFirebaseStorage.getReference(Constants.Other_images + "/" + i.getLastPathSegment());
            storageReference.putFile(i).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mOthers.add(uri.toString());
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(NewProductActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show();
                    progressBar.endLoading();
                }
            });
        }

    }

    private void saveProduct(String coverImageUrl) {
        DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Products_table);
        String productId = dbRef.push().getKey();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String sellerId = firebaseUser.getUid();
        ProductModel productModel = new ProductModel(productId, sellerId, mProductName, mPrice, mProductDetails);
        productModel.setCoverPic(coverImageUrl);
        dbRef.child(productId).setValue(productModel).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
               // saveOtherImageUrlsToDB(productId);
                progressBar.endLoading();
                Toast.makeText(NewProductActivity.this, "Product Successfully Uploaded", Toast.LENGTH_SHORT).show();
                goToMainActivity();
            }else {
                Log.e(TAG, Objects.requireNonNull(task.getException().getMessage()));
                Toast.makeText(NewProductActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                progressBar.endLoading();
            }
        });
    }

    private void saveOtherImageUrlsToDB(String productId) {
        if(mUriList.size() == mOthers.size()){
            DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Other_image_table);
            dbRef.child(productId).setValue(mOthers).addOnCompleteListener(task ->{
                if(!task.isSuccessful()){
                    Log.e(TAG, Objects.requireNonNull(task.getException().getMessage()));
                    Toast.makeText(NewProductActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                    progressBar.endLoading();
                }else{
                    Toast.makeText(NewProductActivity.this, "Images Successfully Uploaded", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            saveOtherImageUrlsToDB(productId);
        }

    }


    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    private void selectCoverImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Cover Image");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_COVER_CODE);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , CHOOSE_COVER_CODE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Product Image");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_PHOTO_CODE);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , CHOOSE_PHOTO_CODE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED) {
            int outWidth, outHeight;
            final int maxSize = 960;
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        if (selectedImage != null) {
                            int inWidth = selectedImage.getWidth();
                            int inHeight = selectedImage.getHeight();
                            if (inWidth > inHeight) {
                                outWidth = maxSize;
                                outHeight = (inHeight * maxSize) / inWidth;
                            } else {
                                outHeight = maxSize;
                                outWidth = (inWidth * maxSize) / inHeight;
                            }
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(selectedImage, outWidth, outHeight, false);
                            bitmaps.add(resizedBitmap);
                            mUriList.add(data.getData());
                        }
                    }

                    break;
                case CHOOSE_PHOTO_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            if (bitmap != null) {
                                int inWidth = bitmap.getWidth();
                                int inHeight = bitmap.getHeight();
                                if (inWidth > inHeight) {
                                    outWidth = maxSize;
                                    outHeight = (inHeight * maxSize) / inWidth;
                                } else {
                                    outHeight = maxSize;
                                    outWidth = (inWidth * maxSize) / inHeight;
                                }
                                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
                                bitmaps.add(resizedBitmap);
                                mUriList.add(data.getData());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                    break;
                case CHOOSE_COVER_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        mUri =  data.getData();
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            if (bitmap != null) {
                                int inWidth = bitmap.getWidth();
                                int inHeight = bitmap.getHeight();
                                if (inWidth > inHeight) {
                                    outWidth = maxSize;
                                    outHeight = (inHeight * maxSize) / inWidth;
                                } else {
                                    outHeight = maxSize;
                                    outWidth = (inWidth * maxSize) / inHeight;
                                }
                                coverBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);
                                coverPicImageView.setImageBitmap(coverBitmap);
                                coverPicImageView.setVisibility(View.VISIBLE);

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case TAKE_COVER_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        mUri =  data.getData();
                        if (selectedImage != null) {
                            int inWidth = selectedImage.getWidth();
                            int inHeight = selectedImage.getHeight();
                            if (inWidth > inHeight) {
                                outWidth = maxSize;
                                outHeight = (inHeight * maxSize) / inWidth;
                            } else {
                                outHeight = maxSize;
                                outWidth = (inWidth * maxSize) / inHeight;
                            }
                           coverBitmap = Bitmap.createScaledBitmap(selectedImage, outWidth, outHeight, false);
                            coverPicImageView.setImageBitmap(coverBitmap);
                            coverPicImageView.setVisibility(View.VISIBLE);

                        }
                    }

                    break;

            }

            initRecyclerView();

        }

    }

    private void initRecyclerView() {
        mAdapter = new ProductImagesAdapter(this, bitmaps, this);
        imagesRecyclerView.setAdapter(mAdapter);
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

    @Override
    public void onDeleteItemClick(int position) {
        mAdapter.removeImageAt(position);
        initRecyclerView();
    }
}