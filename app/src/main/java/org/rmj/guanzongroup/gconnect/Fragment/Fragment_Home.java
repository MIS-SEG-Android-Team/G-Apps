package org.rmj.guanzongroup.gconnect.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import org.rmj.g3appdriver.dev.Database.Entities.EClientInfo;
import org.rmj.g3appdriver.dev.Repositories.RClientInfo;
import org.rmj.g3appdriver.etc.ViewPagerProperty;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Events;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMHome;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    private VMHome mViewModel;
    private RClientInfo loClient;

    private LinearLayout layout_events;
    private ViewPager2 slider_events;

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
    }

    private void initListener(){

    }

    private void initAdapter(){

        List<Adapter_Events.GuanzonEvents> laEvents = new ArrayList<>();

        laEvents.add(new Adapter_Events.GuanzonEvents("dreamboy", R.drawable.dreamboy));
        laEvents.add(new Adapter_Events.GuanzonEvents("campusprincess", R.drawable.campusprincess));
        laEvents.add(new Adapter_Events.GuanzonEvents("bikerbabe", R.drawable.kay));
        laEvents.add(new Adapter_Events.GuanzonEvents("guanzonbulilit", R.drawable.kay));


        Adapter_Events loAdapter = new Adapter_Events(laEvents, slider_events);
        slider_events.setAdapter(loAdapter);

        ViewPagerProperty loViewPagerProperty = new ViewPagerProperty(slider_events);

        loViewPagerProperty.initSliderPadding(
                new ViewPagerProperty.Padding_Property(150, 150,
                        0, 0, false, false, 3)
        );

        loViewPagerProperty.initSliderPageTransformer();

    }

    private void initDisplayonLogin(int isDisplayed){
        layout_events.setVisibility(isDisplayed);
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