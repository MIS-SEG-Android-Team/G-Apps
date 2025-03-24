package org.rmj.guanzongroup.useraccount.Fragment;

import static org.rmj.guanzongroup.documentscanner.constants.ScanConstants.IDCodexx;
import static org.rmj.guanzongroup.documentscanner.constants.ScanConstants.IDFacexx;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.AppConstants;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.Obj.UserIdentification;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.documentscanner.DocScanner;
import org.rmj.guanzongroup.useraccount.Activity.Activity_DocumentScan;
import org.rmj.guanzongroup.useraccount.Activity.Activity_IDVerification;
import org.rmj.guanzongroup.useraccount.Activity.Activity_ProfileVerification;
import org.rmj.guanzongroup.useraccount.Etc.IDDetail;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.OnSubmitIDPictureListener;
import org.rmj.guanzongroup.useraccount.ViewModel.VMID2Verification;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Fragment_ID2 extends Fragment {

    private VMID2Verification mViewModel;

    private final IDDetail poDetail = new IDDetail();

    private ImageView imageFront, imageBack;
    private AutoCompleteTextView spnIDType;
    private TextInputEditText txtExpire,
            txtIDNmbr;

    private MessageBox poDialogx;
    private Dialog_Loading poLoad;

    private boolean cFront;

    private final ActivityResultLauncher<String> poRequest = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(!result){
                Toast.makeText(requireActivity(), "Please allow camera permissions to proceed", Toast.LENGTH_SHORT).show();
            } else {
                InitializeCamera(cFront);
            }
        }
    });

    private final ActivityResultLauncher<Intent> poCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            try {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Intent loIntent = result.getData();
                    String lsFileNme = loIntent.getStringExtra("sImageInf");
                    FileInputStream loStream = requireActivity().openFileInput(lsFileNme);
                    Bitmap bmp = BitmapFactory.decodeStream(loStream);
                    loStream.close();

                    if (cFront) {
                        String lsFileNm = mViewModel.getUserID() + "-" + poDetail.getsIDCodexx() + "-" + AppConstants.DOC_FILE_VALID_ID + "_Front.jpg";
                        poDetail.setsFrontImg(lsFileNm);
                        String lsResult = DocScanner.saveBitmap2SD(bmp, lsFileNm);
                        if(lsResult == null){

                        } else {
                            imageFront.setImageBitmap(bmp);
                            poDetail.setsFrntPath(lsResult);
                        }
                    } else {
                        String lsFileNm = mViewModel.getUserID() + "-" + poDetail.getsIDCodexx() + "-" + AppConstants.DOC_FILE_VALID_ID + "_Back.jpg";
                        poDetail.setsBackImgx(lsFileNm);
                        String lsResult = DocScanner.saveBitmap2SD(bmp, lsFileNm);
                        if(lsResult == null){

                        } else {
                            imageBack.setImageBitmap(bmp);
                            poDetail.setsBackPath(lsResult);
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    });

    public static Fragment_ID2 newInstance() {
        return new Fragment_ID2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(VMID2Verification.class);
        View view = inflater.inflate(R.layout.fragment_i_d2, container, false);
        poDialogx = new MessageBox(requireActivity());
        poLoad = new Dialog_Loading(requireActivity());

        spnIDType = view.findViewById(R.id.tie_validIDNames);
        txtIDNmbr = view.findViewById(R.id.tie_idNox);

        txtExpire = view.findViewById(R.id.tie_expryDte1);

        imageFront = view.findViewById(R.id.img_frontID);
        imageBack = view.findViewById(R.id.img_backID);

        poDialogx.initDialog();

        List<UserIdentification> loList = Activity_IDVerification.getInstance().getIDList();

        ArrayList<String> lsIDList = new ArrayList<>();
        for (int x = 0; x < loList.size(); x++) {
            lsIDList.add(loList.get(x).getIDName());
        }

        spnIDType.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, lsIDList));
        spnIDType.setOnItemClickListener((parent, view1, position, id) -> {
            try {
                for (int x = 0; x < loList.size(); x++) {
                    if (spnIDType.getText().toString().trim().equalsIgnoreCase(loList.get(x).getIDName())) {

                        //Check if selected ID is already selected as the first id
                        String lsIDCode1 = Activity_IDVerification.getInstance().getsIDCodexx();
                        IDCodexx = lsIDCode1;
                        if(loList.get(x).getIDCode().equalsIgnoreCase(lsIDCode1)){
                            spnIDType.setText("");
                            Toast.makeText(requireActivity(), "Please select other valid ID type.", Toast.LENGTH_SHORT).show();
                        } else {
                            poDetail.setsIDCodexx(loList.get(x).getIDCode());
                            poDetail.setcHasExpre(loList.get(x).hasExpiry());
                            poDetail.setcHasExpre(loList.get(x).hasExpiry());

                            if (loList.get(x).hasExpiry().equalsIgnoreCase("1")) {
                                view.findViewById(R.id.til_expryDte1).setVisibility(View.VISIBLE);
                            } else {
                                view.findViewById(R.id.til_expryDte1).setVisibility(View.GONE);
                            }

                            view.findViewById(R.id.textview).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.img_frontID).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.btnCaptureFront).setVisibility(View.VISIBLE);

                            poDetail.setcHasBackx(loList.get(x).hasBack());
                            if (loList.get(x).hasBack().equalsIgnoreCase("1")) {
                                view.findViewById(R.id.textview1).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.img_backID).setVisibility(View.VISIBLE);
                                view.findViewById(R.id.btnCaptureBack).setVisibility(View.VISIBLE);
                            } else {
                                view.findViewById(R.id.textview1).setVisibility(View.GONE);
                                view.findViewById(R.id.img_backID).setVisibility(View.GONE);
                                view.findViewById(R.id.btnCaptureBack).setVisibility(View.GONE);
                            }
                        }
                        break;
                    }
                }
                imageFront.setImageBitmap(null);
                imageBack.setImageBitmap(null);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        txtIDNmbr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null || !s.toString().trim().isEmpty()) {
                    poDetail.setsIDNumber(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtExpire.setOnClickListener(v -> {
            final Calendar newCalendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            final DatePickerDialog StartTime = new DatePickerDialog(requireActivity(), (view131, year, monthOfYear, dayOfMonth) -> {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String lsDate = dateFormatter.format(newDate.getTime());
                poDetail.setdExpiryxx(lsDate);
                txtExpire.setText(getDate(lsDate));
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            StartTime.getDatePicker().setMinDate(new Date().getTime());
            StartTime.show();
        });

        view.findViewById(R.id.btnCaptureFront).setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                poRequest.launch(Manifest.permission.CAMERA);
            } else if(!poDetail.getsIDCodexx().trim().isEmpty()) {
                cFront = true;
                IDFacexx = "Front";
                InitializeCamera(true);
            } else {
                Toast.makeText(requireActivity(), "Please select valid ID 1 first", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btnCaptureBack).setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                poRequest.launch(Manifest.permission.CAMERA);
            } else if(!poDetail.getsIDCodexx().trim().isEmpty()) {
                cFront = false;
                IDFacexx = "Back";
                InitializeCamera(false);
            } else {
                Toast.makeText(requireActivity(), "Please select valid ID 1 first", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btn_Submit).setOnClickListener(v -> {
            if(poDetail.getsIDCodexx() == null){
                Toast.makeText(requireActivity(), "Please select type of ID.", Toast.LENGTH_SHORT).show();
                return;
            } else if(poDetail.getsIDNumber() == null){
                Toast.makeText(requireActivity(), "Please enter id number.", Toast.LENGTH_SHORT).show();
                return;
            } else if(poDetail.getsFrontImg() == null){
                Toast.makeText(requireActivity(), "Please take a picture of the front of your ID", Toast.LENGTH_SHORT).show();
                return;
            }

            if(poDetail.getcHasBackx().equalsIgnoreCase("1")){
                if(poDetail.getsBackImgx() == null){
                    Toast.makeText(requireActivity(), "Please take a picture of the back of your ID", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            SubmitImage();
        });
        return view;
    }


    private void InitializeCamera(boolean cFront){
        Intent loIntent = new Intent(requireActivity(), Activity_DocumentScan.class);
        poCamera.launch(loIntent);
//        if(cFront) {
//            ImageFileHandler.InitializeMainCamera(requireActivity(), (intent, path, fileName) -> {
//                poDetail.setsFrntPath(path);
//                poDetail.setsFrontImg(fileName);
//            });
//        } else {
//            ImageFileHandler.InitializeMainCamera(requireActivity(), (intent, path, fileName) -> {
//                poDetail.setsBackPath(path);
//                poDetail.setsBackImgx(fileName);
//                poCamera.launch(intent);
//            });
//        }
    }

    private void SubmitImage(){
        mViewModel.SubmitIDPicture(poDetail, new OnSubmitIDPictureListener() {
            @Override
            public void OnSubmit(String title, String message) {
                poLoad.initDialog(title, message);
                poLoad.show();
            }

            @Override
            public void OnSuccess(String args) {
                poLoad.dismiss();
                requireActivity().startActivity(new Intent(requireActivity(), Activity_ProfileVerification.class));
                requireActivity().finish();
            }

            @Override
            public void OnFailed(String message) {
                poLoad.dismiss();

                poDialogx.setIcon(R.drawable.baseline_error_24);
                poDialogx.setTitle("ID Verification");
                poDialogx.setMessage(message);
                poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

                poDialogx.show();

            }
        });
    }

    public String getDate(String val){
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = null;
        try {
            formattedDate = formatter.format(fromUser.parse(val));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}