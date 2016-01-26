package com.interview.iso.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.interview.iso.R;

/**
 * Created by lu.nguyenvan2 on 11/11/2015.
 */
public class ChapterViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_webview);
        ImageView imageView = (ImageView)findViewById(R.id.btn_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        int id = intent.getIntExtra("id",1);

        WebView wv = (WebView)findViewById(R.id.webview);
        String uri = String.format("file:///android_asset/chapter%d.html",id);
        wv.loadUrl(uri);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
