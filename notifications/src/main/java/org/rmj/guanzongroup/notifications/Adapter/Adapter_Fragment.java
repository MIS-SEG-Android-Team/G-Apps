package org.rmj.guanzongroup.notifications.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.List;

public class Adapter_Fragment extends FragmentStateAdapter {
    private List<Fragment> mFragmentlist = new ArrayList<>();
    private List<String> mTitle = new ArrayList<>();

    public Adapter_Fragment(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentlist.get(position);
    }
    @Override
    public int getItemCount() {
        return mFragmentlist.size();
    }

    public void addFragment(Fragment fragment) {
        this.mFragmentlist.add(fragment);
    }

    public void addTitle(String title) {
        this.mTitle.add(title);
    }
}

