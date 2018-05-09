package ishopgo.com.exhibition.ui.main.productmanager.detail;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import ishopgo.com.exhibition.R;
import ishopgo.com.exhibition.ui.base.list.ClickableAdapter;

/**
 * Created by hoangnh on 3/20/2018.
 */

public class ProductManagerDetailImagesAdapter extends ClickableAdapter<String> {

    @Override
    public int getChildLayoutResource(int viewType) {
        return R.layout.item_image_horizontal;
    }

    @Override
    public ViewHolder<String> createHolder(@NonNull View v, int viewType) {
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder<String> holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof Holder) {
            ((Holder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getListener().click(holder.getAdapterPosition(), getItem(holder.getLayoutPosition()), 0);
                }
            });
        }
    }

    class Holder extends ClickableAdapter.ViewHolder<String> {
        ImageView iv_image, btn_delete;

        public Holder(View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }

        @Override
        public void populate(@NonNull String data) {
            super.populate(data);
            btn_delete.setVisibility(View.GONE);
            iv_image.setBackgroundColor(Color.TRANSPARENT);
            Glide.with(itemView.getContext()).load(data).apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)).into(iv_image);
        }

    }

}