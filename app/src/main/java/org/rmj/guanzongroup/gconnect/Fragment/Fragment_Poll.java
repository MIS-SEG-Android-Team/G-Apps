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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Candidates;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.gconnect.ViewModel.VMPoll;

import java.util.List;

public class Fragment_Poll extends Fragment {

    private VMPoll mViewModel;

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

        mViewModel = new ViewModelProvider(requireActivity()).get(VMPoll.class);

        initViews(view);
        initListener();
        initArguments(); //todo: trigger on first view initialization
        initObservables();

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

        //TODO: CHANGE TAB INDICATOR ON SELECTION
        tab_candidates.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:
                        initCandidates("M00120000001");
                        break;
                    case 1:
                        initCandidates("M00120000002");
                        break;
                    case 2:
                        initCandidates("M00120000003");
                        break;
                    case 3:
                        initCandidates("M00120000004");
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

                        case "M00120000001": //dreamboy
                            tab_candidates.selectTab(tab_candidates.getTabAt(0), true);
                            break;

                        case "M00120000002": //campus princess
                            tab_candidates.selectTab(tab_candidates.getTabAt(1), true);
                            break;

                        case "M00120000003": //biker babe
                            tab_candidates.selectTab(tab_candidates.getTabAt(2), true);
                            break;

                        case "M00120000004": //guanzon bulilit
                            tab_candidates.selectTab(tab_candidates.getTabAt(3), true);
                            break;
                    }
                }

            }

        }

    }

    private void initObservables(){

        if (argsParams != null){
            initCandidates(argsParams.getString("eventID"));
        }
    }

    private void initCandidates(String eventIDxx){

        mViewModel.GetCandidates(eventIDxx).observe(getViewLifecycleOwner(), new Observer<List<ECandidates>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<ECandidates> eCandidates) {

                if (eCandidates != null){

                    adapter_candidates = new Adapter_Candidates(requireContext(), eCandidates);

                    adapter_candidates.notifyDataSetChanged();

                    rv_candidates.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                    rv_candidates.setAdapter(adapter_candidates);

                }
            }
        });

    }

}
