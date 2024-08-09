package org.rmj.guanzongroup.gconnect.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.g3appdriver.dev.Database.Entities.EMcBrand;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.guanzongroup.ganado.Activities.Activity_ProductSelection;
import org.rmj.guanzongroup.ganado.Dialog.DialogDisclosure;
import org.rmj.guanzongroup.ganado.ViewModel.VMBrandList;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_MCProductImageSlider;
import org.rmj.guanzongroup.gconnect.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Fragment_MCProducts extends Fragment {

    private VMBrandList mViewModel;
    private RecyclerView img_slider;
    private ActivityResultLauncher<String[]> poRequest;
    private DialogDisclosure dialogDisclosure;
    private Intent loIntent;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homeproductslider, container, false);

        mViewModel = new ViewModelProvider(requireActivity()).get(VMBrandList.class);
        img_slider = view.findViewById(R.id.img_slider);
        dialogDisclosure = new DialogDisclosure(requireActivity());

        poRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            startActivity(loIntent);
        });

        initProducts();

        return view;
    }

    public void initProducts(){
        mViewModel.getBrandList().observe(requireActivity(), new Observer<List<EMcBrand>>() {
            @Override
            public void onChanged(List<EMcBrand> eMcBrands) {
                if (eMcBrands.size() > 0) {
                    HashMap<Integer, String> logos = new HashMap<>();

                    for (int i = 0; i < eMcBrands.size(); i++) {
                        String sName = eMcBrands.get(i).getBrandNme();
                        switch (sName) {
                            case "HONDA":
                                logos.put(R.drawable.hondalogo, eMcBrands.get(i).getBrandIDx());
                                break;
                            case "SUZUKI":
                                logos.put(R.drawable.suzuki, eMcBrands.get(i).getBrandIDx());
                                break;
                            case "KAWASAKI":
                                logos.put(R.drawable.kawasakilogo, eMcBrands.get(i).getBrandIDx());
                                break;
                            case "YAMAHA":
                                logos.put(R.drawable.yamahalogo, eMcBrands.get(i).getBrandIDx());
                                break;
                        }
                    }

                    Adapter_MCProductImageSlider loAdapter = new Adapter_MCProductImageSlider(requireActivity(), logos, new Adapter_MCProductImageSlider.TabSelected() {
                        @Override
                        public void onSelected(String lsBrandID, String lsBrandNm) {

                            loIntent = new Intent(requireActivity(), Activity_ProductSelection.class);
                            loIntent.putExtra("lsBrandID", lsBrandID);
                            loIntent.putExtra("lsBrandNm", lsBrandNm);

                            if(ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                                startActivity(loIntent);
                            }else {
                                showDisclosure();
                            }
                        }
                    });
                    img_slider.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
                    img_slider.setAdapter(loAdapter);
                }
            }
        });
    }

    public void showDisclosure(){

        dialogDisclosure.initDialog(new DialogDisclosure.onDisclosure() {
            @Override
            public void onAccept() {
                dialogDisclosure.dismiss();

                List<String> lsPermissions = new ArrayList<>();

                if(ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    lsPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                if(ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    lsPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }

                poRequest.launch(lsPermissions.toArray(new String[0]));
            }

            @Override
            public void onDecline() {
                dialogDisclosure.dismiss();

                MessageBox loMessage = new MessageBox(requireActivity());
                loMessage.initDialog();
                loMessage.setTitle("Disclosure");
                loMessage.setMessage("Disclosure denied. Unable to retrieve product brands");
                loMessage.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

                loMessage.show();
            }
        });

        dialogDisclosure.setMessage("Guanzon Circle collects location data to enable saving of product inquiry when the app is in use.");
        dialogDisclosure.show();
    }
}
