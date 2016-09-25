package com.example.han.popularmovies.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.han.popularmovies.fragment.SettingFragment;

/**
 * Created by Han on 2016/9/25.
 */
public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
//    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
        
//        if (getSupportActionBar() != null){
//            Log.d(TAG, "onCreate: ActionBar not null");
//        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingFragment())
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                Log.d(TAG, "onOptionsItemSelected: back");
                NavUtils.navigateUpFromSameTask(this);
//                onBackPressed();
                return true;
            default:
                return true;
        }
    }
}
