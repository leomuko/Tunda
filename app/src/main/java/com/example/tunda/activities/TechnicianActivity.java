package com.example.tunda.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tunda.R;
import com.example.tunda.helpers.technicianItem;
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

public class TechnicianActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton fab;
    private GroupAdapter mAdapter;
    private Context mC;
    private RecyclerView mRecyclerView;
    private FirebaseUser mFirebaseUser;
    private ProgressBar mProgressBar;
    private List<TechnicianModel> allProductsList = new ArrayList<>();
    private TechnicianViewModel mTechnicianViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician);
        mC = getApplicationContext();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hire Technician");

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = firebaseAuth.getCurrentUser();
        fab = findViewById(R.id.fab);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.hire_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mC, 2));

        mAdapter = new GroupAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(mC, TechnicianDetailsActivity.class);
                intent.putExtra("product", allProductsList.get(mAdapter.getAdapterPosition(item)));
                startActivity(intent);
            }
        });

        if (mFirebaseUser != null && Objects.equals(mFirebaseUser.getEmail(), "admin@tunda.com")) {
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mC, NewTechnicianActivity.class);
                startActivity(intent);
            }
        });

        mTechnicianViewModel = new ViewModelProvider(this).get(TechnicianViewModel.class);

        fecthingProducts();
    }

    private void fecthingProducts() {
        mRecyclerView.setAdapter(mAdapter);
        mTechnicianViewModel.loadingProducts();
        mTechnicianViewModel.mProductLiveData.observe(TechnicianActivity.this, new Observer<List<TechnicianModel>>() {
            @Override
            public void onChanged(List<TechnicianModel> technicianModels) {
                allProductsList.clear();
                for(TechnicianModel t : technicianModels){
                    allProductsList.add(t);
                    mAdapter.add(new technicianItem(t, mC));
                    mAdapter.notifyDataSetChanged();
                }
                mProgressBar.setVisibility(View.GONE);

            }
        });

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