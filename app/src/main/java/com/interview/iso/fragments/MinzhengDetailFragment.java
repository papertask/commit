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
 * Created by MasterDev on 3/17/16.
 */
public class MinzhengDetailFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_minzheng_content, container,false);
        return v;
    }

    public void doBackAction() {
        MainActivity activity = (MainActivity) getActivity();
        activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_people_rescue_procedure), "MinzhengFragment", "police_guide", 0));
    }
}
