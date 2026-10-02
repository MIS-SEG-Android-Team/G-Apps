package org.rmj.guanzongroup.notifications.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.guanzongroup.notifications.Activity.Activity_PreviewMessage;
import org.rmj.guanzongroup.notifications.Activity.Activity_ViewNotification;
import org.rmj.guanzongroup.notifications.Adapter.Adapter_Notifications;
import org.rmj.guanzongroup.notifications.Adapter.Adapter_RegularMessage;
import org.rmj.guanzongroup.notifications.R;
import org.rmj.guanzongroup.notifications.ViewModel.VMNotifications;


public class Fragment_Notification extends Fragment {

    private VMNotifications mViewModel;
    private RecyclerView recyclerView;
    private ConstraintLayout ln_empty;

    private Adapter_RegularMessage adapter;
    private Adapter_Notifications adapters;
    public static Fragment_Notification newInstance() {
        return new Fragment_Notification();
    }

    public Fragment_Notification() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__notification, container, false);

        mViewModel = new ViewModelProvider(getActivity()).get(VMNotifications.class);
        recyclerView = view.findViewById(R.id.recycler_view_notifications);
        ln_empty = view.findViewById(R.id.ln_empty);

        LinearLayoutManager loManager = new LinearLayoutManager(requireActivity());
        loManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(loManager);
        recyclerView.setHasFixedSize(true);

        mViewModel.GetRegularMessagesSystemNotifs().observe(getActivity(), notif -> {
            if (notif.size() > 0) {
                ln_empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapters = new Adapter_Notifications(notif, (fsMesgIDxx, lsCreated, fsMesgType, lsDataSent) -> {
                    Intent loIntent = new Intent(getActivity(), Activity_ViewNotification.class);
                    loIntent.putExtra("sMsgIDxxx", fsMesgIDxx);
                    loIntent.putExtra("sMsgTypex", fsMesgType);
                    loIntent.putExtra("sDataSent", lsDataSent);
                    startActivity(loIntent);
                });

//                adapter = new Adapter_RegularMessage(notif);

                recyclerView.setAdapter(adapters);
                adapters.notifyDataSetChanged();
            } else {
                ln_empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });

        return view;
    }
}