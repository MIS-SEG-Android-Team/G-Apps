package org.rmj.guanzongroup.marketplace.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.rmj.g3appdriver.dev.Repositories.RProduct;
import org.rmj.g3appdriver.etc.FilterType;
import org.rmj.guanzongroup.marketplace.Adapter.Adapter_ProductList;
import org.rmj.guanzongroup.marketplace.R;
import org.rmj.guanzongroup.marketplace.ViewModel.VMProductList;

public class Activity_ProductList extends AppCompatActivity {

    private VMProductList mViewModel;

    private Toolbar toolbar;
    private SearchView searchView;
    private LinearLayout lnLoading;
    private RecyclerView recyclerView;
    private TextView lblNoItem;

    private Adapter_ProductList poAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        mViewModel = new ViewModelProvider(Activity_ProductList.this).get(VMProductList.class);

        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchview);
        lnLoading = findViewById(R.id.lnLoading);
        recyclerView = findViewById(R.id.rv_products);
        lblNoItem = findViewById(R.id.textView);
        recyclerView.setLayoutManager(new GridLayoutManager(Activity_ProductList.this,
                2, RecyclerView.VERTICAL, false));
        String lsBrandNme = getIntent().getStringExtra("xBrandNme");
        toolbar.setTitle(lsBrandNme);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SetupListView(lsBrandNme);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.SearchItem(query, new VMProductList.OnSearchItemListener() {
                    @Override
                    public void OnSearch(String title, String message) {
                        lnLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void OnSearch() {
                        lnLoading.setVisibility(View.GONE);
                        mViewModel.GetProductsOnBrand(query, lsBrandNme).observe(Activity_ProductList.this, oProducts -> {
                            try{
                                if(oProducts.size() > 0) {
                                    recyclerView.setVisibility(View.VISIBLE);
                                    lblNoItem.setVisibility(View.GONE);
                                    poAdapter = new Adapter_ProductList(oProducts, listingId -> {
                                        Intent loIntent = new Intent(Activity_ProductList.this, Activity_ProductOverview.class);
                                        loIntent.putExtra("sListngId", listingId);
                                        startActivity(loIntent);
                                    });
                                    recyclerView.setAdapter(poAdapter);
                                    poAdapter.notifyDataSetChanged();
                                } else {
                                    lblNoItem.setVisibility(View.VISIBLE);
                                    lblNoItem.setText("No item found for keyword '" +query+ "' under " +lsBrandNme+" Brand");
                                    recyclerView.setVisibility(View.GONE);
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        });
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if(newText == null){
//                    SetupListView(lsBrandNme);
//                } else if(newText.isEmpty()){
//                    SetupListView(lsBrandNme);
//                }
                return false;
            }
        });
        ImageView clearButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        clearButton.setOnClickListener(v -> {
            if(searchView.getQuery().length() == 0) {
                searchView.setIconified(true);
            } else {
                // Do your task here
                searchView.setQuery("", false);
                SetupListView(lsBrandNme);
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

    private void SetupListView(String lsBrandNme){
        mViewModel.SetProductFilter(new RProduct.oFilterx(FilterType.BRAND_NAME, lsBrandNme, ""));
        mViewModel.GetFilterType().observe(Activity_ProductList.this, oFilterx -> {
            try{
                mViewModel.GetBrandProductList(0, oFilterx.getFilter(), oFilterx.getArgs1()).observe(Activity_ProductList.this, oProducts -> {
                    if(oProducts.size() > 0) {
                        lblNoItem.setVisibility(View.GONE);
                        poAdapter = new Adapter_ProductList(oProducts, listingId -> {
                            Intent loIntent = new Intent(Activity_ProductList.this, Activity_ProductOverview.class);
                            loIntent.putExtra("sListngId", listingId);
                            startActivity(loIntent);
                        });
                        recyclerView.setAdapter(poAdapter);
                        poAdapter.notifyDataSetChanged();
                    } else {
                        lblNoItem.setVisibility(View.VISIBLE);
                        lblNoItem.setText("Currently there are no available items for product '" + lsBrandNme + "'");
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}