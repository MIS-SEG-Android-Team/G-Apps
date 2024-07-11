package org.rmj.guanzongroup.digitalgcard.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import org.rmj.g3appdriver.dev.Database.Entities.EBranchInfo;
import org.rmj.g3appdriver.dev.Database.Entities.EGcardApp;
import org.rmj.g3appdriver.dev.Database.Entities.EPointsRequest;
import org.rmj.g3appdriver.etc.ConnectionUtil;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.etc.Telephony;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.g3appdriver.lib.GCardCore.CodeGenerator;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.digitalgcard.Dialogs.Dialog_TransactionPIN;
import org.rmj.guanzongroup.digitalgcard.R;
import org.rmj.guanzongroup.digitalgcard.ViewModel.VMGCardOffline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Activity_GCardOffline extends AppCompatActivity {
    private VMGCardOffline mviewmodel;
    private MaterialToolbar toolbar;
    private MaterialAutoCompleteTextView tie_gcard_number;
    private MaterialAutoCompleteTextView tie_branch;
    private MaterialAutoCompleteTextView tie_src;
    private TextInputEditText tie_date;
    private TextInputEditText tie_refno;
    private TextInputEditText tie_otp;
    private MaterialButton btn_Submit;
    private HashMap<String, String> loCardNmbrs = new HashMap<>();
    private HashMap<String, String> loBranch = new HashMap<>();
    private HashMap<String, String> loSource = new HashMap<>();
    private Dialog_Loading poDialog;
    private MessageBox poMessage;
    private String loMessage;
    private ConnectionUtil poConn;
    private String OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_gcard_offline);

        mviewmodel = new ViewModelProvider(this).get(VMGCardOffline.class);
        poDialog = new Dialog_Loading(this);
        poMessage = new MessageBox(this);
        poConn = new ConnectionUtil(this);

        toolbar = findViewById(R.id.toolbar);
        tie_gcard_number = findViewById(R.id.tie_gcard_number);
        tie_branch = findViewById(R.id.tie_branch);
        tie_date = findViewById(R.id.tie_date);
        tie_src = findViewById(R.id.tie_src);
        tie_refno = findViewById(R.id.tie_refno);
        tie_otp = findViewById(R.id.tie_otp);
        btn_Submit = findViewById(R.id.btn_Submit);

        OTP = GenerateOTP();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        setListeners();
        SendOfflineEntries();
        SetGcardNmbrs();
        SetBranch();
        SetSource();

        tie_otp.setText(OTP);
    }

    public String GenerateOTP(){
        StringBuilder sBuilder = new StringBuilder();

        do {
            int randomNumber = new Random().nextInt(9);
            sBuilder.append(randomNumber);
        }while (sBuilder.toString().length() < 6);

        return sBuilder.toString();
    }

    private String GetCurrentDate() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        Date date = cal.getTime();

        SimpleDateFormat sFormat = new SimpleDateFormat();
        return sFormat.format(date);
    }

    private Boolean ValidateEntry() {
        String cardnumber = tie_gcard_number.getText().toString();
        String branch = tie_branch.getText().toString();
        String transdate = tie_date.getText().toString();
        String srctype = tie_src.getText().toString();
        String refno = tie_refno.getText().toString();
        String otp = tie_otp.getText().toString();

        if (cardnumber.isEmpty() || cardnumber == null) {
            loMessage = "Please select card number";
            return false;
        }

        if (branch.isEmpty() || branch == null) {
            loMessage = "Please select branch";
            return false;
        }

        if (transdate.isEmpty() || transdate == null) {
            loMessage = "Please select transaction date";
            return false;
        }

        if (srctype.isEmpty() || srctype == null) {
            loMessage = "Please select source";
            return false;
        }

        if (refno.isEmpty() || refno == null) {
            loMessage = "Please enter reference number";
            return false;
        }

        if (otp.isEmpty() || otp == null) {
            loMessage = "Please enter otp number";
            return false;
        }

        return true;
    }

    private void setListeners() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tie_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSelectedDate();
            }
        });
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                poMessage.initDialog();
                poMessage.setTitle("Points Request");

                try {
                    if (ValidateEntry()) {
                        SimpleDateFormat sFormat = new SimpleDateFormat("MMMM dd, yyyy");
                        Date dTransact = sFormat.parse(tie_date.getText().toString());

                        SimpleDateFormat sFormat1 = new SimpleDateFormat("yyyy-MM-dd");

                        EPointsRequest loParams = new EPointsRequest();
                        loParams.setsTransNox(new CodeGenerator().generateTransNox());
                        loParams.setsGCardNox(loCardNmbrs.get(tie_gcard_number.getText().toString()));
                        loParams.setdTransact(sFormat1.format(dTransact));
                        loParams.setsBranchCd(loBranch.get(tie_branch.getText().toString()));
                        loParams.setsReferNox(tie_refno.getText().toString());
                        loParams.setsSourceCd(loSource.get(tie_src.getText().toString()));
                        loParams.setsOTPasswd(tie_otp.getText().toString());
                        loParams.setnTranAmtx(0.00);
                        loParams.setnPointsxx(0.00);
                        loParams.setcTranStat("0");
                        loParams.setcSendStat("0");
                        loParams.setsUserIDxx(new AccountInfo(Activity_GCardOffline.this).getUserID());
                        loParams.setsIMEINoxx(new Telephony(Activity_GCardOffline.this).getDeviceID());
                        loParams.setdRequestd(GetCurrentDate());
                        loParams.setdTimeStmp(GetCurrentDate());

                        mviewmodel.SaveRequest(loParams, new VMGCardOffline.onRequest() {
                            @Override
                            public void onLoad(String title, String message) {
                                poDialog.initDialog(title, message);
                                poDialog.show();
                            }
                            @Override
                            public void onFinished(String result) {
                                poDialog.dismiss();

                                poMessage.setMessage(result);
                                poMessage.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                    @Override
                                    public void OnButtonClick(View view, AlertDialog dialog) {
                                        dialog.dismiss();

                                        tie_gcard_number.setText("");
                                        tie_gcard_number.setEnabled(true);

                                        tie_branch.setText("");
                                        tie_date.setText("");
                                        tie_src.setText("");
                                        tie_refno.setText("");
                                        tie_otp.setText("");

                                        Dialog_TransactionPIN loDialog = new Dialog_TransactionPIN(Activity_GCardOffline.this);
                                        loDialog.initDialog(OTP, "Submit this OTP to nearest branch for claiming your GCard Points");
                                    }
                                });
                                poMessage.show();
                            }
                        });
                    } else {
                        poMessage.setMessage(loMessage);
                        poMessage.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                        poMessage.show();
                    }
                }catch (Exception e){
                    poMessage.setMessage(e.getMessage());
                    poMessage.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });
                    poMessage.show();
                }
            }
        });
    }

    private void SetGcardNmbrs() {
        mviewmodel.GetCardNumbers().observe(this, new Observer<List<EGcardApp>>() {
            @Override
            public void onChanged(List<EGcardApp> eGcardApps) {
                if (eGcardApps.size() > 0) {

                    List<String> Gcards = new ArrayList<>();
                    for (int i = 0; i < eGcardApps.size(); i++) {
                        EGcardApp gcardApp = eGcardApps.get(i);

                        String sActive = gcardApp.getActvStat();
                        if (sActive.equalsIgnoreCase("1")) {
                            Gcards.add(gcardApp.getCardNmbr());

                            loCardNmbrs.put(gcardApp.getCardNmbr(), gcardApp.getGCardNox());
                        }
                    }

                    if (Gcards.size() == 1){
                        tie_gcard_number.setText(Gcards.get(0));
                        tie_gcard_number.setEnabled(false);
                    }else if (Gcards.size() > 1){
                        tie_gcard_number.setEnabled(true);
                        tie_gcard_number.setAdapter(new ArrayAdapter<String>(Activity_GCardOffline.this,
                                R.layout.support_simple_spinner_dropdown_item, Gcards));
                    }
                }
            }
        });
    }

    private void SetBranch() {
        mviewmodel.getMotorBranches().observe(this, new Observer<List<EBranchInfo>>() {
            @Override
            public void onChanged(List<EBranchInfo> eBranchInfos) {
                List<String> branches = new ArrayList<>();
                if (eBranchInfos.size() > 0) {
                    for (int i = 0; i < eBranchInfos.size(); i++) {
                        EBranchInfo foData = eBranchInfos.get(i);
                        branches.add(foData.getBranchNm());

                        loBranch.put(foData.getBranchNm(), foData.getBranchCd());
                    }
                }

                tie_branch.setAdapter(new ArrayAdapter<String>(Activity_GCardOffline.this,
                        R.layout.support_simple_spinner_dropdown_item, branches));
            }
        });
    }

    private void SetSource() {
        //TODO: TEMPORARY HARD CODED
        loSource.put("MC Sales", "M02910000001");
        loSource.put("SP Sales", "M02910000002");
        loSource.put("Job Order", "M02910000003");
        loSource.put("Monthly Payment", "M02910000004");

        List<String> sources = new ArrayList<>();
        for (Map.Entry<String, String> entry : loSource.entrySet()) {
            sources.add(entry.getKey());
        }

        tie_src.setAdapter(new ArrayAdapter<>(Activity_GCardOffline.this,
                R.layout.support_simple_spinner_dropdown_item, sources));
    }

    private void GetSelectedDate() {
        final Calendar newCalendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");

        // Set the maximum date to one month from the current date
        long maxDateInMillis = newCalendar.getTimeInMillis();

        final DatePickerDialog StartTime = new DatePickerDialog(Activity_GCardOffline.this,
                android.R.style.Theme_Holo_Dialog, (view131, year, monthOfYear, dayOfMonth) -> {
            try {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                String lsDate = dateFormatter.format(newDate.getTime());
                tie_date.setText(lsDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        // Set the default selection to the current date
        StartTime.getDatePicker().init(
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH),
                null
        );

        StartTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        StartTime.getDatePicker().setMaxDate(maxDateInMillis); // Set the maximum date

        StartTime.show();
    }

    private void SendOfflineEntries(){
        mviewmodel.GetPendingRqsts().observe(this, new Observer<List<EPointsRequest>>() {
            @Override
            public void onChanged(List<EPointsRequest> ePointsRequests) {
                if (poConn.isDeviceConnected()){
                    if (ePointsRequests.size() > 0){

                        Toast.makeText(Activity_GCardOffline.this, "Sending requests . . .", Toast.LENGTH_LONG)
                                .show();

                        for (int i = 0; i < ePointsRequests.size(); i++){
                            try {
                                EPointsRequest loData = ePointsRequests.get(i);
                                Log.d(getClass().getSimpleName(), loData.getsTransNox());
                                mviewmodel.UploadPendingRequests(loData, new VMGCardOffline.onRequestPending() {
                                    @Override
                                    public void onLoad(String message) {
                                        Log.d(getClass().getSimpleName(), message);
                                    }
                                    @Override
                                    public void onSuccess(String result) {
                                        Log.d(getClass().getSimpleName(), result);
                                    }
                                    @Override
                                    public void onFailed(String result) {
                                        Log.d(getClass().getSimpleName(), result);
                                    }
                                });

                                Thread.sleep(1000);

                            }catch (Exception e){
                                Log.d(getClass().getSimpleName(), e.getMessage());
                            }
                        }

                        Toast.makeText(Activity_GCardOffline.this, "Finished processing your requests", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        });
    }
}