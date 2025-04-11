package org.rmj.guanzongroup.gconnect.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.Entities.ECandidates;
import org.rmj.g3appdriver.utils.ImageFileManager;
import org.rmj.guanzongroup.gconnect.Dialog.Dialog_Candidate_Details;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.gconnect.ViewModel.VMPoll;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Candidates extends RecyclerView.Adapter<Adapter_Candidates.VH_Candidates>{

    private final Context context;
    private final List<ECandidates> candidates;
    private final CandidateFilter poFilter;
    private final VMPoll mviewModel;
    private final Fragment fragment;

    private List<ECandidates> candidatesFiltered;

    public Adapter_Candidates(Context context, Fragment fragment, List<ECandidates> candidates, VMPoll mviewModel){
        this.context = context;
        this.candidates = candidates;
        this.poFilter = new CandidateFilter(this);
        this.candidatesFiltered = candidates;
        this.mviewModel = mviewModel;
        this.fragment = fragment;
    }

    public CandidateFilter getFilter(){
        return poFilter;
    }

    @NonNull
    @Override
    public VH_Candidates onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_candidate, parent, false);
        return new VH_Candidates(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH_Candidates holder, int position) {

        try {

            //todo: load image urls to view
            JSONObject urlImgs = new JSONObject(candidatesFiltered.get(position).getUrlImgs());
            String urlPrimary = urlImgs.getString("master");

            ImageFileManager.LoadImageToView(urlPrimary,
                    holder.img_candidate);

            //todo: display total votes
            holder.mtv_votes.setText(String.valueOf(candidatesFiltered.get(position).getVotes()));

            //todo: view candidate details
            holder.img_candidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        //todo: initialize dialog details
                        Dialog_Candidate_Details loDialog = new Dialog_Candidate_Details(context,
                                fragment, candidatesFiltered.get(position), mviewModel);

                        loDialog.new Dialog_Details().initDialogDetails();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return candidatesFiltered.size();
    }

    public class CandidateFilter extends Filter{

        private final Adapter_Candidates adapter;

        public CandidateFilter(Adapter_Candidates adapter){
            this.adapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint.length() > 0){

                List<ECandidates> filteredCandidates = new ArrayList<>();
                for (ECandidates values: candidates){
                    if (values.getsEntryNme().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredCandidates.add(values);
                    }else if (values.getsSchoolNm().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredCandidates.add(values);
                    }
                }

                candidatesFiltered = filteredCandidates;

            }else {
                candidatesFiltered = candidates;
            }

            results.values = candidatesFiltered;
            results.count = candidatesFiltered.size();

            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            adapter.candidatesFiltered = (List<ECandidates>) results.values;
            adapter.notifyDataSetChanged();
        }
    }

    public static class VH_Candidates extends RecyclerView.ViewHolder {

        private final ShapeableImageView img_candidate;
        private final MaterialTextView mtv_votes;

        public VH_Candidates(@NonNull View itemView) {
            super(itemView);

            img_candidate = itemView.findViewById(R.id.img_candidate);
            mtv_votes = itemView.findViewById(R.id.mtv_votes);

        }
    }
}
