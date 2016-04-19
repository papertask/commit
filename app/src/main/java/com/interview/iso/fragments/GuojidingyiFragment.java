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
public class GuojidingyiFragment extends BaseFragment {
    LinearLayout btn_goto_1, btn_goto_2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_law_index, container,false);
        btn_goto_1 = (LinearLayout)v.findViewById(R.id.button_goto_guoji);
        btn_goto_2 = (LinearLayout)v.findViewById(R.id.button_goto_guonei);
        btn_goto_1.setOnClickListener(onClickListener);
        btn_goto_2.setOnClickListener(onClickListener);
        return v;
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            MainActivity activity = (MainActivity) getActivity();
            if (v.getId() == R.id.button_goto_guoji) {
                activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_legal_definition), "GovInternationalFragment", "police_guide_guoji", 0));
            } else {
                activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_legal_definition), "GuoneidingyiFragment", "police_guide_guonei", 0));
            }
        }
    };
}
