package com.interview.iso.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.base.MenuItem;
import com.interview.iso.utils.AppData;

/**
 * Created by Castorim on 3/8/2016.
 */
public class PoliceGuideFragment extends BaseFragment {

    View rootView;
    LinearLayout btn_chapter_index, btn_chapter_1, btn_chapter_2, btn_chapter_3, btn_chapter_4, btn_chapter_5, btn_chapter_6, btn_chapter_7, btn_chapter_8, btn_chapter_9, btn_chapter_10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_police_guide_index, container, false);

        btn_chapter_index = (LinearLayout) rootView.findViewById(R.id.police_guide_index);
        btn_chapter_index.setOnClickListener(onClickListener);
        btn_chapter_1 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch1);
        btn_chapter_1.setOnClickListener(onClickListener);
        btn_chapter_2 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch2);
        btn_chapter_2.setOnClickListener(onClickListener);
        btn_chapter_3 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch3);
        btn_chapter_3.setOnClickListener(onClickListener);
        btn_chapter_4 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch4);
        btn_chapter_4.setOnClickListener(onClickListener);
        btn_chapter_5 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch5);
        btn_chapter_5.setOnClickListener(onClickListener);
        btn_chapter_6 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch6);
        btn_chapter_6.setOnClickListener(onClickListener);
        btn_chapter_7 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch7);
        btn_chapter_7.setOnClickListener(onClickListener);
        btn_chapter_8 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch8);
        btn_chapter_8.setOnClickListener(onClickListener);
        btn_chapter_9 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch9);
        btn_chapter_9.setOnClickListener(onClickListener);
        btn_chapter_10 = (LinearLayout) rootView.findViewById(R.id.police_guide_ch10);
        btn_chapter_10.setOnClickListener(onClickListener);

        return rootView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.police_guide_index:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_index);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_index);
                    break;
                case R.id.police_guide_ch1:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_1);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_1);
                    break;
                case R.id.police_guide_ch2:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_2);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_2);
                    break;
                case R.id.police_guide_ch3:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_3);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_3);
                    break;
                case R.id.police_guide_ch4:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_4);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_4);
                    break;
                case R.id.police_guide_ch5:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_5);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_5);
                    break;
                case R.id.police_guide_ch6:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_6);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_6);
                    break;
                case R.id.police_guide_ch7:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_7);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_7);
                    break;
                case R.id.police_guide_ch8:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_8);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_8);
                    break;
                case R.id.police_guide_ch9:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_9);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_9);
                    break;
                case R.id.police_guide_ch10:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_10);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_10);
                    break;
                default:
                    AppData.getInstance().setPoliceChapterTitleId(R.string.police_guide_title_index);
                    AppData.getInstance().setPoliceChapterContentId(R.string.police_guide_content_index);
                    break;
            }
            MainActivity activity = (MainActivity) getActivity();
            activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_police_crackdown_guide), "PoliceGuideChapterFragment", "police_guide", 0));
        }
    };
}
