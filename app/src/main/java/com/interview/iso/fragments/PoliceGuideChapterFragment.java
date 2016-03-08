package com.interview.iso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.interview.iso.R;
import com.interview.iso.utils.AppData;


/**
 * Created by Castorim on 3/8/2016.
 */
public class PoliceGuideChapterFragment extends BaseFragment {
    View rootView;
    TextView txtTitle, txtContent;
    ImageView imgTable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_police_guide_chapter, container, false);
        txtTitle = (TextView) rootView.findViewById(R.id.txt_police_guide_title);
        txtContent = (TextView) rootView.findViewById(R.id.txt_police_guide_content);
        imgTable = (ImageView) rootView.findViewById(R.id.police_guide_ch10_table);
        txtTitle.setText(getResources().getString(AppData.getInstance().getPoliceChapterTitleId()));
        txtContent.setText(getResources().getString(AppData.getInstance().getPoliceChapterContentId()));
        if (AppData.getInstance().getPoliceChapterTitleId() == R.string.police_guide_title_10) {
            imgTable.setVisibility(View.VISIBLE);
        } else {
            imgTable.setVisibility(View.GONE);
        }
        return rootView;
    }
}
