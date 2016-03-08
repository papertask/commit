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
public class PeopleGovFragment extends BaseFragment {
    TextView btnReadmore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_people_gov, container,false);
        btnReadmore = (TextView) v.findViewById(R.id.btn_readmore_people_gov);
        btnReadmore.setOnClickListener(onClickListener);
        return v;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_readmore_people_gov) {
                MainActivity activity = (MainActivity)getActivity();
                activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_people_rescue_procedure), "PeopleGovDetailFragment", "police_guide", 0));
            }
        }
    };
}
