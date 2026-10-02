package org.rmj.guanzongroup.marketplace.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.etc.CashFormatter;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductQueries;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;
import org.rmj.guanzongroup.marketplace.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMProductQueries;
import org.rmj.guanzongroup.marketplace.databinding.ActivityProductQueriesBinding;

import java.util.Objects;

public class Activity_ProductQueries extends AppCompatActivity {

    private final String TAG = Activity_ProductQueries.class.getSimpleName();
    private VMProductQueries mViewModel;
    private ActivityProductQueriesBinding mBinding;
    private MessageBox poDialogx;
    private Dialog_Loading poLoading;
    private String psItemIdx = "", psEntryNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mBinding.getRoot());

        mBinding = ActivityProductQueriesBinding.inflate(getLayoutInflater());
        poDialogx = new MessageBox(Activity_ProductQueries.this);
        poLoading = new Dialog_Loading(Activity_ProductQueries.this);
        mViewModel = new ViewModelProvider(Activity_ProductQueries.this).get(VMProductQueries.class);

        poDialogx.initDialog();
        getExtras();

        setSupportActionBar(mBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Asked Questions");

        displayData();

        mBinding.btnSend.setOnClickListener(v -> {
            if(!mBinding.tieQuestion.getText().toString().trim().isEmpty()) {
                String lsInput = mBinding.tieQuestion.getText().toString().trim();
                mViewModel.sendProductInquiry(psItemIdx, lsInput, new OnTransactionsCallback() {
                    @Override
                    public void onLoading() {
                        poLoading.initDialog("Marketplace", "Sending question/inquiry. Please wait.");
                        poLoading.show();
                    }

                    @Override
                    public void onSuccess(String fsMessage) {
                        mBinding.tieQuestion.setText("");
                        poLoading.dismiss();

                        poDialogx.setIcon(R.drawable.ic_baseline_message_24);
                        poDialogx.setTitle("Marketplace");
                        poDialogx.setMessage(fsMessage);
                        poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });

                        poDialogx.show();
                    }

                    @Override
                    public void onFailed(String fsMessage) {
                        poLoading.dismiss();

                        poDialogx.setIcon(R.drawable.baseline_error_24);
                        poDialogx.setTitle("Marketplace");
                        poDialogx.setMessage(fsMessage);
                        poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                            @Override
                            public void OnButtonClick(View view, AlertDialog dialog) {
                                dialog.dismiss();
                            }
                        });

                        poDialogx.show();

                    }
                });
            } else {

                poDialogx.setIcon(R.drawable.baseline_error_24);
                poDialogx.setTitle("Marketplace");
                poDialogx.setMessage("Please enter your question/inquiry.");
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getExtras() {

        if(getIntent().hasExtra("sListngId")) {

            psItemIdx = getIntent().getStringExtra("sListngId");
        } else {

            poDialogx.setIcon(R.drawable.baseline_error_24);
            poDialogx.setTitle("Marketplace");
            poDialogx.setMessage("Product does not exist.");
            poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    dialog.dismiss();
                    finish();
                }
            });

            poDialogx.show();

        }

        if(getIntent().hasExtra("nEntryNox")){
            psEntryNo = getIntent().getStringExtra("nEntryNox");
        }
    }

    private void displayData() {
        showProductInfo();
        showInquiries();
    }

    private void showProductInfo() {
        mViewModel.getProductInfo(psItemIdx).observe(Activity_ProductQueries.this, product -> {
            try {
                String lsSoldQty = "Sold: " + product.getSoldQtyx();
                JSONArray laJson = new JSONArray(product.getImagesxx());
                String sampleImg = laJson.getJSONObject(0).getString("sImageURL");
                Picasso.get().load(sampleImg).into(mBinding.imgProdct);
                mBinding.txtProdNm.setText(product.getModelNme());
                mBinding.txtSoldQt.setText(lsSoldQty);
                mBinding.txtPricex.setText(CashFormatter.parse(product.getUnitPrce()));
            } catch (NullPointerException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void showInquiries() {
        poLoading.initDialog("Product Inquiries", "Loading product inquiries. Please wait...");
        poLoading.show();
        mViewModel.ImportInquiries(psItemIdx, new VMProductQueries.OnInquiryReviewsImportCallback() {
            @Override
            public void OnImport(String args) {
                try {
                    poLoading.dismiss();
                    JSONObject loJson = new JSONObject(args);
                    mBinding.rvQueries.setVisibility(View.VISIBLE);
                    mBinding.lblNoFaqs.setVisibility(View.GONE);
                    Adapter_ProductQueries loAdapter = new Adapter_ProductQueries(
                            getFilteredFaqs(loJson.getJSONArray("detail")),
                            false);
                    loAdapter.notifyDataSetChanged();
                    mBinding.rvQueries.setLayoutManager(new LinearLayoutManager(Activity_ProductQueries.this));
                    mBinding.rvQueries.setHasFixedSize(true);
                    mBinding.rvQueries.setAdapter(loAdapter);

                    if(!psEntryNo.isEmpty()){
                        mBinding.cvHLQuestion.setVisibility(View.VISIBLE);
                        JSONArray laJson = loJson.getJSONArray("detail");
                        for(int x = 0; x < laJson.length(); x++){
                            JSONObject loDetail = laJson.getJSONObject(x);
                            if(loDetail.getString("nEntryNox").equalsIgnoreCase(psEntryNo)){
                                mBinding.txtClientName.setText(loDetail.getString("sUserName"));
                                mBinding.txtDateAsked.setText(loDetail.getString("dCreatedx"));
                                mBinding.txtQuery.setText(loDetail.getString("sQuestion"));
                                mBinding.txtResponse.setText(loDetail.getString("sReplyxxx"));
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mBinding.rvQueries.setVisibility(View.GONE);
                    mBinding.lblNoFaqs.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void OnFailed(String message) {
                Log.e(TAG, message);
                poLoading.dismiss();
                mBinding.rvQueries.setVisibility(View.GONE);
                mBinding.lblNoFaqs.setVisibility(View.VISIBLE);
            }
        });
    }

    private JSONArray getFilteredFaqs(JSONArray foArray) {
        JSONArray loArray = new JSONArray();
        if(psEntryNo.isEmpty()) {
            for (int x = 0; x < foArray.length(); x++) {
                try {
                    if (!foArray.getJSONObject(x).getString("sReplyxxx")
                            .equalsIgnoreCase("null")) {
                        loArray.put(foArray.getJSONObject(x));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int x = 0; x < foArray.length(); x++) {
                try {
                    if(!foArray.getJSONObject(x).getString("nEntryNox").equalsIgnoreCase(psEntryNo)) {
                        if (!foArray.getJSONObject(x).getString("sReplyxxx")
                                .equalsIgnoreCase("null")) {
                            loArray.put(foArray.getJSONObject(x));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return loArray;
    }
}