package com.example.steganography.home.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.steganography.DecodeFragment;
import com.example.steganography.home.encode.EcodeFragment;


public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                return new EcodeFragment();
            case 1:
            default:
                return new DecodeFragment();

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Encode";
            case 1:
            default:
                return "decode";
        }


    }


}
