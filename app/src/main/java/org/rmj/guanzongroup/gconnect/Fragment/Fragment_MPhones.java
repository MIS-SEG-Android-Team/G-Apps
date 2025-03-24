package org.rmj.guanzongroup.gconnect.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_MPProductImageSlider;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMHome;

import java.util.ArrayList;
import java.util.List;

public class Fragment_MPhones extends Fragment {
    private VMHome mviewModel;
    private RecyclerView poSlider;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homeproductslider, container, false);

        mviewModel = new ViewModelProvider(requireActivity()).get(VMHome.class);
        poSlider = view.findViewById(R.id.img_slider);
        mviewModel.GetBrandNames().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> oBrands) {
                if (oBrands.size() > 0){

                    List<Integer> logos = new ArrayList<>();
                    for (int i = 0; i < oBrands.size(); i++){

                        String sName = oBrands.get(i);
                        switch (sName){
                            case "Apple":
                                logos.add(R.drawable.apple_logo);
                                break;
                            case "HUAWEI":
                                logos.add(R.drawable.huawei_logo);
                                break;
                            case "Honor":
                                logos.add(R.drawable.honor_logo);
                                break;
                            case "Oppo":
                                logos.add(R.drawable.oppo_logo);
                                break;
                            case "Realme":
                                logos.add(R.drawable.realme_logo);
                                break;
                            case "Samsung":
                                logos.add(R.drawable.samsung_logo);
                                break;
                            case "Tecno":
                                logos.add(R.drawable.tecno_logo);
                                break;
                            case "Vivo":
                                logos.add(R.drawable.vivo_logo);
                                break;
                            case "Xiaomi":
                                logos.add(R.drawable.xiaomi_logo);
                                break;
                        }
                    }

                    Adapter_MPProductImageSlider loAdapter = new Adapter_MPProductImageSlider(requireActivity(), logos);
                    poSlider.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
                    poSlider.setAdapter(loAdapter);
                }
            }
        });

        return view;
    }
}
