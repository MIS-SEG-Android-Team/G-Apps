package org.rmj.guanzongroup.gconnect.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.g3appdriver.utils.ImageFileManager;
import org.rmj.guanzongroup.gconnect.Dialog.Dialog_ImagePreview;
import org.rmj.guanzongroup.gconnect.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Adapter_Candidates extends RecyclerView.Adapter<Adapter_Candidates.VH_Candidates>{

    private final Context context;
    private final List<String> candidates;
    private final CandidateFilter poFilter;
    private List<String> candidatesFiltered;

    public Adapter_Candidates(Context context, List<String> candidates){
        this.context = context;
        this.candidates = candidates;
        this.poFilter = new CandidateFilter(this);
        this.candidatesFiltered = candidates;
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

        String imgPath = "http://192.165.10.65:80/candidatesimage/";

        holder.mtv_name.setText(candidatesFiltered.get(position));

        ImageFileManager.LoadImageToView(imgPath + candidatesFiltered.get(position)+".jpg",
                holder.img_candidate);

        holder.img_candidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_ImagePreview loDialog = new Dialog_ImagePreview(context,
                        imgPath + candidatesFiltered.get(position)+".jpg");

                loDialog.initDialog();
            }
        });

        holder.btn_vote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    buttonView.setCompoundDrawablesWithIntrinsicBounds(null, context.getResources().getDrawable(R.drawable.baseline_check_circle_24), null, null);
                }else {
                    buttonView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }
        });
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

                List<String> filteredCandidates = new ArrayList<>();
                for (String values: candidates){
                    if (values.toLowerCase().contains(constraint.toString().toLowerCase())){
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

            adapter.candidatesFiltered = (List<String>) results.values;
            adapter.notifyDataSetChanged();
        }
    }

    public class VH_Candidates extends RecyclerView.ViewHolder {

        public MaterialTextView mtv_name;
        private ShapeableImageView img_candidate;
        private ToggleButton btn_vote;

        public VH_Candidates(@NonNull View itemView) {
            super(itemView);

            mtv_name = itemView.findViewById(R.id.mtv_name);
            img_candidate = itemView.findViewById(R.id.img_candidate);
            btn_vote = itemView.findViewById(R.id.btn_vote);
        }
    }
}
