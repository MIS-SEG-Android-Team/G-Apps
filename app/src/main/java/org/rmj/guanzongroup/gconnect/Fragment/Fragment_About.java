package org.rmj.guanzongroup.gconnect.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.rmj.guanzongroup.gconnect.databinding.FragmentAboutBinding;
import org.rmj.guanzongroup.notifications.Activity.Activity_Browser;

public class Fragment_About extends Fragment {

    private FragmentAboutBinding mBinding;

    public Fragment_About() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentAboutBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent loIntent = new Intent(requireActivity(), Activity_Browser.class);

        mBinding.lnkTermsC.setOnClickListener(v -> {
            loIntent.putExtra("cTermsDsp", 0);
            startActivity(loIntent);
        });

        mBinding.lnkPrivcy.setOnClickListener(v -> {
            loIntent.putExtra("cTermsDsp", 1);
            loIntent.putExtra("args", "3");
            loIntent.putExtra("url_link", "https://www.guanzongroup.com.ph/guanzon-connect/");
            startActivity(loIntent);
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

}