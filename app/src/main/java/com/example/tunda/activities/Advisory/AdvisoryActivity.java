package com.example.tunda.activities.Advisory;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tunda.MainActivity;
import com.example.tunda.R;
import com.example.tunda.activities.Technician.NewTechnicianActivity;
import com.example.tunda.activities.Technician.TechnicianActivity;
import com.example.tunda.activities.Technician.TechnicianDetailsActivity;
import com.example.tunda.helpers.AdvisoryItem;
import com.example.tunda.models.AdvisoryModel;
import com.example.tunda.models.TechnicianModel;
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