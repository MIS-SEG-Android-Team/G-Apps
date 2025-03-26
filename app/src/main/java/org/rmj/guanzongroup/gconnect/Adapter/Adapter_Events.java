package org.rmj.guanzongroup.gconnect.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.smarteist.autoimageslider.SliderViewAdapter;

import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Adapter_Events extends RecyclerView.Adapter<Adapter_Events.VHSlider_Events> {

    private List<GuanzonEvents> laEvents;
    private ViewPager2 viewPager2;

    public Adapter_Events(List<GuanzonEvents> laEvents, ViewPager2 viewPager2) {
        this.laEvents = laEvents;
        this.viewPager2 = viewPager2;
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
        holder.btn_event.setImageResource(laEvents.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return laEvents.size();
    }


    public class VHSlider_Events extends RecyclerView.ViewHolder{

        private ImageButton btn_event;

        public VHSlider_Events(View itemView) {
            super(itemView);

            btn_event = itemView.findViewById(R.id.btn_event);
        }
    }

    public static class GuanzonEvents{
        private String name;
        private int image;

        public GuanzonEvents(String name, int image){
            this.name = name;
            this.image = image;
        }

        public String getName(){
            return name;
        }

        public int getImage(){
            return image;
        }
    }
}
