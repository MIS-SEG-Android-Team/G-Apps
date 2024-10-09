package org.rmj.guanzongroup.ganado.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.dev.Database.DataAccessObject.DTownInfo;
import org.rmj.g3appdriver.etc.LoadDialog;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.etc.MessageBoxInstallment;
import org.rmj.guanzongroup.ganado.R;
import org.rmj.guanzongroup.ganado.ViewModel.VMPersonalInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Activity_ClientInfo extends AppCompatActivity {

    private VMPersonalInfo mViewModel;
    private MessageBox poMessage ;
    private MessageBoxInstallment icMessage;
    private LoadDialog poDialogx;
    private TextInputEditText txtLastNm, txtFrstNm, txtMiddNm, txtSuffixx,  txtBirthDt,
            txtEmailAdd, txtMobileNo,  txtHouseNox, txtAddress;

    private MaterialAutoCompleteTextView txtMunicipl,txtBPlace;
    private RadioGroup rgGender;
    private MaterialAutoCompleteTextView spinner_relation;
    private MaterialButton btnContinue;

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_client_info);

        poMessage = new MessageBox(Activity_ClientInfo.this);
        poDialogx = new LoadDialog(Activity_ClientInfo.this);

        initWidgets();

        mViewModel.InitializeApplication(getIntent());

        mViewModel.InitGeoLocation(Activity_ClientInfo.this);

        mViewModel.getRelation().observe(Activity_ClientInfo.this, eRelations->{
            try {
                ArrayList<String> string = new ArrayList<>();
                for (int x = 0; x < eRelations.size(); x++) {
                    String lsColor = eRelations.get(x).getRelatnDs();
                    string.add(lsColor);

                }
                ArrayAdapter<String> adapters = new ArrayAdapter<>(Activity_ClientInfo.this, android.R.layout.simple_spinner_dropdown_item, string.toArray(new String[0]));
                spinner_relation.setText(eRelations.get(0).getRelatnDs());
                spinner_relation.setText(eRelations.get(0).getRelatnDs());
                mViewModel.getModel().setsReltionx(eRelations.get(0).getRelatnID());
                spinner_relation.setAdapter(adapters);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        spinner_relation.setOnItemClickListener(new OnItemClickListener(spinner_relation));
        mViewModel.GetTownProvinceList().observe(Activity_ClientInfo.this, new Observer<List<DTownInfo.TownProvinceInfo>>() {
            @Override
            public void onChanged(List<DTownInfo.TownProvinceInfo> loList) {
                try {
                    ArrayList<String> string = new ArrayList<>();
                    for (int x = 0; x < loList.size(); x++) {
                        String lsTown = loList.get(x).sTownName + ", " + loList.get(x).sProvName;
                        string.add(lsTown);

                    }
                    ArrayAdapter<String> adapters = new ArrayAdapter<>(Activity_ClientInfo.this, android.R.layout.simple_spinner_dropdown_item, string.toArray(new String[0]));
                    txtBPlace.setAdapter(adapters);
                    txtBPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int x = 0; x < loList.size(); x++) {
                                String lsLabel = loList.get(x).sTownName + ", " + loList.get(x).sProvName;
                                String lsSlctd = txtBPlace.getText().toString().trim();
                                if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                    mViewModel.getModel().setBirthPlc(loList.get(x).sTownIDxx);
                                    mViewModel.getModel().setsBirthIDx(lsLabel);
                                    break;
                                }

                            }
                        }
                    });
                    ArrayAdapter<String> adapters1 = new ArrayAdapter<>(Activity_ClientInfo.this, android.R.layout.simple_spinner_dropdown_item, string.toArray(new String[0]));
                    txtMunicipl.setAdapter(adapters1);
                    txtMunicipl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int x = 0; x < loList.size(); x++) {
                                String lsLabel = loList.get(x).sTownName + ", " + loList.get(x).sProvName;
                                String lsSlctd = txtMunicipl.getText().toString().trim();
                                if (lsSlctd.equalsIgnoreCase(lsLabel)) {
                                    mViewModel.getModel().setTownIDxx(loList.get(x).sTownIDxx);
                                    mViewModel.getModel().setsMncplNme(lsLabel);
                                    break;
                                }

                            }



                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        txtBirthDt.setOnClickListener(v -> {
            final Calendar newCalendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
            final DatePickerDialog StartTime = new DatePickerDialog(Activity_ClientInfo.this,
                    android.R.style.Theme_Holo_Dialog, (view131, year, monthOfYear, dayOfMonth) -> {
                try {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    String lsDate = dateFormatter.format(newDate.getTime());
                    txtBirthDt.setText(lsDate);
                    Date loDate = new SimpleDateFormat("MMMM dd, yyyy").parse(lsDate);
                    lsDate = new SimpleDateFormat("yyyy-MM-dd").format(loDate);
                    mViewModel.getModel().setBirthDte(lsDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            StartTime.getDatePicker().setMaxDate(new Date().getTime());
            StartTime.show();
        });

        rgGender.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_male) {
                mViewModel.getModel().setGenderCd("0");
            }
            if (i == R.id.rb_female) {
                mViewModel.getModel().setGenderCd("1");
            }
        });

        btnContinue.setOnClickListener(v ->{
            mViewModel.InitGeoLocation(Activity_ClientInfo.this);
            mViewModel.getModel().setFrstName(txtFrstNm.getText().toString().trim());
            mViewModel.getModel().setMiddName(txtMiddNm.getText().toString().trim());
            mViewModel.getModel().setLastName(txtLastNm.getText().toString().trim());
            mViewModel.getModel().setSuffixNm(txtSuffixx.getText().toString().trim());
            mViewModel.getModel().setHouseNox(txtHouseNox.getText().toString());
            mViewModel.getModel().setAddressx(txtAddress.getText().toString().trim());
            mViewModel.getModel().setEmailAdd(txtEmailAdd.getText().toString().trim());
            mViewModel.getModel().setMobileNo(txtMobileNo.getText().toString());

            if (!mViewModel.InitGeoLocation(Activity_ClientInfo.this)){
                poMessage.initDialog();
                poMessage.setIcon(R.drawable.baseline_error_24);
                poMessage.setTitle("Kita Moto");
                poMessage.setMessage(mViewModel.GetMessage());
                poMessage.setPositiveButton("Dismiss", (view, dialog) -> {
                    dialog.dismiss();
                });
                poMessage.show();

            }else {
                mViewModel.SaveData(new VMPersonalInfo.OnSaveInquiry() {
                    @Override
                    public void OnSave() {
                        poDialogx.initDialog("Benta", "Saving inquiry. Please wait...", false);
                        poDialogx.show();
                    }
                    @Override
                    public void OnSuccess(String args) {

                        poDialogx.dismiss();
                        poMessage.initDialog();
                        poMessage.setIcon(R.drawable.ic_baseline_message_24);
                        poMessage.setTitle("Kita Moto");
                        poMessage.setMessage("Motorcycle inquiry saved successfully!");
                        poMessage.setPositiveButton("Dismiss", (view, dialog) -> {
                            poMessage.dismiss();
                            Intent loIntent = new Intent(Activity_ClientInfo.this, Activity_Installment_Summary.class);
                            loIntent.putExtra("sTransNox", args);
                            startActivity(loIntent);
                            overridePendingTransition(R.anim.anim_intent_slide_in_right, R.anim.anim_intent_slide_out_left);
                            finish();

                        });
                        poMessage.show();
                    }
                    @Override
                    public void OnFailed(String message) {
                        poDialogx.dismiss();
                        poMessage.initDialog();
                        poMessage.setIcon(R.drawable.baseline_error_24);
                        poMessage.setTitle("Kita Moto");
                        poMessage.setMessage(message);
                        poMessage.setPositiveButton("Dismiss", (view1, dialog) -> dialog.dismiss());
                        poMessage.show();
                    }
                });
            }
        });
    }

    private void initWidgets() {
        toolbar = findViewById(R.id.toolbar_PersonalInfo);
        mViewModel = new ViewModelProvider(Activity_ClientInfo.this).get(VMPersonalInfo.class);
        poMessage = new MessageBox(Activity_ClientInfo.this);
        icMessage = new MessageBoxInstallment(Activity_ClientInfo.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        txtLastNm = findViewById(R.id.txt_lastname);
        txtFrstNm = findViewById(R.id.txt_firstname);
        txtMiddNm = findViewById(R.id.txt_middname);
        txtSuffixx = findViewById(R.id.txt_suffix);
        txtBirthDt = findViewById(R.id.txt_birthdate);
        txtBPlace = findViewById(R.id.txt_bpTown);
        rgGender = findViewById(R.id.rg_gender);
        txtMobileNo = findViewById(R.id.txt_mobile);
        txtEmailAdd = findViewById(R.id.txt_emailAdd);
        txtHouseNox = findViewById(R.id.txt_houseNox);
        txtAddress = findViewById(R.id.txt_address);
        txtMunicipl = findViewById(R.id.txt_town);
        spinner_relation = findViewById(R.id.spinner_relation);

        btnContinue = findViewById(R.id.btnContinue);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showConfirmationDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            showConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }
    public void onDestroy() {
        mViewModel.RemoveInQuiry();
        getViewModelStore().clear();
        super.onDestroy();

    }
    private class OnItemClickListener implements AdapterView.OnItemClickListener {

        private final View loView;

        public OnItemClickListener(View loView) {
            this.loView = loView;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(loView == spinner_relation){
                mViewModel.getRelation().observe(Activity_ClientInfo.this, relations->{
                    mViewModel.getModel().setsReltionx(relations.get(i).getRelatnID());

                });
            }
        }
    }
    private void showConfirmationDialog(){
        poMessage.initDialog();
        poMessage.setPositiveButton("Yes", (view, dialog) -> {
            dialog.dismiss();
            mViewModel.RemoveInQuiry();
            finish();
            overridePendingTransition(org.rmj.g3appdriver.R.anim.anim_intent_slide_in_left, org.rmj.g3appdriver.R.anim.anim_intent_slide_out_right);

        });
        poMessage.setNegativeButton("No", (view, dialog) -> dialog.dismiss());
        poMessage.setIcon(R.drawable.baseline_contact_support_24);
        poMessage.setTitle("Kita Moto");
        poMessage.setMessage("Do you really want to exit? Every detail entered will be removed.");
        poMessage.show();
    }
}