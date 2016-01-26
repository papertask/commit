package com.interview.iso.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.interview.iso.R;
import com.interview.iso.models.Question;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.Constants;
import com.interview.iso.utils.DBHelper;
import com.interview.iso.utils.DataPreferenceManager;

/**
 * Created by lu.nguyenvan2 on 10/27/2015.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //check first run app
                if(DataPreferenceManager.getInstance(SplashActivity.this).getDataIntFromHolder(Constants.SELECT_TYPE)!=0)
                {
                    AppData.getInstance().setApptype(DataPreferenceManager.getInstance(SplashActivity.this).getDataIntFromHolder(Constants.SELECT_TYPE));
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else {
                    //input question into database
                    AppData.getInstance().setApptype(Constants.POLICY_TYPE);
                    startActivity(new Intent(SplashActivity.this, FirstAppActivity.class));
                    finish();
                }
            }
        },4000);
    }
}
