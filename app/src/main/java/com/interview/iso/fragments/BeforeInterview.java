package com.interview.iso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;

/**
 * Created by lu.nguyenvan2 on 10/30/2015.
 */
public class BeforeInterview extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_before_question, container, false);
        Button btnNext = (Button)v.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.UpdateFragment("QuestionFragment");
            }
        });
        return v;

    }

}
