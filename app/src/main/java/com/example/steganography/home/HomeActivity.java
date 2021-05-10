package com.example.steganography.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityHomeAcivityBinding;
import com.example.steganography.home.main.MainActivity;
import com.example.steganography.home.main.SectionPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


public class HomeActivity extends BaseActivity<HomeViewModel, ActivityHomeAcivityBinding>
        implements NavigationView.OnNavigationItemSelectedListener {
    public NavigationView navigationView;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    MainActivity mainActivity = new MainActivity();
    SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        tabLayout = findViewById(R.id.Home_tabs);
        viewPager = findViewById(R.id.Home_Page_View);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
    }


    @Override
    protected HomeViewModel initViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);

    }

    @Override
    protected int getLayOut() {
        return R.layout.activity_home_acivity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}