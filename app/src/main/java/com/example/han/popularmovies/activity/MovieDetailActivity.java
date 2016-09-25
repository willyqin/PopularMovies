package com.example.han.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.popularmovies.R;
import com.example.han.popularmovies.entity.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by Han on 2016/9/25.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = "MovieDetailActivity";
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Movie currentMovie = (Movie) getIntent().getSerializableExtra("movie");
        
        if (getIntent().getSerializableExtra("movie") == null){
            Log.d(TAG, "onCreate: null");
        }

        ((TextView)findViewById(R.id.detail_title)).setText(currentMovie.getTitle());
        ((TextView)findViewById(R.id.detail_overview)).setText(currentMovie.getOverview());
        ((TextView)findViewById(R.id.detail_vote)).setText(currentMovie.getVote_average());
        ((TextView)findViewById(R.id.detail_date)).setText(currentMovie.getRelease_date());

        String url = "http://image.tmdb.org/t/p/w780" + currentMovie.getPoster_path();

        Picasso.with(this)
                .load(url)
//                .transform(new MyTransformation())
                .error(R.mipmap.ic_launcher)
                .into((ImageView)findViewById(R.id.detail_image));

    }


}
