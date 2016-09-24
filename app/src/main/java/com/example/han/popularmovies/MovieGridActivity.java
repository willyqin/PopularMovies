package com.example.han.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.han.popularmovies.adapter.GridAdapter;
import com.example.han.popularmovies.entity.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MovieGridActivity extends AppCompatActivity {
    private static final String TAG = "MovieGridActivity";
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<Movie> moviesList;
    private GridAdapter gridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);


        moviesList = new ArrayList<Movie>();
        updateMovies();

        gridAdapter = new GridAdapter(moviesList,this);

        recyclerView = (RecyclerView) findViewById(R.id.grid_recyclerview);
//        recyclerView = (RecyclerView) findViewById(R.id.temp_recyclerview);
        toolbar = (Toolbar) findViewById(R.id.grid_toolbar);

        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setAdapter(gridAdapter);
    }

    private void updateMovies() {
        new DownLoadTask().execute("popularity.desc");
    }

    private class DownLoadTask extends AsyncTask<String,Void,List<Movie>>{
        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == 0){
                return null;
            }
            final String BASE_URI = "https://api.themoviedb.org/3/discover/movie?";
            final String SORT_TYPE = "sort_by";
            final String KEY = "api_key";
            final String KEY_NUM = "3a1dd5861232f4a8f3c192d8f7f398f0";
            Uri uri = Uri.parse(BASE_URI).buildUpon()
                    .appendQueryParameter(SORT_TYPE,params[0])
                    .appendQueryParameter(KEY,KEY_NUM)
                    .build();

            String JsonStr;
            Log.d(TAG, "doInBackground: " + uri.toString());
            BufferedReader bufferedReader = null;
            HttpsURLConnection urlConnection = null;
            try {
                URL url = new URL(uri.toString());
                urlConnection = (HttpsURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null)
                    JsonStr = null;
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                if (sb.length() == 0){
                    JsonStr = null;
                }
                JsonStr = new String(sb);
                Log.d(TAG, "doInBackground: 下载成功" );
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: first IOException");
                JsonStr = null;
            } finally {
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                if (bufferedReader != null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.d(TAG, "doInBackground: sec IOException");
                        e.printStackTrace();
                    }
                }
            }
            try {
                return getMoviesFromJson(JsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> tempmoviesList) {
            moviesList.clear();
            moviesList.addAll(tempmoviesList);
            gridAdapter.notifyDataSetChanged();
            super.onPostExecute(tempmoviesList);
        }
    }

    private List<Movie> getMoviesFromJson(String jsonStr) throws JSONException {
        Log.d(TAG, "getMoviesFromJson: before GSON");
        Gson gson = new Gson();
        List<Movie> movieList = gson.fromJson(new JSONObject(jsonStr).getString("results"),new TypeToken<List<Movie>>(){}.getType());
        for (Movie movie : movieList){
            Log.d(TAG, "getMoviesFromJson: title   " + movie.getTitle());
            Log.d(TAG, "getMoviesFromJson: poster_path   " + movie.getPoster_path());
            Log.d(TAG, "getMoviesFromJson: ***********************");
        }
        return movieList;
    }

}
