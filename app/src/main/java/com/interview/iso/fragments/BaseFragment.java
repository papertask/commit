package com.interview.iso.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by lu.nguyenvan2 on 10/26/2015.
 */
public class BaseFragment extends Fragment {
    String mTitle;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
