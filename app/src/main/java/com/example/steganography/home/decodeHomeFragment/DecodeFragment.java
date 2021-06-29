package com.example.steganography.home.decodeHomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.base.BaseFragment;
import com.example.steganography.databinding.DecodeFragmentBinding;
import com.example.steganography.imageInImage.decode.DecodeImageActivity;
import com.example.steganography.textInImage.decode.DecodeTextActivity;

public class DecodeFragment extends BaseFragment<DecodeHomeFragmentViewModel, DecodeFragmentBinding> implements DecodeHomeFragmentNavigator {

    private DecodeHomeFragmentViewModel mViewModel;

    public static DecodeFragment newInstance() {
        return new DecodeFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBinding.setVm(viewModel);
        viewModel.navigator = this;
    }

    @Override
    protected DecodeHomeFragmentViewModel getViewModel() {
        return new ViewModelProvider(this).get(DecodeHomeFragmentViewModel.class);
    }

    @Override
    public DecodeFragmentBinding getViewBinding() {
        return DecodeFragmentBinding.inflate(LayoutInflater.from(getContext()));

    }


    @Override
    public void openDecodeTextActivity() {
        Intent intent = new Intent(this.getContext(), DecodeTextActivity.class);
        startActivity(intent);
    }

    @Override
    public void OpenDecodeImageActivity() {
        Intent intent = new Intent(this.getContext(), DecodeImageActivity.class);
        startActivity(intent);

    }
}
