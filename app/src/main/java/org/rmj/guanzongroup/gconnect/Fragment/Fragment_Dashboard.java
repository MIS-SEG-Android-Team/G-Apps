package org.rmj.guanzongroup.gconnect.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.FragmentAdapter;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.notifications.Fragment.Fragment_Promotion;

public class Fragment_Dashboard extends Fragment {
    private static final String TAG = Fragment_Dashboard.class.getSimpleName();
    private View view;
    private BottomNavigationView botNav;
    private ViewPager2 viewPager;
    private RClientInfo loClient;

    public static Fragment_Dashboard newInstance() {
        return new Fragment_Dashboard();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        loClient = new RClientInfo(requireActivity());

        initViews();
        setupPages();

        loClient.getClientInfo().observe(getViewLifecycleOwner(), new Observer<EClientInfo>() {
            @Override
            public void onChanged(EClientInfo eClientInfo) {

                if (eClientInfo != null){
                    //botNav.getMenu().findItem(R.id.nav_Bingo).setVisible(true); todo: for future use, postponed
                    botNav.getMenu().findItem(R.id.nav_Barcode).setVisible(true);
                    botNav.getMenu().findItem(R.id.nav_Poll).setVisible(true);
                }else {
                    //botNav.getMenu().findItem(R.id.nav_Bingo).setVisible(false); todo: for future use, postponed
                    botNav.getMenu().findItem(R.id.nav_Barcode).setVisible(false);
                    botNav.getMenu().findItem(R.id.nav_Poll).setVisible(false);
                }
            }
        });

        botNav.setBackground(null);
        botNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                viewPager.setCurrentItem(0);
                Log.d(TAG, "Home Selected.");
            }  else if(item.getItemId() == R.id.nav_promos){
                viewPager.setCurrentItem(1);
                Log.d(TAG, "Guanzon Panalo Selected.");
            }  else if(item.getItemId() == R.id.nav_Barcode){
                viewPager.setCurrentItem(2);
                Log.d(TAG, "Barcode Selected.");
            }  else if(item.getItemId() == R.id.nav_Poll){
                viewPager.setCurrentItem(3);
                Log.d(TAG, "Voting Poll Selected.");
            }
            return true;
        });

        return view;
    }
    private void initViews() {
        viewPager = view.findViewById(R.id.viewpager);
        botNav = view.findViewById(R.id.bottom_navigation);
    }
    private void setupPages(){
        Fragment[] loFragments = new Fragment[]{
                new Fragment_Home(),
                new Fragment_Promotion(),
                new Fragment_PhoneBarcode(),
                new Fragment_Poll()};

        FragmentAdapter loAdapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle());
        loAdapter.initFragments(loFragments);

        viewPager.setAdapter(loAdapter);
    }
}