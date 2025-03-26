package org.rmj.guanzongroup.gconnect.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Adapter_Products  extends RecyclerView.Adapter<Adapter_Products.VHProducts> {

    List<Product_Data> laProducts;

    public Adapter_Products(List<Product_Data> laProducts) {
        this.laProducts = laProducts;
    }

    @NonNull
    @Override
    public VHProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHProducts(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_products, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull VHProducts holder, int position) {

        holder.icon_product.setImageResource(laProducts.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return laProducts.size();
    }

    public class VHProducts extends RecyclerView.ViewHolder {

        private ShapeableImageView icon_product;

        public VHProducts(@NonNull View itemView) {
            super(itemView);

            this.icon_product = itemView.findViewById(R.id.icon_product);
        }
    }

    public static class Product_Data{

        private String brand;
        private int image;

        private int imagelogo;

        public Product_Data(String brand, int image, int imagelogo) {
            this.brand = brand;
            this.image = image;
            this.imagelogo = imagelogo;
        }

        public String getBrand() {
            return brand;
        }

        public int getImage() {
            return image;
        }

        public int getImagelogo() {
            return imagelogo;
        }

    }
}
