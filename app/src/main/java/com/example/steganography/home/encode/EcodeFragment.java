package com.example.steganography.home.encode;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.steganography.base.BaseFragment;
import com.example.steganography.databinding.EcodeFragmentBinding;
import com.example.steganography.imageInImage.encode.EncodeImageActivity;
import com.example.steganography.textInImage.encode.EncodeTextActivity;

public class EcodeFragment extends BaseFragment<EcodeViewModel, EcodeFragmentBinding> implements EncodeNavigator {


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataBinding.setVm(viewModel);
        viewModel.navigator = this;


    }

    @Override
    protected EcodeViewModel getViewModel() {
        return new ViewModelProvider(this).get(EcodeViewModel.class);
    }

    @Override
    public EcodeFragmentBinding getViewBinding() {


        return EcodeFragmentBinding.inflate(LayoutInflater.from(getContext()));


    }

    @Override
    public void openEncodeTextActivity() {
        Intent intent = new Intent(this.getContext(), EncodeTextActivity.class);
        startActivity(intent);
    }

    @Override
    public void openEncodeImageActivity() {
        Intent intent = new Intent(this.getContext(), EncodeImageActivity.class);
        startActivity(intent);

    }
}
