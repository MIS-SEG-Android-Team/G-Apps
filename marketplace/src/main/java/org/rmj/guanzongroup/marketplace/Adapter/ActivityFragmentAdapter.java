package org.rmj.guanzongroup.marketplace.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragmentAdapter extends FragmentStateAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String>  mTitle = new ArrayList<>();

    public ActivityFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }
    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }

    public void addTitle(String title){
        this.mTitle.add(title);
    }
    public void addFragment(Fragment fragment){
        this.mFragmentList.add(fragment);
    }
    public void clear(){
        this.mFragmentList.clear();
        this.mTitle.clear();
    }
}
