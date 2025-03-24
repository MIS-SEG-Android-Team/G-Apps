package org.rmj.guanzongroup.notifications.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import org.rmj.guanzongroup.notifications.R;
import org.rmj.guanzongroup.notifications.ViewModel.VMNotifications;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Notifications extends Fragment {
    private VMNotifications mViewModel;
    private TabLayout tabLayout;
    private TextView noNotif;
    private ViewPager2 viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        mViewModel = new ViewModelProvider(requireActivity()).get(VMNotifications.class);
        FragmentAdapter loAdapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle());

        initViews(view);
        setTabListener();

        viewPager.setAdapter(loAdapter);

        return view;
    }

    private void initViews(View v) {
        tabLayout = v.findViewById(R.id.tab_layout);
        noNotif = v.findViewById(R.id.lbl_no_notifications);
        viewPager = v.findViewById(R.id.viewpager);
    }
    private void setTabListener(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private static class FragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> loList = new ArrayList<>();
        private final List<String> loTitle = new ArrayList<>();

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);

            loList.add(new Fragment_MessageList());
            loList.add(new Fragment_Notification());
            loList.add(new Fragment_Panalo());

            loTitle.add("Notifications");
            loTitle.add("Messages");
            loTitle.add("Panalo");
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return loList.get(position);
        }
        @Override
        public int getItemCount() {
            return loList.size();
        }
    }
}