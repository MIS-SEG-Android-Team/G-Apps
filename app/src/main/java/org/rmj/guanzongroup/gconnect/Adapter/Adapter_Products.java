package org.rmj.guanzongroup.gconnect.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import org.rmj.guanzongroup.gconnect.R;

import java.util.List;

public class Adapter_Products  extends RecyclerView.Adapter<Adapter_Products.VHProducts> {

    private List<Product_Data> laProducts;
    private onSelectListener callback;

    public Adapter_Products(List<Product_Data> laProducts, onSelectListener callback) {
        this.laProducts = laProducts;
        this.callback = callback;
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
        holder.img_logo.setImageResource(laProducts.get(position).getImagelogo());

        holder.btn_inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSelect(laProducts.get(position).getBrand());
            }
        });
    }

    @Override
    public int getItemCount() {
        return laProducts.size();
    }

    public static class VHProducts extends RecyclerView.ViewHolder {

        private ShapeableImageView icon_product;
        private ShapeableImageView img_logo;
        private MaterialButton btn_inquire;

        public VHProducts(@NonNull View itemView) {
            super(itemView);

            this.icon_product = itemView.findViewById(R.id.icon_product);
            this.img_logo = itemView.findViewById(R.id.img_logo);
            this.btn_inquire = itemView.findViewById(R.id.btn_inquire);
        }
    }

    public static class Product_Data{

        private final String brand;
        private final int image;
        private final int imagelogo;

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

    public interface onSelectListener{
        void onSelect(String brandName);
    }
}
