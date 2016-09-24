package com.example.han.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.han.popularmovies.R;
import com.example.han.popularmovies.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Han on 2016/9/24.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private static final String TAG = "GridAdapter";
    private List<Movie> movies;
    private Context mContext;
    public GridAdapter(List<Movie> movies,Context mContext) {
        this.movies = movies;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + movies.get(position).getTitle());
        final ImageView imageView = holder.imageView;
        final String url = "http://image.tmdb.org/t/p/w185" + movies.get(position).getPoster_path();
        Log.d(TAG, "onBindViewHolder:  " + url);  //图片地址是正确的

        Picasso.with(mContext)
                .load(url)
                .error(R.drawable.test)
                .into(imageView);
//        Picasso.with(mContext).load(R.drawable.test).into(imageView);

//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    final Bitmap bitmap = Picasso.with(mContext.getApplicationContext()).load(url).get();
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//                            imageView.setImageBitmap(bitmap);
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }


    @Override
    public int getItemCount() {
//        Log.d(TAG, "getItemCount: " + movies.size());
        return movies.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recycler_image);
        }
    }
}
