package com.interview.iso.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interview.iso.R;

/**
 * Created by Castorim on 3/8/2016.
 */
public class TranslatorFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_translator, container,false);

        return v;
    }
}
