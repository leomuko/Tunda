package com.example.tunda.fragments.Seller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tunda.R;
import com.example.tunda.activities.DetailsActivity;
import com.example.tunda.activities.LoginActivity;
import com.example.tunda.activities.NewProductActivity;
import com.example.tunda.helpers.Loading;
import com.example.tunda.helpers.homeItem;
import com.example.tunda.models.ProductModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SellerFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private Button mSignInButton;
    private FloatingActionButton fab;
    private GroupAdapter mAdapter;
    private FragmentActivity mC;
    private RecyclerView mRecyclerView;
    private SellerViewModel mSellerViewModel;
    private FirebaseUser mFirebaseUser;
    private ProgressBar mProgressBar;
    private List<ProductModel> allProductsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_seller, container, false);
        mC = getActivity();

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = firebaseAuth.getCurrentUser();
        mSignInButton = rootView.findViewById(R.id.signInButton);
        fab = rootView.findViewById(R.id.fab);
        mProgressBar = rootView.findViewById(R.id.progressBar);

        if (mFirebaseUser == null) {
            Toast.makeText(getContext(), "To Become a seller, You must login", Toast.LENGTH_LONG).show();
            mSignInButton.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), NewProductActivity.class);
                startActivity(intent);
            }
        });


        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = rootView.findViewById(R.id.sellerRv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mC, 2));

        mAdapter = new GroupAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(mC, DetailsActivity.class);
                intent.putExtra("product",allProductsList.get(mAdapter.getAdapterPosition(item)) );
                startActivity(intent);
            }
        });


        mSellerViewModel = new ViewModelProvider(this).get(SellerViewModel.class);
        if(mFirebaseUser != null){
            fecthingProducts();
        }

        return rootView;
    }

    private void fecthingProducts() {
        mRecyclerView.setAdapter(mAdapter);
        mSellerViewModel.loadingProducts();
        mSellerViewModel.productHashMap.observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                allProductsList.clear();
                for (ProductModel p : productModels) {


                    if(p.getSellerID().equals(mFirebaseUser.getUid())){
                        allProductsList.add(p);
                        mAdapter.add(new homeItem(p, mC));
                        mAdapter.notifyDataSetChanged();
                    }
                    mProgressBar.setVisibility(View.GONE);

                }
            }
        });

    }
}