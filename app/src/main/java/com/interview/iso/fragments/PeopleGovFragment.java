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
 * Created by Castorim on 3/8/2016.
 */
public class PeopleGovFragment extends BaseFragment {
    private LinearLayout btnChapter1, btnChapter2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_people_gov, container,false);
        btnChapter1 = (LinearLayout)v.findViewById(R.id.btn_people_chapter_1);
        btnChapter2 = (LinearLayout)v.findViewById(R.id.btn_people_chapter_2);
        btnChapter1.setOnClickListener(onClickListener);
        btnChapter2.setOnClickListener(onClickListener);
        return v;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MainActivity activity = (MainActivity)getActivity();
            if (v.getId() == R.id.btn_people_chapter_1) {
                activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_trafficking_victims_indicators), "PeopleGovDetailFragment", "people_gov_chapter", 0), 1);
            } else {
                activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_trafficking_victims_indicators), "PeopleGovDetailFragment", "people_gov_chapter", 0), 2);
            }
        }
    };
}
