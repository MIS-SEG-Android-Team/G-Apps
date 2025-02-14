package org.rmj.guanzongroup.gconnect.Adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.Entities.EBingoCard;
import org.rmj.guanzongroup.gconnect.R;
import java.util.List;

public class Adapter_CardNumbers extends RecyclerView.Adapter<Adapter_CardNumbers.VH_CardNumbers>{

    List<EBingoCard> nmbrSet;

    public Adapter_CardNumbers(List<EBingoCard> nmbrSet){
        this.nmbrSet = nmbrSet;
    }

    @NonNull
    @Override
    public VH_CardNumbers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VH_CardNumbers(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cardnumbers, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH_CardNumbers holder, int position) {

        try {

            JSONObject entry = new JSONObject(nmbrSet.get(position).getColList());

            holder.mtv_num1.setText(entry.getString("0")); // LETTER B
            holder.mtv_num2.setText(entry.getString("1")); // LETTER I
            holder.mtv_num3.setText(entry.getString("2")); // LETTER N
            holder.mtv_num4.setText(entry.getString("3")); // LETTER G
            holder.mtv_num5.setText(entry.getString("4")); // LETTER O

            holder.mtv_num1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return nmbrSet.size();
    }

    class VH_CardNumbers extends  RecyclerView.ViewHolder{

        MaterialButton mtv_num1;
        MaterialButton mtv_num2;
        MaterialButton mtv_num3;
        MaterialButton mtv_num4;
        MaterialButton mtv_num5;

        public VH_CardNumbers(@NonNull View itemView) {
            super(itemView);

            mtv_num1 = itemView.findViewById(R.id.mtv_num1);
            mtv_num2 = itemView.findViewById(R.id.mtv_num2);
            mtv_num3 = itemView.findViewById(R.id.mtv_num3);
            mtv_num4 = itemView.findViewById(R.id.mtv_num4);
            mtv_num5 = itemView.findViewById(R.id.mtv_num5);

        }
    }
}
