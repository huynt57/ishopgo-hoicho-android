package ishopgo.com.exhibition.ui.main.productmanager.detail;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import ishopgo.com.exhibition.R;
import ishopgo.com.exhibition.model.product_manager.ProductRelated;
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter;

/**
 * Created by hoangnh on 3/20/2018.
 */

public class ProductsManagerDetailRelatedAdapter extends ClickableAdapter<ProductRelated.Product> {

    @Override
    public int getChildLayoutResource(int viewType) {
        return R.layout.item_product_retated_horizontal;
    }

    @Override
    public ViewHolder<ProductRelated.Product> createHolder(@NonNull View v, int viewType) {
        return new ProductAdapter(v);
    }

    class ProductAdapter extends ClickableAdapter.ViewHolder<ProductRelated.Product> {
        private ImageView iv_spk_product_related_image, btn_spk_product_related_delete;
        private TextView tv_spk_product_related_name, tv_spk_product_related_price;
        public ProductAdapter(View itemView) {
            super(itemView);
            iv_spk_product_related_image = itemView.findViewById(R.id.iv_spk_product_related_image);
            tv_spk_product_related_name = itemView.findViewById(R.id.tv_spk_product_related_name);
            tv_spk_product_related_price = itemView.findViewById(R.id.tv_spk_product_related_price);
            btn_spk_product_related_delete = itemView.findViewById(R.id.btn_spk_product_related_delete);
        }

        @Override
        public void populate(@NonNull ProductRelated.Product product) {
            super.populate(product);
            btn_spk_product_related_delete.setVisibility(View.GONE);
            iv_spk_product_related_image.setBackgroundColor(Color.TRANSPARENT);
            Glide.with(itemView.getContext()).load(product.getImage()).apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)).into(iv_spk_product_related_image);
            tv_spk_product_related_name.setText(product.getName());
            tv_spk_product_related_price.setText(product.getPrice().toString());
        }

    }
}