package com.interview.iso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.base.MenuItem;

/**
 * Created by Castorim on 3/8/2016.
 */
public class GovInternationalFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_int_law, container,false);

        return v;
    }

    public void doBackAction() {
        MainActivity activity = (MainActivity) getActivity();
        activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_legal_definition), "GuojidingyiFragment", "police_guide", 0));
    }
}
