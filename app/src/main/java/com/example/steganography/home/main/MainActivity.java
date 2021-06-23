package com.example.steganography.home.main;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.R;
import com.example.steganography.base.BaseActivity;
import com.example.steganography.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<Nav_header_ViewModel, ActivityMainBinding> {
    SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databinding.HomePageView.setAdapter(adapter);
        viewModel.navigator = this;
        databinding.HomeTabs.setupWithViewPager(databinding.HomePageView);

    }

    @Override
    protected ActivityMainBinding getDataBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_main);

    }

    @Override
    protected Nav_header_ViewModel initViewModel() {
        return new ViewModelProvider(this).get(Nav_header_ViewModel.class);
    }

}
