package org.rmj.guanzongroup.marketplace.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import org.rmj.guanzongroup.marketplace.Adapter.ActivityFragmentAdapter;
import org.rmj.guanzongroup.marketplace.R;

public class Fragment_ItemCart extends Fragment {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ActivityFragmentAdapter adapter;

    public static Fragment_ItemCart newInstance() {
        return new Fragment_ItemCart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_cart, container, false);

        initWidgets(v);
        setupTabLayoutListener();

        viewPager.setAdapter(adapter);

        return v;
    }

    private void initWidgets(View view){
        tabLayout = view.findViewById(R.id.tabLayout_item_cart_fragment_indicator);
        viewPager = view.findViewById(R.id.viewpager_fragment_item_cart);
        adapter = new ActivityFragmentAdapter(getChildFragmentManager(), getLifecycle());
    }

    private void setupTabLayoutListener(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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