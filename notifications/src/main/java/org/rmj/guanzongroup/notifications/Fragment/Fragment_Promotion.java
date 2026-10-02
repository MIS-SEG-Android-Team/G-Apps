package org.rmj.guanzongroup.notifications.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.guanzongroup.notifications.Activity.Activity_Browser;
import org.rmj.guanzongroup.notifications.Adapter.Adapter_EventsPromos;
import org.rmj.guanzongroup.notifications.R;
import org.rmj.guanzongroup.notifications.ViewModel.VMEventsPromos;

public class Fragment_Promotion extends Fragment {

    private VMEventsPromos mViewModel;
    private RecyclerView eventsPromoView;
    private Adapter_EventsPromos poAdapter;

    public static Fragment_Promotion newInstance() {
        return new Fragment_Promotion();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(VMEventsPromos.class);

        View view = inflater.inflate(R.layout.fragment__promotion, container, false);

        eventsPromoView = view.findViewById(R.id.recycler_view_EventsPromo);

        LinearLayoutManager loManager = new LinearLayoutManager(requireActivity());
        loManager.setOrientation(RecyclerView.VERTICAL);

        eventsPromoView.setLayoutManager(loManager);
        eventsPromoView.setHasFixedSize(true);

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

        return view;
    }
}