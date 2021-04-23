package com.example.tunda.activities.Technician;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tunda.MainActivity;
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
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            } else {
                mRecyclerView.setAdapter(mAdapter);
                mTechnicianViewModel.loadingProducts();
                mTechnicianViewModel.mProductLiveData.observe(TechnicianActivity.this, new Observer<List<TechnicianModel>>() {
                    @Override
                    public void onChanged(List<TechnicianModel> technicianModels) {
                        allProductsList.clear();
                        allProductsList.addAll(technicianModels);
                        populateRecyclerView();
                        mProgressBar.setVisibility(View.GONE);

                    }
                });
            }

        }

        private void populateRecyclerView () {
            mAdapter.clear();
            for (TechnicianModel t : allProductsList) {
                mAdapter.add(new technicianItem(t, mC));
                mAdapter.notifyDataSetChanged();
            }
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){
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