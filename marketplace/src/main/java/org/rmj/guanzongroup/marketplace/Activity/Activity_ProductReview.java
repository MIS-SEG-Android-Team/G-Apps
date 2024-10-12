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
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductReview;
import org.rmj.guanzongroup.marketplace.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMProductReview;
import org.rmj.guanzongroup.marketplace.databinding.ActivityProductReviewBinding;

import java.util.Objects;

public class Activity_ProductReview extends AppCompatActivity {
    private static final String TAG = Activity_ProductOverview.class.getSimpleName();
    private VMProductReview mViewModel;
    private ActivityProductReviewBinding mBinding;
    private MessageBox poDialogx;
    private Dialog_Loading poLoad;
    private String psItemIdx = "", psEntryNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(Activity_ProductReview.this).get(VMProductReview.class);
        mBinding = ActivityProductReviewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        poLoad = new Dialog_Loading(Activity_ProductReview.this);
        getExtras();

        setSupportActionBar(mBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Review");

        poDialogx = new MessageBox(Activity_ProductReview.this);
        poDialogx.initDialog();

        displayData();
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
        showReviews();
    }

    private void showProductInfo() {
        mViewModel.getProductInfo(psItemIdx).observe(Activity_ProductReview.this, product -> {
            try {
                String lsSoldQty = "Sold: " + product.getSoldQtyx();
                JSONArray laJson = new JSONArray(product.getImagesxx());
                String sampleImg = laJson.getJSONObject(0).getString("sImageURL");
                Picasso.get().load(sampleImg).into(mBinding.imgProdct);
                mBinding.txtProdNm.setText(product.getModelNme());
                mBinding.txtSoldQt.setText(lsSoldQty);
                mBinding.ratingBar.setRating(4.5f);
            } catch (NullPointerException | JSONException e) {
                e.printStackTrace();
                finish();
            }
        });
    }

    private void showReviews() {
        poLoad.initDialog("Product Reviews", "Loading product reviews. Please wait...");
        poLoad.show();
        mViewModel.ImportReviews(psItemIdx, new VMProductReview.OnInquiryReviewsImportCallback() {
            @Override
            public void OnImport(String args) {
                try {
                    poLoad.dismiss();
                    mBinding.rvRatings.setVisibility(View.VISIBLE);
                    mBinding.lblNoRevs.setVisibility(View.GONE);
                    JSONObject loJson = new JSONObject(args);
                    Adapter_ProductReview loAdapter = new
                            Adapter_ProductReview(getFilteredFaqs(loJson.getJSONArray("detail")),
                            false);
                    loAdapter.notifyDataSetChanged();
                    mBinding.rvRatings.setLayoutManager(new LinearLayoutManager(Activity_ProductReview.this));
                    mBinding.rvRatings.setHasFixedSize(true);
                    mBinding.rvRatings.setAdapter(loAdapter);

                    if(!psEntryNo.isEmpty()){
                        mBinding.cvHLReview.setVisibility(View.VISIBLE);
                        JSONArray laJson = loJson.getJSONArray("detail");
                        for(int x = 0; x < laJson.length(); x++){
                            JSONObject loDetail = laJson.getJSONObject(x);
                            if(loDetail.getString("nEntryNox").equalsIgnoreCase(psEntryNo)){
                                mBinding.ratings.setRating(Integer.parseInt(loDetail.getString("nRatingxx")));
                                mBinding.txtClientName.setText(loDetail.getString("sUserName"));
                                mBinding.txtDateReview.setText(loDetail.getString("dCreatedx"));
                                mBinding.txtReview.setText(loDetail.getString("sRemarksx"));
                                mBinding.txtResponse.setText(loDetail.getString("sReplyxxx"));
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mBinding.rvRatings.setVisibility(View.GONE);
                    mBinding.lblNoRevs.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void OnFailed(String message) {
                Log.e(TAG, message);
                poLoad.dismiss();
                mBinding.rvRatings.setVisibility(View.GONE);
                mBinding.lblNoRevs.setVisibility(View.VISIBLE);
            }
        });
    }

    private JSONArray getFilteredFaqs(JSONArray foArray){
        JSONArray loArray = new JSONArray();
        if(!psEntryNo.isEmpty()) {
            for (int x = 0; x < foArray.length(); x++) {
                try {
                    if (!foArray.getJSONObject(x).getString("nEntryNox").equalsIgnoreCase(psEntryNo)) {
                        loArray.put(foArray.getJSONObject(x));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return loArray;
        } else {
            return foArray;
        }
    }
}