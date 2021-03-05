package com.example.tunda.fragments.Home;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tunda.R;
import com.example.tunda.activities.DetailsActivity;
import com.example.tunda.helpers.Loading;
import com.example.tunda.helpers.homeItem;
import com.example.tunda.models.ProductModel;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private GroupAdapter mAdapter;
    private HomeFragmentViewModel mHomeFragmentViewModel;
    private FragmentActivity mC;
    private RecyclerView mHomeRecyclerView;
    private ProgressBar mProgressBar;
    private List<ProductModel> allProductsList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initHomeViewModel();
        mHomeRecyclerView = rootView.findViewById(R.id.home_recycler_view);
        mC = getActivity();
        mProgressBar = rootView.findViewById(R.id.progressBar);



        mHomeRecyclerView.setLayoutManager(new GridLayoutManager(mC, 2));

        mAdapter = new GroupAdapter();
        mHomeRecyclerView.setAdapter(mAdapter);
        fecthingProducts();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent intent = new Intent(mC, DetailsActivity.class);
                intent.putExtra("product",allProductsList.get(mAdapter.getAdapterPosition(item)) );
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void fecthingProducts() {
        mHomeRecyclerView.setAdapter(mAdapter);
        mHomeFragmentViewModel.loadingProducts();
        mHomeFragmentViewModel.mProductLiveData.observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                allProductsList.clear();
                allProductsList.addAll(productModels);

                for(ProductModel p : productModels){
                    mAdapter.add(new homeItem(p, mC));
                    mAdapter.notifyDataSetChanged();
                }
               mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initHomeViewModel() {
        mHomeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
    }
}