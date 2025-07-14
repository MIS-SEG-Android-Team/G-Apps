package org.rmj.guanzongroup.useraccount.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import org.rmj.guanzongroup.useraccount.R;

import java.util.ArrayList;

public class Adapter_AccountSettings extends RecyclerView.Adapter<Adapter_AccountSettings.SettingsMenuHolder> {
    private final ArrayList<String[]> poMenuLst;
    private final OnSettingsMenuSelection callBack;

    public Adapter_AccountSettings(ArrayList<String[]> foMenuLst, OnSettingsMenuSelection foCallBck){
        this.poMenuLst = foMenuLst;
        this.callBack = foCallBck;
    }

    @NonNull
    @Override
    public SettingsMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_menu_list, parent, false);

        return new SettingsMenuHolder(view, callBack);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsMenuHolder holder, int position) {
        String[] loMenu = poMenuLst.get(position);
        switch(loMenu[0]) {
            case "0":
                holder.imgIcon.setImageResource(R.drawable.ic_settings_person_24);
                break;
            case "1":
                holder.imgIcon.setImageResource(R.drawable.ic_settings_wallet_24);
                break;
            case "2":
                holder.imgIcon.setImageResource(R.drawable.ic_settings_shipping_24);
                break;
            case "3":
                holder.imgIcon.setImageResource(R.drawable.ic_settings_card_24);
                break;
            default:
                holder.imgIcon.setImageResource(R.drawable.ic_menu_gallery);
                break;
        }
        holder.lblMenuTitle.setText(loMenu[1]);
        holder.lblDescription.setText(loMenu[2]);
    }

    @Override
    public int getItemCount() {
        return poMenuLst.size();
    }

    public static class SettingsMenuHolder extends RecyclerView.ViewHolder{

        public ImageView imgIcon;
        public MaterialTextView lblMenuTitle;
        public MaterialTextView lblDescription;

        public SettingsMenuHolder(@NonNull View itemView, OnSettingsMenuSelection callBack) {
            super(itemView);

            imgIcon = itemView.findViewById(R.id.imgIcon);
            lblMenuTitle = itemView.findViewById(R.id.lblMenuTitle);
            lblDescription = itemView.findViewById(R.id.lblDescription);

            itemView.setOnClickListener(v -> {

                int position = getAdapterPosition();

                if(position != RecyclerView.NO_POSITION) {
                    callBack.onClick(position);
                }
            });

        }
    }

    public interface OnSettingsMenuSelection {
        void onClick(int position);
    }

}
