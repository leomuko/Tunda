package com.example.tunda.activities.Advisory;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tunda.MainActivity;
import com.example.tunda.R;
import com.example.tunda.helpers.AdvisoryItem;
import com.example.tunda.models.AdvisoryModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AdvisoryActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton fab;
    private GroupAdapter mAdapter;
    private Context mC;
    private RecyclerView mRecyclerView;
    private FirebaseUser mFirebaseUser;
    private ProgressBar mProgressBar;
    private AdvisoryViewModel mAdvisoryViewModel;
    private List<AdvisoryModel> allAdvisories = new ArrayList<>();
    Button filterButton;
    String cropToFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisory);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Advisories");

        mC = getApplicationContext();

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = firebaseAuth.getCurrentUser();
        fab = findViewById(R.id.fab);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.advisory_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mC));
        filterButton = findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });

        if (mFirebaseUser != null && Objects.equals(mFirebaseUser.getEmail(), "admin@tunda.com")) {
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mC, NewAdvisoryActivity.class);
                startActivity(intent);
            }
        });

        mAdapter = new GroupAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(mC, AdvisoryDetailsActivity.class);
                intent.putExtra("product", allAdvisories.get(mAdapter.getAdapterPosition(item)));
                startActivity(intent);
            }
        });

        mAdvisoryViewModel = new ViewModelProvider(this).get(AdvisoryViewModel.class);

        fetchingAdvisories();
    }

    private void openFilterDialog() {
        Dialog dialog;
        String[] cropList = {"None","Beans","Maize", "Peas"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Crop To Filter");
        builder.setSingleChoiceItems(cropList, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Done!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog) dialog).getListView();
                Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                cropToFilter = String.valueOf(checkedItem);
                if(String.valueOf(checkedItem).equals("None")){
                    fetchingAdvisories();
                }else{
                    doFilter(String.valueOf(checkedItem));
                }


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void doFilter(String cropType) {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected) {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.GONE);
        }else {
            mRecyclerView.setAdapter(mAdapter);
            mAdvisoryViewModel.loadingAdvisories();
            mAdvisoryViewModel.mProductLiveData.observe(AdvisoryActivity.this, new Observer<List<AdvisoryModel>>() {
                @Override
                public void onChanged(List<AdvisoryModel> advisoryModels) {
                    allAdvisories.clear();
                    for(AdvisoryModel a : advisoryModels){
                        if(a.getAdvisoryCropType().equals(cropType)){
                            allAdvisories.add(a);
                        }
                    }
                    populateRecyclerView();
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void fetchingAdvisories() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected) {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.GONE);
        }else {
            mRecyclerView.setAdapter(mAdapter);
            mAdvisoryViewModel.loadingAdvisories();
            mAdvisoryViewModel.mProductLiveData.observe(AdvisoryActivity.this, new Observer<List<AdvisoryModel>>() {
                @Override
                public void onChanged(List<AdvisoryModel> advisoryModels) {
                    allAdvisories.clear();
                    allAdvisories.addAll(advisoryModels);
                    populateRecyclerView();
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void populateRecyclerView() {
        mAdapter.clear();
        for(AdvisoryModel a : allAdvisories){
            mAdapter.add(new AdvisoryItem(a, mC));
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}