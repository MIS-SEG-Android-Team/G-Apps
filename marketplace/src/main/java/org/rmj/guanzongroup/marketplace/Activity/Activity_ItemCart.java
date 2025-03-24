package org.rmj.guanzongroup.marketplace.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import org.rmj.guanzongroup.marketplace.Fragment.Fragment_GCardItemCart;
import org.rmj.guanzongroup.digitalgcard.Fragment.Fragment_Redeemables;
import org.rmj.guanzongroup.digitalgcard.ViewModel.VMGCardSystem;
import org.rmj.guanzongroup.marketplace.Adapter.ActivityFragmentAdapter;
import org.rmj.guanzongroup.marketplace.Fragment.Fragment_MPItemCart;
import org.rmj.guanzongroup.marketplace.R;

import java.util.Objects;

public class Activity_ItemCart extends AppCompatActivity {
    private VMGCardSystem mViewModel;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ActivityFragmentAdapter adapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_cart);

        initWidgets();
        setupTabLayoutListener();

        mViewModel = new ViewModelProvider(Activity_ItemCart.this).get(VMGCardSystem.class);

        if(getIntent().getStringExtra("args").equalsIgnoreCase("1")){

            getSupportActionBar().setTitle("Item Cart");

            mViewModel.getActiveGcard().observe(this, eGcardApp -> {
                try {
                    adapter.clear();
                    adapter.addFragment(new Fragment_MPItemCart());

                    tabLayout.addTab(tabLayout.newTab().setText("MarketPlace"));

                    mViewModel.GetMarketplaceItemCartCount().observe(Activity_ItemCart.this, count -> {
                        try{
                            if(count > 0) {
                                Objects.requireNonNull(tabLayout.getTabAt(0)).getOrCreateBadge().setNumber(count);
                            } else {
                                Objects.requireNonNull(tabLayout.getTabAt(0)).removeBadge();
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    });

                    if (eGcardApp != null) {
                        adapter.addFragment(new Fragment_GCardItemCart());

                        tabLayout.addTab(tabLayout.newTab().setText("GCard"));
                        tabLayout.setVisibility(View.VISIBLE);

                        mViewModel.GetGcardCartItemCount().observe(Activity_ItemCart.this, count -> {
                            try{
                                if(count > 0) {
                                    Objects.requireNonNull(tabLayout.getTabAt(1)).getOrCreateBadge().setNumber(count);
                                } else {
                                    Objects.requireNonNull(tabLayout.getTabAt(1)).removeBadge();
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        });

                    } else {
                        tabLayout.setVisibility(View.GONE);
                    }
                    viewPager.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }else {
            getSupportActionBar().setTitle("Redeemables");
            adapter.clear();
            adapter.addFragment(new Fragment_Redeemables());
            viewPager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            tabLayout.setVisibility(View.GONE);
        }
    }
    @SuppressLint("NewApi")
    private void initWidgets(){
        toolbar = findViewById(R.id.toolbar_item_cart);
        tabLayout = findViewById(R.id.tabLayout_item_cart_fragment_indicator);
        viewPager = findViewById(R.id.viewpager_fragment_item_cart);
        adapter = new ActivityFragmentAdapter(getSupportFragmentManager(), getLifecycle());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupTabLayoutListener(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}