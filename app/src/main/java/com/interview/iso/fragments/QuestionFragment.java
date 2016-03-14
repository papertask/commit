package com.interview.iso.fragments;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.base.MenuItem;
import com.interview.iso.models.Person;
import com.interview.iso.models.Question;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.Constants;
import com.interview.iso.utils.DBHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by lu.nguyenvan2 on 10/26/2015.
 * Modified by Castorim on 03/10/2016
 */
public class QuestionFragment extends BaseFragment {
    private int radio_id;
    RadioGroup radioGroup;

    Map<Integer, Question> mListQuestion;
    int current_question = 0;
    TextView tv_question_en, tv_question_cn, tv_question_basic_cn, tv_question_basic_en, tv_header;
    LinearLayout lnYes, lnYesMa, lnNo, lnNoMa, lnSkip, btn_next, ln_action_question;
    Button btn_play_audio;
    // Question 13 container
    RelativeLayout topContainer, bottomContainer;
    ScrollView middleContainer;
    RelativeLayout q13ConfirmContainer;
    LinearLayout q13_btn_yes, q13_btn_no;

    // variables for marrage screen
    TextView tv_gov_term_title_cn, tv_gov_term_desc_cn, tv_gov_term_title_fo, tv_gov_term_desc_fo, ctrl_space;

    public static MediaPlayer mPlayer;
    Map<String, Integer> mAnswer;
    List<Integer> mQueueQuestion;
    View rootView;

    boolean q13_confirmed = false;
    boolean need_extra_question = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_question, container, false);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.answer_radio_group);
        Button btn_share = (Button) rootView.findViewById(R.id.btn_share_intent);
        btn_play_audio = (Button) rootView.findViewById(R.id.btn_play_audio);
        btn_next = (LinearLayout) rootView.findViewById(R.id.next);
        tv_question_cn = (TextView) rootView.findViewById(R.id.tv_question_cn);
        tv_question_en = (TextView) rootView.findViewById(R.id.tv_question_en);
        tv_question_basic_cn = (TextView) rootView.findViewById(R.id.basic_query_cn);
        tv_question_basic_en = (TextView) rootView.findViewById(R.id.basic_query_en);
        ln_action_question = (LinearLayout) rootView.findViewById(R.id.ln_action_question);
        tv_header = (TextView) rootView.findViewById(R.id.header);
        lnYes = (LinearLayout) rootView.findViewById(R.id.ln_yes);
        lnYesMa = (LinearLayout) rootView.findViewById(R.id.ln_yes_ma);
        lnNo = (LinearLayout) rootView.findViewById(R.id.ln_no);
        lnNoMa = (LinearLayout) rootView.findViewById(R.id.ln_no_ma);
        lnSkip = (LinearLayout) rootView.findViewById(R.id.ln_skip);

        lnYes.setOnClickListener(onClickListener);
        lnYesMa.setOnClickListener(onClickListener);
        lnNo.setOnClickListener(onClickListener);
        lnNoMa.setOnClickListener(onClickListener);
        lnSkip.setOnClickListener(onClickListener);

        // Initialize containers and q13 confirm dialog
        topContainer = (RelativeLayout) rootView.findViewById(R.id.container_top);
        middleContainer = (ScrollView) rootView.findViewById(R.id.scrollview_container);
        bottomContainer = (RelativeLayout) rootView.findViewById(R.id.container_bottom);
        q13_btn_yes = (LinearLayout) rootView.findViewById(R.id.btn_13_yes);
        q13_btn_no = (LinearLayout) rootView.findViewById(R.id.btn_13_no);
        q13ConfirmContainer = (RelativeLayout) rootView.findViewById(R.id.container_q_13_confirm);

        q13_btn_yes.setOnClickListener(onClickListener);
        q13_btn_no.setOnClickListener(onClickListener);

        // variables for marrage screen
        tv_gov_term_title_cn = (TextView) rootView.findViewById(R.id.marrage_title_cn);
        tv_gov_term_desc_cn = (TextView) rootView.findViewById(R.id.marrage_title_cn_1);
        tv_gov_term_title_fo = (TextView) rootView.findViewById(R.id.marrage_title_fo);
        tv_gov_term_desc_fo = (TextView) rootView.findViewById(R.id.marrage_title_fo_1);
        ctrl_space = (TextView) rootView.findViewById(R.id.question_header_space);

        if (AppData.getInstance().getLanguage().equals("lao")) {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lao.ttf");
            tv_question_en.setTypeface(tf_mm3);
            tv_question_basic_en.setTypeface(tf_mm3);
            tv_question_cn.setTypeface(tf_mm3);
            tv_gov_term_title_fo.setTypeface(tf_mm3);
            tv_gov_term_desc_fo.setTypeface(tf_mm3);
        } else if (AppData.getInstance().getLanguage().equals("my")) {
            /* Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/mm.ttf");
            tv_question_en.setTypeface(tf_mm3);
            tv_question_basic_en.setTypeface(tf_mm3);
            tv_question_cn.setTypeface(tf_mm3);
            tv_gov_term_title_fo.setTypeface(tf_mm3);
            tv_gov_term_desc_fo.setTypeface(tf_mm3); */
        } else if (AppData.getInstance().getLanguage().equals("km")) {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/khmerOS.ttf");
            tv_question_en.setTypeface(tf_mm3);
            tv_question_basic_en.setTypeface(tf_mm3);
            tv_question_cn.setTypeface(tf_mm3);
            tv_gov_term_title_fo.setTypeface(tf_mm3);
            tv_gov_term_desc_fo.setTypeface(tf_mm3);
        }
        radioGroup.check(0);
        Log.e("group_id", radio_id + "");
        //btn_fullScreen.setOnClickListener(onClickListener);
        btn_next.setOnClickListener(onClickListener);
        btn_play_audio.setOnClickListener(onClickListener);
        btn_share.setOnClickListener(onClickListener);
        mListQuestion = AppData.getInstance().getListQuestion(AppData.getInstance().getApptype());
        mQueueQuestion = new ArrayList<>();
        initQuestion();
//        if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
//            initQuestion();
//        } else {
//            mQueueQuestion.add(current_question);
//            if (mListQuestion != null) {
//                if (AppData.getInstance().getLanguage().equals("vn")) {
//                    tv_question_en.setText(mListQuestion.get(current_question).question_vn);
//                } else if (AppData.getInstance().getLanguage().equals("my")) {
//                    tv_question_en.setText(mListQuestion.get(current_question).question_my);
//                } else if (AppData.getInstance().getLanguage().equals("lao")) {
//                    tv_question_en.setText(mListQuestion.get(current_question).question_lao);
//                }else if (AppData.getInstance().getLanguage().equals("km")){
//                    tv_question_en.setText(mListQuestion.get(current_question).question_km);
//                }
//                tv_question_cn.setText(mListQuestion.get(current_question).question_cn);
//            }
//        }

        return rootView;
    }

    private void initQuestion() {

        tv_question_basic_cn.setVisibility(View.VISIBLE);
        tv_question_basic_en.setVisibility(View.VISIBLE);
        if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
            tv_question_cn.setTextSize(18);
            if (AppData.getInstance().getLanguage().equals("lao")) {
                // tv_question_en.setText(getString(R.string.question_policy_lao1));
                // tv_question_basic_en.setText(getString(R.string.question_policy_lao));
                tv_question_basic_cn.setText(getString(R.string.question_policy_cn) + getString(R.string.question_policy_lao) + getString(R.string.question_policy_cn_1) + getString(R.string.question_policy_lao_1) + getString(R.string.question_policy_cn_2) + getString(R.string.question_policy_lao_2));
                tv_question_cn.setText(getString(R.string.question_policy_cn1) + getString(R.string.question_policy_lao1));
            } else if (AppData.getInstance().getLanguage().equals("vn")) {
                // tv_question_en.setText(getString(R.string.question_policy_vn1));
                // tv_question_basic_en.setText(getString(R.string.question_policy_vn));
                tv_question_basic_cn.setText(getString(R.string.question_policy_cn) + getString(R.string.question_policy_lao) + getString(R.string.question_policy_cn_1) + getString(R.string.question_policy_vn_1) + getString(R.string.question_policy_cn_2) + getString(R.string.question_policy_vn_2));
                tv_question_cn.setText(getString(R.string.question_policy_cn1) + getString(R.string.question_policy_vn1));
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                //tv_question_en.setText(getString(R.string.question_policy_km1));
                //tv_question_basic_en.setText(getString(R.string.question_policy_km));
                tv_question_basic_cn.setText(getString(R.string.question_policy_cn) + getString(R.string.question_policy_lao) + getString(R.string.question_policy_cn_1) + getString(R.string.question_policy_km_1) + getString(R.string.question_policy_cn_2) + getString(R.string.question_policy_km_2));
                tv_question_cn.setText(getString(R.string.question_policy_cn1) + getString(R.string.question_policy_km1));
            } else if (AppData.getInstance().getLanguage().equals("my")) {
                // tv_question_en.setText(getString(R.string.question_policy_my1));
                // tv_question_basic_en.setText(getString(R.string.question_policy_my));
                tv_question_basic_cn.setText(getString(R.string.question_policy_cn) + getString(R.string.question_policy_my) + getString(R.string.question_policy_cn_1) + getString(R.string.question_policy_my_1) + getString(R.string.question_policy_cn_2) + getString(R.string.question_policy_my_2));
                tv_question_cn.setText(getString(R.string.question_policy_cn1) + getString(R.string.question_policy_my1));
            }

            // tv_question_basic_cn.setText(getString(R.string.question_policy_cn));
            // tv_question_cn.setText(getString(R.string.question_policy_cn1));
        } else {
            RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) lnNo.getLayoutParams();
            layout.width = LinearLayout.LayoutParams.MATCH_PARENT;
            lnNo.setLayoutParams(layout);
            lnSkip.setVisibility(View.GONE);

            if (AppData.getInstance().getLanguage().equals("lao")) {
                tv_question_en.setText(getString(R.string.quetion_gov_lao));
                tv_question_basic_en.setText(getString(R.string.quetion_gov_cn1));
                tv_question_basic_cn.setText(getString(R.string.quetion_gov_cn));
                tv_question_cn.setText(getString(R.string.quetion_gov_lao1));
                tv_gov_term_title_cn.setText(getResources().getString(R.string.title_gov_term));
                tv_gov_term_desc_cn.setText(getResources().getString(R.string.header_gov_term));
                tv_gov_term_desc_fo.setText(getResources().getString(R.string.header_gov_term_lao));
                tv_gov_term_title_fo.setText(getResources().getString(R.string.title_gov_term_lao));
            } else if (AppData.getInstance().getLanguage().equals("vn")) {
                tv_question_basic_cn.setText(getString(R.string.quetion_gov_cn));
                tv_question_cn.setText(getString(R.string.quetion_gov_vn));
                tv_question_en.setText(getString(R.string.quetion_gov_vn1));
                tv_question_basic_en.setText(getString(R.string.quetion_gov_cn1));
                tv_gov_term_title_cn.setText(getResources().getString(R.string.title_gov_term));
                tv_gov_term_desc_cn.setText(getResources().getString(R.string.header_gov_term));
                tv_gov_term_desc_fo.setText(getResources().getString(R.string.header_gov_term_vn));
                tv_gov_term_title_fo.setText(getResources().getString(R.string.title_gov_term_vn));
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                tv_question_en.setText(getString(R.string.quetion_gov_km));
                tv_question_basic_en.setText(getString(R.string.quetion_gov_cn1));
                tv_question_basic_cn.setText(getString(R.string.quetion_gov_cn));
                tv_question_cn.setText(getString(R.string.quetion_gov_km1));
                tv_gov_term_title_cn.setText(getResources().getString(R.string.title_gov_term));
                tv_gov_term_desc_cn.setText(getResources().getString(R.string.header_gov_term));
                tv_gov_term_desc_fo.setText(getResources().getString(R.string.header_gov_term_km));
                tv_gov_term_title_fo.setText(getResources().getString(R.string.title_gov_term_km));
            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tv_question_en.setText(getString(R.string.quetion_gov_my));
                tv_question_basic_en.setText(getString(R.string.quetion_gov_cn1));
                tv_question_basic_cn.setText(getString(R.string.quetion_gov_cn));
                tv_question_cn.setText(getString(R.string.quetion_gov_my1));
                tv_gov_term_title_cn.setText(getResources().getString(R.string.title_gov_term));
                tv_gov_term_desc_cn.setText(getResources().getString(R.string.header_gov_term));
                tv_gov_term_desc_fo.setText(getResources().getString(R.string.header_gov_term_my));
                tv_gov_term_title_fo.setText(getResources().getString(R.string.title_gov_term_my));
            }
            tv_gov_term_title_cn.setVisibility(View.VISIBLE);
            tv_gov_term_desc_cn.setVisibility(View.VISIBLE);
            tv_gov_term_desc_fo.setVisibility(View.VISIBLE);
            tv_gov_term_title_fo.setVisibility(View.VISIBLE);
            ctrl_space.setVisibility(View.VISIBLE);
            tv_header.setVisibility(View.VISIBLE);
            setHeaderTitle("婚姻登记问卷 - 基本询问");

        }
        RadioButton rdNo = (RadioButton) rootView.findViewById(R.id.rd_no);

        RadioButton rdYes = (RadioButton) rootView.findViewById(R.id.rd_yes);
        rdYes.setChecked(true);
        rdYes.setTextSize(10f);
        if (AppData.getInstance().getLanguage().equals("vn")) {
            rdYes.setText("是(Có)");
            rdNo.setText("不是(Không)");
        } else if (AppData.getInstance().getLanguage().equals("lao")) {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lao.ttf");
            rdNo.setTypeface(tf_mm3);
            rdYes.setTypeface(tf_mm3);
            rdYes.setText("是(ແມ່ນ)");
            rdNo.setText("不是(ບໍ່ແມ່ນ)");
        } else if (AppData.getInstance().getLanguage().equals("my")) {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/mm.ttf");
            rdNo.setTypeface(tf_mm3);
            rdYes.setTypeface(tf_mm3);
            rdYes.setText("是(ဟုတ္ပါသည္ ။)");
            rdNo.setText("不是(မဟုတ္ပါ။)");
        } else if (AppData.getInstance().getLanguage().equals("km")) {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/khmerOS.ttf");
            rdNo.setTypeface(tf_mm3);
            rdYes.setTypeface(tf_mm3);
            rdYes.setText("是(យល់ព្រម)");
            rdNo.setText("不是(គ្មាន)");
        }
        rdNo.setVisibility(View.GONE);
        current_question = -1;
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.updateActionBar(false);
        if (current_question == -1) {
            showYesNoButton(false);
        } else {
            showYesNoButton(true);
        }

    }

    private void showYesNoButton(Boolean value) {
        if (value) {
            ln_action_question.setVisibility(View.VISIBLE);
            if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
                lnYes.setVisibility(View.VISIBLE);
                lnYesMa.setVisibility(View.GONE);
                lnNo.setVisibility(View.VISIBLE);
                lnNoMa.setVisibility(View.GONE);
            } else {
                lnYes.setVisibility(View.GONE);
                lnYesMa.setVisibility(View.VISIBLE);
                lnNo.setVisibility(View.GONE);
                lnNoMa.setVisibility(View.VISIBLE);
            }
            btn_next.setVisibility(View.GONE);
        } else {
            ln_action_question.setVisibility(View.GONE);
            btn_next.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (current_question == -1) {
            showYesNoButton(false);
        } else {
            showYesNoButton(true);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ln_yes:
                case R.id.ln_yes_ma:
                    if (current_question >= 0) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.updateActionBar(true);
                        showYesNoButton(true);
                    }
                    if (mPlayer != null && mPlayer.isPlaying()) {
                        mPlayer.reset();
                        // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                        // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        btn_play_audio.setBackgroundResource(R.drawable.oval_play);
                    }
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                        loadNextStep_Policy1(1);
                    else
                        loadNextStep_Government1(1);
                    break;
                case R.id.ln_no:
                case R.id.ln_no_ma:
                    if (current_question >= 0) {
                        showYesNoButton(true);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.updateActionBar(true);
                    }
                    if (mPlayer != null && mPlayer.isPlaying()) {
                        mPlayer.reset();
                        // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                        // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        btn_play_audio.setBackgroundResource(R.drawable.oval_play);
                    }
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                        loadNextStep_Policy1(0);
                    else
                        loadNextStep_Government1(0);

                    break;
                case R.id.ln_skip:
                    if (current_question >= 0) {
                        showYesNoButton(true);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.updateActionBar(true);
                    }
                    if (mPlayer != null && mPlayer.isPlaying()) {
                        mPlayer.reset();
                        // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                        // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    }
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                        loadNextStep_Policy1(2);
                    else
                        loadNextStep_Government1(2);

                    break;
                case R.id.next:
                    showYesNoButton(true);
                    //if (current_question >= 0) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.updateActionBar(true);
                    //}
                    if (mPlayer != null && mPlayer.isPlaying()) {
                        mPlayer.reset();
                        // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                        // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        btn_play_audio.setBackgroundResource(R.drawable.oval_play);
                    }

                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                        loadNextStep_Policy1(1);
                    else
                        loadNextStep_Government1(1);
                    break;
                case R.id.btn_play_audio:
                    if (mPlayer == null)
                        mPlayer = new MediaPlayer();
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                        mPlayer = new MediaPlayer();
                        // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                        // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        btn_play_audio.setBackgroundResource(R.drawable.oval_play);
                    } else {
                        // Drawable img = getContext().getResources().getDrawable(R.drawable.icon_pause);
                        // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        btn_play_audio.setBackgroundResource(R.drawable.button_pause);
                        String apptype = "";
                        if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                            apptype = "policy";
                        else
                            apptype = "government";
                        String file_name = "";
                        if (current_question > -1)
                            file_name = String.format("Sound/%s/%s/%d.mp3", apptype, AppData.getInstance().getLanguage(), current_question + 1);//1
                        else
                            file_name = String.format("Sound/%s/%s/%s.mp3", apptype, AppData.getInstance().getLanguage(), "introduction");
                        Play(file_name);
                    }
                    break;
                case R.id.btn_share_intent:
                    shareIntent();
                    break;
                case R.id.btn_13_yes:
                    // Confirm Yes after Question 13
                    need_extra_question = true;
                    showHideQ13Confirm(false);
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
                        current_question = 12;
                        loadNextStep_Policy1(radioGroup.getCheckedRadioButtonId() == R.id.rd_yes?1:0);
                    } else {
                        current_question = 2;
                        loadNextStep_Government();
                    }
                    break;
                case R.id.btn_13_no:
                    // Confirm No after Question 13
                    need_extra_question = false;
                    showHideQ13Confirm(false);
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
                        current_question = 17;
                        Policy_Extra_Process();
                    } else {
                        current_question = 4;
                        loadNextStep_Government();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void Play(String fileName) {

        try {
            if (mPlayer == null)
                mPlayer = new MediaPlayer();
            mPlayer.reset();
            AssetFileDescriptor descriptor = getActivity().getAssets().openFd(fileName);
            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();
            if (start == end) {
                // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                btn_play_audio.setBackgroundResource(R.drawable.oval_play);
                return;
            }
            //update paused

            mPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                    // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    btn_play_audio.setBackgroundResource(R.drawable.oval_play);
                }
            });
            mPlayer.start();

        } catch (Exception ex) {
            // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
            // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            btn_play_audio.setBackgroundResource(R.drawable.oval_play);
            if (mPlayer != null && mPlayer.isPlaying())
                mPlayer.pause();
        }
    }

    public void Pause() {
        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.pause();
    }

    public void setHeaderTitle(String str_title) {
        MainActivity activity = (MainActivity) getActivity();
        ((TextView) activity.findViewById(R.id.title)).setText(str_title);
    }

    public void showHideQ13Confirm( boolean bShow ) {
        MainActivity activity = (MainActivity) getActivity();
        if ( bShow ) {
            ((Toolbar) activity.findViewById(R.id.toolbar)).setVisibility(View.GONE);
            topContainer.setVisibility(View.GONE);
            middleContainer.setVisibility(View.GONE);
            bottomContainer.setVisibility(View.GONE);
            TextView txtConfirm = (TextView)q13ConfirmContainer.findViewById(R.id.txt_confirm_message);
            if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                txtConfirm.setText(getResources().getString(R.string.q13_confirm_policy));
            else
                txtConfirm.setText(getResources().getString(R.string.q13_confirm_gov));
            q13ConfirmContainer.setVisibility(View.VISIBLE);
        } else {
            ((Toolbar) activity.findViewById(R.id.toolbar)).setVisibility(View.VISIBLE);
            q13ConfirmContainer.setVisibility(View.GONE);
            topContainer.setVisibility(View.VISIBLE);
            middleContainer.setVisibility(View.VISIBLE);
            bottomContainer.setVisibility(View.VISIBLE);
        }
    }

    public void loadNextStep_Policy1(int value) {
        Log.e("CURRENT_QUESTION", "current_question " + current_question);
        tv_question_basic_en.setText("");
        tv_question_basic_cn.setVisibility(View.GONE);

        if (mAnswer == null)
            mAnswer = new HashMap<>();
        if (current_question > -1) {
            mAnswer.put(current_question + 1 + "", value);
        } else {
            RadioButton rdNo = (RadioButton) rootView.findViewById(R.id.rd_no);
            rdNo.setVisibility(View.VISIBLE);
        }
        if (current_question >= 12) {
            Policy_Extra_Process();
            return;
        }

        if (mPlayer == null)
            mPlayer = new MediaPlayer();
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
            // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            btn_play_audio.setBackgroundResource(R.drawable.oval_play);
        }
        if (current_question == 0) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_no)
                current_question += 3;
            else
                current_question++;
        } else {
            current_question++;
        }

        mQueueQuestion.add(current_question);
        radioGroup.check(0);
        Integer current_number = current_question + 1;

        if (AppData.getInstance().getLanguage().equals("vn")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_vn);

        } else if (AppData.getInstance().getLanguage().equals("my")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_my);
        } else if (AppData.getInstance().getLanguage().equals("lao")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_lao);
        } else if (AppData.getInstance().getLanguage().equals("km")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_km);
        }
        tv_question_cn.setText(mListQuestion.get(current_question).question_cn);
        setHeaderTitle("公安问卷 - 问题 " + Integer.toString(current_number));
    }

    public void loadNextStep_Government() {
        tv_header.setVisibility(View.GONE);
        if (current_question < 1) {
            RadioButton rdNo = (RadioButton) rootView.findViewById(R.id.rd_no);
            rdNo.setVisibility(View.VISIBLE);
        }

        if (current_question == 3) {
            //store current and play audio
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
                mAnswer.put(current_question + 1 + "", 1);
            } else {
                mAnswer.put(current_question + 1 + "", 0);
            }

            mQueueQuestion.add(current_question);
            current_question++;
            return;
        }

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayer = new MediaPlayer();
            // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
            // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            btn_play_audio.setBackgroundResource(R.drawable.oval_play);
        }

        if (mAnswer == null)
            mAnswer = new HashMap<>();

        if (current_question > 3) {
            MainActivity activity = (MainActivity) getActivity();
            DBHelper db = new DBHelper(activity);
            JSONObject json = new JSONObject(mAnswer);
            db.createAnswer(AppData.getInstance().getPersonID(), json.toString());
            Person person = db.getPerson(AppData.getInstance().getPersonID());
            AppData.getInstance().setPerson_selection(person);
            Pause();
            activity.didSelectMenuItem(new MenuItem("问卷内容", "QuestionnaireDetailMarriage", "detail", 0));
            return;
        }

        if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
            mAnswer.put(current_question + 1 + "", 1);
        } else {
            mAnswer.put(current_question + 1 + "", 0);
        }

        radioGroup.check(0);


        mQueueQuestion.add(current_question);
        current_question++;
        if (AppData.getInstance().getLanguage().equals("vn")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_vn);

        } else if (AppData.getInstance().getLanguage().equals("my")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_my);
        } else if (AppData.getInstance().getLanguage().equals("lao")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_lao);
        } else if (AppData.getInstance().getLanguage().equals("km")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_km);
        }
        tv_question_cn.setText(mListQuestion.get(current_question).question_cn);
        setHeaderTitle("公安问卷 - 问题 " + Integer.toString(current_question+1));

    }

    public void loadNextStep_Government1(int value) {
        Log.e("CURRENT_QUESTION", "current_question " + current_question);
        tv_gov_term_title_cn.setVisibility(View.GONE);
        tv_gov_term_desc_cn.setVisibility(View.GONE);
        tv_gov_term_desc_fo.setVisibility(View.GONE);
        tv_gov_term_title_fo.setVisibility(View.GONE);
        ctrl_space.setVisibility(View.GONE);
        tv_header.setVisibility(View.GONE);
        tv_question_basic_cn.setVisibility(View.GONE);
        tv_question_basic_en.setVisibility(View.GONE);
        if (current_question < 1) {
            RadioButton rdNo = (RadioButton) rootView.findViewById(R.id.rd_no);
            rdNo.setVisibility(View.VISIBLE);
        }

        if (current_question == 3) {
            if ( q13_confirmed  == false ) {
                showHideQ13Confirm(true);
                mAnswer.put(current_question + 1 + "", value);
                mQueueQuestion.add(current_question);
                current_question++;
                q13_confirmed = true;
                return;
            }
        }
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayer = new MediaPlayer();
            btn_play_audio.setBackgroundResource(R.drawable.oval_play);
        }
        if (mAnswer == null)
            mAnswer = new HashMap<>();

        if (current_question <= 4 && current_question >= 0) {
            mAnswer.put(current_question + 1 + "", value);
            mQueueQuestion.add(current_question);
            current_question++;
        } else {
            current_question ++;
        }

        if (current_question > 3) {
            MainActivity activity = (MainActivity) getActivity();
            DBHelper db = new DBHelper(activity);
            JSONObject json = new JSONObject(mAnswer);
            db.createAnswer(AppData.getInstance().getPersonID(), json.toString());
            Person person = db.getPerson(AppData.getInstance().getPersonID());
            AppData.getInstance().setPerson_selection(person);
            Pause();
            activity.didSelectMenuItem(new MenuItem("问卷内容", "QuestionnaireDetailMarriage", "detail", 0));
            return;
        }

        if (AppData.getInstance().getLanguage().equals("vn")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_vn);
        } else if (AppData.getInstance().getLanguage().equals("my")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_my);
        } else if (AppData.getInstance().getLanguage().equals("lao")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_lao);
        } else if (AppData.getInstance().getLanguage().equals("km")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_km);
        }
        tv_question_cn.setText(mListQuestion.get(current_question).question_cn);
        setHeaderTitle("婚姻登记问卷 - 问题 " + Integer.toString(current_question + 1));
    }

    private void Policy_Extra_Process() {
        //check list answer
        Integer current_number = current_question + 1;
        if (current_question == 12) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
                mAnswer.put(current_question + 1 + "", 1);
            } else {
                mAnswer.put(current_question + 1 + "", 0);
            }
            if ( q13_confirmed  == false ) {
                showHideQ13Confirm(true);
                q13_confirmed = true;
                return;
            }

            if ( need_extra_question == false ) {
                current_question = 17;
                Policy_Extra_Process();
                return;
            }

            if ((mAnswer.containsKey("2") && mAnswer.get("2") > -1 && mAnswer.get("6") > -1) || (mAnswer.containsKey("2") && mAnswer.get("6") > -1 && mAnswer.get(7 + "") > -1)
                    || (mAnswer.containsKey("2") && mAnswer.get("2") > -1 && mAnswer.get(8 + "") > -1) || (mAnswer.containsKey("2") && mAnswer.get(2 + "") > -1 && mAnswer.get(9 + "") > -1)
                    || (mAnswer.containsKey("2") && mAnswer.get("2") > -1 && mAnswer.get(10 + "") > -1) || (mAnswer.containsKey("2") && mAnswer.get(2 + "") > -1 && mAnswer.get(11 + "") > -1)
                    || (mAnswer.containsKey("3") && mAnswer.get("3") > -1 && mAnswer.get(6 + "") > -1) || (mAnswer.containsKey("3") && mAnswer.get(3 + "") > -1 && mAnswer.get(7 + "") > -1)
                    || (mAnswer.containsKey("3") && mAnswer.get("3") > -1 && mAnswer.get(8 + "") > -1) || (mAnswer.containsKey("3") && mAnswer.get(3 + "") > -1 && mAnswer.get(9 + "") > -1)
                    || (mAnswer.containsKey("3") && mAnswer.get("3") > -1 && mAnswer.get(10 + "") > -1) || (mAnswer.containsKey("3") && mAnswer.get(3 + "") > -1 && mAnswer.get(11 + "") > -1
                    || mAnswer.get("4") > -1 || mAnswer.get("5") > -1
                    || (mAnswer.get(7 + "") > -1 && mAnswer.get(9 + "") > -1) || (mAnswer.get(7 + "") > -1 && mAnswer.get(10 + "") > -1)
                    || (mAnswer.get(7 + "") > -1 && mAnswer.get(11 + "") > -1) || (mAnswer.get(8 + "") > -1 && mAnswer.get(9 + "") > -1)
                    || (mAnswer.get(8 + "") > -1 && mAnswer.get(10 + "") > -1) || (mAnswer.get(8 + "") > -1 && mAnswer.get(11 + "") > -1)
                    || (mAnswer.get(12 + "") > -1))) {
                current_question++;
            } else
                current_question += 2;

            radioGroup.check(0);
            if (AppData.getInstance().getLanguage().equals("vn")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_vn);
            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_my);
            } else if (AppData.getInstance().getLanguage().equals("lao")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_lao);
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_km);
            }


            tv_question_cn.setText(mListQuestion.get(current_question).question_cn);
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                mPlayer = new MediaPlayer();
            }
            setHeaderTitle("公安问卷 - 问题 " + Integer.toString(current_question + 1));
            mQueueQuestion.add(current_question);
            return;

        } else if (current_question >= 13 && current_question <= 16 && need_extra_question == true) {
            showHideQ13Confirm(false);
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
                mAnswer.put(current_question + 1 + "", 1);
            } else {
                mAnswer.put(current_question + 1 + "", 0);
            }

/*            if ((current_question == 14) && mAnswer.containsKey(13) && mAnswer.get(13 + "") != null)
                current_question++;
            else {
                if (current_question == 14)
                    current_question += 2;
                else
                    current_question++;
            }
*/

            radioGroup.check(0);
            current_question ++;
            if (current_question >= 17) {
                MainActivity activity = (MainActivity) getActivity();
                mAnswer = new TreeMap<String, Integer>(mAnswer);
                JSONObject json = new JSONObject(mAnswer);
                DBHelper db = new DBHelper(activity);
                db.createAnswer(AppData.getInstance().getPersonID(), json.toString());
                //go to detail
                Person person = db.getPerson(AppData.getInstance().getPersonID());
                AppData.getInstance().setPerson_selection(person);

                activity.didSelectMenuItem(new MenuItem("问卷内容", "QuestionnaireDetailFragment", "NewQuestionnaire", 0));
                return;
            }
            if (AppData.getInstance().getLanguage().equals("vn")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_vn);

            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_my);
            } else if (AppData.getInstance().getLanguage().equals("lao")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_lao);
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_km);
            }
            tv_question_cn.setText(mListQuestion.get(current_question).question_cn);

            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                mPlayer = new MediaPlayer();
            }
            setHeaderTitle("公安问卷 - 问题 " + Integer.toString(current_number));
            mQueueQuestion.add(current_question);
            // current_question ++;
            // loadNextStep_Policy1(0);
            return;
        }
        if ( current_question < 17) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
                mAnswer.put(current_question + 1 + "", 1);
            } else {
                mAnswer.put(current_question + 1 + "", 0);
            }
        }
        MainActivity activity = (MainActivity) getActivity();
        mAnswer = new TreeMap<String, Integer>(mAnswer);
        JSONObject json = new JSONObject(mAnswer);
        DBHelper db = new DBHelper(activity);
        db.createAnswer(AppData.getInstance().getPersonID(), json.toString());
        //go to detail
        Person person = db.getPerson(AppData.getInstance().getPersonID());
        AppData.getInstance().setPerson_selection(person);

        activity.didSelectMenuItem(new MenuItem("问卷内容", "QuestionnaireDetailFragment", "NewQuestionnaire", 0));
    }

    private void shareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Interview shared");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void doBackQuestion() {

        int size = mQueueQuestion.size();
        Log.e("Current Question: ", Integer.toString(current_question));
        if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE && current_question >= 13 && need_extra_question == true) {
            return;
        }
        if (size > 1 && current_question > 0) {
            Integer current_number = current_question;
            int question = mQueueQuestion.get(size - 2);
            mAnswer.remove(current_question + "");
            current_question = question;
            mQueueQuestion.remove(size - 1);
            radioGroup.check(0);
            if (AppData.getInstance().getLanguage().equals("vn")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_vn);

            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_my);
            } else if (AppData.getInstance().getLanguage().equals("lao")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_lao);
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_km);
            }
            tv_question_cn.setText(mListQuestion.get(current_question).question_cn);
            if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
                setHeaderTitle("公安问卷 - 问题 " + Integer.toString(current_number==0?1:current_number));
            } else {
                setHeaderTitle("婚姻登记问卷 - 问题 " + Integer.toString(current_number==0?1:current_number));
            }
            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.pause();
                mPlayer = new MediaPlayer();
                btn_play_audio.setBackgroundResource(R.drawable.oval_play);
            }
        } else {
            mQueueQuestion.clear();
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateActionBar(false);
            initQuestion();
            if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
                setHeaderTitle("公安问卷 - 基本询问");
            } else {
                setHeaderTitle("婚姻登记问卷 - 基本询问");
            }
        }



    }
}