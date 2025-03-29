package org.rmj.guanzongroup.gconnect.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import org.rmj.g3appdriver.dev.Database.Entities.EEvents;
import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Adapter_Events extends RecyclerView.Adapter<Adapter_Events.VHSlider_Events> {

    private final List<EEvents> laEvents;
    private final ViewPager2 viewPager2;
    private final onSelectListener callback;

    public Adapter_Events(List<EEvents> laEvents, ViewPager2 viewPager2, onSelectListener callback) {
        this.laEvents = laEvents;
        this.viewPager2 = viewPager2;
        this.callback = callback;
    }

    @NonNull
    @Override
    public VHSlider_Events onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHSlider_Events(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sliderview, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull VHSlider_Events holder, int position) {

        Glide.with(holder.itemView)
                .load(laEvents.get(position).getImageURL())
                .fitCenter()
                .into(holder.btn_event);

        if (viewPager2.getCurrentItem() == position){
            holder.btn_event.setElevation(0.85f);
            holder.btn_event.setAlpha(1f);
        }else {
            holder.btn_event.setElevation(0.15f);
            holder.btn_event.setAlpha(0.8f);
        }

        holder.btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.onSelect(position, laEvents.get(position).getTransNox());

            }
        });

    }

    @Override
    public int getItemCount() {
        return laEvents.size();
    }

    public static class VHSlider_Events extends RecyclerView.ViewHolder{

        private ImageButton btn_event;

        public VHSlider_Events(View itemView) {
            super(itemView);

            btn_event = itemView.findViewById(R.id.btn_event);
        }
    }

    public interface onSelectListener{
        void onSelect(int position, String eventIDxx);
    }
}
