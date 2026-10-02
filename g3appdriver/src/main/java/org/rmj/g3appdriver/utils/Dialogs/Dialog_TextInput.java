package org.rmj.g3appdriver.utils.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.rmj.g3appdriver.R;

import java.util.Objects;

public class Dialog_TextInput {

    private AlertDialog poDialogx;
    private final Context poContext;

    public Dialog_TextInput(Context foContext) {
        this.poContext = foContext;
    }

    public void initDialog(String fsInpType, OnDialogConfirmation foCallBck){
        View view = LayoutInflater.from(poContext).inflate(R.layout.dialog_text_input,
                null, false);
        AlertDialog.Builder loBuilder = new AlertDialog.Builder(poContext);
        loBuilder.setView(view).setCancelable(false);
        poDialogx = loBuilder.create();
        poDialogx.setCancelable(false);

        TextView lblTitlex = view.findViewById(R.id.lbl_title);
        TextInputLayout tilInputx = view.findViewById(R.id.til_input);
        TextInputEditText txtInputx = view.findViewById(R.id.txt_input);
        Button btnPositive = view.findViewById(R.id.btn_positive);
        Button btnNegative = view.findViewById(R.id.btn_negative);

        lblTitlex.setText("Please enter " + Objects.requireNonNull(fsInpType));
        tilInputx.setHint(Objects.requireNonNull(fsInpType));

        btnPositive.setOnClickListener(v -> foCallBck.onConfirm(txtInputx.getText().toString().trim(), poDialogx));
        btnNegative.setOnClickListener(v -> foCallBck.onCancel(poDialogx));
    }

    public void show(){
        poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
        poDialogx.show();
    }

    public interface OnDialogConfirmation {
        void onConfirm(String fsInputx, AlertDialog dialog);
        void onCancel(AlertDialog dialog);
    }

}
