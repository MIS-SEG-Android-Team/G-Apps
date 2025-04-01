package org.rmj.guanzongroup.gconnect.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.etc.ViewPagerProperty;
import org.rmj.g3appdriver.utils.ImageFileManager;
import org.rmj.guanzongroup.gconnect.R;

import java.util.ArrayList;
import java.util.List;

public class Dialog_Candidate_Details {

    private Context context;
    private AlertDialog poDialogx;
    private ECandidates details;

    public Dialog_Candidate_Details(Context context, ECandidates details){
        this.context = context;
        this.details = details;
    }

    public class Dialog_Details{

        private ShapeableImageView loImg;
        private MaterialTextView mtv_name;
        private MaterialTextView mtv_school;
        private MaterialTextView mtv_votes;
        private LinearLayout layout_norecord;
        private ViewPager2 list_images;
        private MaterialButton btn_vote;

        private Adapter_ImageDetails loAdapter;

        public void initDialogDetails(){

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_candidate_details, null, false);

            AlertDialog.Builder loBuilder =  new AlertDialog.Builder(context);
            loBuilder
                    .setView(view)
                    .setCancelable(true);

            poDialogx = loBuilder.create();

            initViews(view);
            initDetails();
            initListener();

            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
            poDialogx.show();

        }

        private void initViews(View view){

            loImg = view.findViewById(R.id.img_candidate);

            mtv_name = view.findViewById(R.id.mtv_name);
            mtv_school = view.findViewById(R.id.mtv_school);
            mtv_votes = view.findViewById(R.id.mtv_votes);
            layout_norecord = view.findViewById(R.id.layout_norecord);
            list_images = view.findViewById(R.id.list_images);

            btn_vote = view.findViewById(R.id.btn_vote);

        }

        private void initDetails(){

            try{

                //todo: get url from list, and load to object
                JSONArray loArr = new JSONArray(details.getUrlImgs());
                ImageFileManager.LoadImageToView(loArr.get(0).toString(), loImg);

                //todo: set details
                mtv_name.setText(details.getName());
                mtv_school.setText(details.getSchool());
                mtv_votes.setText(details.getVotes());

                //todo: if image urls not empty
                if (loArr.length() > 0){

                    //todo: display viewpager and hide no record layout
                    layout_norecord.setVisibility(View.GONE);
                    list_images.setVisibility(View.VISIBLE);

                    //todo: initialize list of url string for images
                    List<String> urlImgs = new ArrayList<>();
                    for (int i = 0; i < loArr.length(); i++){

                        urlImgs.add(loArr.getString(i));

                    }

                    //todo: initialize adapter with list of images
                    loAdapter =
                            new Adapter_ImageDetails(urlImgs, list_images, new Adapter_ImageDetails.onItemClickListener() {
                                @Override
                                public void onItemClick(String url) {

                                    ImageFileManager.LoadImageToView(url, loImg);

                                }
                            });

                    list_images.setAdapter(loAdapter);

                    /**
                     * Set viewpager properties and design
                     * Custom library for designing viewpager.
                     * Add new designs , for future layout viewpager design
                     * Guillier 03/28/2025
                     **/
                    ViewPagerProperty loViewPagerProperty = new ViewPagerProperty(list_images);

                    loViewPagerProperty.initSliderPadding(
                            new ViewPagerProperty.Padding_Property(195, 195,
                                    0, 0, false, false, 3)
                    );

                    loViewPagerProperty.initSliderPageTransformer();

                }else {

                    //todo: if image urls are empty, hide viewpager and show no record layout
                    layout_norecord.setVisibility(View.VISIBLE);
                    list_images.setVisibility(View.GONE);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        private void initListener(){

            //todo: viewpager page change listener
            list_images.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            list_images.setCurrentItem(position);

                            //todo: observe, this might cause a crash / delay of updating the adapter.
                            //todo: should be called once only
                            loAdapter.notifyDataSetChanged();

                        }
                    });

                }
            });

        }
    }

    public class Adapter_ImageDetails extends RecyclerView.Adapter<Adapter_ImageDetails.VH_ImageList>{

        private final List<String> laImgs;
        private final ViewPager2 viewPager2;
        private final onItemClickListener callback;

        public Adapter_ImageDetails(List<String> laImgs, ViewPager2 viewPager2, onItemClickListener callback) {
            this.laImgs = laImgs;
            this.viewPager2 = viewPager2;
            this.callback = callback;
        }

        @NonNull
        @Override
        public VH_ImageList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH_ImageList(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_imageevent_vpagelist, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter_ImageDetails.VH_ImageList holder, int position) {

            Glide.with(holder.itemView)
                    .load(laImgs.get(position))
                    .fitCenter()
                    .into(holder.img_candidate);

            if (viewPager2.getCurrentItem() == position){
                holder.img_candidate.setElevation(1f);
                holder.img_candidate.setAlpha(1f);
            }else {
                holder.img_candidate.setElevation(0.15f);
                holder.img_candidate.setAlpha(0.8f);
            }

            holder.img_candidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callback.onItemClick(laImgs.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
            return laImgs.size();
        }

        public static class VH_ImageList extends RecyclerView.ViewHolder {

            private final ShapeableImageView img_candidate;

            public VH_ImageList(@NonNull View itemView) {
                super(itemView);

                img_candidate = itemView.findViewById(R.id.img_candidate);
            }
        }

        public interface onItemClickListener{
            void onItemClick(String url);
        }

    }

}
