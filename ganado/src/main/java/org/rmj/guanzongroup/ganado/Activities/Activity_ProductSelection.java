package org.rmj.guanzongroup.ganado.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import org.rmj.g3appdriver.etc.MessageBox;
import org.rmj.g3appdriver.utils.Dialogs.Dialog_Loading;
import org.rmj.guanzongroup.ganado.Adapter.ProductSelectionAdapter;
import org.rmj.guanzongroup.ganado.Dialog.DialogDisclosure;
import org.rmj.guanzongroup.ganado.R;
import org.rmj.guanzongroup.ganado.ViewModel.VMProductSelection;

import java.util.Objects;

public class Activity_ProductSelection extends AppCompatActivity {

    private ActivityResultLauncher<String[]> poRequest;

    private VMProductSelection mViewModel;
    private ProductSelectionAdapter adapter;

    private ShapeableImageView brandselectedimg;
    private RecyclerView rvMcModel;
    private TextView txtBrandNm;
    private SearchView searchView;

    private String lsBrandIDxx;

    private int backgroundResId;
    private String backgroundResIdCat;

    private MessageBox poMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_selection);

        mViewModel = new ViewModelProvider(Activity_ProductSelection.this).get(VMProductSelection.class);
        poMessage = new MessageBox(Activity_ProductSelection.this);

        initView(); //todo: init views
        initListener(); //todo: init listeners

        //todo: init passed brand id from intent
        if (getIntent().hasExtra("lsBrandID")){

            String lsBrandID = getIntent().getStringExtra("lsBrandID");

            if (!lsBrandID.isEmpty()){

                lsBrandIDxx = lsBrandID;
            }
        }

        //todo: init passed brand name from intent
        if (getIntent().hasExtra("lsBrandNm")){

            String lsBrandNm = getIntent().getStringExtra("lsBrandNm");

            if (!lsBrandNm.isEmpty()){

                txtBrandNm.setText(lsBrandNm);
            }
        }

        initObservables(); //todo: init observables

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_intent_slide_in_left, R.anim.anim_intent_slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void intentToSelection(String BrandID, String ModelID, String ImgLink){

        Intent intent = new Intent(Activity_ProductSelection.this, Activity_ProductInquiry.class);
        intent.putExtra("lsBrandID", BrandID);
        intent.putExtra("lsModelID", ModelID);
        intent.putExtra("lsBrandNm", getIntent().getStringExtra("lsBrandNm"));
        intent.putExtra("lsImgLink", ImgLink);
        intent.putExtra("bgbrandimage", backgroundResId);
        intent.putExtra("backgroundold", backgroundResIdCat);

        startActivity(intent);
        overridePendingTransition(R.anim.anim_intent_slide_in_right, R.anim.anim_intent_slide_out_left);

    }

    private void initView() {

        rvMcModel = findViewById(R.id.rvMcModel);
        txtBrandNm = findViewById(R.id.lblBrand);
        searchView = findViewById(R.id.searchview);
        brandselectedimg = findViewById(R.id.imageprodselection);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_selection);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void initListener(){

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    adapter.filterModel(newText);
                } catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    private void initObservables(){

        mViewModel.GetModelsList(lsBrandIDxx).observe(Activity_ProductSelection.this, eMcModels -> {

            if (eMcModels.size() > 0){

                brandselectedimg.setImageResource(getBrandImageResource(lsBrandIDxx));

                adapter = new ProductSelectionAdapter(eMcModels, new ProductSelectionAdapter.OnModelClickListener() {
                    @Override
                    public void OnClick(String ModelID, String BrandID, String ImgLink) {

                        //notify user that gcash number must be registered to earn rewards
                        if (mViewModel.GetGcashNox() == null || mViewModel.GetGcashNox().isEmpty()){
                            poMessage.initDialog();
                            poMessage.setIcon(R.drawable.ic_toast_warning);
                            poMessage.setTitle("Kita Moto");
                            poMessage.setMessage("Warning! Gcash number is not set. You must register to earn your rewards.");
                            poMessage.setPositiveButton("Continue", (view, dialog) -> {

                                dialog.dismiss();

                                intentToSelection(BrandID, ModelID, ImgLink);

                            });

                            poMessage.show();

                        }else {
                            intentToSelection(BrandID, ModelID, ImgLink);
                        }

                    }
                });

                rvMcModel.setAdapter(adapter);
                rvMcModel.setLayoutManager(new GridLayoutManager(Activity_ProductSelection.this, 2, RecyclerView.VERTICAL, false));

            }
        });

    }

    private int getBrandImageResource(String brandIndex) {
        switch (brandIndex) {
            case "M0W1001":
                return R.drawable.img_honda_brand_header; // Replace with your actual image resource
            case "M0W1002":
                return R.drawable.img_suzuki_brand_header; // Replace with your actual image resource
            case "M0W1003":
                return R.drawable.img_yamaha_brand_header; // Replace with your actual image resource
            case "M0W1009":
                return R.drawable.img_kawasaki_brand_header; // Replace with your actual image resource
            default:
                return R.drawable.img_imageview_place_holder; // Replace with your default image resource
        }
    }
}