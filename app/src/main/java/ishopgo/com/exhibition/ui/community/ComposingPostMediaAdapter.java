package ishopgo.com.exhibition.ui.community;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import ishopgo.com.exhibition.R;
import ishopgo.com.exhibition.model.PostMedia;

/**
 * Created by hoangnh on 4/27/2018.
 */
public class ComposingPostMediaAdapter extends RecyclerView.Adapter<ComposingPostMediaAdapter.ViewHolder> {

    private ArrayList<PostMedia> postMedias;
    private ArrayList<PostMedia> removedMedias;

    public ComposingPostMediaAdapter(ArrayList<PostMedia> postMedias) {
        this.postMedias = postMedias;

        removedMedias = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_horizontal, parent, false);
        final ViewHolder vh = new ViewHolder(itemView);

        vh.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vh.getLayoutPosition();
                PostMedia removedItem = postMedias.remove(position);
                notifyItemRemoved(position);

                removedMedias.add(removedItem);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (postMedias != null) {
            holder.bind(postMedias.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return postMedias == null ? 0 : postMedias.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView btnDelete, ivImage;


        ViewHolder(View view) {
            super(view);
            ivImage = itemView.findViewById(R.id.iv_image);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        void bind(PostMedia postMedia) {
            ivImage.setBackgroundColor(Color.TRANSPARENT);
            Glide.with(itemView.getContext()).load(postMedia.getUri()).apply(RequestOptions.placeholderOf(R.drawable.image_placeholder)).into(ivImage);
        }
    }
}