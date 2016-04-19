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
public class PeopleGovDetailFragment extends BaseFragment {
    private int int_chapter_index = 1;
    private TextView ctrlContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_people_gov_chapter, container,false);
        ctrlContent = (TextView) rootView.findViewById(R.id.txt_people_chapter);
        if (this.int_chapter_index == 2) {
            ctrlContent.setText(getResources().getString(R.string.people_gov_chapter_2));
        } else {
            ctrlContent.setText(getResources().getString(R.string.people_gov_chapter_1));
        }

        return rootView;
    }

    public void doBackAction() {
        MainActivity activity = (MainActivity)getActivity();
        activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_trafficking_victims_indicators), "PeopleGovFragment", "police_guide", 0));
    }

    public void setChapterIndex( int index ) {
        this.int_chapter_index = index;
    }
}
