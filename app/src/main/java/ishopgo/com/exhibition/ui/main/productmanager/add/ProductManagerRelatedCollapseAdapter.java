package ishopgo.com.exhibition.ui.main.productmanager.add;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ishopgo.com.exhibition.R;
import ishopgo.com.exhibition.ui.main.productmanager.ProductManagerProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hoangnh on 3/20/2018.
 */

public class ProductManagerRelatedCollapseAdapter extends RecyclerView.Adapter<ProductManagerRelatedCollapseAdapter.ViewHolder> {
    static List<ProductManagerProvider> listProduct;
    static ArrayList<ProductManagerProvider> removeProductList;

    public ProductManagerRelatedCollapseAdapter(List<ProductManagerProvider> listProduct) {
        ProductManagerRelatedCollapseAdapter.listProduct = listProduct;
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
                ProductManagerProvider removedItem = listProduct.remove(position);
                notifyItemRemoved(position);

                removeProductList.add(removedItem);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (listProduct != null) {
            holder.bind(listProduct.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listProduct == null ? 0 : listProduct.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_spk_product_related_image, btn_spk_product_related_delete;
        private TextView tv_spk_product_related_name, tv_spk_product_related_price;


        ViewHolder(View view) {
            super(view);
            iv_spk_product_related_image = itemView.findViewById(R.id.iv_spk_product_related_image);
            tv_spk_product_related_name = itemView.findViewById(R.id.tv_spk_product_related_name);
            tv_spk_product_related_price = itemView.findViewById(R.id.tv_spk_product_related_price);
            btn_spk_product_related_delete = itemView.findViewById(R.id.btn_spk_product_related_delete);
        }

        void bind(ProductManagerProvider product) {
            iv_spk_product_related_image.setBackgroundColor(Color.TRANSPARENT);
            Glide.with(itemView.getContext()).load(product.provideImage()).apply(RequestOptions.placeholderOf(ishopgo.com.exhibition.R.drawable.image_placeholder)).into(iv_spk_product_related_image);
            tv_spk_product_related_name.setText(product.provideName());
            tv_spk_product_related_price.setText(product.providePrice());
        }
    }
}
