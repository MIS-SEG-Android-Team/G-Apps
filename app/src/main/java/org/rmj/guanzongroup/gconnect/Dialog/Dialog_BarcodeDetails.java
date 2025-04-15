package org.rmj.guanzongroup.gconnect.Dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.g3appdriver.utils.Task.OnTaskExecuteListener;
import org.rmj.g3appdriver.utils.Task.TaskExecutor;
import org.rmj.guanzongroup.gconnect.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dialog_BarcodeDetails {

    //todo dialog objects
    private final Context context;
    private final MessageBox poMessage;
    private final Dialog_Loading poLoad;
    private onDialogButton callback;

    private AlertDialog poDialogx;
    private String message;

    //todo personal info objects
    private LinearLayout layout_personaldetails;
    private TextInputEditText tie_lname;
    private TextInputEditText tie_fname;
    private TextInputEditText tie_mname;
    private TextInputEditText tie_suffix;
    private TextInputEditText tie_address;
    private TextInputEditText tie_mobile;
    private MaterialAutoCompleteTextView tie_towncity;

    //todo payment info objects
    private LinearLayout layout_payment;
    private MaterialAutoCompleteTextView tie_paytype;
    private MaterialAutoCompleteTextView tie_terms;
    private MaterialAutoCompleteTextView tie_financer;
    private TextInputEditText tie_amount;

    //todo payment info layout objects
    private LinearLayout layout_finance;
    private ConstraintLayout layout_terms;
    private LinearLayout til_financer;

    //todo: dialog button objects
    private MaterialButton btn_continue;
    private MaterialButton btn_cancel;

    //todo personal info data
    private List<DTownInfo.TownProvinceInfo> townProvinceInfos;
    private final HashMap<String, String> loTownMap = new HashMap<>();

    //todo payment info data
    private HashMap<String, String> laFinancer = new HashMap<>();


    public Dialog_BarcodeDetails(Context context, List<DTownInfo.TownProvinceInfo> townProvinceInfos){

        this.context = context;
        this.poMessage = new MessageBox(context);
        this.poLoad = new Dialog_Loading(context);
        this.townProvinceInfos = townProvinceInfos;

    }

    //todo message methods
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
                poMessage.setIcon(R.drawable.baseline_contact_support_24);
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
    private String getMessage(){
        return message;
    }

    //todo dialog object displays
    public void initDialog(onDialogButton onListener){

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_personalinfo, null, false);

        AlertDialog.Builder loBuilder =  new AlertDialog.Builder(context);
        loBuilder.setCancelable(false)
                .setView(view);
        poDialogx = loBuilder.create();

        callback = onListener;

        initViews(view);
        initAdapter();
        initFinancer();
        initListener();

        poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
        poDialogx.show();

    }

    private void initViews(View view){

        //todo personal info fields

        layout_personaldetails = view.findViewById(R.id.layout_personaldetails);

        tie_lname = view.findViewById(R.id.tie_lname);
        tie_fname = view.findViewById(R.id.tie_fname);
        tie_mname = view.findViewById(R.id.tie_mname);
        tie_suffix = view.findViewById(R.id.tie_suffix);

        tie_address = view.findViewById(R.id.tie_address);
        tie_towncity = view.findViewById(R.id.tie_towncity);

        tie_mobile = view.findViewById(R.id.tie_mobile);

        //todo payment info fields

        layout_payment = view.findViewById(R.id.layout_payment);

        tie_paytype = view.findViewById(R.id.tie_paytype);
        tie_amount = view.findViewById(R.id.tie_amount);

        layout_finance = view.findViewById(R.id.layout_finance);
        layout_terms = view.findViewById(R.id.layout_terms);
        til_financer = view.findViewById(R.id.til_financer);

        tie_terms = view.findViewById(R.id.tie_terms);
        tie_financer = view.findViewById(R.id.tie_financer);

        btn_continue = view.findViewById(R.id.btn_continue);
        btn_cancel = view.findViewById(R.id.btn_cancel);

    }
    private void initAdapter(){

        //todo personal info fields
        if(townProvinceInfos != null){

            if (townProvinceInfos.size() > 0){

                List<String> loTowns = new ArrayList<>();

                //todo: iterate town and province info
                for (DTownInfo.TownProvinceInfo townInfo: townProvinceInfos){

                    String sTownName = townInfo.sTownName +", "+ townInfo.sProvName;

                    //todo: add town to list, if not added
                    if (!loTowns.contains(sTownName)){
                        loTowns.add(sTownName);
                        loTownMap.put(townInfo.sTownIDxx, sTownName);
                    }

                }

                tie_towncity.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item ,loTowns));
            }
        }

        //todo payment fields
        List<String> payTypes = List.of("Cash", "Credit Card", "Financing");
        tie_paytype.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, payTypes));

        List<String> payTerms = List.of("3", "6", "12", "24", "36");
        tie_terms.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, payTerms));
    }
    private void initListener(){

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

                        if (layout_payment.getVisibility() == View.VISIBLE){
                            return isValidPaymentInfo();
                        }

                        if (layout_personaldetails.getVisibility() == View.VISIBLE){
                            return isValidPersonalInfo();
                        }

                        return isValidPaymentInfo();
                    }

                    @Override
                    public void OnPostExecute(Object object) {

                        if ((Boolean) object){

                            poLoad.dismiss();

                            if (layout_payment.getVisibility() == View.VISIBLE){

                                layout_payment.setVisibility(View.GONE);
                                layout_personaldetails.setVisibility(View.VISIBLE);

                                btn_continue.setText("Generate QR");
                            }else if (layout_personaldetails.getVisibility() == View.VISIBLE){
                                poDialogx.dismiss();
                                callback.onGenerateQR(collectInfo()); //todo: return on submit
                            }

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

                if (layout_payment.getVisibility() == View.VISIBLE){
                    poDialogx.dismiss();
                }else if (layout_personaldetails.getVisibility() == View.VISIBLE){
                    layout_personaldetails.setVisibility(View.GONE);
                    layout_payment.setVisibility(View.VISIBLE);
                    btn_continue.setText("CONTINUE");
                }

            }
        });
    }

    private String getAdapterID(HashMap<String, String> map, String value){

        String returnID = "";
        for (Map.Entry<String, String> entry : map.entrySet()){

            if (entry.getValue().equalsIgnoreCase(value)){
                returnID = entry.getKey();
                break;
            }
        }

        return returnID;
    }

    //todo personal info methods
    private Boolean isValidPersonalInfo(){
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

    private Barcode_Details collectInfo(){

        Barcode_Details info = new Barcode_Details();

        //todo: initialize personal info
        info.setLname(tie_lname.getText().toString());
        info.setFname(tie_fname.getText().toString());
        info.setMname(tie_mname.getText().toString());
        info.setSuffix(tie_suffix.getText().toString());
        info.setMobile(tie_mobile.getText().toString());
        info.setAddress(tie_address.getText().toString());
        info.setTownId(getAdapterID(loTownMap, tie_towncity.getText().toString()));

        //todo: initialize payment info
        info.setPaytype(tie_paytype.getText().toString());
        info.setAmount(Double.parseDouble(tie_amount.getText().toString()));
        info.setTerms(Integer.parseInt(tie_terms.getText().toString()));
        info.setFinancer(getAdapterID(laFinancer, tie_financer.getText().toString()));

        return info;

    }

    //todo payment info methods
    private void initFinancer(){

        HashMap<String, String> laFinancerVal = new HashMap<>();
        laFinancerVal.put("C00118000296", "NorthPoint Excelsior Credit Corporation");
        laFinancerVal.put("C0W110000001", "Samsung Electronics Philippines Corporation");
        laFinancerVal.put("GCO116000731", "Home Credit Philippines");
        laFinancerVal.put("GCO116000734", "Flexi, Finance");
        laFinancerVal.put("GCO121000006", "GCash");

        laFinancer = laFinancerVal;

        List<String> financers = new ArrayList<>(laFinancer.values());
        tie_financer.setAdapter(new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, financers));

    }
    private void displayPaymentType(int position){

        switch (position){

            case 0: //todo: cash
                layout_finance.setVisibility(View.GONE);
                break;
            case 1: //todo: credit card
                layout_finance.setVisibility(View.VISIBLE);
                layout_terms.setVisibility(View.VISIBLE);
                til_financer.setVisibility(View.GONE);
                break;
            case 2: //todo: financer
                layout_finance.setVisibility(View.VISIBLE);
                layout_terms.setVisibility(View.GONE);
                til_financer.setVisibility(View.VISIBLE);
                break;
        }
    }

    private Boolean isValidPaymentInfo(){

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
                case "financing" -> isFinancerValid(amount);
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
    private Boolean isFinancerValid(double amount){

        if (amount <= 0.00){
            message = "Invalid credit amount";
            return false;
        } else {

            if (tie_financer.getText().toString().isEmpty()){
                message = "Please select financer";
                return false;
            }else {

                if (!laFinancer.containsValue(tie_financer.getText().toString())){
                    message = "Invalid financer";
                    return false;
                }else {

                    Boolean isValid = false;
                    for (Map.Entry<String, String> loMap: laFinancer.entrySet()){

                        if (loMap.getValue().equalsIgnoreCase(tie_financer.getText().toString())){

                            if (loMap.getKey().isEmpty()){
                                message = "Invalid financer id";
                                isValid = false;
                                break;
                            }

                            isValid = true;
                            break;

                        }

                    }

                    return isValid;
                }
            }
        }

    }

    public interface onDialogButton{
        void onGenerateQR(Barcode_Details foVal);
    }

    public class Dialog_QRImage{

        private ImageView img_QRCode;
        private MaterialButton btn_close;

        public void initDialogQRImage(Bitmap bitmapQR){

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_qrbarcode, null, false);

            AlertDialog.Builder loBuilder =  new AlertDialog.Builder(context);
            loBuilder.setCancelable(false)
                    .setView(view);
            poDialogx = loBuilder.create();

            img_QRCode = view.findViewById(R.id.img_QRCode);
            btn_close = view.findViewById(R.id.btn_close);

            img_QRCode.setImageBitmap(bitmapQR);
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    poDialogx.dismiss();
                }
            });

            poDialogx.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            poDialogx.getWindow().getAttributes().windowAnimations = org.rmj.g3appdriver.R.style.PopupAnimation;
            poDialogx.show();

        }
    }

    public static class Barcode_Details{

        //todo personal info data
        private String lname = "";
        private String fname = "";
        private String mname = "";
        private String suffix = "";
        private String mobile = "";
        private String address = "";
        private String townId = "";

        //todo payment info data
        private String paytype = "";
        private String financer = "";
        private double amount = 0.00;
        private int terms = 0;

        //todo personal info data initializer
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTownId() {
            return townId;
        }

        public void setTownId(String townId) {
            this.townId = townId;
        }

        //todo payment info data initializers

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getFinancer() {
            return financer;
        }

        public void setFinancer(String financer) {
            this.financer = financer;
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
    }

}
