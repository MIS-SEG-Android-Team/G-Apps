package org.rmj.guanzongroup.gconnect.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import org.rmj.guanzongroup.gconnect.Fragment.Fragment_MCProducts;
import org.rmj.guanzongroup.gconnect.Fragment.Fragment_MPhones;

import java.util.ArrayList;
import java.util.List;

public class ProductSlider_Adapter extends FragmentStateAdapter {

    private Fragment[] fragments;

    public ProductSlider_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void initFragments(Fragment[] fragments) {
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
