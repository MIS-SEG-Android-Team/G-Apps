package org.guanzongroup.com.creditapp.Activities;

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
import org.guanzongroup.com.creditapp.Adapter.Adapter_LoanProductList;
import org.guanzongroup.com.creditapp.R;
import org.guanzongroup.com.creditapp.ViewModel.VMLoanProductList;

import java.util.Objects;

public class Activity_LoanProductList extends AppCompatActivity {

    private VMLoanProductList mViewModel;

    private Toolbar toolbar;
    private SearchView txtSearch;
    private RecyclerView recyclerView;
    private LinearLayout lnLoading;
    private TextView lblNoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loan_product_list);

        mViewModel = new ViewModelProvider(Activity_LoanProductList.this).get(VMLoanProductList.class);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Apply For A Loan");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        txtSearch = findViewById(R.id.txtSearch);
        lnLoading = findViewById(R.id.lnLoading);
        lblNoItem = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(Activity_LoanProductList.this, 2, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        InitializeProductList();

        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mViewModel.SearchItem(query, new VMLoanProductList.OnSearchCallback() {
                    @Override
                    public void OnSearch() {
                        lnLoading.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void OnSearchFinish() {
                        lnLoading.setVisibility(View.GONE);
                        mViewModel.searchLoanProduct(query).observe(Activity_LoanProductList.this, products -> {
                            try{
                                if(products.size() > 0){
                                    lblNoItem.setVisibility(View.GONE);
                                    Adapter_LoanProductList loAdapter = new Adapter_LoanProductList(products, new Adapter_LoanProductList.OnItemClick() {
                                        @Override
                                        public void onClick(String fsListIdx) {
                                            Intent loIntent = new Intent(Activity_LoanProductList.this, Activity_ItemPreview.class);
                                            loIntent.putExtra("sListngID", fsListIdx);
                                            startActivity(loIntent);
                                            finish();
                                        }

                                        @Override
                                        public void onApplyLoanClick(String fsListIdx, String BrandNme, String ModelID) {
                                            Intent loIntent = new Intent(Activity_LoanProductList.this, Activity_LoanTerm.class);
                                            loIntent.putExtra("sListngID", fsListIdx);
                                            loIntent.putExtra("sUnitAppl", BrandNme);
                                            loIntent.putExtra("sModelIDx", ModelID);
                                            startActivity(loIntent);
                                            finish();
                                        }
                                    });
                                    recyclerView.setAdapter(loAdapter);
                                } else {
                                    lblNoItem.setVisibility(View.VISIBLE);
                                    lblNoItem.setText("No item found for keyword '" + query + "'");
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
                return false;
            }
        });
        ImageView clearButton = txtSearch.findViewById(androidx.appcompat.R.id.search_close_btn);
        clearButton.setOnClickListener(v -> {
            if(txtSearch.getQuery().length() == 0) {
                txtSearch.setIconified(true);
            } else {
                // Do your task here
                txtSearch.setQuery("", false);
                InitializeProductList();
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

    private void InitializeProductList(){
        mViewModel.getProductList().observe(Activity_LoanProductList.this, products -> {
            try{
                if(products.size() > 0){
                    Adapter_LoanProductList loAdapter = new Adapter_LoanProductList(products, new Adapter_LoanProductList.OnItemClick() {
                        @Override
                        public void onClick(String fsListIdx) {
                            Intent loIntent = new Intent(Activity_LoanProductList.this, Activity_ItemPreview.class);
                            loIntent.putExtra("sListngID", fsListIdx);
                            startActivity(loIntent);
                            finish();
                        }

                        @Override
                        public void onApplyLoanClick(String fsListIdx, String BrandNme, String ModelID) {
                            Intent loIntent = new Intent(Activity_LoanProductList.this, Activity_LoanTerm.class);
                            loIntent.putExtra("sListngID", fsListIdx);
                            loIntent.putExtra("sUnitAppl", BrandNme);
                            loIntent.putExtra("sModelIDx", ModelID);
                            startActivity(loIntent);
                            finish();
                        }
                    });
                    recyclerView.setAdapter(loAdapter);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}