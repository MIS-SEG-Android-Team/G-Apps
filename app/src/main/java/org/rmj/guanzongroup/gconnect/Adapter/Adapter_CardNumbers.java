package org.rmj.guanzongroup.gconnect.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

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

            String BNumber = entry.getString("0");
            String INumber = entry.getString("1");
            String NNumber = entry.getString("2");
            String GNumber = entry.getString("3");
            String ONumber = entry.getString("4");

            //TODO: SET DEFAULT TEXT
            holder.mtv_num1.setText(BNumber); // LETTER B
            holder.mtv_num2.setText(INumber); // LETTER I
            holder.mtv_num3.setText(NNumber); // LETTER N
            holder.mtv_num4.setText(GNumber); // LETTER G
            holder.mtv_num5.setText(ONumber); // LETTER O

            holder.mtv_num1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){
                        holder.mtv_num1.setTextOn(BNumber);
                        buttonView.setBackgroundResource(R.color.colorNavBottomDark);
                    }else {
                        holder.mtv_num1.setTextOff(BNumber);
                        buttonView.setBackgroundResource(R.color.white);
                    }

                }
            });

            holder.mtv_num2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){
                        holder.mtv_num2.setTextOn(INumber);
                        buttonView.setBackgroundResource(R.color.colorNavBottomDark);
                    }else {
                        holder.mtv_num2.setTextOff(INumber);
                        buttonView.setBackgroundResource(R.color.white);
                    }

                }
            });

            holder.mtv_num3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){
                        holder.mtv_num3.setTextOn(NNumber);
                        buttonView.setBackgroundResource(R.color.colorNavBottomDark);
                    }else {
                        holder.mtv_num3.setTextOff(NNumber);
                        buttonView.setBackgroundResource(R.color.white);
                    }

                }
            });

            holder.mtv_num4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){
                        holder.mtv_num4.setTextOn(GNumber);
                        buttonView.setBackgroundResource(R.color.colorNavBottomDark);
                    }else {
                        holder.mtv_num4.setTextOff(GNumber);
                        buttonView.setBackgroundResource(R.color.white);
                    }

                }
            });

            holder.mtv_num5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked){
                        holder.mtv_num5.setTextOn(ONumber);
                        buttonView.setBackgroundResource(R.color.colorNavBottomDark);
                    }else {
                        holder.mtv_num5.setTextOff(ONumber);
                        buttonView.setBackgroundResource(R.color.white);
                    }

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

        ToggleButton mtv_num1;
        ToggleButton mtv_num2;
        ToggleButton mtv_num3;
        ToggleButton mtv_num4;
        ToggleButton mtv_num5;

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
