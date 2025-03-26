package org.rmj.guanzongroup.gconnect.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.ViewPagerProperty;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Events;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Product_Tabs;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMHome;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    private VMHome mViewModel;
    private RClientInfo loClient;

    private LinearLayout layout_events;
    private ViewPager2 slider_events;
    private Adapter_Events loAdapter;

    private LinearLayout layout_products;
    private TabLayout tab_products;
    private ViewPager2 vpage_products;

    public Fragment_Home() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(VMHome.class);
        loClient = new RClientInfo(requireActivity());

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        initObservables();

        return view;
    }

    private void initViews(View v) {

        slider_events = v.findViewById(R.id.slider_events);
        layout_events = v.findViewById(R.id.layout_events);

        layout_products = v.findViewById(R.id.layout_products);
        tab_products = v.findViewById(R.id.tab_products);
        vpage_products = v.findViewById(R.id.vpage_products);
    }

    private void initListener(){

        //todo: viewpager page change listener
        slider_events.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                slider_events.setCurrentItem(position);

                //todo: observe, this might cause a crash / delay of updating the adapter.
                //todo: should be called once only
                loAdapter.notifyDataSetChanged();

            }
        });

    }

    private void initTabs(){
        tab_products.addTab(tab_products.newTab().setText("Motorcycles"));
        tab_products.addTab(tab_products.newTab().setText("Mobile"));
    }

    private void initAdapter(){

        List<Adapter_Events.GuanzonEvents> laEvents = new ArrayList<>();

        laEvents.add(new Adapter_Events.GuanzonEvents("dreamboy", R.drawable.dreamboy));
        laEvents.add(new Adapter_Events.GuanzonEvents("campusprincess", R.drawable.campusprincess));
        laEvents.add(new Adapter_Events.GuanzonEvents("bikerbabe", R.drawable.kay));
        laEvents.add(new Adapter_Events.GuanzonEvents("guanzonbulilit", R.drawable.kay));

        loAdapter = new Adapter_Events(laEvents, slider_events);
        slider_events.setAdapter(loAdapter);

        //todo: set viewpager properties, this is a custom library for designing viewpager
        ViewPagerProperty loViewPagerProperty = new ViewPagerProperty(slider_events);

        loViewPagerProperty.initSliderPadding(
                new ViewPagerProperty.Padding_Property(150, 150,
                        0, 0, false, false, 3)
        );

        loViewPagerProperty.initSliderPageTransformer();

        List<Fragment> laFragments = new ArrayList<>();

        Fragment_Products fragment_motor = new Fragment_Products();

        Bundle args1 = new Bundle();
        args1.putString("product", "motorcycle");
        fragment_motor.setArguments(args1);

        Fragment_Products fragment_mobile = new Fragment_Products();

        Bundle args2 = new Bundle();
        args2.putString("product", "mobile");
        fragment_mobile.setArguments(args2);

        laFragments.add(fragment_motor);
        laFragments.add(fragment_mobile);


        Adapter_Product_Tabs adapterProductTabs = new Adapter_Product_Tabs(getParentFragmentManager(), getLifecycle());

        adapterProductTabs.initFragments(laFragments);

        vpage_products.setAdapter(adapterProductTabs);

    }

    private void initDisplayonLogin(int isDisplayed){
        layout_events.setVisibility(isDisplayed);
        layout_products.setVisibility(isDisplayed);
    }

    private void initObservables(){

        loClient.getClientInfo().observe(getViewLifecycleOwner(), new Observer<EClientInfo>() {
            @Override
            public void onChanged(EClientInfo eClientInfo) {

                if (eClientInfo != null){

                    //todo: display after login
                    initDisplayonLogin(View.VISIBLE);
                    initAdapter();
                    initListener();
                    initTabs();

                    if (eClientInfo.getVerified() > 0){

                    }else {

                    }
                }else {
                    initDisplayonLogin(View.GONE);
                }
            }
        });

    }

}