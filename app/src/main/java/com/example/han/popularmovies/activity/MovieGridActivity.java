package com.example.han.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.han.popularmovies.R;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activitymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_setting:
                startActivity(new Intent(this,SettingActivity.class));
                return true;
            default:
                return true;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);



        moviesList = new ArrayList<Movie>();


        gridAdapter = new GridAdapter(moviesList,this);

        recyclerView = (RecyclerView) findViewById(R.id.grid_recyclerview);
        toolbar = (Toolbar) findViewById(R.id.grid_toolbar);

        setSupportActionBar(toolbar);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        recyclerView.setAdapter(gridAdapter);

        gridAdapter.setRecyclerviewItemClickListener(new GridAdapter.RecyclerviewItemClickListener() {
            @Override
            public void onItemClick(View view, Movie movie) {
                Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        if (isConnected()) {
            updateMovies();
        }
        super.onResume();
    }

    private void updateMovies() {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        String sort_type = preference.getBoolean(getString(R.string.sort_change_key),true) ? getString(R.string.sort_pop) : getString(R.string.sort_vote);
        new DownLoadTask().execute(sort_type);
    }

    private class DownLoadTask extends AsyncTask<String,Void,List<Movie>>{
        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == 0){
                return null;
            }
            String BASE_URI = "https://api.themoviedb.org/3";
            String KEY_NUM = "";                                               //这个地方应当是个人Key数字
            String API_STR = "?api_key=";
            StringBuilder stringBuilder = new StringBuilder(BASE_URI)
                .append(params[0]).append(API_STR + KEY_NUM);

            Uri uri = Uri.parse(stringBuilder.toString());

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

    public boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return  networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}
