package org.rmj.guanzongroup.useraccount.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.etc.FormatUIText;
import org.rmj.g3appdriver.etc.InputFieldController;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.useraccount.Model.CompleteAccountDetailsInfo;
import org.rmj.guanzongroup.useraccount.R;
import org.rmj.guanzongroup.useraccount.ViewModel.VMAccountDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class Activity_CompleteAccountDetails extends AppCompatActivity {
    private static final String TAG = Activity_CompleteAccountDetails.class.getSimpleName();
    private VMAccountDetails mViewModel;
    private CompleteAccountDetailsInfo poDataMdl;
    private Toolbar toolbar;
    private Dialog_Loading poLoading;
    private MessageBox poDialog;
    private TextInputEditText txtLastNm, txtFirstN, txtMidNme, txtSuffix, txtBdatex, txtTaxNox,
            txtHouseN, txtStreet, txtGCashNo;
    private AutoCompleteTextView txtBplace, txtGender, txtCivilS,
            txtCtizen,
            txtTownCt,
            txtBarngy;
    private MaterialButton btnSaveDt, btnScan;
    private static final int QR_RESULT_CODE = 1; // This should match the code you used in setResult(1, loIntent)
    private String psBDate, ClientID, SourceCD, SourceNo;

    private final ActivityResultLauncher<Intent> poArl = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == QR_RESULT_CODE) {
                    Intent loIntent = result.getData();
                    if (loIntent != null) {
                        ClientID = loIntent.getStringExtra("ClientID");
                        SourceCD = loIntent.getStringExtra("SourceCD");
                        SourceNo = loIntent.getStringExtra("SourceNo");

                        addScannedClientQR();
                    } else {
                        Toast.makeText(this, "No data result receive.", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_account_details);

        initObjects();
        initViews();
        setUpToolbar();
        setInputOptions();

        poDialog.initDialog();

        btnSaveDt.setOnClickListener(v -> saveAccountDetails());
        btnScan.setOnClickListener((v-> scanAccountDetails()));
    }

    private void initObjects() {
        mViewModel = new ViewModelProvider(Activity_CompleteAccountDetails.this)
                .get(VMAccountDetails.class);
        poDataMdl = new CompleteAccountDetailsInfo();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        poDialog = new MessageBox(Activity_CompleteAccountDetails.this);
        txtLastNm = findViewById(R.id.tie_accountUpdate);
        txtFirstN = findViewById(R.id.tie_firstname);
        txtMidNme = findViewById(R.id.tie_middname);
        txtSuffix = findViewById(R.id.tie_suffix);
        txtBdatex = findViewById(R.id.tie_bdate);
        txtBplace = findViewById(R.id.tie_bplace);
        txtGender = findViewById(R.id.tie_gender);
        txtCivilS = findViewById(R.id.tie_civil_stat);
        txtCtizen = findViewById(R.id.tie_citizen);
        txtGCashNo = findViewById(R.id.tie_gCashNo);
        txtTaxNox = findViewById(R.id.tie_taxId);
        txtHouseN = findViewById(R.id.tie_houseNo);
        txtStreet = findViewById(R.id.tie_street);
        txtTownCt = findViewById(R.id.tie_towncity);
        txtBarngy = findViewById(R.id.tie_barangay);
        btnSaveDt = findViewById(R.id.btnSave);
        btnScan = findViewById(R.id.btn_Scan);
    }

    private void initData(){
        mViewModel.getClientDetail().observe(Activity_CompleteAccountDetails.this, eClientInfo -> {
            if(eClientInfo != null){
                txtLastNm.setText(eClientInfo.getLastName());
                txtFirstN.setText(eClientInfo.getFrstName());
                txtMidNme.setText(eClientInfo.getMiddName());
                txtSuffix.setText(eClientInfo.getSuffixNm());
                txtBdatex.setText(FormatUIText.formatGOCasBirthdate(eClientInfo.getBirthDte()));
                poDataMdl.setBirthDate(eClientInfo.getBirthDte());
                psBDate = eClientInfo.getBirthDte();
                txtGCashNo.setText(eClientInfo.getGCashNo());

                mViewModel.getBirthplace(eClientInfo.getBirthPlc()).observe(Activity_CompleteAccountDetails.this, info->{
                    txtBplace.setText(info);
                    poDataMdl.setBirthPlace(eClientInfo.getBirthPlc());
                });

                String genderCode = eClientInfo.getGenderCd();
                poDataMdl.setGender(genderCode);

                Map<String, String> genderMap = Map.of("0", "Male", "1", "Female", "2", "LGBTQ");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    txtGender.setText(genderMap.getOrDefault(genderCode, "Unknown"));
                }


                String civilStatCd = eClientInfo.getCvilStat();
                poDataMdl.setCivilStat(civilStatCd);

                Map<String, String> civilStatusMap = Map.of(
                        "0", "Single",
                        "1", "Married",
                        "2", "Separated",
                        "3", "Widowed",
                        "4", "Single-Parent",
                        "5", "Single-Parent W/ Live-in Partner"
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    txtCivilS.setText(civilStatusMap.getOrDefault(civilStatCd, "Unknown"));
                }

                txtHouseN.setText(eClientInfo.getHouseNo1());
                txtStreet.setText(eClientInfo.getAddress1());
                txtBarngy.setText(eClientInfo.getBrgyIDx1());

                mViewModel.GetTownProvinceName(eClientInfo.getTownIDx1()).observe(Activity_CompleteAccountDetails.this, info ->{
                    try {
                        txtTownCt.setText(info);
                        poDataMdl.setTownCity(eClientInfo.getTownIDx1());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                mViewModel.GetBarangay(eClientInfo.getBrgyIDx1()).observe(Activity_CompleteAccountDetails.this, info ->{
                    try {
                        txtBarngy.setText(info);
                        poDataMdl.setBarangay(eClientInfo.getBrgyIDx1());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                disableFields(txtLastNm, txtFirstN, txtMidNme, txtSuffix, txtBdatex,txtHouseN,txtStreet);
                disableAutoCompleteTextViews(txtBplace,txtGender,txtCivilS,txtTownCt,txtBarngy);
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Complete Details");
    }

    private void popUpCloseConfirmationDialog() {

        poDialog.setIcon(R.drawable.baseline_contact_support_24);
        poDialog.setTitle("Complete Account Details");
        poDialog.setMessage("Are you sure you want to cancel filling in account details?");

        poDialog.setPositiveButton("Yes", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
                Intent loIntent = new Intent();
                loIntent.putExtra("result", "cancelled");
                finish();
            }
        });

        poDialog.setNegativeButton("No", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
            }
        });

        poDialog.show();
    }

    private void disableFields(TextInputEditText... textInputEditTexts) {
        for (TextInputEditText editText : textInputEditTexts) {
            editText.setFocusable(false);
        }
    }
    private void disableAutoCompleteTextViews(AutoCompleteTextView... autoCompleteTextViews) {
        for (AutoCompleteTextView autoCompleteTextView : autoCompleteTextViews) {
            autoCompleteTextView.setClickable(false);
            autoCompleteTextView.setFocusable(false);
            autoCompleteTextView.setFocusableInTouchMode(false);
            txtBdatex.setOnClickListener(null);
            txtBplace.setOnClickListener(null);
            txtGender.setOnClickListener(null);
            txtCivilS.setOnClickListener(null);
        }
    }

    private void scanAccountDetails() {
        Intent loIntent = new Intent(Activity_CompleteAccountDetails.this, Activity_ClientInfo_QR.class);
        poArl.launch(loIntent);
    }
    private void addScannedClientQR(){
        mViewModel.importClientInfo(ClientID, SourceCD,SourceNo,new VMAccountDetails.OnClientInfoCallBack() {
            @Override
            public void onLoading() {
                poLoading = new Dialog_Loading(Activity_CompleteAccountDetails.this);
                poLoading.initDialog("Account Details", "Importing Client Info Data. Please wait.");
                poLoading.show();

            }
            @Override
            public void onSuccess(String fsMessage) {
                poLoading.dismiss();
                initData();
            }
            @Override
            public void onFailed(String fsMessage) {
                poLoading.dismiss();

                poDialog.setIcon(R.drawable.baseline_error_24);
                poDialog.setTitle("Account Details");
                poDialog.setMessage(fsMessage);
                poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                    @Override
                    public void OnButtonClick(View view, AlertDialog dialog) {
                        dialog.dismiss();
                    }
                });

                poDialog.show();

            }
        });
    }

    private void setInfoModelValues() {
        poDataMdl.setLastName(Objects.requireNonNull(Objects.requireNonNull(txtLastNm.getText()).toString().trim()));
        poDataMdl.setFirstName(Objects.requireNonNull(Objects.requireNonNull(txtFirstN.getText()).toString().trim()));
        poDataMdl.setMiddName(Objects.requireNonNull(Objects.requireNonNull(txtMidNme.getText()).toString().trim()));
        poDataMdl.setSuffixName(Objects.requireNonNull(Objects.requireNonNull(txtSuffix.getText()).toString().trim()));
        poDataMdl.setBirthDate(psBDate);
        poDataMdl.setsGCashNox(Objects.requireNonNull(Objects.requireNonNull(txtGCashNo.getText()).toString().trim()));
        poDataMdl.setTaxIdNumber(Objects.requireNonNull(Objects.requireNonNull(txtTaxNox.getText()).toString().trim()));
        poDataMdl.setHouseNumber(Objects.requireNonNull(Objects.requireNonNull(txtHouseN.getText()).toString().trim()));
        poDataMdl.setAddress(Objects.requireNonNull(Objects.requireNonNull(txtStreet.getText()).toString().trim()));
    }
    private void setInputOptions() {
        /** Date Auto Formatter */
        txtBdatex.setOnClickListener(v ->  {
            final Calendar newCalendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
            final DatePickerDialog StartTime = new DatePickerDialog(Activity_CompleteAccountDetails.this,
                    android.R.style.Theme_Holo_Dialog,  (view131, year, monthOfYear, dayOfMonth) -> {
                try {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String lsDate = dateFormatter.format(newDate.getTime());
                    txtBdatex.setText(lsDate);
                    Date loDate = new SimpleDateFormat("MMMM dd, yyyy").parse(lsDate);
                    lsDate = new SimpleDateFormat("yyyy-MM-dd").format(loDate);
                    Log.d(TAG, "Save formatted time: " + lsDate);
                    psBDate = lsDate;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            StartTime.getDatePicker().setMaxDate(new Date().getTime());
            StartTime.show();
        });

        /** AutoCompleteTextviews */

        // Setup auto complete town/city fields.
        mViewModel.getTownCityList().observe(Activity_CompleteAccountDetails.this, townObj ->{
            try {
                ArrayList<String> lsTownCts = mViewModel.getTownCityForInput(townObj);
                txtBplace.setAdapter(
                        InputFieldController.getAutoCompleteData(
                                Activity_CompleteAccountDetails.this, lsTownCts
                        )
                );
                txtTownCt.setAdapter(
                        InputFieldController.getAutoCompleteData(
                                Activity_CompleteAccountDetails.this, lsTownCts
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mViewModel.getCountryList().observe(Activity_CompleteAccountDetails.this, countries -> {
            try {
                ArrayList<String> lsCountry = mViewModel.getCountryForInput(countries);
                txtCtizen.setAdapter(
                        InputFieldController.getAutoCompleteData(
                                Activity_CompleteAccountDetails.this,
                                lsCountry
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /** Dropdowns */

        // Setup gender field.
        txtGender.setAdapter(
                InputFieldController.getAutoCompleteData(
                        Activity_CompleteAccountDetails.this,
                        mViewModel.getGenderList()
                )
        );

        // Setup Civil status field.
        txtCivilS.setAdapter(
                InputFieldController.getAutoCompleteData(
                        Activity_CompleteAccountDetails.this,
                        mViewModel.getCivilStatusList()
                )
        );


        /** Set OnItemClickListeners on Auto Complete Input Fields */
        txtBplace.setOnItemClickListener((adapterView, view, i, l) -> {
            mViewModel.getTownCityList().observe(this, townCties -> {
                try {
                    for(int x = 0; x < townCties.size(); x++) {
                        String lsTownCty = "";
                        lsTownCty = townCties.get(x).sTownNm + ", " + townCties.get(x).sProvNm;
                        if(lsTownCty.equalsIgnoreCase(txtBplace.getText().toString().trim())) {
                            poDataMdl.setBirthPlace(townCties.get(x).sTownID);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        txtTownCt.setOnItemClickListener((adapterView, view, i, l) -> {
            mViewModel.getTownCityList().observe(this, townObjs -> {
                try {
                    for (int x = 0; x < townObjs.size(); x++) {
                        String lsTownCt = townObjs.get(x).sTownNm + ", " + townObjs.get(x).sProvNm;
                        if (txtTownCt.getText().toString().equalsIgnoreCase(lsTownCt)) {
                            poDataMdl.setTownCity(townObjs.get(x).sTownID);
                            mViewModel.getBarangayList(townObjs.get(x).sTownID).observe(this, brgys -> {
                                txtBarngy.setAdapter(
                                        InputFieldController.getAutoCompleteData(
                                                Activity_CompleteAccountDetails.this,
                                                mViewModel.getBarangayForInput(brgys)
                                        )
                                );

                                txtBarngy.setOnItemClickListener((adapterView1, view1, i1, l1) -> {
                                    for(int x1 = 0; x1 < brgys.size(); x1++){
                                        if(brgys.get(x1).getBrgyName().equalsIgnoreCase(txtBarngy.getText().toString().trim())){
                                            poDataMdl.setBarangay(brgys.get(x1).getBrgyIDxx());
                                            break;
                                        }
                                    }
                                });
                            });
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        txtCtizen.setOnItemClickListener((adapterView, view, i, l) -> {
            mViewModel.getCountryList().observe(this, countries -> {
                try {
                    for(int x = 0; x < countries.size(); x++) {
                        if(countries.get(x).getNational() != null && !countries.get(x).getNational().isEmpty()) {
                            if(countries.get(x).getNational().equalsIgnoreCase(txtCtizen.getText().toString().trim())) {
                                poDataMdl.setCitizenship(countries.get(x).getCntryCde());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        txtGender.setOnItemClickListener((adapterView, view, i, l) -> poDataMdl.setGender(String.valueOf(i)));
        txtCivilS.setOnItemClickListener((adapterView, view, i, l) -> poDataMdl.setCivilStat(String.valueOf(i)));
    }
    private void saveAccountDetails() {
        setInfoModelValues();

        if(poDataMdl.isDataValid()) {
            mViewModel.completeClientInfo(poDataMdl.getClientEntityValues(), new VMAccountDetails.OnTransactionCallBack() {
                @Override
                public void onLoading() {
                    poLoading = new Dialog_Loading(Activity_CompleteAccountDetails.this);
                    poLoading.initDialog("Account Details", "Saving account information. Please wait.");
                    poLoading.show();
                }
                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();

                    poDialog.setIcon(R.drawable.ic_baseline_message_24);
                    poDialog.setTitle("Account Details");
                    poDialog.setMessage(fsMessage);
                    poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();

                            Intent intent = new Intent("android.intent.action.SUCCESS_LOGIN");
                            intent.putExtra("args", "auth");
                            sendBroadcast(intent);

                            finish();
                        }
                    });

                    poDialog.show();
                }
                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();

                    poDialog.setIcon(R.drawable.baseline_error_24);
                    poDialog.setTitle("Account Details");
                    poDialog.setMessage(fsMessage);
                    poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });

                    poDialog.show();

                }
            });
        } else {

            poDialog.setIcon(R.drawable.baseline_error_24);
            poDialog.setTitle("Account Details");
            poDialog.setMessage(poDataMdl.getMessage());
            poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    dialog.dismiss();
                }
            });

            poDialog.show();

        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            popUpCloseConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_RESULT_CODE && resultCode == RESULT_OK && data != null) {
            ClientID = data.getStringExtra("ClientID");
            SourceCD = data.getStringExtra("SourceCD");
            SourceNo = data.getStringExtra("SourceNo");

            mViewModel.importClientInfo(ClientID, SourceCD,SourceNo,new VMAccountDetails.OnClientInfoCallBack() {
                @Override
                public void onLoading() {
                    poLoading = new Dialog_Loading(Activity_CompleteAccountDetails.this);
                    poLoading.initDialog("Account Details", "Importing Client Info Data. Please wait.");
                    poLoading.show();
                }
                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();
                    initData();
                }
                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();

                    poDialog.setIcon(R.drawable.baseline_error_24);
                    poDialog.setTitle("Account Details");
                    poDialog.setMessage(fsMessage);
                    poDialog.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            dialog.dismiss();
                        }
                    });

                    poDialog.show();

                }
            });

        }
    }
    @Override
    public void onBackPressed() {
        popUpCloseConfirmationDialog();
    }
}

