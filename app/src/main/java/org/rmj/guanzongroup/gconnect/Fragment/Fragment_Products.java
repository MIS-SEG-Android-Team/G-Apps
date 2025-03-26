package org.rmj.guanzongroup.gconnect.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.guanzongroup.gconnect.Adapter.Adapter_Products;
import org.rmj.guanzongroup.gconnect.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fragment_Products extends Fragment {

    private RecyclerView rcv_products;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.fragment_products, container, false);

        rcv_products = view.findViewById(R.id.rcv_products);

        Bundle argsTab = getArguments();
        String tabMode = argsTab.getString("product");

        List<Adapter_Products.Product_Data> laProducts = new ArrayList<>();

        switch (Objects.requireNonNull(tabMode)){

            case "motorcycle":

                laProducts.add(new Adapter_Products.Product_Data("Yamaha", R.drawable.yamaha, R.drawable.yamahalogo));
                laProducts.add(new Adapter_Products.Product_Data("Honda", R.drawable.honda, R.drawable.hondalogo));
                laProducts.add(new Adapter_Products.Product_Data("Suzuki", R.drawable.suzuki, R.drawable.suzukilogo));

                break;
            case "mobile":
                break;
        }

        rcv_products.setAdapter(new Adapter_Products(laProducts));


        return view;
    }
}
