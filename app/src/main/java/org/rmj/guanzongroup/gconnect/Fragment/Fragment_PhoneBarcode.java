package org.rmj.guanzongroup.gconnect.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rmj.g3appdriver.dev.Database.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EBarcode;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.digitalgcard.Activity.Activity_QrCodeScanner;
import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Barcodes;
import org.rmj.guanzongroup.gconnect.Dialog.Dialog_BarcodeDetails;
import org.rmj.guanzongroup.gconnect.R;
import org.rmj.guanzongroup.gconnect.ViewModel.VMBarcode;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class Fragment_PhoneBarcode extends Fragment {

    private RecyclerView rv_products;
    private FloatingActionButton fabScan;
    private FloatingActionButton fab_submit;
    private MaterialTextView mtv_tapme;
    private MaterialTextView mtv_tapmesubmit;
    private LinearLayout layout_personalform;

    private VMBarcode mviewModel;
    private Dialog_Loading dialogLoad;
    private MessageBox messageBox;

    private JSONObject loQRData;
    private List<DTownInfo.TownProvinceInfo> townProvinceInfoList;

    @SuppressLint("NewApi")
    private final ActivityResultLauncher<Intent> poArlBarcode =  registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                Log.d("PhoneBarcode", String.valueOf(result.getResultCode()));

                //todo: check intent action
                if (result.getResultCode() == Activity.RESULT_FIRST_USER){

                    //todo: check result data
                    if (result.getData().getStringExtra("result") == null){

                        //todo: show message, failed to read empty barcode
                        initMessage("Failed to read barcode", "Okay", "",
                                2, false, new onMessage() {
                                    @Override
                                    public void onPosBtnListener() {

                                    }

                                    @Override
                                    public void onNegBtnListener() {

                                    }
                                });

                    }else {

                        //todo: double check result data
                        if (!result.getData().getStringExtra("result").isEmpty()){

                            //todo: show confirmation, save barcode
                            initMessage("Save Barcode?", "Yes", "No",
                                    3, true, new onMessage() {
                                        @Override
                                        public void onPosBtnListener() {

                                            String barcodeid = "MX01" +
                                                    LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")) +
                                                    mviewModel.countBarcode() + 1;

                                            EBarcode barcode = new EBarcode();
                                            barcode.setBarcodeIdxx(barcodeid);
                                            barcode.setBarcode(result.getData().getStringExtra("result"));

                                            mviewModel.saveBarcode(barcode);

                                            Toast.makeText(requireActivity(), "Barcode saved successfully", Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onNegBtnListener() {
                                            Toast.makeText(requireActivity(), "Barcode saving cancelled", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }else {

                            //todo: show message, failed to read empty barcode
                            initMessage("Failed to read barcode", "Okay",
                                    "", 2, false, new onMessage() {
                                        @Override
                                        public void onPosBtnListener() {

                                        }

                                        @Override
                                        public void onNegBtnListener() {

                                        }
                                    });
                        }

                    }

                }else {
                    if (result.getResultCode() == Activity.RESULT_CANCELED){
                        //todo: show toast message, barcode cancelled
                        Toast.makeText(requireActivity(), "Barcode reading cancelled", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    private final ActivityResultLauncher<String> poArlPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), granted-> {

                //todo: check camera permission, if granted then launch barcode scanner else show toast
                if (granted){
                    Intent loIntent = new Intent(requireActivity(), Activity_QrCodeScanner.class);
                    poArlBarcode.launch(loIntent);
                }else {
                    Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_barcode, container, false);
        mviewModel = new ViewModelProvider(requireActivity()).get(VMBarcode.class);
        dialogLoad = new Dialog_Loading(requireActivity());
        messageBox = new MessageBox(requireActivity());

        initViews(view); //todo: view initialization
        initAnimation(); //todo: animation initialization
        initListener(); //todo: listener initialization
        initObservables(); //todo: observables initialization
        initAdapter(); //todo: adapter initialization

        loQRData = new JSONObject();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initAnimation();
    }

    private void initViews(View view){

        rv_products = view.findViewById(R.id.rv_products);
        fabScan = view.findViewById(R.id.fab_scan);
        mtv_tapme = view.findViewById(R.id.mtv_tapmescan);
        mtv_tapmesubmit = view.findViewById(R.id.mtv_tapmesubmit);
        fab_submit = view.findViewById(R.id.fab_submit);
        layout_personalform = view.findViewById(R.id.layout_personalform);

    }

    private void initAnimation(){

        //todo: load text animation and listener for action button
        Animation anim = AnimationUtils.loadAnimation(requireActivity(), R.anim.anim_intent_slide_in_right);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //todo: after animation, load another animation
                mtv_tapme.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.bounce_animation));
                mtv_tapmesubmit.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.bounce_animation));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //todo: start animation
        mtv_tapme.startAnimation(anim);
        mtv_tapmesubmit.startAnimation(anim);
    }

    private void initListener(){

        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mviewModel.CheckPermission(new VMBarcode.onCheckPermission() {
                    @Override
                    public void onChecking(String message) {
                        dialogLoad.initDialog("Guanzon Connect", message);
                        dialogLoad.show();
                    }

                    @Override
                    public void onPermissionGranted() {

                        dialogLoad.dismiss();

                        //todo: launch barcode, if camera permission granted
                        Intent loIntent = new Intent(requireActivity(), Activity_QrCodeScanner.class);
                        poArlBarcode.launch(loIntent);
                    }

                    @Override
                    public void onPermissionDenied(String message) {

                        dialogLoad.dismiss();

                        //todo: show message, camera permission denied
                        initMessage(message, "Okay", "", 2, false, new onMessage() {
                            @Override
                            public void onPosBtnListener() {
                                poArlPermission.launch(Manifest.permission.CAMERA);
                            }

                            @Override
                            public void onNegBtnListener() {

                            }
                        });

                    }
                });
            }
        });
        fab_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialogDetails();
            }
        });

    }

    private void initObservables(){
        mviewModel.GetTownProvinceList().observe(getViewLifecycleOwner(), new Observer<List<DTownInfo.TownProvinceInfo>>() {
            @Override
            public void onChanged(List<DTownInfo.TownProvinceInfo> townProvinceInfos) {

                if (townProvinceInfos != null){

                    if (townProvinceInfos.size() > 0){

                        townProvinceInfoList = townProvinceInfos;
                    }
                }
            }
        });
    }

    private void initAdapter(){

        mviewModel.getBarcodeList().observe(getViewLifecycleOwner(), new Observer<List<EBarcode>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<EBarcode> eBarcodes) {

                Adapter_Barcodes adapter_barcodes = new Adapter_Barcodes(requireActivity(), eBarcodes, new Adapter_Barcodes.onDeleteRow() {
                    @Override
                    public void onDelete(String barcodeID) {
                        mviewModel.deleteBarcode(barcodeID);
                    }
                });
                adapter_barcodes.notifyDataSetChanged();

                rv_products.setAdapter(adapter_barcodes);
                rv_products.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

                //TODO: SHOW BUTTON IF LIST IS NOT EMPTY
                if (eBarcodes.size() > 0){
                    layout_personalform.setVisibility(View.VISIBLE);
                }else {
                    layout_personalform.setVisibility(View.GONE);
                }

            }
        });
    }

    private void initDialogDetails(){

        //todo: initialize sub classes, for personal and payment info
        Dialog_BarcodeDetails.Dialog_PersonalInfo dialogPersonalInfo =
                new Dialog_BarcodeDetails(requireActivity()).new Dialog_PersonalInfo();

        //todo: collect personal info result, initialize to QR data
        dialogPersonalInfo.initDialogPersonalInfo(townProvinceInfoList, new Dialog_BarcodeDetails.Dialog_PersonalInfo.onDialogButton() {
            @Override
            public void onContinue(Dialog_BarcodeDetails.Personal_Info foVal) {

                try {

                    //todo: check parsed data, if not empty then initialize QR data and proceed to payment
                    if (ParsePersonalInfo(foVal) != null){

                        initQRData("sCustInfo", ParsePersonalInfo(foVal));

                        Dialog_BarcodeDetails.Dialog_PaymentDetails dialogPaymentDetails =
                                new Dialog_BarcodeDetails(requireActivity()).new Dialog_PaymentDetails();

                        //todo: collect payment info result, initialize to QR data
                        dialogPaymentDetails.initDialogPayment(new Dialog_BarcodeDetails.Dialog_PaymentDetails.onSubmit() {

                            @Override
                            public void onGenerateQR(Dialog_BarcodeDetails.Payment_Info foVal) {

                                //todo: check parsed data, if not empty then collect result and initialize QR data
                                if (ParsePaymentInfo(foVal) != null){

                                    //todo: collect payment info result, initialize to QR data
                                    initQRData("sPaymInfo", ParsePaymentInfo(foVal));

                                    //todo: observe and collect scanned barcodes, if not empty then proceed to generating qr image
                                    mviewModel.getBarcodeList().observe(getViewLifecycleOwner(), new Observer<List<EBarcode>>() {
                                        @Override
                                        public void onChanged(List<EBarcode> eBarcodes) {

                                            if (eBarcodes != null){

                                                if (eBarcodes.size() > 0){ //todo: if barcode list is not empty

                                                    try {

                                                        JSONArray loIEMI = new JSONArray(); //todo: initialize array

                                                        for (EBarcode eBarcode: eBarcodes){

                                                            loIEMI.put(eBarcode.getBarcode()); //todo: insert to json array
                                                        }

                                                        loQRData.put("sSerialNo", loIEMI); //todo: insert serial list to QR data

                                                        //todo: generate qr image
                                                        mviewModel.generateQR(loQRData, new VMBarcode.onGenerateQR() {
                                                            @Override
                                                            public void onGenerating() {
                                                                dialogLoad.initDialog("Guanzon Connect", "Generating QR Image...");
                                                                dialogLoad.show();
                                                            }

                                                            @Override
                                                            public void onQRGenerated(Bitmap bitmap) {
                                                                dialogLoad.dismiss();

                                                                Dialog_BarcodeDetails.Dialog_QRImage  qrPreview =
                                                                        new Dialog_BarcodeDetails(requireActivity()).new Dialog_QRImage();

                                                                qrPreview.initDialogQRImage(bitmap);
                                                            }

                                                            @Override
                                                            public void onQRGenerationFailed(String message) {
                                                                dialogLoad.dismiss();

                                                                initMessage(message, "Okay", "", 2, false, new onMessage() {
                                                                    @Override
                                                                    public void onPosBtnListener() {

                                                                    }

                                                                    @Override
                                                                    public void onNegBtnListener() {

                                                                    }
                                                                });
                                                            }
                                                        });

                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }
                                        }
                                    });

                                }else {

                                    initMessage("Payment info qr data is empty", "Okay", "",
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

                    }else {

                        initMessage("Personal info qr data is empty", "Okay", "",
                                2, false, new onMessage() {
                                    @Override
                                    public void onPosBtnListener() {
                                    }

                                    @Override
                                    public void onNegBtnListener() {
                                    }
                                });
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private void initQRData(String key, JSONObject data){
        try {
            loQRData.put(key, data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private JSONObject ParsePersonalInfo(Dialog_BarcodeDetails.Personal_Info loVal){

        try {

            JSONObject loObj = new JSONObject();
            loObj.put("firstName", loVal.getFname());
            loObj.put("middleName", loVal.getMname());
            loObj.put("lastName", loVal.getLname());
            loObj.put("suffixName", loVal.getSuffix());
            loObj.put("mobileNumber", loVal.getMobile());
            loObj.put("address", loVal.getAddress());
            loObj.put("townID", loVal.getTownId());

            return loObj;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject ParsePaymentInfo(Dialog_BarcodeDetails.Payment_Info loVal){

        try {

            JSONObject loObj = new JSONObject();
            loObj.put("sPayAmt", loVal.getAmount()); //todo: set amount

            //todo: validate payment type, put objects based on payment type
            switch (loVal.getPaytype().toLowerCase()){

                case "cash":
                    loObj.put("paymentForm", "0");
                    break;
                case "credit card":
                    loObj.put("paymentForm", "0");
                    loObj.put("term", loVal.getTerms());
                    break;
                case "northpoint":
                    loObj.put("paymentForm", "0");
                    loObj.put("term", loVal.getTerms());
                    loObj.put("serviceCharge", loVal.getScAmt());
                    break;
                default:
                    loObj.put("paymentForm", "0");
                    break;
            }

            return loObj;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void initMessage(String message, String btnPos, String btnNeg,
                                int type, Boolean forConfirm, onMessage callback){

        messageBox.initDialog();
        messageBox.setTitle("Guanzon Connect");
        messageBox.setMessage(message);

        switch (type){

            case 1: //todo: success message
                messageBox.setIcon(R.drawable.ic_baseline_message_24);
                break;
            case 2: //todo: error message
                messageBox.setIcon(R.drawable.baseline_error_24);
                break;
            case 3: //todo: confirm message
                messageBox.setIcon(R.drawable.baseline_contact_support_24);
                break;
            default:
                messageBox.setIcon(R.drawable.ic_baseline_message_24);
                break;
        }

        messageBox.setPositiveButton(btnPos, new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
                callback.onPosBtnListener();
            }
        });

        if (forConfirm){
            messageBox.setNegativeButton(btnNeg, new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    dialog.dismiss();
                    callback.onNegBtnListener();
                }
            });
        }

        messageBox.show();

    }

    private interface onMessage{
        void onPosBtnListener();
        void onNegBtnListener();
    }
}
