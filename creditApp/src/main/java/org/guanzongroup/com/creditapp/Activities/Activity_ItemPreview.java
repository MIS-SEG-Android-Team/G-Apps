package org.guanzongroup.com.creditapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.guanzongroup.com.creditapp.Adapter.Adapter_ImageSlider;
import org.guanzongroup.com.creditapp.Adapter.Adapter_ProductDescription;
import org.guanzongroup.com.creditapp.Model.HomeImageSliderModel;
import org.guanzongroup.com.creditapp.R;
import org.guanzongroup.com.creditapp.ViewModel.VMItemPreview;
import org.json.JSONArray;
import org.json.JSONException;
import org.rmj.g3appdriver.etc.CashFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Activity_ItemPreview extends AppCompatActivity {
    private static final String TAG = Activity_ItemPreview.class.getSimpleName();

    private VMItemPreview mViewModel;

    private Toolbar toolbar;
    private LinearLayout poItmSpec;
    private SliderView poSliderx;
    private RecyclerView rvItmSpec;
    private TextView txtProdNm, txtUntPrc, txtBrandx, txtCatgry, txtColorx, txtStocks,
            txtBriefx;

    private TextView btnAppLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(Activity_ItemPreview.this).get(VMItemPreview.class);
        setContentView(R.layout.activity_item_preview);
        initWidgets();

        String lsLstID = getIntent().getStringExtra("sListngID");

        mViewModel.getProductInfo(lsLstID).observe(Activity_ItemPreview.this, product -> {
            try{
                txtProdNm.setText(Objects.requireNonNull(product.getModelNme()));
                txtUntPrc.setText(CashFormatter.parse(Objects.requireNonNull(product.getUnitPrce())));
                txtBrandx.setText(Objects.requireNonNull(product.getBrandNme()));
                txtCatgry.setText(Objects.requireNonNull(product.getCategrNm()));
                txtColorx.setText(Objects.requireNonNull(product.getColorNme()));
                txtStocks.setText(Objects.requireNonNull(product.getQtyOnHnd()));
                txtBriefx.setText(Objects.requireNonNull(product.getBriefDsc()));
                setFullDescription(Objects.requireNonNull(product.getDescript()));

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

                btnAppLoan.setOnClickListener(view -> {
                    Intent loIntent = new Intent(Activity_ItemPreview.this, Activity_LoanTerm.class);
                    loIntent.putExtra("sListngID", product.getListngID());
                    loIntent.putExtra("sModelIDx", product.getModelIDx());
                    loIntent.putExtra("sUnitAppl", product.getBrandNme());
                    startActivity(loIntent);
                    finish();
                });
            } catch (Exception e){
                e.printStackTrace();
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

    private void initWidgets(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        poItmSpec = findViewById(R.id.layout_specifications);
        poSliderx = findViewById(R.id.imgSlider);
        poSliderx.setIndicatorAnimation(IndicatorAnimationType.WORM);
        poSliderx.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        poSliderx.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        poSliderx.setIndicatorSelectedColor(Color.WHITE);
        poSliderx.setIndicatorUnselectedColor(Color.GRAY);
        poSliderx.setScrollTimeInSec(5);
        poSliderx.startAutoCycle();

        rvItmSpec = findViewById(R.id.rv_specifications);
        rvItmSpec.setLayoutManager(new LinearLayoutManager(Activity_ItemPreview.this));
        rvItmSpec.setHasFixedSize(true);

        txtProdNm = findViewById(R.id.txt_product_name);
        txtUntPrc = findViewById(R.id.txt_product_price);
        txtBrandx = findViewById(R.id.txt_brand_name);
        txtCatgry = findViewById(R.id.txt_category);
        txtColorx = findViewById(R.id.txt_variant);
        txtStocks = findViewById(R.id.txt_stocks);
        txtBriefx = findViewById(R.id.txt_brief_desc);

        btnAppLoan = findViewById(R.id.btnAppLoan);
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
}