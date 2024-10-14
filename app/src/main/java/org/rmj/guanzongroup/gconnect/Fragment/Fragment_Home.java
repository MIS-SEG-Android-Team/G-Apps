package org.rmj.guanzongroup.gconnect.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.guanzongroup.gconnect.Adapter.ProductSlider_Adapter;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.g3appdriver.lib.Promotions.Adapter_ImageSlider;
import org.rmj.g3appdriver.lib.Promotions.model.HomeImageSliderModel;
import org.rmj.guanzongroup.marketplace.ViewModel.VMHome;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    private VMHome mViewModel;
    private MaterialCardView mcv_promotions;
    private SliderView poSliderx;
    private SliderView imgSlider_mc;
    private SliderView imgSlider_phones;
    private TabLayout tab_choices;
    private ViewPager2 vpage;
    private AccountInfo loAccount;

    public Fragment_Home() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(VMHome.class);
        loAccount = new AccountInfo(requireActivity());

        View view = inflater.inflate(R.layout.fragment_home, container, true);

        initViews(view);
        displayData();

        if (loAccount.getVerificationStatus() > 0){
            tab_choices.setVisibility(View.VISIBLE);
            vpage.setVisibility(View.VISIBLE);
        }else {
            tab_choices.setVisibility(View.GONE);
            vpage.setVisibility(View.GONE);
        }

        return view;
    }

    private void initViews(View v) {
        mcv_promotions = v.findViewById(R.id.mcv_promotions);
        poSliderx = v.findViewById(R.id.imgSlider);
        imgSlider_mc = v.findViewById(R.id.imgSlider_mc);
        imgSlider_phones = v.findViewById(R.id.imgSlider_phones);
        tab_choices = v.findViewById(R.id.tab_choices);
        vpage = v.findViewById(R.id.vpage);

        poSliderx.setIndicatorAnimation(IndicatorAnimationType.WORM);
        poSliderx.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        poSliderx.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        poSliderx.setIndicatorSelectedColor(Color.WHITE);
        poSliderx.setIndicatorUnselectedColor(Color.GRAY);
        poSliderx.setScrollTimeInSec(5);
        poSliderx.startAutoCycle();

        imgSlider_mc.setIndicatorAnimation(IndicatorAnimationType.WORM);
        imgSlider_mc.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imgSlider_mc.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        imgSlider_mc.setIndicatorSelectedColor(Color.WHITE);
        imgSlider_mc.setIndicatorUnselectedColor(Color.GRAY);
        imgSlider_mc.setScrollTimeInSec(5);
        imgSlider_mc.startAutoCycle();

        imgSlider_phones.setIndicatorAnimation(IndicatorAnimationType.WORM);
        imgSlider_phones.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imgSlider_phones.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        imgSlider_phones.setIndicatorSelectedColor(Color.WHITE);
        imgSlider_phones.setIndicatorUnselectedColor(Color.GRAY);
        imgSlider_phones.setScrollTimeInSec(5);
        imgSlider_phones.startAutoCycle();
    }

    private void displayData() {
        setSliderImages();
        setTabSlider();
    }

    private void setSliderImages() {

        List<HomeImageSliderModel> loSliders = new ArrayList<>();

        mViewModel.GetPromoLinkList().observe(getViewLifecycleOwner(), ePromos -> {
            try {
                if (ePromos.size() > 0){
                    mcv_promotions.setVisibility(View.VISIBLE);

                    for (int x = 0; x < ePromos.size(); x++) {
                        loSliders.add(new HomeImageSliderModel(ePromos.get(x).getImageUrl()));
                    }

                    Adapter_ImageSlider adapter = new Adapter_ImageSlider(loSliders, args -> {
                        try{
                            Intent intent = new Intent("android.intent.action.SUCCESS_LOGIN");
                            intent.putExtra("url_link", ePromos.get(args).getPromoUrl());
                            intent.putExtra("browser_args", "1");
                            intent.putExtra("args", "promo");
                            requireActivity().sendBroadcast(intent);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    });

                    poSliderx.setSliderAdapter(adapter);
                }else {
                    mcv_promotions.setVisibility(View.GONE);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setTabSlider(){

        ProductSlider_Adapter sliderAdapter = new ProductSlider_Adapter(getChildFragmentManager(), getLifecycle());
        sliderAdapter.initFragments(new Fragment[]{new Fragment_MCProducts(), new Fragment_MPhones()});

        vpage.setAdapter(sliderAdapter);

        tab_choices.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpage.setCurrentItem(tab.getPosition(), true);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}