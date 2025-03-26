package org.rmj.guanzongroup.gconnect.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class Adapter_Product_Tabs extends FragmentStateAdapter {

    List<Fragment> laFragments;

    public Adapter_Product_Tabs(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void initFragments(List<Fragment> laFragments){
        this.laFragments = laFragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return laFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return laFragments.size();
    }
}
