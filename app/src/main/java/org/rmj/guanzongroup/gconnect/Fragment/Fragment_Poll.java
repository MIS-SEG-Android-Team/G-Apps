package org.rmj.guanzongroup.gconnect.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_poll, container, false);

        tab_candidates = view.findViewById(R.id.tab_candidates);
        tv_search = view.findViewById(R.id.tv_search);
        rv_candidates = view.findViewById(R.id.rv_candidates);

        List<String> candidates1 = List.of("Mitch Cardigan", "Jewel Myers", "Lyndsay Rivera", "Sofia Reyes");
        List<String> candidates2 = List.of("Carmina Dela Cruz", "Sharmaine Aquino", "Jenny Rogers", "Ashley De Vera");

        switch (tab_candidates.getSelectedTabPosition()){

            case 0:
                tab_candidates.setSelectedTabIndicator(R.drawable.background_guanzontableft);
                adapter_candidates = new Adapter_Candidates(requireContext(), candidates1);

                adapter_candidates.notifyDataSetChanged();
                rv_candidates.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                rv_candidates.setAdapter(adapter_candidates);
                break;
            case 1:
                tab_candidates.setSelectedTabIndicator(R.drawable.background_guanzontabright);
                adapter_candidates = new Adapter_Candidates(requireContext(), candidates2);

                adapter_candidates.notifyDataSetChanged();
                rv_candidates.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                rv_candidates.setAdapter(adapter_candidates);
                break;
        }

        //TODO: CHANGE TAB INDICATOR ON SELECTION
        tab_candidates.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){

                    case 0:
                        tab_candidates.setSelectedTabIndicator(R.drawable.background_guanzontableft);
                        adapter_candidates = new Adapter_Candidates(requireContext(), candidates1);

                        adapter_candidates.notifyDataSetChanged();
                        rv_candidates.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                        rv_candidates.setAdapter(adapter_candidates);
                        break;
                    case 1:
                        tab_candidates.setSelectedTabIndicator(R.drawable.background_guanzontabright);
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

        return view;

    }
}
