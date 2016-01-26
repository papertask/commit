package com.interview.iso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;

import com.interview.iso.R;

/**
 * Created by lu.nguyenvan2 on 12/28/2015.
 */
public class CommitHelpFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.commit_help_fragment,container,false);
        WebView web_viewobj=(WebView)rootView.findViewById(R.id.webview_help);
        web_viewobj.getSettings().setJavaScriptEnabled(true);
        web_viewobj.getSettings().setLoadsImagesAutomatically(true);
        web_viewobj.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_viewobj.loadUrl("file:///android_asset/chapter11.html");

        return rootView;
    }
}
