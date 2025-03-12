package org.rmj.guanzongroup.gconnect.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;

import org.rmj.g3appdriver.dev.Database.Entities.EBarcode;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Adapter_Barcodes extends RecyclerView.Adapter<Adapter_Barcodes.VH_AdapterBarcodes> {

    private Context context;
    private List<EBarcode> barcodeList;
    private onDeleteRow callback;

    public Adapter_Barcodes(Context context, List<EBarcode> barcodeList, onDeleteRow callback){
        this.barcodeList = barcodeList;
        this.callback = callback;
        this.context = context;
    }

    @NonNull
    @Override
    public VH_AdapterBarcodes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH_AdapterBarcodes(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_barcodelist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH_AdapterBarcodes holder, int position) {
        holder.mtv_index.setText(String.valueOf(position + 1));
        holder.mtv_barcode.setText(barcodeList.get(position).getBarcode());
        holder.btn_imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageBox poMessage = new MessageBox(context);
                poMessage.initDialog();
                poMessage.setTitle("Guanzon Connect");
                poMessage.setMessage("Are you sure you want to delete this barcode?");
                poMessage.setIcon(R.drawable.baseline_contact_support_24);

                poMessage.setPositiveButton("Yes", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();

                        //barcodeList.remove(position);
                        callback.onDelete(barcodeList.get(position).getBarcodeIdxx());
                    }
                });

                poMessage.setNegativeButton("No", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

                poMessage.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return barcodeList.size();
    }

    public interface onDeleteRow{
        void onDelete(String barcodeID);
    }

    public class VH_AdapterBarcodes extends RecyclerView.ViewHolder{

        private MaterialTextView mtv_index;
        private MaterialTextView mtv_barcode;
        private ImageButton btn_imgdelete;

        public VH_AdapterBarcodes(@NonNull View itemView) {
            super(itemView);

            mtv_index = itemView.findViewById(R.id.mtv_index);
            mtv_barcode = itemView.findViewById(R.id.mtv_barcode);
            btn_imgdelete = itemView.findViewById(R.id.btn_imgdelete);

        }
    }
}
