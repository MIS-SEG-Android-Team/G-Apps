package org.guanzongroup.com.creditapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.guanzongroup.com.creditapp.Adapter.Adapter_InstallmentPlans;
import org.guanzongroup.com.creditapp.R;
import org.guanzongroup.com.creditapp.ViewModel.VMLoanTerm;
import org.json.JSONArray;
import org.rmj.g3appdriver.etc.CashFormatter;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.CreditApp.model.LoanTerm;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;

import java.util.List;
import java.util.Objects;

public class Activity_LoanTerm extends AppCompatActivity {

    private static Activity_LoanTerm instance;
    private VMLoanTerm mViewModel;
    private Dialog_Loading poLoad;
    private MessageBox poMessage;

    private Toolbar toolbar;

    private ImageView imgView;
    private TextView lblProdctx, lblUnitPrc;
    private TextInputEditText txtDownPay, txtMonthly;
    private LinearLayout lnMonthly;
    private MaterialButton btnNext;

    private RecyclerView recyclerView;

    private LoanTerm poTerm;
    private String lsBrndNm = null;

    public static Activity_LoanTerm getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loan_term);

        instance = this;
        mViewModel = new ViewModelProvider(Activity_LoanTerm.this).get(VMLoanTerm.class);
        poLoad = new Dialog_Loading(Activity_LoanTerm.this);
        poMessage = new MessageBox(Activity_LoanTerm.this);

        poMessage.initDialog();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Apply For A Loan");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        imgView = findViewById(R.id.imgProdct);
        lblProdctx = findViewById(R.id.txtProdNm);
        lblUnitPrc = findViewById(R.id.lblUnitPrce);
        txtDownPay = findViewById(R.id.tie_downPayment);
        txtMonthly = findViewById(R.id.tie_monthlyPay);
        recyclerView = findViewById(R.id.recyclerView);
        lnMonthly = findViewById(R.id.lnMonthlyPaym);
        btnNext = findViewById(R.id.btnNext);

        String lsListID = getIntent().getStringExtra("sListngID");
        String lsModlID = getIntent().getStringExtra("sModelIDx");
        mViewModel.GetProductInfo(lsListID).observe(Activity_LoanTerm.this, product -> {
            try{
                JSONArray laJson = new JSONArray(product.getImagesxx());
                String sampleImg = laJson.getJSONObject(0).getString("sImageURL");
                Picasso.get().load(sampleImg).into(imgView);
                lblProdctx.setText(product.getModelNme());
                lblUnitPrc.setText(CashFormatter.parse(product.getUnitPrce()));

                lsBrndNm = product.getBrandNme();

                mViewModel.ImportInstallmentPlans(lsModlID, new VMLoanTerm.OnDownloadInstallmentPlan() {
                    @Override
                    public void OnDownload(String title, String message) {
                        poLoad.initDialog(title, message);
                        poLoad.show();
                    }

                    @Override
                    public void OnDownload(List<LoanTerm> args) {
                        poLoad.dismiss();
                        LinearLayoutManager loManager = new LinearLayoutManager(Activity_LoanTerm.this);
                        loManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(loManager);
                        txtDownPay.setText(CashFormatter.parse(args.get(0).getnDownPaym()));
                        Adapter_InstallmentPlans loAdapter = new Adapter_InstallmentPlans(args, args1 -> {
                            poTerm = args1;
                            lnMonthly.setVisibility(View.VISIBLE);
                            txtMonthly.setText(CashFormatter.parse(args1.getnMonAmort()));
                        });
                        recyclerView.setAdapter(loAdapter);
                    }

                    @Override
                    public void OnFailed(String message) {
                        poLoad.dismiss();

                        poMessage.setIcon(R.drawable.baseline_error_24);
                        poMessage.setTitle("Apply For A Loan");
                        poMessage.setMessage(message);
                        poMessage.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                poMessage.dismiss();
                            }
                        });

                        poMessage.show();

                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        btnNext.setOnClickListener(view -> {
            if(poTerm == null){
                Toast.makeText(Activity_LoanTerm.this, "Please select installment plan.", Toast.LENGTH_SHORT).show();
            } else {
                Intent loIntent = new Intent(Activity_LoanTerm.this, Activity_ApplicantInfo.class);
                loIntent.putExtra("sListngID", lsListID);
                loIntent.putExtra("sUnitAppl", lsBrndNm);
                loIntent.putExtra("sModelIDx", poTerm.getsModelIDx());
                loIntent.putExtra("sLoanTerm", poTerm.getsLoanTerm());
                loIntent.putExtra("sDownPaym", poTerm.getnDownPaym());
                loIntent.putExtra("nMonAmort", poTerm.getnMonAmort());
                loIntent.putExtra("nDiscount", poTerm.getnRebatesx());
                loIntent.putExtra("nMiscExps", poTerm.getnMiscChrg());
                startActivity(loIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            startActivity(new Intent(Activity_LoanTerm.this, Activity_LoanProductList.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}