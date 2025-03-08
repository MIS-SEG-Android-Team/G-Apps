package org.rmj.guanzongroup.gconnect.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Dialog_BarcodeDetails {

    private final Context context;
    private final MessageBox poMessage;
    private AlertDialog poDialogx;
    private final Dialog_Loading poLoad;
    private String message;

    public Dialog_BarcodeDetails(Context context){
        this.context = context;
        this.poMessage = new MessageBox(context);
        this.poLoad = new Dialog_Loading(context);
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
            loBuilder.setCancelable(false)
                    .setView(view);
            poDialogx = loBuilder.create();

            initViews(view);

            btn_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TaskExecutor.Execute(null, new OnTaskExecuteListener() {
                        @Override
                        public void OnPreExecute() {
                            poLoad.initDialog("Guanzon Connect", "Validating Informaation...");
                            poLoad.show();
                        }

                        @Override
                        public Object DoInBackground(Object args) {

                            if (isValidInfo()){ //todo: validate info
                                return true;
                            }else {
                                return false;
                            }
                        }

                        @Override
                        public void OnPostExecute(Object object) {

                            if ((Boolean) object){
                                poLoad.dismiss();
                                poDialogx.dismiss();

                                //todo: return personal info
                                callback.onContinue(collectInfo());

                            }else {
                                poLoad.dismiss();

                                //todo: show message
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

                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    poDialogx.dismiss();
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

                //todo: final check, mobile number format and length
                if (isMobileValid()){
                    return true;
                }else {
                    return false;
                }

            }
        }

        private Boolean isMobileValid(){

            String mobile = tie_mobile.getText().toString();
            if (mobile.length() != 11){
                message = "Mobile number must be 11 digits";
                return false;
            }else {

                if (mobile.matches("^09[0-9]{9}")){
                    return true;
                }else {
                    message = "Mobile number must start with 09+";
                    return false;
                }
            }
        }

        private Personal_Info collectInfo(){

            //todo: initialize personal info
            Personal_Info info = new Personal_Info();
            info.setLname(tie_lname.getText().toString());
            info.setFname(tie_fname.getText().toString());
            info.setMname(tie_mname.getText().toString());
            info.setSuffix(tie_suffix.getText().toString());
            info.setMobile(tie_mobile.getText().toString());

            return info;

        }

        public interface onDialogButton{
            void onContinue(Personal_Info foVal);
        }

    }

    public class Dialog_PaymentDetails{

        private MaterialAutoCompleteTextView tie_paytype;
        private TextInputEditText tie_amount;

        private LinearLayout layout_finance;
        private ConstraintLayout layout_terms;
        private TextInputLayout til_sc;

        private MaterialAutoCompleteTextView tie_terms;
        private TextInputEditText tie_sc;

        private MaterialButton btn_continue;
        private MaterialButton btn_cancel;

        public void initDialogPayment(onSubmit callback){

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_paymentinfo, null, false);

            AlertDialog.Builder loBuilder =  new AlertDialog.Builder(context);
            loBuilder.setCancelable(false)
                    .setView(view);
            poDialogx = loBuilder.create();

            initViews(view);
            initPayTypes();
            initPayTerms();

            //todo: display terms and sc field, based on selected pay type
            tie_paytype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    displayPaymentType(position);
                }
            });

            btn_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TaskExecutor.Execute(null, new OnTaskExecuteListener() {
                        @Override
                        public void OnPreExecute() {
                            poLoad.initDialog("Guanzon Connect", "Validating Informaation...");
                            poLoad.show();
                        }

                        @Override
                        public Object DoInBackground(Object args) {

                            //todo: validate info
                            if (isValidInfo()){
                                return true;
                            }else {
                                return false;
                            }
                        }

                        @Override
                        public void OnPostExecute(Object object) {

                            if ((Boolean) object){
                                poLoad.dismiss();
                                poDialogx.dismiss();
                                callback.onGenerateQR(collectInfo()); //todo: return on submit
                            }else {
                                poLoad.dismiss();

                                //todo: show message
                                initMessage(getMessage(), "Okay", "", 2, false, new onMessage() {
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
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    poDialogx.dismiss();
                }
            });

            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
            poDialogx.show();

        }

        private void initViews(View view){

            tie_paytype = view.findViewById(R.id.tie_paytype);
            tie_amount = view.findViewById(R.id.tie_amount);

            layout_finance = view.findViewById(R.id.layout_finance);
            layout_terms = view.findViewById(R.id.layout_terms);
            til_sc = view.findViewById(R.id.til_sc);

            tie_terms = view.findViewById(R.id.tie_terms);
            tie_sc = view.findViewById(R.id.tie_sc);

            btn_continue = view.findViewById(R.id.btn_continue);
            btn_cancel = view.findViewById(R.id.btn_cancel);

        }

        private void initPayTypes(){

            List<String> payTypes = List.of("Cash", "Credit Card", "Northpoint");
            tie_paytype.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, payTypes));
        }

        private void initPayTerms(){

            List<String> payTerms = List.of("3", "6", "12", "24", "36");
            tie_terms.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, payTerms));
        }

        private Boolean isValidInfo(){

            if (tie_paytype.getText().toString().isEmpty()){ //todo: payment type, required
                message = "Payment type is required";
                return false;
            } else if (tie_amount.getText().toString().isEmpty()) { //todo: amount, required
                message = "Amount is required";
                return false;
            }else {

                Boolean isValid;
                String payType = tie_paytype.getText().toString();
                double amount = Double.parseDouble(tie_amount.getText().toString());

                //todo: validate field data, based on selected pay type
                isValid = switch (payType.toLowerCase()) {
                    case "cash" -> isCashValid(amount);
                    case "credit card" -> isCreditAmtValid(amount);
                    case "northpoint" -> isNorthpointValid(amount);
                    default -> false;
                };

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

        private void displayPaymentType(int position){

            switch (position){

                case 0: //todo: cash
                    layout_finance.setVisibility(View.GONE);
                    break;
                case 1: //todo: credit card
                    layout_finance.setVisibility(View.VISIBLE);
                    layout_terms.setVisibility(View.VISIBLE);
                    til_sc.setVisibility(View.GONE);
                    break;
                case 2: //todo: northpoint
                    layout_finance.setVisibility(View.VISIBLE);
                    layout_terms.setVisibility(View.VISIBLE);
                    til_sc.setVisibility(View.VISIBLE);
                    break;
            }
        }

        private Payment_Info collectInfo(){

            Payment_Info info = new Payment_Info();
            info.setPaytype(tie_paytype.getText().toString());
            info.setAmount(Double.parseDouble(tie_amount.getText().toString()));
            info.setTerms(Integer.parseInt(tie_terms.getText().toString()));
            info.setScAmt(Double.parseDouble(tie_sc.getText().toString()));

            return info;
        }

        public interface onSubmit{
            void onGenerateQR(Payment_Info foVal);
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

    public static class Payment_Info{

        private String paytype = "";
        private double amount = 0.00;
        private int terms = 0;
        private double scAmt = 0.00;

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getTerms() {
            return terms;
        }

        public void setTerms(int terms) {
            this.terms = terms;
        }

        public double getScAmt() {
            return scAmt;
        }

        public void setScAmt(double scAmt) {
            this.scAmt = scAmt;
        }
    }
}
