package org.rmj.guanzongroup.gconnect.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_BranchList;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.gconnect.ViewModel.VMBranchDetails;

import java.util.ArrayList;
import java.util.List;

public class Fragment_FindUs extends Fragment {

    private VMBranchDetails mViewModel;
    private Adapter_BranchList poAdapter;
    private TabLayout tabLayout;
    private AppCompatImageView imgHeader;
    private ImageButton btnFilter;
    private RecyclerView recyclerView;
    private TextInputEditText txtSearch;
    private AutoCompleteTextView txtFilter;
    private TextInputLayout tilFilter;

    private Adapter_BranchList.OnBranchClickListener mListener;
    private List<EBranchInfo> poMcBranch;
    private List<EBranchInfo> poMpBranch;

    public Fragment_FindUs() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_us, container, false);
        initViews(view);

        mViewModel = new ViewModelProvider(requireActivity()).get(VMBranchDetails.class);

        mViewModel.DownloadBranches();
        mViewModel.getMotorBranches().observe(getViewLifecycleOwner(), motorBranches -> {
            try {
                poMcBranch = motorBranches;
                poAdapter = new Adapter_BranchList(motorBranches, mListener);
                recyclerView.setAdapter(poAdapter);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        mViewModel.getMobileBranches().observe(getViewLifecycleOwner(), mobileBranches -> poMpBranch = mobileBranches);

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    poAdapter.Filter().filter(s.toString());
                    poAdapter.notifyDataSetChanged();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setTabLayout();

        mViewModel.GetTownList().observe(getViewLifecycleOwner(), towns -> {
            try{
                ArrayList<String> loList = new ArrayList<>();
                for(int x = 0; x < towns.size(); x++){
                    String lsTownNm = towns.get(x).sTownNm;
                    String lsProvNm = towns.get(x).sProvNm;
                    loList.add(lsTownNm + ", " + lsProvNm);
                }
                txtFilter.setAdapter(new ArrayAdapter<>(requireActivity(),
                        android.R.layout.simple_list_item_1,
                        loList.toArray(new String[0])));

                txtFilter.setOnClickListener(v -> {
                    String lsInput = txtFilter.getText().toString();
                    for(int x = 0; x < towns.size(); x++){
                        if(loList.get(x).equalsIgnoreCase(lsInput)){

                        }
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tilFilter.getVisibility() == View.VISIBLE) {
                    tilFilter.setVisibility(View.GONE);
                } else {
                    tilFilter.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    private void initViews(View v) {
        tabLayout = v.findViewById(R.id.tab_layout);
        txtSearch = v.findViewById(R.id.tie_searchBranch);
        btnFilter = v.findViewById(R.id.btnFilter);
        txtFilter = v.findViewById(R.id.tieTownProvince);
        tilFilter = v.findViewById(R.id.tilTownProvince);
        tabLayout.addTab(tabLayout.newTab().setText("Motorcycle"));
        tabLayout.addTab(tabLayout.newTab().setText("Mobile Phones"));
        recyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager loManager = new LinearLayoutManager(requireActivity());
        loManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(loManager);
        imgHeader = v.findViewById(R.id.img_header);
        mListener = args -> {
            try{
                Uri gmmIntentUri;
                Intent mapIntent;
                if(args.getLatitude() == 0.0 && args.getLongtude() == 0.0){
                    gmmIntentUri = Uri.parse("geo:0,0?q=guanzon " + args.getAddressx());
                } else {
                    gmmIntentUri = Uri.parse("geo:" + args.getLatitude() + ", " + args.getLongtude() + "?q=guanzon");
//                        callback.OnInitLocation(mapIntent);
                }
                mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } catch (Exception e){
                e.printStackTrace();
//                DialogBranchInfo loBranch = new DialogBranchInfo(requireActivity())
            };
//                loBranch.InitDialog(args, new DialogBranchInfo.BranchDialogActionCallback() {
//                    @Override
//                    public void OnInitLocation(Intent args) {
//                        startActivity(args);
//                    }
//                });
        };
    }

    private void setTabLayout(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Bitmap bmpHeader;
                switch(tab.getPosition()) {
                    case 0:
                        bmpHeader = BitmapFactory.decodeResource(requireActivity().getResources(),
                                R.drawable.guanzon_motors);
                        imgHeader.setImageBitmap(bmpHeader);
                        initMCBranches();
                        recyclerView.setAdapter(poAdapter);
                        break;
                    case 1:
                        bmpHeader = BitmapFactory.decodeResource(requireActivity().getResources(),
                                R.drawable.guanzon_mobitek);
                        imgHeader.setImageBitmap(bmpHeader);
                        initMPBranches();
                        recyclerView.setAdapter(poAdapter);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initMCBranches() {
        try {
            poAdapter = new Adapter_BranchList(poMcBranch, mListener);
            poAdapter.notifyDataSetChanged();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initMPBranches() {
        try {
            poAdapter = new Adapter_BranchList(poMpBranch, mListener);
            poAdapter.notifyDataSetChanged();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}