package org.rmj.guanzongroup.marketplace.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rmj.g3appdriver.etc.CashFormatter;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.lib.Account.AccountInfo;
import org.rmj.g3appdriver.utils.Dialogs.BottomDialog_AddToCart;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.g3appdriver.lib.Promotions.Adapter_ImageSlider;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductDescription;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductList;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductQueries;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductReview;
import org.rmj.guanzongroup.marketplace.Etc.OnTransactionsCallback;
import org.rmj.g3appdriver.lib.Promotions.model.HomeImageSliderModel;
import org.rmj.guanzongroup.marketplace.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMProductOverview;
import org.rmj.guanzongroup.useraccount.Activity.Activity_CompleteAccountDetails;
import org.rmj.guanzongroup.useraccount.Activity.Activity_Login;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Activity_ProductOverview extends AppCompatActivity {
    private static final String TAG = Activity_ProductOverview.class.getSimpleName();
    private AccountInfo poAccount;
    private VMProductOverview mViewModel;
    private Toolbar toolbar;
    private Dialog_Loading poLoading;
    private MessageBox poDialogx;
    private LinearLayout poItmSpec;
    private SliderView poSliderx;
    private RecyclerView rvItmSpec, rvRatings, rvQueries, rvSuggest;
    private ImageView imgPromox;
    private BadgeDrawable loBadge;
    private TextView txtProdNm, txtUntPrc, txtSoldQt, txtBrandx, txtCatgry, txtColorx, txtStocks,
            txtBriefx, lblNoRevs, lblNoFaqs, lblNoSugg, txtRates;

    private TextView btnAddCrt, btnBuyNow;
    private MaterialButton btnSeeRev, btnAskQst;

    private String psItemIdx = "";
    private String psProduct = "";
    private String psAvlQtyx = "";
    private String psPricexx = "";

    public boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_overview);

        mViewModel = new ViewModelProvider(Activity_ProductOverview.this)
                .get(VMProductOverview.class);

        poAccount = new AccountInfo(Activity_ProductOverview.this);
        poDialogx = new MessageBox(Activity_ProductOverview.this);

        poDialogx.initDialog();

        getExtras();
        initViews();
        setUpToolbar();
        displayData();

        btnSeeRev.setOnClickListener(v -> {
            if(!isClick) {
                isClick = true;
                Intent loIntent = new Intent(Activity_ProductOverview.this, Activity_ProductReview.class);
                loIntent.putExtra("sListngId", psItemIdx);
                startActivity(loIntent);
                isClick = false;
            } else {
                Toast.makeText(Activity_ProductOverview.this, "Please wait...", Toast.LENGTH_SHORT).show();
            }
        });

        btnAskQst.setOnClickListener(v -> {
            if(!isClick) {
                isClick = true;
                if (isLoggedIn()) {
                    if (poAccount.getVerificationStatus() == 0) {
                        Intent loIntent = new Intent(Activity_ProductOverview.this,
                                Activity_CompleteAccountDetails.class);
                        startActivity(loIntent);
                    } else {
                        Intent loIntent = new Intent(Activity_ProductOverview.this, Activity_ProductQueries.class);
                        loIntent.putExtra("sListngId", psItemIdx);
                        startActivity(loIntent);
                    }
                }
                isClick = false;
            } else {
                Toast.makeText(Activity_ProductOverview.this, "Please wait...", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddCrt.setOnClickListener(v -> {
            if(!isClick) {
                isClick = true;
                addToCart();
            } else {
                Toast.makeText(Activity_ProductOverview.this, "Please wait...", Toast.LENGTH_SHORT).show();
            }
        });
        btnBuyNow.setOnClickListener(v -> buyNow());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        } else {
            Intent intent = new Intent(Activity_ProductOverview.this, Activity_ItemCart.class);
            intent.putExtra("args", "1");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getExtras() {

        if(getIntent().hasExtra("sListngId")) {

            psItemIdx = getIntent().getStringExtra("sListngId");
            Log.d(TAG, psItemIdx);
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

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        poItmSpec = findViewById(R.id.layout_specifications);

        // Image Slider Setup
        poSliderx = findViewById(R.id.imgSlider);
        poSliderx.setIndicatorAnimation(IndicatorAnimationType.WORM);
        poSliderx.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        poSliderx.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        poSliderx.setIndicatorSelectedColor(Color.WHITE);
        poSliderx.setIndicatorUnselectedColor(Color.GRAY);
        poSliderx.setScrollTimeInSec(5);
        poSliderx.startAutoCycle();

        rvItmSpec = findViewById(R.id.rv_specifications);
        rvItmSpec.setLayoutManager(new LinearLayoutManager(Activity_ProductOverview.this));
        rvItmSpec.setHasFixedSize(true);

        rvRatings = findViewById(R.id.rv_ratings);
        rvRatings.setLayoutManager(new LinearLayoutManager(Activity_ProductOverview.this));
        rvRatings.setHasFixedSize(true);

        rvQueries = findViewById(R.id.rv_faqs);
        rvQueries.setLayoutManager(new LinearLayoutManager(Activity_ProductOverview.this));
        rvQueries.setHasFixedSize(true);

        rvSuggest = findViewById(R.id.rv_suggests);
        rvSuggest.setLayoutManager(new GridLayoutManager(Activity_ProductOverview.this,
                2, RecyclerView.VERTICAL, false));
        rvSuggest.setHasFixedSize(true);

        imgPromox = findViewById(R.id.imgPromox);
        txtProdNm = findViewById(R.id.txt_product_name);
        txtUntPrc = findViewById(R.id.txt_product_price);
        txtRates = findViewById(R.id.txt_ratings);
        txtSoldQt = findViewById(R.id.txt_product_sold_count);
        txtBrandx = findViewById(R.id.txt_brand_name);
        txtCatgry = findViewById(R.id.txt_category);
        txtColorx = findViewById(R.id.txt_variant);
        txtStocks = findViewById(R.id.txt_stocks);
        txtBriefx = findViewById(R.id.txt_brief_desc);
        lblNoRevs = findViewById(R.id.lblNoRatings);
        lblNoFaqs = findViewById(R.id.lblNoFaqs);
        lblNoSugg = findViewById(R.id.lblNoSuggests);

        btnSeeRev = findViewById(R.id.btnSeeMoreReviews);
        btnAskQst = findViewById(R.id.btnAskQuestion);
        btnAddCrt = findViewById(R.id.btnText_addToCart);
        btnBuyNow = findViewById(R.id.btnText_buyNow);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void displayData() {
        mViewModel.GetCartItemCount().observe(Activity_ProductOverview.this, count -> {
            try {
                toolbar = findViewById(R.id.toolbar);
                if(count > 0) {
                    loBadge = BadgeDrawable.create(Activity_ProductOverview.this);
                    loBadge.setNumber(count);
                    BadgeUtils.attachBadgeDrawable(loBadge, toolbar, R.id.item_cart);
                } else {
                    BadgeUtils.detachBadgeDrawable(loBadge, toolbar, R.id.item_cart);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        mViewModel.getProductInfo(psItemIdx).observe(Activity_ProductOverview.this, product -> {
            try {
                showPromoBanner();
                psProduct = Objects.requireNonNull(product.getModelNme());
                psAvlQtyx = product.getQtyOnHnd();
                psPricexx = CashFormatter.parse(Objects.requireNonNull(product.getUnitPrce()));

                txtProdNm.setText(Objects.requireNonNull(product.getModelNme()));
                txtUntPrc.setText(CashFormatter.parse(Objects.requireNonNull(product.getUnitPrce())));
                DecimalFormat format = new DecimalFormat("0.0");
                txtRates.setText(format.format(Double.parseDouble(product.getRatingxx())));
                txtSoldQt.setText(Objects.requireNonNull(product.getSoldQtyx()) + " Sold");
                txtBrandx.setText(Objects.requireNonNull(product.getBrandNme()));
                txtCatgry.setText(Objects.requireNonNull(product.getCategrNm()));
                txtColorx.setText(Objects.requireNonNull(product.getColorNme()));
                txtStocks.setText(Objects.requireNonNull(product.getQtyOnHnd()));
                txtBriefx.setText(Objects.requireNonNull(product.getBriefDsc()));
                setFullDescription(Objects.requireNonNull(product.getDescript()));

                mViewModel.ImportReviews(product.getListngID(), new VMProductOverview.OnInquiryReviewsImportCallback() {
                    @Override
                    public void OnImport(String args) {
                        try {
                            rvRatings.setVisibility(View.VISIBLE);
                            lblNoRevs.setVisibility(View.GONE);
                            btnSeeRev.setVisibility(View.VISIBLE);
                            JSONObject loJson = new JSONObject(args);
                            Adapter_ProductReview loAdapter = new
                                    Adapter_ProductReview(loJson.getJSONArray("detail"),
                                    true);
                            loAdapter.notifyDataSetChanged();
                            rvRatings.setAdapter(loAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            rvRatings.setVisibility(View.GONE);
                            lblNoRevs.setVisibility(View.VISIBLE);
                            btnSeeRev.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void OnFailed(String message) {
                        Log.e(TAG, message);
                        rvRatings.setVisibility(View.GONE);
                        lblNoRevs.setVisibility(View.VISIBLE);
                        btnSeeRev.setVisibility(View.GONE);
                    }
                });

                mViewModel.ImportInquiries(product.getListngID(), new VMProductOverview.OnInquiryReviewsImportCallback() {
                        @Override
                        public void OnImport(String args) {
                            try {
                                JSONObject loJson = new JSONObject(args);
                                rvQueries.setVisibility(View.VISIBLE);
                                lblNoFaqs.setVisibility(View.GONE);
                                Adapter_ProductQueries loAdapter = new Adapter_ProductQueries(
                                        getFilteredFaqs(loJson.getJSONArray("detail")),
                                        true);
                                loAdapter.notifyDataSetChanged();
                                rvQueries.setAdapter(loAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                rvQueries.setVisibility(View.GONE);
                                lblNoFaqs.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void OnFailed(String message) {
                            Log.e(TAG, message);
                            rvQueries.setVisibility(View.GONE);
                            lblNoFaqs.setVisibility(View.VISIBLE);
                        }
                    });

                showSuggestItems(product.getBrandNme());

                Adapter_ImageSlider adapter = new Adapter_ImageSlider(getSliderImages(product.getImagesxx()), args -> {
                    try {
                        List<HomeImageSliderModel> loList = getSliderImages(product.getImagesxx());

//                        Picasso.get().load(loList.get(args).getImageUrl()).placeholder(R.drawable.ic_no_image_available)
//                                .error(R.drawable.ic_no_image_available).into(imgPrview);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });

                poSliderx.setSliderAdapter(adapter);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }

    private void setFullDescription(String fsDescrip) {
        try {
            JSONArray loArrayxx = new JSONArray(fsDescrip);
            if(loArrayxx.length() > 0) {
                poItmSpec.setVisibility(View.VISIBLE);
                Adapter_ProductDescription loAdapter = new Adapter_ProductDescription(loArrayxx);
                loAdapter.notifyDataSetChanged();
                rvItmSpec.setAdapter(loAdapter);
            } else {
                poItmSpec.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showSuggestItems(String fsBrandNm) {
        mViewModel.getProductList(0, fsBrandNm).observe(Activity_ProductOverview.this, products -> {
            try {
                if(products.size() > 0) {
                    rvSuggest.setVisibility(View.VISIBLE);
                    lblNoSugg.setVisibility(View.GONE);
                    Adapter_ProductList loAdapter = new Adapter_ProductList(products, listingId -> {
                        Intent loIntent = new Intent(Activity_ProductOverview.this
                                , Activity_ProductOverview.class);
                        loIntent.putExtra("sListngId", listingId);
                        startActivity(loIntent);
                        finish();
                    });
                    loAdapter.notifyDataSetChanged();
                    rvSuggest.setAdapter(loAdapter);
                } else {
                    rvSuggest.setVisibility(View.GONE);
                    lblNoSugg.setVisibility(View.VISIBLE);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                rvSuggest.setVisibility(View.GONE);
                lblNoSugg.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showPromoBanner() {
        boolean isThereAPromoForItem = true;
        if(isThereAPromoForItem) {
//            String sampleUrl = "https://www.toysrus.com.bn/dw/image/v2/BDGJ_PRD/on/demandware.static/-/Sites-storefront-catalog-toysrus-bn/default/dw660375c3/categories/brands/W-Z/zuru-2560x320-2.jpg?q=75";
            String sampleUrl = "http://192.168.10.141/integsys/marketplace/uploads/promo_banner/Promo%20banner%202560x320px.png";
            imgPromox.setVisibility(View.VISIBLE);
            Picasso.get().load(sampleUrl).into(imgPromox);
        }
    }

    private void addToCart() {
        if(isLoggedIn()) {
            if (poAccount.getVerificationStatus() == 0) {
                Intent loIntent = new Intent(Activity_ProductOverview.this,
                        Activity_CompleteAccountDetails.class);
                startActivity(loIntent);
                isClick = false;
            } else {
                final BottomDialog_AddToCart dialog = new BottomDialog_AddToCart(psProduct, psAvlQtyx, psPricexx);
                dialog.setDialogCallback(new BottomDialog_AddToCart.OnAddToCart() {
                    @Override
                    public void onClick(int fnItemQty) {
                        try {
                            mViewModel.addUpdateCart(psItemIdx, fnItemQty, new OnTransactionsCallback() {
                                @Override
                                public void onLoading() {
                                    poLoading = new Dialog_Loading(Activity_ProductOverview.this);
                                    poLoading.initDialog("Add to Cart",
                                            "Adding to cart. Please wait.");
                                    poLoading.show();
                                }

                                @Override
                                public void onSuccess(String fsMessage) {
                                    isClick = false;
                                    poLoading.dismiss();

                                    poDialogx.setIcon(R.drawable.ic_baseline_message_24);
                                    poDialogx.setTitle("Add to Cart");
                                    poDialogx.setMessage(fsMessage);
                                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                        @Override
                                        public void OnButtonClick(View view, AlertDialog dialog) {
                                            poDialogx.dismiss();
                                        }
                                    });

                                    poDialogx.show();
                                }

                                @Override
                                public void onFailed(String fsMessage) {
                                    isClick = false;
                                    poLoading.dismiss();

                                    poDialogx.setIcon(R.drawable.baseline_error_24);
                                    poDialogx.setTitle("Add to Cart");
                                    poDialogx.setMessage(fsMessage);
                                    poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                        @Override
                                        public void OnButtonClick(View view, AlertDialog dialog) {
                                            poDialogx.dismiss();
                                        }
                                    });

                                    poDialogx.show();

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDismiss() {
                        isClick = false;
                    }
                });

                dialog.show(getSupportFragmentManager(), "Add To Cart");
            }
        }
    }

    private void buyNow() {

        if(!isClick) {

            if (isLoggedIn()) {

                if (poAccount.getVerificationStatus() == 0) {
                    Intent loIntent = new Intent(Activity_ProductOverview.this,
                            Activity_CompleteAccountDetails.class);
                    startActivity(loIntent);
                    isClick = false;
                } else {

                    mViewModel.buyNow(psItemIdx, 1, new OnTransactionsCallback() {
                        @Override
                        public void onLoading() {
                            poLoading = new Dialog_Loading(Activity_ProductOverview.this);
                            poLoading.initDialog("Marketplace", "Order processing. Please wait.");
                            poLoading.show();
                        }

                        @Override
                        public void onSuccess(String fsMessage) {
                            isClick = false;
                            poLoading.dismiss();
                            Intent loIntent = new Intent(Activity_ProductOverview.this,
                                    Activity_PlaceOrder.class);
                            loIntent.putExtra("cBuyNowxx", true);
                            startActivity(loIntent);
                        }

                        @Override
                        public void onFailed(String fsMessage) {
                            isClick = false;
                            poLoading.dismiss();

                            poDialogx.setIcon(R.drawable.baseline_error_24);
                            poDialogx.setTitle("Marketplace");
                            poDialogx.setMessage(fsMessage);
                            poDialogx.setPositiveButton("Dismiss", new MessageBox.DialogButton() {
                                @Override
                                public void OnButtonClick(View view, AlertDialog dialog) {
                                    poDialogx.dismiss();
                                }
                            });

                            poDialogx.show();

                        }
                    });
                }
            }
        } else {
            Toast.makeText(Activity_ProductOverview.this, "Please wait...", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLoggedIn() {
        if(!poAccount.getLoginStatus()) {
            Intent loIntent = new Intent(Activity_ProductOverview.this, Activity_Login.class);
            startActivity(loIntent);
            return false;
        } else {
            return true;
        }
    }

    private List<HomeImageSliderModel> getSliderImages(String fsVal) throws Exception{
        List<HomeImageSliderModel> loSliders = new ArrayList<>();

        JSONArray laJson = new JSONArray(fsVal);

        //start the value of for loop to 1 instead of 0
        // to skip the first item which is not for product overview
        for(int x = 1; x < laJson.length(); x++){
            loSliders.add(new HomeImageSliderModel(laJson.getJSONObject(x).getString("sImageURL")));
        }
        return loSliders;
    }

    private JSONArray getFilteredFaqs(JSONArray foArray) {
        JSONArray loArray = new JSONArray();
        for(int x = 0; x < foArray.length(); x++) {
            try {
                if (!foArray.getJSONObject(x).getString("sReplyxxx")
                        .equalsIgnoreCase("null")) {
                    loArray.put(foArray.getJSONObject(x));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return loArray;
    }
}