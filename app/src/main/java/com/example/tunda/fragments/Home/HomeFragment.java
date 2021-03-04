package com.example.tunda.fragments.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tunda.R;
import com.example.tunda.helpers.Loading;
import com.example.tunda.helpers.homeItem;
import com.example.tunda.models.ProductModel;
import com.xwray.groupie.GroupAdapter;

import java.util.List;


public class HomeFragment extends Fragment {

    private GroupAdapter mAdapter;
    private HomeFragmentViewModel mHomeFragmentViewModel;
    private FragmentActivity mC;
    private RecyclerView mHomeRecyclerView;
    final Loading progressBar = new Loading();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initHomeViewModel();
        mHomeRecyclerView = rootView.findViewById(R.id.home_recycler_view);
        mC = getActivity();
        progressBar.startLoading(mC);



        mHomeRecyclerView.setLayoutManager(new GridLayoutManager(mC, 2));

        mAdapter = new GroupAdapter();
        mHomeRecyclerView.setAdapter(mAdapter);
        fecthingProducts();
        return rootView;
    }

    private void fecthingProducts() {
        mHomeRecyclerView.setAdapter(mAdapter);
        mHomeFragmentViewModel.loadingProducts();
        mHomeFragmentViewModel.mProductLiveData.observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {

                for(ProductModel p : productModels){
                    mAdapter.add(new homeItem(p, mC));
                    mAdapter.notifyDataSetChanged();
                }
                progressBar.endLoading();
            }
        });
    }

    private void initHomeViewModel() {
        mHomeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
    }
}