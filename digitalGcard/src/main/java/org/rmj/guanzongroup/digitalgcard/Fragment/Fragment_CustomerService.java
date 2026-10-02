package org.rmj.guanzongroup.digitalgcard.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.guanzongroup.digitalgcard.R;
import org.rmj.guanzongroup.digitalgcard.ViewModel.VMCustomerService;

public class Fragment_CustomerService extends Fragment {

    private VMCustomerService mViewModel;

    public static Fragment_CustomerService newInstance() {
        return new Fragment_CustomerService();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(VMCustomerService.class);

        return inflater.inflate(R.layout.fragment_customer_service, container, false);
    }

}