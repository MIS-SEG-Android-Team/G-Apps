package org.rmj.guanzongroup.marketplace.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.guanzongroup.marketplace.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMShippingAddress;

public class FragmentShippingAddress extends Fragment {

    private VMShippingAddress mViewModel;

    public static FragmentShippingAddress newInstance() {
        return new FragmentShippingAddress();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(VMShippingAddress.class);
        // TODO: Use the ViewModel
        
        return inflater.inflate(R.layout.fragment_shipping_addres, container, false);
    }
}