package org.rmj.guanzongroup.gconnect.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Adapter_Candidates extends RecyclerView.Adapter<Adapter_Candidates.VH_Candidates>{

    private final Context context;
    private final List<String> candidates;

    public Adapter_Candidates(Context context, List<String> candidates){
        this.context = context;
        this.candidates = candidates;
    }

    @NonNull
    @Override
    public VH_Candidates onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_candidate, parent, false);
        return new VH_Candidates(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH_Candidates holder, int position) {
        holder.mtv_name.setText(candidates.get(position));
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
        return candidates.size();
    }

    public class VH_Candidates extends RecyclerView.ViewHolder {

        public MaterialTextView mtv_name;
        private ToggleButton btn_vote;

        public VH_Candidates(@NonNull View itemView) {
            super(itemView);

            mtv_name = itemView.findViewById(R.id.mtv_name);
            btn_vote = itemView.findViewById(R.id.btn_vote);
        }
    }
}
