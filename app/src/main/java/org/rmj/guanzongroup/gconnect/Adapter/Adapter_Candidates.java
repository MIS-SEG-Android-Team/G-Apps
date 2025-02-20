package org.rmj.guanzongroup.gconnect.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Adapter_Candidates extends RecyclerView.Adapter<Adapter_Candidates.VH_Candidates>{

    private final List<String> candidates;

    public Adapter_Candidates(List<String> candidates){
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
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    public class VH_Candidates extends RecyclerView.ViewHolder {

        public MaterialTextView mtv_name;

        public VH_Candidates(@NonNull View itemView) {
            super(itemView);

            mtv_name = itemView.findViewById(R.id.mtv_name);
        }
    }
}
