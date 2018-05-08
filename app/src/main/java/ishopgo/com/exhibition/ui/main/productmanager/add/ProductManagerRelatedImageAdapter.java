package ishopgo.com.exhibition.ui.main.productmanager.add;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ishopgo.com.exhibition.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hoangnh on 3/20/2018.
 */

public class ProductManagerRelatedImageAdapter extends RecyclerView.Adapter<ProductManagerRelatedImageAdapter.ViewHolder> {
    static List<ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider> ProductList;
    static ArrayList<ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider> removeProductList;

    public ProductManagerRelatedImageAdapter(List<ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider> ProductList) {
        ProductManagerRelatedImageAdapter.ProductList = ProductList;
        removeProductList = new ArrayList<>();

    }

    public ArrayList<ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider> getRemovedProductList() {
        return removeProductList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_retated_horizontal, parent, false);
        final ViewHolder vh = new ViewHolder(itemView);
        vh.btn_spk_product_related_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = vh.getLayoutPosition();
                ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider removedItem = ProductList.remove(position);
                notifyItemRemoved(position);

                removeProductList.add(removedItem);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (ProductList != null) {
            holder.bind(ProductList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return ProductList == null ? 0 : ProductList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

                private android.widget.ImageView iv_spk_product_related_image, btn_spk_product_related_delete;
                private android.widget.TextView tv_spk_product_related_name, tv_spk_product_related_price;


        ViewHolder(View view) {
            super(view);
            iv_spk_product_related_image = itemView.findViewById(ishopgo.com.exhibition.R.id.iv_spk_product_related_image);
            tv_spk_product_related_name = itemView.findViewById(ishopgo.com.exhibition.R.id.tv_spk_product_related_name);
            tv_spk_product_related_price = itemView.findViewById(ishopgo.com.exhibition.R.id.tv_spk_product_related_price);
            btn_spk_product_related_delete = itemView.findViewById(ishopgo.com.exhibition.R.id.btn_spk_product_related_delete);
        }

        void bind(ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider product) {
            iv_spk_product_related_image.setBackgroundColor(Color.TRANSPARENT);
            Glide.with(itemView.getContext()).load(product.provideImage()).apply(RequestOptions.placeholderOf(ishopgo.com.exhibition.R.drawable.image_placeholder)).into(iv_spk_product_related_image);
            tv_spk_product_related_name.setText(product.provideName());
            tv_spk_product_related_price.setText(product.providePrice());
        }
        }
}
