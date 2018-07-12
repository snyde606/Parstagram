package me.snyde606.parstagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ParseException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import me.snyde606.parstagram.models.PostObj;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<PostObj> mPosts;
    private Context context;
    private ClickListener listen;

    public PostAdapter(List<PostObj> tweets, ClickListener lis){
        mPosts = tweets;
        listen = lis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView, listen);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostObj post = mPosts.get(position);
        String timeAgo = getRelativeTimeAgo(post.getCreatedAt());

        //TODO - Favorite feature
//        if(post.favorited)
//            holder.ivFavorite.setActivated(true);
//        else
//            holder.ivFavorite.setActivated(false);

        holder.tvUsername.setText(post.getUsername());
        holder.tvDescription.setText(post.getDescription());
        holder.tvTimelineTimeAgo.setText(getRelativeTimeAgo(post.getCreatedAt()));

        Bitmap takenImage = BitmapFactory.decodeFile(post.getImageFile().getAbsolutePath());
        // RESIZE BITMAP, see section below
        // Load the taken image into a preview
        holder.ivTimelineImage.setImageBitmap(takenImage);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvUsername;
        public TextView tvDescription;
        public ImageView ivTimelineImage;
        public TextView tvTimelineTimeAgo;
//        public ImageView ivFavorite;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView, ClickListener listener){
            super(itemView);

            listenerRef = new WeakReference<>(listener);
            ivTimelineImage = (ImageView) itemView.findViewById(R.id.ivTimelineImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvTimelineTimeAgo = (TextView) itemView.findViewById(R.id.tvTimelineTimeAgo);
//            tvTimeAgo = (TextView) itemView.findViewById(R.id.tvTimeAgo);
//            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);

            ivTimelineImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Log.d("PostAdapter", "View clicked: " + getAdapterPosition());
            listenerRef.get().switchToDetail(getAdapterPosition());

//            if(v.getId() == ivFavorite.getId() && !ivFavorite.isActivated()) {
//                ivFavorite.setActivated(true);
//                listenerRef.get().onFavoriteClicked(getAdapterPosition());
//            }
//            else if(v.getId() == ivFavorite.getId() && ivFavorite.isActivated()) {
//                ivFavorite.setActivated(false);
//                listenerRef.get().onUnFavoriteClicked(getAdapterPosition());
//            }
        }

    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        Log.d("PostAdapter", rawJsonDate);
        String twitterFormat = "EEE MMM dd HH:mm:ss zzz yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<PostObj> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    public interface ClickListener {

        void switchToDetail(int position);

//        void onFavoriteClicked(int position);
//        void onUnFavoriteClicked(int position);


    }

}
