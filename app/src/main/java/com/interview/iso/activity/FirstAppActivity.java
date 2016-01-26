package com.interview.iso.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.interview.iso.R;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.Constants;
import com.interview.iso.utils.DataPreferenceManager;

/**
 * Created by lu.nguyenvan2 on 10/28/2015.
 */
public class FirstAppActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_function_selection);
        CardView ivPolicy = (CardView)findViewById(R.id.police);
        CardView ivGovernment = (CardView)findViewById(R.id.government);
        ivPolicy.setOnClickListener(onClickListener);
        ivGovernment.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(FirstAppActivity.this,MainActivity.class);
            switch (v.getId()){
                case R.id.police:
                    AppData.getInstance().setApptype(Constants.POLICY_TYPE);
                    DataPreferenceManager.getInstance(FirstAppActivity.this).writeIntData(Constants.SELECT_TYPE, Constants.POLICY_TYPE);
                    startActivity(intent);
                    break;
                case R.id.government:
                    AppData.getInstance().setApptype(Constants.GOVERNMENT_TYPE);
                    DataPreferenceManager.getInstance(FirstAppActivity.this).writeIntData(Constants.SELECT_TYPE, Constants.GOVERNMENT_TYPE);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}
