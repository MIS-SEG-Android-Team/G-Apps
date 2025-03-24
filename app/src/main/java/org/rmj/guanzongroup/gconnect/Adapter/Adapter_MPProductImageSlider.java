package org.rmj.guanzongroup.gconnect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.marketplace.Activity.Activity_ProductList;
import java.util.List;

public class Adapter_MPProductImageSlider extends RecyclerView.Adapter<Adapter_MPProductImageSlider.SliderAdapterVH> {
    private Context context;
    private List<Integer> oSlideLst;
    private AccountInfo loAccount;

    public Adapter_MPProductImageSlider(Context context, List<Integer> oSlideLst){
        this.context = context;
        this.oSlideLst = oSlideLst;
        this.loAccount = new AccountInfo(context);
    }

    @NonNull
    @Override
    public SliderAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_productslider, parent, false);
        return new Adapter_MPProductImageSlider.SliderAdapterVH(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull SliderAdapterVH holder, int position) {
        holder.imageViewBackground.setImageResource(oSlideLst.get(position));
        holder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loAccount.getLoginStatus()){

                    if (loAccount.getVerificationStatus() > 0){
                        String sBrand = "";

                        Integer logo = oSlideLst.get(position);
                        switch (logo){
                            case R.drawable.apple_logo:
                                sBrand = "Apple";
                                break;
                            case R.drawable.huawei_logo:
                                sBrand = "HUAWEI";
                                break;
                            case R.drawable.honor_logo:
                                sBrand = "Honor";
                                break;
                            case R.drawable.oppo_logo:
                                sBrand = "Oppo";
                                break;
                            case R.drawable.realme_logo:
                                sBrand = "Realme";
                                break;
                            case R.drawable.samsung_logo:
                                sBrand = "Samsung";
                                break;
                            case R.drawable.tecno_logo:
                                sBrand = "Tecno";
                                break;
                            case R.drawable.vivo_logo:
                                sBrand = "Vivo";
                                break;
                            case R.drawable.xiaomi_logo:
                                sBrand = "Xiaomi";
                                break;
                        }

                        Intent loIntent = new Intent(context, Activity_ProductList.class);
                        loIntent.putExtra("xBrandNme", sBrand);
                        context.startActivity(loIntent);
                    }else {
                        Toast.makeText(context, "Unable to use feature. Please complete and verify your account", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(context, "Unable to use feature. Please login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return oSlideLst.size();
    }
    public static class SliderAdapterVH extends RecyclerView.ViewHolder {
        public View itemView;
        public ImageView imageViewBackground;
        public SliderAdapterVH(View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.imageViewBackground = itemView.findViewById(org.rmj.g3appdriver.R.id.iv_auto_image_slider);
        }
    }
}
