package org.rmj.guanzongroup.marketplace.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;
import org.rmj.guanzongroup.marketplace.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMWriteProductReview;
import org.rmj.guanzongroup.marketplace.databinding.ActivityWriteProductReviewBinding;

import java.util.Objects;

public class Activity_WriteProductReview extends AppCompatActivity {

    private VMWriteProductReview mViewModel;
    private ActivityWriteProductReviewBinding mBinding;
    private MessageBox poDialogx;
    private Dialog_Loading poLoading;

    private String psItemIdx = "", psOrderID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(Activity_WriteProductReview.this)
                .get(VMWriteProductReview.class);

        mBinding = ActivityWriteProductReviewBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        poDialogx = new MessageBox(this);
        poLoading = new Dialog_Loading(this);

        poDialogx.initDialog();

        getExtra();
        setProductDetails();

        mBinding.ratingBar.setMax(5);
        mBinding.btnSaveRv.setOnClickListener(v -> saveReview());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            popUpCloseConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        popUpCloseConfirmationDialog();
    }

    private void getExtra() {

        if(getIntent().hasExtra("sListngId")) {

            psItemIdx = getIntent().getStringExtra("sListngId");
            psOrderID = getIntent().getStringExtra("sTransNox");
        } else {

            poDialogx.setIcon(R.drawable.baseline_error_24);
            poDialogx.setTitle("Marketplace");
            poDialogx.setMessage("Product does not exist.");
            poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    poDialogx.dismiss();
                    finish();
                }
            });

            poDialogx.show();
        }
    }

    private void setProductDetails() {
        mViewModel.getProductInfo(psItemIdx).observe(Activity_WriteProductReview.this, eProduct -> {
            try {
                JSONArray laJson = new JSONArray(eProduct.getImagesxx());
                String sampleImg = laJson.getJSONObject(0).getString("sImageURL");
                Picasso.get().load(sampleImg).into(mBinding.imgProdct);
                mBinding.txtProdNm.setText(Objects.requireNonNull(eProduct.getModelNme()));
                mBinding.txtPricex.setText(Objects.requireNonNull(eProduct.getUnitPrce()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void saveReview() {

        if(mBinding.txtReview.getText().toString().trim().isEmpty()) {

            poDialogx.setIcon(R.drawable.baseline_error_24);
            poDialogx.setTitle("Product Review");
            poDialogx.setMessage("Please enter a product review.");
            poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                @Override
                public void OnButtonClick(View view, AlertDialog dialog) {
                    poDialogx.dismiss();
                }
            });

            poDialogx.show();

        } else {

            String lsReviewx = mBinding.txtReview.getText().toString().trim();

            mViewModel.saveReview(psOrderID, psItemIdx, mBinding.ratingBar.getNumStars(), lsReviewx,
                    new OnTransactionsCallback() {
                @Override
                public void onLoading() {
                    poLoading.initDialog("Product Review", "Processing. Please wait.");
                    poLoading.show();
                }

                @Override
                public void onSuccess(String fsMessage) {
                    poLoading.dismiss();

                    poDialogx.setIcon(R.drawable.ic_baseline_message_24);
                    poDialogx.setTitle("Product Review");
                    poDialogx.setMessage(fsMessage);
                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            poDialogx.dismiss();
                            finish();
                        }
                    });

                    poDialogx.show();

                }

                @Override
                public void onFailed(String fsMessage) {
                    poLoading.dismiss();

                    poDialogx.setIcon(R.drawable.baseline_error_24);
                    poDialogx.setTitle("Product Review");
                    poDialogx.setMessage(fsMessage);
                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                        @Override
                        public void OnButtonClick(View view, AlertDialog dialog) {
                            poDialogx.dismiss();
                            finish();
                        }
                    });

                    poDialogx.show();

                }
            });
        }
    }

    private void popUpCloseConfirmationDialog() {

        poDialogx.setIcon(R.drawable.baseline_contact_support_24);
        poDialogx.setTitle("Product Review");
        poDialogx.setMessage("Are you sure you want to cancel reviewing this product?");

        poDialogx.setPositiveButton("Yes", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
                finish();
            }
        });

        poDialogx.setNegativeButton("No", new MessageBox.DialogButton() {
            @Override
            public void OnButtonClick(View view, AlertDialog dialog) {
                dialog.dismiss();
            }
        });

        poDialogx.show();
    }

}