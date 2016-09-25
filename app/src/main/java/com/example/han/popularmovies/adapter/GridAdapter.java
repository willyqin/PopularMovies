package com.example.han.popularmovies.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.han.popularmovies.R;
import com.example.han.popularmovies.entity.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by Han on 2016/9/24.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "GridAdapter";
    private List<Movie> movies;
    private Context mContext;
    private RecyclerviewItemClickListener  recyclerviewItemClickListener;

    public GridAdapter(List<Movie> movies,Context mContext) {
        this.movies = movies;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        view.setOnClickListener(this);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + movies.get(position).getTitle());
        ImageView imageView = holder.imageView;
        String url = "http://image.tmdb.org/t/p/w780" + movies.get(position).getPoster_path();
        Log.d(TAG, "onBindViewHolder:  " + url);  //图片地址是正确的

        holder.itemView.setTag(movies.get(position));
        Picasso.with(mContext)
                .load(url)
                .transform(new MyTransformation())
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    public void setRecyclerviewItemClickListener(RecyclerviewItemClickListener recyclerviewItemClickListener) {
        this.recyclerviewItemClickListener = recyclerviewItemClickListener;
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: " + movies.size());
        return movies.size();

    }

    @Override
    public void onClick(View v) {
        if (recyclerviewItemClickListener != null){
            recyclerviewItemClickListener.onItemClick(v,(Movie)v.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_image);
        }
    }
    class MyTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {

            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();


            int targetWidth = metrics.widthPixels / 2;
            if(source.getWidth()==0){
                return source;
            }

            if(source.getWidth()<targetWidth){
                return source;
            }else{
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                if (targetHeight != 0 && targetWidth != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }
            }
        }

        @Override
        public String key() {
            return "Transformation desireWidth";
        }
    }

    public static interface RecyclerviewItemClickListener{
        void onItemClick(View view,Movie movie);
    }
}
