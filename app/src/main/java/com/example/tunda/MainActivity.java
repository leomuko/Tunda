package com.example.tunda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.tunda.activities.TechnicianActivity;
import com.example.tunda.fragments.EditProfile.EditProfileFragment;
import com.example.tunda.fragments.helpAndFeedback.HelpAndFeedbackFragment;
import com.example.tunda.fragments.Home.HomeFragment;
import com.example.tunda.fragments.Seller.SellerFragment;
import com.example.tunda.helpers.drawerToggleHelper;
import com.example.tunda.models.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, drawerToggleHelper {

    private DrawerLayout mDrawerLayout;
    private Long backPressedTime = System.currentTimeMillis();
    private View mHeaderLayout;
    private Fragment mNavFragmentSelected = null;
    private BottomNavigationView mBottomNav;
    private ActionBarDrawerToggle mToggle;
    private MainActivityViewModel mMainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHeaderLayout = navigationView.getHeaderView(0);

        mBottomNav = findViewById(R.id.bottom_navigation);
        mBottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        initViewModel();

    }

    private void initViewModel() {
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.getUserData();
        mMainActivityViewModel.userLiveData.observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                populateNavHeader(userModel);
            }
        });

    }

    private void populateNavHeader(UserModel userModel) {
        ImageView userProfile = mHeaderLayout.findViewById(R.id.header_profile_image);
        TextView userName = mHeaderLayout.findViewById(R.id.header_userName);
        TextView userEmail = mHeaderLayout.findViewById(R.id.header_userEmail);

        userName.setText(userModel.getName());
        userEmail.setText(userModel.getPhone());

        Picasso.get().load(userModel.getProfilePic()).placeholder(R.drawable.ic_account).into(userProfile);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.bottomNav_home_page:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.bottomNav_Stores:
                            selectedFragment = new SellerFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            case R.id.nav_hire:
                Intent intent1 = new Intent(MainActivity.this, TechnicianActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_edit_profile:
                mNavFragmentSelected = new EditProfileFragment();
                mBottomNav.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mNavFragmentSelected).commit();
                break;
            case R.id.nav_help:
                mNavFragmentSelected = new HelpAndFeedbackFragment();
                mBottomNav.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        mNavFragmentSelected).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share App", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logout:
                mMainActivityViewModel.logOut();
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                break;

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mNavFragmentSelected != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                ActivityCompat.finishAffinity(MainActivity.this);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @SuppressLint("WrongConstant")
    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            mToggle.setDrawerIndicatorEnabled(true);
            mToggle.syncState();

        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mToggle.setDrawerIndicatorEnabled(false);
            mToggle.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            mToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            mToggle.syncState();

        }
    }


}