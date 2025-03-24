package org.rmj.guanzongroup.notifications.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rmj.guanzongroup.notifications.Activity.Activity_Browser;
import org.rmj.guanzongroup.notifications.Adapter.Adapter_EventsPromos;
import org.rmj.guanzongroup.notifications.R;
import org.rmj.guanzongroup.notifications.ViewModel.VMEventsPromos;

public class Fragment_EventsPromos extends Fragment {

    private VMEventsPromos mViewModel;
    private RecyclerView eventsPromoView;
    private Adapter_EventsPromos poAdapter;
    public static Fragment_EventsPromos newInstance() {
        return new Fragment_EventsPromos();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(VMEventsPromos.class);
        View view = inflater.inflate(R.layout.fragment_events_promos, container, false);
        initViews(view);
        // TODO: Use the ViewModel
        if(getArguments().get("gcardInstance").toString().equalsIgnoreCase("1")){
            mViewModel.DownloadPromos();
            mViewModel.getPromos().observe(getViewLifecycleOwner(), promos -> {
                poAdapter = new Adapter_EventsPromos(requireActivity(), (url, args) -> {
                    Intent intent = new Intent(getActivity(), Activity_Browser.class);
                    intent.putExtra("url_link", url);
                    intent.putExtra("args", args);
                    startActivity(intent);
                },promos, "1");
                eventsPromoView.setAdapter(poAdapter);
                poAdapter.notifyDataSetChanged();
            });
        } else {
            mViewModel.DownloadEvents();
            mViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
                poAdapter = new Adapter_EventsPromos(requireActivity(),events, (url, args) -> {
                    Intent intent = new Intent(getActivity(), Activity_Browser.class);
                    intent.putExtra("url_link", url);
                    intent.putExtra("args", args);
                    startActivity(intent);
                },"2");

                eventsPromoView.setAdapter(poAdapter);
                poAdapter.notifyDataSetChanged();
            });
        }
        return view;
    }

    void initViews(View v){
        eventsPromoView = v.findViewById(R.id.recycler_view_EventsPromo);
        LinearLayoutManager loManager = new LinearLayoutManager(requireActivity());
        loManager.setOrientation(RecyclerView.VERTICAL);
        eventsPromoView.setLayoutManager(loManager);
        eventsPromoView.setHasFixedSize(true);
    }
}