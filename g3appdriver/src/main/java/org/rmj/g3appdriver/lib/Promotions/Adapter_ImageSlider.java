package org.rmj.g3appdriver.lib.Promotions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.rmj.g3appdriver.R;
import org.rmj.g3appdriver.lib.Promotions.model.HomeImageSliderModel;

import java.util.ArrayList;
import java.util.List;

public class Adapter_ImageSlider extends SliderViewAdapter<Adapter_ImageSlider.SliderAdapterVH> {
    private List<HomeImageSliderModel> oSlideLst = new ArrayList<>();
    private final OnImageSliderClickListener mListener;

    public interface OnImageSliderClickListener{
        void OnClick(int args);
    }

    public Adapter_ImageSlider(List<HomeImageSliderModel> oSlideLst, OnImageSliderClickListener listener) {
        this.oSlideLst = oSlideLst;
        this.mListener = listener;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image_slider, null);
        return new SliderAdapterVH(inflate);
    }
    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        HomeImageSliderModel loSlide = oSlideLst.get(position);
        Glide.with(viewHolder.itemView)
                .load(loSlide.getImageUrl())
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(v -> {
            mListener.OnClick(position);
        });
    }
    @Override
    public int getCount() {
        return oSlideLst.size();
    }

    public static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }

}
