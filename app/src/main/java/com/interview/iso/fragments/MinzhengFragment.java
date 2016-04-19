package com.interview.iso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.base.MenuItem;

/**
 * Created by MasterDev on 3/17/16.
 */
public class MinzhengFragment extends BaseFragment {
    private TextView btnMore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_minzheng_index, container,false);
        btnMore = (TextView)v.findViewById(R.id.button_minzheng_more);
        btnMore.setOnClickListener(onClickListener);
        return v;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MainActivity activity = (MainActivity)getActivity();
            activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_people_rescue_procedure), "MinzhengDetailFragment", "police_guide_minzheng", 0));
        }
    };
}
