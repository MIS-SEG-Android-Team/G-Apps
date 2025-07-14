package org.rmj.guanzongroup.marketplace.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.guanzongroup.digitalgcard.Activity.Activity_ManageGcard;
import org.rmj.guanzongroup.marketplace.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMSearchItem;
import org.rmj.guanzongroup.marketplace.databinding.FragmentSearchResultBinding;

public class Fragment_SearchResult extends Fragment {

    private VMSearchItem mViewModel;
    private FragmentSearchResultBinding mBinding;

    public Fragment_SearchResult() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentSearchResultBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(VMSearchItem.class);

        mBinding.rvSuggest.setLayoutManager(new LinearLayoutManager(requireActivity()));
        mBinding.rvSuggest.setHasFixedSize(true);

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

}