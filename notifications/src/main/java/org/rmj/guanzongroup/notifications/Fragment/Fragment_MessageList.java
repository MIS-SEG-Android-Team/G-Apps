package org.rmj.guanzongroup.notifications.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.guanzongroup.notifications.R;
import org.rmj.guanzongroup.notifications.ViewModel.VMMessageList;


public class Fragment_MessageList extends Fragment {

    private VMMessageList mViewModel;

    public static Fragment_MessageList newInstance() {
        return new Fragment_MessageList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(VMMessageList.class);

        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

}