package org.rmj.guanzongroup.gconnect.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.guanzongroup.gconnect.R;

public class Dialog_BarcodeDetails {

    private final Context context;
    private final MessageBox poMessage;
    private AlertDialog poDialogx;
    private String message;

    public Dialog_BarcodeDetails(Context context){
        this.context = context;
        this.poMessage = new MessageBox(context);
    }

    public class Dialog_PersonalInfo{

        private TextInputEditText tie_lname;
        private TextInputEditText tie_fname;

        private TextInputEditText tie_mname;
        private TextInputEditText tie_suffix;

        private TextInputEditText tie_mobile;
        private MaterialButton btn_continue;
        private MaterialButton btn_cancel;

        public void initDialogPersonalInfo(onDialogButton callback){

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_personalinfo, null, false);

            AlertDialog.Builder loBuilder =  new AlertDialog.Builder(context);
            loBuilder.setCancelable(true)
                    .setView(view);
            poDialogx = loBuilder.create();

            initViews(view);

            btn_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isValidInfo()){

                        Personal_Info info = new Personal_Info();
                        info.setLname(tie_lname.getText().toString());
                        info.setFname(tie_fname.getText().toString());
                        info.setMname(tie_mname.getText().toString());
                        info.setSuffix(tie_suffix.getText().toString());
                        info.setMobile(tie_mobile.getText().toString());

                        callback.onContinue(info);
                    }else {

                        initMessage(getMessage(), "Okay", "",
                                2, false, new onMessage() {
                                    @Override
                                    public void onPosBtnListener() {

                                    }

                                    @Override
                                    public void onNegBtnListener() {

                                    }
                                });
                    }

                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    poDialogx.dismiss();
                    callback.onCancel();
                }
            });

            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
            poDialogx.show();

        }

        private void initViews(View view){

            tie_lname = view.findViewById(R.id.tie_lname);
            tie_fname = view.findViewById(R.id.tie_fname);
            tie_mname = view.findViewById(R.id.tie_mname);
            tie_suffix = view.findViewById(R.id.tie_suffix);
            tie_mobile = view.findViewById(R.id.tie_mobile);

            btn_continue = view.findViewById(R.id.btn_continue);
            btn_cancel = view.findViewById(R.id.btn_cancel);

        }

        private Boolean isValidInfo(){
            if (tie_lname.getText().toString().isEmpty()){
                message = "Lastname is required";
                return false;
            } else if (tie_fname.getText().toString().isEmpty()) {
                message = "Firstname is required";
                return false;
            } else if (tie_mobile.getText().toString().isEmpty()) {
                message = "Mobile is required";
                return false;
            }else {
                return true;
            }
        }

        public interface onDialogButton{
            void onContinue(Personal_Info foVal);
            void onCancel();
        }

    }

    public class Dialog_PaymentDetails{

        private MaterialAutoCompleteTextView tie_paytype;
        private TextInputEditText tie_amount;
        private MaterialAutoCompleteTextView tie_terms;
        private TextInputEditText tie_sc;
        private MaterialButton btn_continue;
        private MaterialButton btn_cancel;

        public void initDialogPayment(onSubmit callback){

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_paymentinfo, null, false);

            AlertDialog.Builder loBuilder =  new AlertDialog.Builder(context);
            loBuilder.setCancelable(true)
                    .setView(view);
            poDialogx = loBuilder.create();

            initViews(view);

            btn_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isValidInfo()){
                        callback.onSuccess();
                    }else {
                        initMessage(getMessage(), "Okay", "", 2, false, new onMessage() {
                            @Override
                            public void onPosBtnListener() {
                                callback.onFailed();
                            }

                            @Override
                            public void onNegBtnListener() {

                            }
                        });
                    }
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onCancel();
                }
            });

            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
            poDialogx.show();

        }

        private void initViews(View view){

            tie_paytype = view.findViewById(R.id.tie_paytype);
            tie_amount = view.findViewById(R.id.tie_amount);
            tie_terms = view.findViewById(R.id.tie_terms);
            tie_sc = view.findViewById(R.id.tie_sc);

            btn_continue = view.findViewById(R.id.btn_continue);
            btn_cancel = view.findViewById(R.id.btn_cancel);

        }

        private Boolean isValidInfo(){

            if (tie_paytype.getText().toString().isEmpty()){
                message = "Payment type is required";
                return false;
            } else if (tie_amount.getText().toString().isEmpty()) {
                message = "Amount is required";
                return false;
            }else {

                Boolean isValid = false;
                String payType = tie_paytype.getText().toString();
                double amount = Integer.parseInt(tie_amount.getText().toString());

                switch (payType.toLowerCase()){

                    case "cash":
                        isValid = isCashValid(amount);
                        break;

                    case "credit card":
                        isValid = isCreditAmtValid(amount);
                        break;

                    case "northpoint":
                        isValid = isNorthpointValid(amount);
                        break;
                }

                return isValid;
            }

        }

        private Boolean isCashValid(double amount){

            if (amount <= 0.00){
                message = "Invalid cash amount";
                return false;
            }else {
                return true;
            }
        }

        private Boolean isCreditAmtValid(double amount){

            if (amount <= 0.00){
                message = "Invalid credit amount";
                return false;
            } else {

                if (tie_terms.getText().toString().isEmpty()){
                    message = "Terms is required";
                    return false;
                }else {
                    return true;
                }
            }

        }

        private Boolean isNorthpointValid(double amount){

            if (amount <= 0.00){
                message = "Invalid credit amount";
                return false;
            } else {

                if (tie_terms.getText().toString().isEmpty()){
                    message = "Terms is required";
                    return false;
                } else if (tie_sc.getText().toString().isEmpty()) {
                    message = "SC Amount is required";
                    return false;
                }else {

                    if (Integer.parseInt(tie_sc.getText().toString()) <= 0.00){
                        message = "Invalid SC Amount";
                        return false;
                    }else {
                        return true;
                    }
                }
            }

        }

        public interface onSubmit{
            void onSuccess();
            void onFailed();
            void onCancel();
        }

    }

    private String getMessage(){
        return message;
    }

    private void initMessage(String message, String btnPos, String btnNeg,
                             int type, Boolean forConfirm, onMessage callback){

        poMessage.initDialog();
        poMessage.setTitle("Guanzon Connect");
        poMessage.setMessage(message);

        switch (type){

            case 1: //todo: success message
                poMessage.setIcon(R.drawable.ic_baseline_message_24);
                break;
            case 2: //todo: error message
                poMessage.setIcon(R.drawable.baseline_error_24);
                break;
            case 3: //todo: confirm message
                poMessage.setIcon(R.drawable.ic_baseline_confirmation_pin_24);
                break;
            default:
                poMessage.setIcon(R.drawable.ic_baseline_message_24);
                break;
        }

        poMessage.setPositiveButton(btnPos, new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
                callback.onPosBtnListener();
            }
        });

        if (forConfirm){
            poMessage.setNegativeButton(btnNeg, new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    dialog.dismiss();
                    callback.onNegBtnListener();
                }
            });
        }

        poMessage.show();

    }

    private interface onMessage{
        void onPosBtnListener();
        void onNegBtnListener();
    }

    public static class Personal_Info{

        private String lname = "";
        private String fname = "";
        private String mname = "";
        private String suffix = "";
        private String mobile = "";

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getMname() {
            return mname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
