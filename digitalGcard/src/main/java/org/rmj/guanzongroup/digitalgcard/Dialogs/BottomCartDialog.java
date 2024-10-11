package org.rmj.guanzongroup.digitalgcard.Dialogs;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.GCardCore.GCardSystem;
import org.rmj.g3appdriver.lib.GCardCore.Obj.CartItem;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.digitalgcard.R;
import org.rmj.guanzongroup.digitalgcard.ViewModel.VMGCardSystem;

public class BottomCartDialog extends BottomSheetDialogFragment {

    //    private VMRedeemables mViewModel;
    private VMGCardSystem mViewModel;


    private String itemTransNox;
    private String itemPromCode;
    private String itemNamex;
    private double itemPntsx;

    private View view;
    private TextView lblItemTotPoints;
    private TextView lblItemName, lblGcardPoints,lblItemPoints;
    private ImageButton btnClose, btnAdd, btnDeduct;
    private EditText txtQuantity;
    private MaterialButton btnAddToCart;

    private Dialog_Loading dialog_loading;
    private MessageBox dialog_success;
    private int Quantity = 1;

    public void setItemTransNox(String itemTransNox) {
        this.itemTransNox = itemTransNox;
    }

    public void setItemPromCode(String itemPromCode) {
        this.itemPromCode = itemPromCode;
    }

    public void setItemNamex(String itemNamex) { this.itemNamex = itemNamex; }

    public void setItemPntsx(double itemPntsx) { this.itemPntsx = itemPntsx; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bottom_dialog_addtocart_box, container, false);
        setupWidgets(view);
        try{
            mViewModel = new ViewModelProvider(requireActivity()).get(VMGCardSystem.class);
            mViewModel.setmContext(GCardSystem.CoreFunctions.REDEMPTION);
            mViewModel.getActiveGcard().observe(requireActivity(), gcard ->{
                lblGcardPoints.setText(String.valueOf(gcard.getAvlPoint()));
            });
            lblItemName.setText(itemNamex);
            lblItemPoints.setText(String.valueOf(itemPntsx));
            lblItemTotPoints.setText(String.valueOf(getTotPoints()));
            btnClose.setOnClickListener(v -> dismiss());

            btnAdd.setOnClickListener(v -> new Handler().post(() -> {
                Quantity += 1;
                txtQuantity.setText(String.valueOf(getQuantity()));
                lblItemTotPoints.setText(String.valueOf(getTotPoints()));
            }));

            btnDeduct.setOnClickListener(v -> new Handler().post(() -> {
                Quantity -= 1;
                txtQuantity.setText(String.valueOf(getQuantity()));
                lblItemTotPoints.setText(String.valueOf(getTotPoints()));
            }));

            btnAddToCart.setOnClickListener(v -> {
                CartItem item = new CartItem(itemTransNox, itemPromCode, Integer.parseInt(txtQuantity.getText().toString()), Double.parseDouble(lblItemTotPoints.getText().toString()));
                mViewModel.addToCart(item, new VMGCardSystem.GcardTransactionCallback() {
                    @Override
                    public void onLoad() {
                        dialog_loading.initDialog("Add to Cart","Adding redeemable item to cart, please wait...");
                        dialog_loading.show();
                    }

                    @Override
                    public void onSuccess(String fsMessage) {
                        dialog_loading.dismiss();
                        showMessage("Success", fsMessage, R.drawable.ic_baseline_message_24);
                    }

                    @Override
                    public void onFailed(String fsMessage) {
                        dialog_loading.dismiss();
                        showMessage("Failed", fsMessage, R.drawable.baseline_error_24);
                    }

                    @Override
                    public void onQrGenerate(Bitmap foBitmap) {

                    }
                });
            });
        }catch (NullPointerException e){
            showMessage("Error", e.getMessage(), R.drawable.baseline_error_24);
        }
        return view;
    }

    private void setupWidgets(View v){
        btnClose = v.findViewById(R.id.btn_bottom_sheet_close);
        lblItemName = v.findViewById(R.id.lbl_bottom_sheet_ItemName);
        lblItemPoints = v.findViewById(R.id.lbl_bottom_sheet_itemPrice);
        txtQuantity = v.findViewById(R.id.txt_bottom_sheet_itemQuantity);
        lblItemTotPoints = v.findViewById(R.id.lbl_bottom_sheet_itemTotal_price);
        lblGcardPoints = v.findViewById(R.id.lbl_bottom_sheet_available_points);
        btnAdd = v.findViewById(R.id.btn_bottom_sheet_add);
        btnDeduct = v.findViewById(R.id.btn_bottom_sheet_deduct);
        btnAddToCart = v.findViewById(R.id.btn_bottom_sheet_addToCart);
        dialog_loading = new Dialog_Loading(requireActivity());

    }
    private void showMessage(String fsTitle,String fsMessage, int fsIcon){
        dialog_success = new MessageBox(requireActivity());
        dialog_success.initDialog();
        dialog_success.setIcon(fsIcon);
        dialog_success.setTitle(fsTitle);
        dialog_success.setMessage(fsMessage);

        dialog_success.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
                dismiss();
            }
        });

        dialog_success.show();
    }
    private double getTotPoints(){
        return Integer.parseInt(txtQuantity.getText().toString()) * itemPntsx;
    }

    private int getQuantity(){
        if(Quantity>5){
            Quantity = 5;
            return 5;
        } else if(Quantity == 0){
            Quantity = 1;
            return 1;
        }
        return Quantity;
    }

}
