package com.example.steganography.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;

import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityHomeAcivityBinding;
import com.example.steganography.home.main.SectionPagerAdapter;
import com.example.steganography.login.Login;
import com.example.steganography.login.LoginViewModel;
import com.example.steganography.ui.About;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


public class HomeActivity extends BaseActivity<HomeViewModel, ActivityHomeAcivityBinding>
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PICK_IMAGE = 100;
    TextView userName;
    public NavigationView navigationView;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    TextView userEmail;
    ImageView profile_image;
    Uri image;
    String mImageUri;
    SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private AppBarConfiguration mAppBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        databinding.setHomeViewModel(viewModel);
        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        tabLayout = findViewById(R.id.Home_tabs);
        viewPager = findViewById(R.id.Home_Page_View);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setNavigationViewListener();


        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        // get user name and email textViews
        userName = headerView.findViewById(R.id.textView_user_name);
        userEmail = headerView.findViewById(R.id.textView_user_email);
        profile_image = headerView.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        userName.setText(preferences.getString("user_name", ""));
        userEmail.setText(preferences.getString("user_email", ""));
        mImageUri = preferences.getString("image", "");
        profile_image.setImageURI(Uri.parse(mImageUri));


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    Uri selected_image = data.getData();
                    // this.grantUriPermission(this.getPackageName(), selected_image, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    // this.getContentResolver().takePersistableUriPermission(selected_image, takeFlags);


                    editor.putString("image", String.valueOf(selected_image));
                    editor.commit();
                    mImageUri = preferences.getString("image", "");
                    profile_image.setImageURI(Uri.parse(mImageUri));


                    break;
            }
        }
    }

    @Override
    protected ActivityHomeAcivityBinding getDataBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_home_acivity);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //  dataHolder=new DataHolder();

        // userName.setText("doaa essam");
        // userEmail.setText(dataHolder.authUser.getEmail());
        //  userName.setText(dataHolder.authUser.getDisplayName());

        // userEmail.setText(dataHolder.dataBaseUser.getUser_email());
        //Log.e("message","ok2"+ dataHolder.dataBaseUser.getUser_email());


    }

    @Override
    protected HomeViewModel initViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_logout: {
                LoginViewModel loginViewModel = new LoginViewModel(getApplication());
                loginViewModel.logout();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(HomeActivity.this, Login.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.nav_about_us: {
                Intent intent = new Intent(HomeActivity.this, About.class);
                startActivity(intent);

                break;

            }
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}