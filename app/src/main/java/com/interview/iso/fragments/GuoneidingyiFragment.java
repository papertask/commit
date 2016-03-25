package com.interview.iso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.base.MenuItem;

/**
 * Created by MasterDev on 3/17/16.
 */
public class GuoneidingyiFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_law_guonei, container,false);
        return v;
    }

    public void doBackAction() {
        MainActivity activity = (MainActivity) getActivity();
        activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_legal_definition), "GuojidingyiFragment", "police_guide", 0));
    }
}
