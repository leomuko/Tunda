package com.example.tunda.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.tunda.models.TechnicianModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

public class NewTechnicianActivity extends AppCompatActivity {

    TextInputEditText nameEt, roleEt, detailsEt, priceEt, locationEt, phoneEt;
    ImageView coverPicImageView;
    TextView addImageTv;
    Button submitButton;
    Bitmap coverBitmap;
    Uri mUri;
    private static final int TAKE_COVER_CODE = 0;
    private static final int CHOOSE_COVER_CODE = 1;
    String name, role, details, price, location, phone;
    final Loading progressBar = new Loading();
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
    private static final String TAG = "NewTechnicianActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_technician);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create New Technician Product");


        nameEt = findViewById(R.id.technicianNameEt);
        roleEt = findViewById(R.id.technicianRoleEt);
        detailsEt = findViewById(R.id.technicianDetailsEt);
        priceEt = findViewById(R.id.technicianBasePriceEt);
        coverPicImageView = findViewById(R.id.techCoverImageView);
        addImageTv = findViewById(R.id.addCoverPic);
        submitButton = findViewById(R.id.saveTechnicianButton);
        locationEt = findViewById(R.id.technicianLocation);
        phoneEt = findViewById(R.id.technicianContact);

        addImageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });


    }

    private void chooseImage() {
        final CharSequence[] options = { "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Technician Profile Image");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

            /*    if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_COVER_CODE);

                } else*/ if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, CHOOSE_COVER_CODE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void submit() {
        name = nameEt.getText().toString();
        role = roleEt.getText().toString();
        details = detailsEt.getText().toString();
        price = priceEt.getText().toString();
        location = locationEt.getText().toString();
        phone = phoneEt.getText().toString();
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "The Name Field Must Not Be Empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(role)){
            Toast.makeText(this, "The Role Field Must Not Be Empty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(details)){
            Toast.makeText(this, "The Details Field Must Not Be Empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(location)){
            Toast.makeText(this, "The Location Field Must Not Be Empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(price)){
            Toast.makeText(this, "The Price Field Must Not Be Empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "The Contact Field Must Not Be Empty", Toast.LENGTH_SHORT).show();
        }else if(coverBitmap == null){
            Toast.makeText(this, "Please Add Cover Pic", Toast.LENGTH_SHORT).show();
        }else{
            progressBar.startLoading(this);
            StorageReference storageReference = mFirebaseStorage.getReference(Constants.Cover_image_path + "/" +mUri.getLastPathSegment());
            storageReference.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            saveProduct(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(NewTechnicianActivity.this, Objects.requireNonNull(e.getMessage()), Toast.LENGTH_SHORT).show();
                    progressBar.endLoading();
                }
            });
        }
    }


    private void saveProduct(String coverImageUrl) {
        DatabaseReference dbRef = mFirebaseDatabase.getReference(Constants.Technician_table);
        String tcnId = dbRef.push().getKey();
        TechnicianModel technicianModel = new TechnicianModel(tcnId, name, location, phone, role, price, details, coverImageUrl);
        dbRef.child(tcnId).setValue(technicianModel).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                progressBar.endLoading();
                Toast.makeText(NewTechnicianActivity.this, "Technician Successfully Uploaded", Toast.LENGTH_SHORT).show();
                goToTechnicianActivity();
            }else {
                Log.e(TAG, Objects.requireNonNull(task.getException().getMessage()));
                Toast.makeText(NewTechnicianActivity.this, Objects.requireNonNull(task.getException().getMessage()), Toast.LENGTH_SHORT).show();
                progressBar.endLoading();
            }
        });
    }

    private void goToTechnicianActivity() {
        Intent intent1 = new Intent(this, TechnicianActivity.class);
        startActivity(intent1);
        finish();
    }

    private void selectImage() {
        final CharSequence[] options = { "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Cover Image");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

            /*    if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_COVER_CODE);

                } else*/ if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, CHOOSE_COVER_CODE);

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
        if (resultCode != RESULT_CANCELED) {
            int outWidth, outHeight;
            final int maxSize = 960;
            switch (requestCode) {
                case CHOOSE_COVER_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        mUri = data.getData();
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
                        mUri = data.getData();
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