package org.rmj.guanzongroup.gconnect.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DVoteLogs;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.etc.FacebookShare;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.etc.ViewPagerProperty;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.g3appdriver.utils.ImageFileManager;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.gconnect.ViewModel.VMPoll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Dialog_Candidate_Details {

    private final Context context;
    private final ECandidates details;
    private final VMPoll mviewModel;
    private final Dialog_Loading poLoad;

    private AlertDialog poDialogx;

    public Dialog_Candidate_Details(Context context, ECandidates details, VMPoll mviewModel){
        this.context = context;
        this.details = details;
        this.mviewModel = mviewModel;
        this.poLoad = new Dialog_Loading(context);
    }

    public class Dialog_Details{

        private ShapeableImageView loImg;
        private MaterialTextView mtv_name;
        private MaterialTextView mtv_school;
        private MaterialTextView mtv_votes;
        private LinearLayout layout_norecord;
        private ViewPager2 list_images;
        private MaterialButton btn_vote;
        private ImageButton img_btnShare;

        private Adapter_ImageDetails loAdapter;

        public void initDialogDetails() throws ParseException {

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_candidate_details, null, false);

            AlertDialog.Builder loBuilder =  new AlertDialog.Builder(context);
            loBuilder
                    .setView(view)
                    .setCancelable(true);

            poDialogx = loBuilder.create();

            initViews(view);
            initDetails();
            initListener();
            allowVoting();

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

            img_btnShare = view.findViewById(R.id.img_btnShare);
            btn_vote = view.findViewById(R.id.btn_vote);

        }

        @SuppressLint("NotifyDataSetChanged")
        private void initDetails(){

            try{

                //todo: get url from list, and load to object
                JSONObject loArr = new JSONObject(details.getUrlImgs());
                JSONArray laUrls = new JSONArray(loArr.getString("details"));

                //todo: display first loaded image
                ImageFileManager.LoadImageToView(laUrls.get(0).toString(), loImg);

                //todo: set details
                mtv_name.setText(details.getsEntryNme());
                mtv_school.setText(details.getsSchoolNm());
                mtv_votes.setText(String.valueOf(mviewModel.GetCandidateVotes(details.getsGroupIDx(), details.getsEvntIDxx())));

                img_btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FacebookShare loShareApp = new FacebookShare(context);
                        loShareApp.OpenShareFeed("https://www.facebook.com/share/p/18FnjVvV9N/");
                    }
                });

                //todo: if image urls not empty
                if (laUrls.length() > 0){

                    //todo: display viewpager and hide no record layout
                    layout_norecord.setVisibility(View.GONE);
                    list_images.setVisibility(View.VISIBLE);

                    //todo: initialize list of url string for images
                    List<String> urlImgs = new ArrayList<>();
                    for (int i = 0; i < laUrls.length(); i++){

                        urlImgs.add(laUrls.getString(i));

                    }

                    //todo: initialize adapter with list of images
                    loAdapter =
                            new Adapter_ImageDetails(urlImgs, list_images, new Adapter_ImageDetails.onItemClickListener() {
                                @Override
                                public void onItemClick(String url) {

                                    ImageFileManager.LoadImageToView(url, loImg);

                                }
                            });

                    loAdapter.notifyDataSetChanged();
                    list_images.setAdapter(loAdapter);

                    /**
                     * Set viewpager properties and design
                     * Custom library for designing viewpager.
                     * Add new designs , for future layout viewpager design
                     * Guillier 03/28/2025
                     **/
                    ViewPagerProperty loViewPagerProperty = new ViewPagerProperty(list_images);

                    //todo get device density width
                    int densWidth = (int) (list_images.getResources().getDisplayMetrics().xdpi);

                    //todo formula to retain padding (density width - ( 30% of density width ))
                    loViewPagerProperty.initSliderPadding(
                            new ViewPagerProperty.Padding_Property((int) (densWidth - (densWidth * 0.3)), (int) (densWidth - (densWidth * 0.3)),
                                    0, 0, false, false, 3));

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

            btn_vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mviewModel.SubmitVote(details.getsGroupIDx(), new VMPoll.onSubmitVote() {
                        @Override
                        public void onLoad() {

                            poLoad.initDialog("Guanzon Connect", "Sending Vote");
                            poLoad.show();
                        }

                        @SuppressLint("SimpleDateFormat")
                        @Override
                        public void onResult(Boolean result, String message) {

                            poLoad.dismiss();

                            MessageBox poMessage = new MessageBox(context);

                            if (result){

                                poMessage.initDialog();
                                poMessage.displayDialogWithIconTitle(R.drawable.baseline_check_circle_24,
                                        "Great!\n\n Your vote has been counted");

                                poMessage.setPositiveButton("Okay", new MessageBox.DialogButton() {
                                    @Override
                                    public void OnButtonClick(View view, AlertDialog dialog) {

                                        dialog.dismiss();
                                        poDialogx.dismiss();

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                            mviewModel.UpdateTimeStmp(details.getsGroupIDx(), details.getsEvntIDxx(),
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));

                                        }else {
                                            mviewModel.UpdateTimeStmp(details.getsGroupIDx(), details.getsEvntIDxx(),
                                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                                        }

                                    }
                                });

                                poMessage.show();

                            }else {

                                poMessage.initDialog();
                                poMessage.setTitle("Guanzon Connect");

                                poMessage.setIcon(org.rmj.g3appdriver.R.drawable.baseline_error_24);

                                if (!message.isEmpty()){
                                    poMessage.setMessage(message);
                                }else {
                                    poMessage.setMessage("Failed to submit vote");
                                }

                                poMessage.show();

                                poMessage.setPositiveButton("Okay", new MessageBox.DialogButton() {
                                    @Override
                                    public void OnButtonClick(View view, AlertDialog dialog) {

                                        dialog.dismiss();
                                    }
                                });

                            }

                        }
                    });
                }
            });
        }

        private void allowVoting() throws ParseException {

            if (mviewModel.CountCategoryVotes(details.getsEvntIDxx()) != null){

                if (hasVotedToday(mviewModel.CountCategoryVotes(details.getsEvntIDxx()))){
                    btn_vote.setEnabled(false);
                } else {
                    btn_vote.setEnabled(true);
                }

            }
        }

        @SuppressLint("SimpleDateFormat")
        private Boolean hasVotedToday(DVoteLogs.LatestVote lastVote) throws ParseException {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if (lastVote.getdTimeStmp() != null){

                    LocalDate loLastVote = LocalDate.parse(lastVote.getdTimeStmp(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate loToday = LocalDateTime.now().toLocalDate();

                    return loLastVote.isEqual(loToday) && lastVote.getTotal() > 0;

                }else {
                    return false;
                }

            }else {

                if (lastVote.getdTimeStmp() != null){

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");

                    Date loLastVote = dtFormat.parse(dtFormat.format(dtFormat.parse(lastVote.getdTimeStmp())));
                    Date loToday = dtFormat.parse(dtFormat.format(Calendar.getInstance().getTime()));

                    return loLastVote.equals(loToday) && lastVote.getTotal() > 0;

                }else {
                    return false;
                }

            }

        }

    }

    public static class Adapter_ImageDetails extends RecyclerView.Adapter<Adapter_ImageDetails.VH_ImageList>{

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
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vpagecandidate_details, parent, false)
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

            private final ImageButton img_candidate;

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
