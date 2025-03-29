package org.rmj.guanzongroup.gconnect.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.ViewPagerProperty;
import org.rmj.guanzongroup.ganado.Activities.Activity_ProductSelection;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Events;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Products;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMHome;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    private VMHome mViewModel;
    private RClientInfo loClient;

    private LinearLayout layout_header;
    private LinearLayout layout_events;
    private ViewPager2 slider_events;
    private Adapter_Events loAdapter;

    private LinearLayout layout_products;
    private TabLayout tab_products;
    private RecyclerView rcv_products;
    private ShapeableImageView siv_kay;

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

        layout_header = v.findViewById(R.id.layout_header);
        slider_events = v.findViewById(R.id.slider_events);
        layout_events = v.findViewById(R.id.layout_events);

        layout_products = v.findViewById(R.id.layout_products);
        tab_products = v.findViewById(R.id.tab_products);
        rcv_products = v.findViewById(R.id.rcv_products);
        siv_kay = v.findViewById(R.id.siv_kay);
    }

    private void initListener(){

        //todo: viewpager page change listener
        slider_events.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        slider_events.setCurrentItem(position);

                        //todo: observe, this might cause a crash / delay of updating the adapter.
                        //todo: should be called once only
                        loAdapter.notifyDataSetChanged();

                    }
                });

            }
        });

    }

    private void initTabs(){
        tab_products.removeAllTabs();
        tab_products.addTab(tab_products.newTab().setText("Motorcycles"));
        tab_products.addTab(tab_products.newTab().setText("Mobile"));
    }

    private void initAdapterData(int tabIndex){

        List<Adapter_Products.Product_Data> laProducts = new ArrayList<>();

        switch (tabIndex){

            case 0:

                rcv_products.setVisibility(View.VISIBLE);
                siv_kay.setVisibility(View.GONE);

                laProducts.add(
                        new Adapter_Products.Product_Data(
                                "Yamaha", R.drawable.yamaha, R.drawable.yamahalogo)
                );

                laProducts.add(
                        new Adapter_Products.Product_Data(
                                "Honda", R.drawable.honda, R.drawable.hondalogo)
                );

                laProducts.add(
                        new Adapter_Products.Product_Data(
                                "Suzuki", R.drawable.suzuki, R.drawable.suzukilogo)
                );

                laProducts.add(
                        new Adapter_Products.Product_Data(
                                "Kawasaki", R.drawable.kay, R.drawable.kay)
                );

                break;

            case 1:

                rcv_products.setVisibility(View.GONE);
                siv_kay.setVisibility(View.VISIBLE);

                break;
        }

        rcv_products.setAdapter(new Adapter_Products(laProducts, new Adapter_Products.onSelectListener() {
            @Override
            public void onSelect(String brandName) {

                if (mViewModel.GetBrandID(brandName.toUpperCase()) != null){

                    if (!mViewModel.GetBrandID(brandName.toUpperCase()).isEmpty()){

                        Intent loIntent = new Intent(requireActivity(), Activity_ProductSelection.class);
                        loIntent.putExtra("lsBrandID", mViewModel.GetBrandID(brandName.toUpperCase()));
                        loIntent.putExtra("lsBrandNm", brandName.toUpperCase());

                        startActivity(loIntent);

                    }
                }
            }
        }));

        rcv_products.setLayoutManager(
                new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        );

    }

    private void initAdapter(){

        /** GET ALL EVENTS IMPORTED**/

        mViewModel.getEvents().observe(getViewLifecycleOwner(), new Observer<List<EEvents>>() {
            @Override
            public void onChanged(List<EEvents> eEvents) {

                if (eEvents != null){

                    if (eEvents.size() > 0){

                        loAdapter = new Adapter_Events(eEvents, slider_events, new Adapter_Events.onSelectListener() {
                            @Override
                            public void onSelect(int position, String eventIDxx) {

                                Fragment_Dashboard loParent = (Fragment_Dashboard) getParentFragment();

                                if (loParent != null){

                                    Bundle loArgs = new Bundle();
                                    loArgs.putString("eventID", String.valueOf(eventIDxx));

                                    loParent.viewPager.setCurrentItem(3);
                                    loParent.loPoll.setArguments(loArgs);
                                    loParent.botNav.setSelectedItemId(R.id.nav_Poll);

                                }

                            }
                        });

                        slider_events.setAdapter(loAdapter);

                        /**
                         * Set viewpager properties and design
                         * Custom library for designing viewpager.
                         * Add new designs , for future layout viewpager design
                         * Guillier 03/28/2025
                         **/
                        ViewPagerProperty loViewPagerProperty = new ViewPagerProperty(slider_events);

                        loViewPagerProperty.initSliderPadding(
                                new ViewPagerProperty.Padding_Property(150, 150,
                                        0, 0, false, false, 3)
                        );

                        loViewPagerProperty.initSliderPageTransformer();

                        layout_events.setVisibility(View.VISIBLE);

                    }else {
                        layout_events.setVisibility(View.GONE);
                    }

                }else {
                    layout_events.setVisibility(View.GONE);
                }
            }
        });

        /** PRODUCT LIST ADAPTER INITIALIZATION **/

        initAdapterData(tab_products.getSelectedTabPosition()); //triggers on first login

        tab_products.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                initAdapterData(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initDisplayonLogin(int isDisplayed){
        layout_header.setVisibility(isDisplayed);
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
                    initListener();
                    initTabs();
                    initAdapter();

                }else {

                    initDisplayonLogin(View.GONE);
                }
            }
        });

    }

}