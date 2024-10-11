package org.rmj.guanzongroup.marketplace.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.rmj.guanzongroup.marketplace.Adapter.Adapter_Wishlist;
import org.rmj.guanzongroup.marketplace.Model.WishListModel;
import org.rmj.guanzongroup.marketplace.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Wishlist extends Fragment {
    private TextView lblPriceDrop,lblPercent;
    private Adapter_Wishlist adapter;
    private List<WishListModel> itemList;
    private RecyclerView recyclerView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        initViews(view);
        setData();

        return view;
    }

    private void initViews(View v) {
        recyclerView = v.findViewById(R.id.recyclerView_WishList);
    }

    private void strikePrice(){
        SpannableString spannableString=new SpannableString("3.1 StrikeThrough Using SpannableString");
        spannableString.setSpan(new StrikethroughSpan(),0,spannableString.length(), 0);

        lblPriceDrop.setText(spannableString);
    }

    private void setData(){
        itemList = new ArrayList<>();

        WishListModel model3 = new WishListModel();
        model3.setWishImage("https://www.guanzongroup.com.ph/wp-content/uploads/2022/03/samsung-galaxy-s22-plus-1.jpg");
        model3.setWishName("Galaxy S22+ (8+256GB)");
        model3.setWishPrice("42,693.00");
        model3.setWishPriceDrop("60,990.00");
        model3.setWishPercent("30");
        itemList.add(model3);
        WishListModel model = new WishListModel();
        model.setWishImage("https://www.guanzongroup.com.ph/wp-content/uploads/2021/05/realme-8-1-510x510.jpg");
        model.setWishName("Realme 8 (8GB+128GB) Realme 8 (8GB+128GB)");
        model.setWishPrice("12,082.36");
        model.setWishPriceDrop("13,133.00");
        model.setWishPercent("8");
        itemList.add(model);
        WishListModel model2 = new WishListModel();
        model2.setWishImage("https://www.guanzongroup.com.ph/wp-content/uploads/2021/09/oppo-reno6-5g-1-510x510.jpg");
        model2.setWishName("Reno 6 5G (8GB+128GB)");
        model2.setWishPrice("22,073.40");
        model2.setWishPriceDrop("24,526.00");
        model2.setWishPercent("10");
        itemList.add(model2);
        WishListModel model1 = new WishListModel();
        model1.setWishImage("https://www.guanzongroup.com.ph/wp-content/uploads/2022/02/realme-c25y-1-510x510.jpg");
        model1.setWishName("Realme C25Y (4+128GB)");
        model1.setWishPrice("7,375.45");
        model1.setWishPriceDrop("8,677.00");
        model1.setWishPercent("15");
        itemList.add(model1);

        adapter = new Adapter_Wishlist(itemList, new Adapter_Wishlist.OnCartAction() {
            @Override
            public void onAddToCart(String fsItemIdx, String fsItemNme, String fsItemPrc) {
            }

            @Override
            public void onRemoveToCart(String val) {

            }

        });

        Log.e("itemCart = ", String.valueOf(itemList.size()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapter.notifyDataSetChanged();


    }
}