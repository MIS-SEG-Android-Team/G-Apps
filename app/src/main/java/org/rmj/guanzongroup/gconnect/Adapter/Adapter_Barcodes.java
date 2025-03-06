package org.rmj.guanzongroup.gconnect.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Adapter_Barcodes extends RecyclerView.Adapter<Adapter_Barcodes.VH_AdapterBarcodes> {

    List<String> barcodeList;

    public Adapter_Barcodes(List<String> barcodeList){
        this.barcodeList = barcodeList;
    }

    @NonNull
    @Override
    public VH_AdapterBarcodes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH_AdapterBarcodes(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_barcodelist, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull VH_AdapterBarcodes holder, int position) {
        holder.mtv_index.setText(String.valueOf(position + 1));
        holder.mtv_barcode.setText(barcodeList.get(position));
    }

    @Override
    public int getItemCount() {
        return barcodeList.size();
    }

    public class VH_AdapterBarcodes extends RecyclerView.ViewHolder{

        private MaterialTextView mtv_index;
        private MaterialTextView mtv_barcode;

        public VH_AdapterBarcodes(@NonNull View itemView) {
            super(itemView);

            mtv_index = itemView.findViewById(R.id.mtv_index);
            mtv_barcode = itemView.findViewById(R.id.mtv_barcode);

        }
    }
}
