package org.rmj.guanzongroup.marketplace.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.rmj.g3appdriver.dev.Database.Entities.EPromo;
import org.rmj.guanzongroup.digitalgcard.Adapter.Adapter_Advertisement;
import org.rmj.guanzongroup.digitalgcard.Model.AdvertisementInfo;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductList;
import org.rmj.guanzongroup.marketplace.ViewModel.VMSearchItem;
import org.rmj.guanzongroup.marketplace.databinding.ActivitySearchItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Activity_SearchItem extends AppCompatActivity {
    private static final String TAG = Activity_SearchItem.class.getSimpleName();
    private VMSearchItem mViewModel;
    private ActivitySearchItemBinding mBinding;

    private Adapter_ProductList loAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(Activity_SearchItem.this).get(VMSearchItem.class);
        mBinding = ActivitySearchItemBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // Get the intent, verify the action and get the query
        setUpToolbar();
        showAds();

        mBinding.searchview.requestFocus();
        mBinding.searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                displayResult(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        ImageView clearButton = mBinding.searchview.findViewById(androidx.appcompat.R.id.search_close_btn);
        clearButton.setOnClickListener(v -> {
            if(mBinding.searchview.getQuery().length() == 0) {
                mBinding.searchview.setIconified(true);
            } else {
                // Do your task here
                mBinding.searchview.setQuery("", false);
                finish();
            }
        });
        mBinding.recyclrVw.setLayoutManager(new GridLayoutManager(Activity_SearchItem.this,
                2, RecyclerView.VERTICAL, false));
        mBinding.recyclrVw.setHasFixedSize(true);
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

    private void setUpToolbar() {
        setSupportActionBar(mBinding.toolBarxx);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Item");
    }

    private void showAds() {
        showPromos();
        showSuggestions();
    }

    private void showPromos() {
        mBinding.recyclrVw.setVisibility(View.GONE);
        mBinding.lnAdvetse.setVisibility(View.VISIBLE);
        List<AdvertisementInfo> loList = new ArrayList<>();
        mViewModel.GetPromoList().observe(Activity_SearchItem.this, ePromos -> {
            try{
                for(int x = 0; x < ePromos.size(); x++){
                    EPromo loPromo = ePromos.get(x);
                    if(loPromo.getDivision() == 0){
                        AdvertisementInfo loAds = new AdvertisementInfo();
                        loAds.setImageUrl(loPromo.getImageSld());
                        loAds.setPromoId(loPromo.getTransNox());
                        loList.add(loAds);
                    }

                }
                Adapter_Advertisement loAdapter = new Adapter_Advertisement(loList, loList.size(), fsId -> {

                });
                mBinding.rvAdvrtse.setAdapter(loAdapter);
                mBinding.rvAdvrtse.setLayoutManager(new LinearLayoutManager(Activity_SearchItem.this));
                loAdapter.notifyDataSetChanged();
            } catch (Exception e){
                e.printStackTrace();
            }
        });

//        poData.setPromoId("1");
//        poData.setImageUrl("https://res.cloudinary.com/dxxbciuwf/image/fetch/f_auto/https://smack.agency/wp-content/uploads/2020/12/how-to-create-a-marketing-campaign-for-your-business-1-scaled.jpg");
//        AdvertisementInfo poData2 = new AdvertisementInfo();
//        poData2.setPromoId("2");
//        poData2.setImageUrl("https://s2.best-wallpaper.net/wallpaper/2560x1920/1108/Waterfalls-and-streams_2560x1920.jpg");
//        loList.add(poData);
//        loList.add(poData2);
    }

    private void showSuggestions() {
        mViewModel.getProductList(0).observe(this, products -> {
            try {
                if(products.size() > 0) {
                    Adapter_ProductList loAdapter = new Adapter_ProductList(products, listingId -> {
                        Intent loIntent = new Intent(this, Activity_ProductOverview.class);
                        loIntent.putExtra("sListngId", listingId);
                        startActivity(loIntent);
                    });
                    mBinding.rvSuggest.setLayoutManager(new GridLayoutManager(this,
                            2, RecyclerView.VERTICAL, false));
                    mBinding.rvSuggest.setHasFixedSize(true);
                    mBinding.rvSuggest.setAdapter(loAdapter);
                    loAdapter.notifyDataSetChanged();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

    private void displayResult(String fsQueryxx) {
        mBinding.lnAdvetse.setVisibility(View.GONE);
        mBinding.recyclrVw.setVisibility(View.VISIBLE);
        mViewModel.RequestProductSearch(fsQueryxx, new VMSearchItem.OnSearchCallback() {
            @Override
            public void OnSearch() {
                mBinding.lnLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnSearchFinish() {
                mBinding.lnLoading.setVisibility(View.GONE);
                mViewModel.GetSearchProductList(fsQueryxx).observe(Activity_SearchItem.this, oProducts -> {
                    try{
                        if(oProducts.size() > 0) {
                            mBinding.textView.setVisibility(View.GONE);
                            loAdapter = new Adapter_ProductList(oProducts, fsListIdx -> {
                                Intent loIntent = new Intent(Activity_SearchItem.this, Activity_ProductOverview.class);
                                loIntent.putExtra("sListngId", fsListIdx);
                                startActivity(loIntent);
                            });
                            mBinding.recyclrVw.setAdapter(loAdapter);
                            loAdapter.notifyDataSetChanged();
                        } else {
                            mBinding.textView.setText("No item found for keyword '" + fsQueryxx + "'");
                            mBinding.textView.setVisibility(View.VISIBLE);
                            mBinding.lnAdvetse.setVisibility(View.VISIBLE);
                            mBinding.recyclrVw.setVisibility(View.GONE);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}