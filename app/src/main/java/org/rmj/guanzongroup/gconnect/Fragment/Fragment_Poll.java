package org.rmj.guanzongroup.gconnect.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Candidates;
import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Fragment_Poll extends Fragment {

    private TabLayout tab_candidates;
    private TextInputEditText tv_search;
    private RecyclerView rv_candidates;
    private Adapter_Candidates adapter_candidates;

    private Bundle argsParams;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_poll, container, false);

        initViews(view);
        initListener();
        initArguments(); //todo: trigger on first view initialization

        return view;

    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

        argsParams = args;

        initArguments(); //todo: trigger on every argument passed

    }

    private void initViews(View view){
        tab_candidates = view.findViewById(R.id.tab_candidates);
        tv_search = view.findViewById(R.id.tv_search);
        rv_candidates = view.findViewById(R.id.rv_candidates);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initListener(){

        List<Adapter_Candidates.Candidate_Details> candidates1 =
                List.of(initData("Mitch Cardigan", "University of Luzon", "1", "MitchCardigan.jpg"),
                        initData("Jewel Myers", "University of Pangasinan", "2", "JewelMyers.jpg"),
                        initData("Lyndsay Rivera", "Universidad De Dagupan", "3", "LyndsayRivera.jpg"),
                        initData("Sofia Reyes", "Lyceum Northwestern University", "4", "SofiaReyes.jpg"));

        List<Adapter_Candidates.Candidate_Details> candidates2 =
                List.of(initData("Carmina Dela Cruz", "University of Luzon", "1", "CarminaDelaCruz.jpg"),
                        initData("Sharmaine Aquino", "University of Pangasinan", "2", "SharmaineAquino.jpg"),
                        initData("Jenny Rogers", "Universidad De Dagupan", "3", "JennyRogers.jpg"),
                        initData("Ashley De Vera", "Lyceum Northwestern University", "4", "AshleyDeVera.jpg"));

        switch (tab_candidates.getSelectedTabPosition()){

            case 0:
                adapter_candidates = new Adapter_Candidates(requireContext(), candidates1);

                adapter_candidates.notifyDataSetChanged();
                rv_candidates.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                rv_candidates.setAdapter(adapter_candidates);
                break;
            case 1:
                adapter_candidates = new Adapter_Candidates(requireContext (), candidates2);

                adapter_candidates.notifyDataSetChanged();
                rv_candidates.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                rv_candidates.setAdapter(adapter_candidates);
                break;
        }

        //TODO: CHANGE TAB INDICATOR ON SELECTION
        tab_candidates.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:
                        adapter_candidates = new Adapter_Candidates(requireContext(), candidates1);

                        adapter_candidates.notifyDataSetChanged();
                        rv_candidates.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                        rv_candidates.setAdapter(adapter_candidates);
                        break;
                    case 1:
                        adapter_candidates = new Adapter_Candidates(requireContext(), candidates2);

                        adapter_candidates.notifyDataSetChanged();
                        rv_candidates.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                        rv_candidates.setAdapter(adapter_candidates);
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

        tv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                try {

                    adapter_candidates.getFilter().filter(s.toString());
                    adapter_candidates.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    adapter_candidates.getFilter().filter(s.toString());
                    adapter_candidates.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initArguments(){

        if (tab_candidates != null){

            if (argsParams != null){

                if (argsParams.containsKey("eventID")){

                    String eventID = argsParams.getString("eventID");

                    switch (eventID){

                        case "M00120000001":
                            tab_candidates.selectTab(tab_candidates.getTabAt(0), true);
                            break;

                        case "M00120000002":
                            tab_candidates.selectTab(tab_candidates.getTabAt(1), true);
                            break;

                        case "M00120000003":
                            tab_candidates.selectTab(tab_candidates.getTabAt(2), true);
                            break;

                        case "M00120000004":
                            tab_candidates.selectTab(tab_candidates.getTabAt(3), true);
                            break;
                    }
                }

            }

        }

    }

    private Adapter_Candidates.Candidate_Details initData(String name, String school, String number, String urlImg){

        Adapter_Candidates.Candidate_Details details = new Adapter_Candidates.Candidate_Details();
        details.setName(name);
        details.setSchool(school);
        details.setNumber(number);
        details.setUrlImg(urlImg);

        return details;
    }
}
