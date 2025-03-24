package org.rmj.guanzongroup.gconnect.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.g3appdriver.utils.ImageFileManager;
import org.rmj.guanzongroup.gconnect.Dialog.Dialog_Candidate_Details;
import org.rmj.guanzongroup.gconnect.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Candidates extends RecyclerView.Adapter<Adapter_Candidates.VH_Candidates>{

    private final Context context;
    private final List<Candidate_Details> candidates;
    private final CandidateFilter poFilter;
    private List<Candidate_Details> candidatesFiltered;

    public Adapter_Candidates(Context context, List<Candidate_Details> candidates){
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

        holder.mtv_name.setText(candidatesFiltered.get(position).getName());

        ImageFileManager.LoadImageToView(imgPath + candidatesFiltered.get(position)+".jpg",
                holder.img_candidate);

        //todo: set image preview
        holder.img_candidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_Candidate_Details loDialog = new Dialog_Candidate_Details(context,
                        candidatesFiltered.get(position));

                loDialog.new Dialog_Preview_Image().initDialog();
            }
        });

        //todo: view candidate details
        holder.btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_Candidate_Details loDialog = new Dialog_Candidate_Details(context,
                        candidatesFiltered.get(position));

                loDialog.new Dialog_Details().initDialogDetails();
            }
        });
    }

    @Override
    public int getItemCount() {
        return candidatesFiltered.size();
    }

    public static class Candidate_Details{

        private String urlImg;
        private String name;
        private String school;
        private String number;

        public String getUrlImg() {
            return urlImg;
        }

        public void setUrlImg(String urlImg) {
            this.urlImg = urlImg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
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

                List<Candidate_Details> filteredCandidates = new ArrayList<>();
                for (Candidate_Details values: candidates){
                    if (values.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredCandidates.add(values);
                    }else if (values.getSchool().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredCandidates.add(values);
                    }else if (values.getNumber().toLowerCase().contains(constraint.toString().toLowerCase())){
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

            adapter.candidatesFiltered = (List<Candidate_Details>) results.values;
            adapter.notifyDataSetChanged();
        }
    }

    public class VH_Candidates extends RecyclerView.ViewHolder {

        public MaterialTextView mtv_name;
        private ShapeableImageView img_candidate;
        private MaterialButton btn_view;

        public VH_Candidates(@NonNull View itemView) {
            super(itemView);

            mtv_name = itemView.findViewById(R.id.mtv_name);
            img_candidate = itemView.findViewById(R.id.img_candidate);
            btn_view = itemView.findViewById(R.id.btn_view);
        }
    }
}
