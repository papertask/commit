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
    TextView tv_question_foreign, tv_question_cn, tv_question_basic_cn, tv_question_basic_en, tv_header;
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
        tv_question_foreign = (TextView) rootView.findViewById(R.id.tv_question_en);
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
            tv_question_foreign.setTypeface(tf_mm3);
            tv_question_basic_en.setTypeface(tf_mm3);
            tv_question_cn.setTypeface(tf_mm3);
            tv_gov_term_title_fo.setTypeface(tf_mm3);
            tv_gov_term_desc_fo.setTypeface(tf_mm3);
        } else if (AppData.getInstance().getLanguage().equals("my")) {
            /* Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/mm.ttf");
            tv_question_foreign.setTypeface(tf_mm3);
            tv_question_basic_en.setTypeface(tf_mm3);
            tv_question_cn.setTypeface(tf_mm3);
            tv_gov_term_title_fo.setTypeface(tf_mm3);
            tv_gov_term_desc_fo.setTypeface(tf_mm3); */
        } else if (AppData.getInstance().getLanguage().equals("km")) {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/khmerOS.ttf");
            tv_question_foreign.setTypeface(tf_mm3);
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
        return rootView;
    }

    private void initQuestion() {

        tv_question_basic_cn.setVisibility(View.VISIBLE);
        tv_question_basic_en.setVisibility(View.VISIBLE);
        if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
            tv_question_cn.setTextSize(18);
            tv_header.setVisibility(View.GONE);
            tv_question_foreign.setVisibility(View.GONE);
            if (AppData.getInstance().getLanguage().equals("lao")) {
                // tv_question_foreign.setText(getString(R.string.question_policy_lao1));
                // tv_question_basic_en.setText(getString(R.string.question_policy_lao));
                tv_question_basic_cn.setText(getString(R.string.question_policy_cn) + getString(R.string.question_policy_lao) + getString(R.string.question_policy_cn_1) + getString(R.string.question_policy_lao_1) + getString(R.string.question_policy_cn_2) + getString(R.string.question_policy_lao_2));
                tv_question_cn.setText(getString(R.string.question_policy_cn1)  + getString(R.string.question_policy_lao1));
            } else if (AppData.getInstance().getLanguage().equals("vn")) {
                // tv_question_foreign.setText(getString(R.string.question_policy_vn1));
                // tv_question_basic_en.setText(getString(R.string.question_policy_vn));
                tv_question_basic_cn.setText(getString(R.string.question_policy_cn) + getString(R.string.question_policy_vn) + getString(R.string.question_policy_cn_1) + getString(R.string.question_policy_vn_1) + getString(R.string.question_policy_cn_2) + getString(R.string.question_policy_vn_2));
                tv_question_cn.setText(getString(R.string.question_policy_cn1)  + getString(R.string.question_policy_vn1));
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                //tv_question_foreign.setText(getString(R.string.question_policy_km1));
                //tv_question_basic_en.setText(getString(R.string.question_policy_km));
                tv_question_basic_cn.setText(getString(R.string.question_policy_cn) + getString(R.string.question_policy_km) + getString(R.string.question_policy_cn_1) + getString(R.string.question_policy_km_1) + getString(R.string.question_policy_cn_2) + getString(R.string.question_policy_km_2));
                tv_question_cn.setText(getString(R.string.question_policy_cn1)  + getString(R.string.question_policy_km1));
            } else if (AppData.getInstance().getLanguage().equals("my")) {
                // tv_question_foreign.setText(getString(R.string.question_policy_my1));
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

            tv_question_basic_en.setText(getString(R.string.quetion_gov_cn));
            tv_gov_term_title_cn.setText(getResources().getString(R.string.title_gov_term));

            if (AppData.getInstance().getLanguage().equals("lao")) {
                tv_question_foreign.setText(R.string.quetion_gov_lao);
            } else if (AppData.getInstance().getLanguage().equals("vn")) {
                tv_question_foreign.setText(getString(R.string.quetion_gov_vn));
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                tv_question_foreign.setText(getResources().getString(R.string.quetion_gov_km));
            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tv_question_foreign.setText(getResources().getString(R.string.quetion_gov_my));
            }
            tv_gov_term_title_cn.setVisibility(View.VISIBLE);
            tv_gov_term_desc_cn.setVisibility(View.GONE);
            tv_gov_term_desc_fo.setVisibility(View.GONE);
            tv_gov_term_title_fo.setVisibility(View.GONE);
            ctrl_space.setVisibility(View.VISIBLE);
            tv_header.setVisibility(View.VISIBLE);
            tv_question_cn.setVisibility(View.GONE);
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
        if (mQueueQuestion.size() == 0) {
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
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
                        checkNextPolicyQuestion(1);
                    } else {
                        checkGovernmentQuestion(1);
                    }
                    break;
                case R.id.ln_no:
                case R.id.ln_no_ma:
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                        checkNextPolicyQuestion(0);
                    else
                        checkGovernmentQuestion(0);

                    break;
                case R.id.ln_skip:
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                        checkNextPolicyQuestion(0);
                    else
                        checkGovernmentQuestion(0);

                    break;
                case R.id.next:
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                        checkNextPolicyQuestion(-1);
                    else
                        checkGovernmentQuestion(-1);
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
                        current_question = -1;
                        if (mQueueQuestion != null && mQueueQuestion.size() > 0)
                            current_question = mQueueQuestion.size();
                        if (current_question > -1)
                            file_name = String.format("Sound/%s/%s/%d.mp3", apptype, AppData.getInstance().getLanguage(), current_question);//1
                        else
                            file_name = String.format("Sound/%s/%s/%s.mp3", apptype, AppData.getInstance().getLanguage(), "introduction");
                        Play(file_name);
                    }
                    break;
                case R.id.btn_share_intent:

                    break;
                case R.id.btn_13_yes: // need to continue
                    // Confirm Yes after Question 13
                    need_extra_question = true;
                    showHideQ13Confirm(false);
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
                        int next_can = nextQuestionFrom13();
                        if (next_can == 0 ) {
                            storeToResult();
                        } else {
                            loadQuestion(next_can);
                        }
                    } else {
                        storeToResult();
                    }
                    break;
                case R.id.btn_13_no:
                    // Confirm No after Question 13
                    need_extra_question = false;
                    showHideQ13Confirm(false);
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
                        loadQuestion(13);
                    } else {
                        loadQuestion(4);
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

    public void doBackQuestion() {

        int size = mQueueQuestion.size();

        if (size > 1 ) {
            int int_last = mQueueQuestion.get(mQueueQuestion.size()-1);
            if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE && int_last == 14 && need_extra_question == true) {
                return;
            }
            mAnswer.remove(int_last + "");
            mQueueQuestion.remove(mQueueQuestion.size()-1);
            loadQuestion(mQueueQuestion.get(mQueueQuestion.size()-1));
            q13_confirmed = false;
            if (int_last <= 13) {
                need_extra_question = false;
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

    public int nextQuestionFrom13() {
        if ((c_a(2) && c_a(6)) || (c_a(2) && c_a(7)) || (c_a(2) && c_a(8)) || (c_a(2) && c_a(9)) ||
                (c_a(2) && c_a(10)) || (c_a(2) && c_a(11)) || (c_a(3) && c_a(6)) || (c_a(3) && c_a(7)) ||
                (c_a(3) && c_a(8)) || (c_a(3) && c_a(9)) || (c_a(3) && c_a(10)) || (c_a(3) && c_a(11)) ||
                (c_a(4)) || (c_a(5)) || (c_a(7) && c_a(9)) || (c_a(7) && c_a(10)) || (c_a(7) && c_a(11)) ||
                (c_a(8) && c_a(9)) || (c_a(8) && c_a(10)) || (c_a(8) && c_a(11)) || (c_a(12))) {
            return 14;
        }
        if ( c_a(13)) {
            return 16;
        }

        if ( (c_a(4) || c_a(5) || c_a(7) || c_a(8))) {
            return 17;
        }

        return 0;
    }

    public void checkNextPolicyQuestion(int int_flag) {
        if (mQueueQuestion.size() > 0) {
            int int_last = mQueueQuestion.get(mQueueQuestion.size() - 1);
            if ( mAnswer == null )
                mAnswer = new HashMap<>();
            if (int_flag >= 0)
                mAnswer.put((int_last)+"", int_flag);
            if (int_last == 1 && int_flag == 0 ) {  // need to skip q2 and q3
                loadQuestion(4);
            } else if (int_last >= 13) { // need to check algorithm
                if ( int_last  == 13 ) {
                    showHideQ13Confirm(true);
                    q13_confirmed = true;
                    return;
                }

                if ( int_last >= 13 && int_last < 15 ) {
                    if ((c_a(2) && c_a(6)) || (c_a(2) && c_a(7)) || (c_a(2) && c_a(8)) || (c_a(2) && c_a(9)) ||
                            (c_a(2) && c_a(10)) || (c_a(2) && c_a(11)) || (c_a(3) && c_a(6)) || (c_a(3) && c_a(7)) ||
                            (c_a(3) && c_a(8)) || (c_a(3) && c_a(9)) || (c_a(3) && c_a(10)) || (c_a(3) && c_a(11)) ||
                            (c_a(4)) || (c_a(5)) || (c_a(7) && c_a(9)) || (c_a(7) && c_a(10)) || (c_a(7) && c_a(11)) ||
                            (c_a(8) && c_a(9)) || (c_a(8) && c_a(10)) || (c_a(8) && c_a(11)) || (c_a(12))) {
                        int_last++;
                        loadQuestion(int_last);
                        return;
                    }
                } else {
                    if ( c_a(13) && int_last < 16) {
                        loadQuestion(16);
                        return;
                    }

                    if ( (c_a(4) || c_a(5) || c_a(7) || c_a(8)) && int_last < 17 ) {
                        loadQuestion(17);
                        return;
                    }

                    storeToResult();
                }
            } else {
                int_last ++;
                loadQuestion(int_last);
            }
        } else {
            tv_question_basic_en.setText("");
            tv_question_basic_cn.setVisibility(View.GONE);
            tv_header.setVisibility(View.GONE);
            // question start
            loadQuestion(1);
        }
    }

    public void checkGovernmentQuestion(int int_flag) {
        if (mQueueQuestion.size() > 0) {
            int int_last = mQueueQuestion.get(mQueueQuestion.size() - 1);
            if ( mAnswer == null )
                mAnswer = new HashMap<>();
            if (int_flag >= 0)
                mAnswer.put((int_last)+"", int_flag);
            if( int_last < 4 ) {
                int_last ++;
                loadQuestion(int_last);
                return;
            }

            if (int_last == 4) {
                showHideQ13Confirm(true);
                return;
            }

            storeToResult();
        } else {
            tv_question_basic_en.setText("");
            tv_question_basic_cn.setVisibility(View.GONE);
            tv_header.setVisibility(View.GONE);
            tv_gov_term_title_cn.setVisibility(View.GONE);
            tv_gov_term_desc_cn.setVisibility(View.GONE);
            tv_gov_term_desc_fo.setVisibility(View.GONE);
            ctrl_space.setVisibility(View.GONE);

            // question start
            loadQuestion(1);
        }
    }

    public void storeToResult() {
        MainActivity activity = (MainActivity) getActivity();
        if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
            if (mQueueQuestion.size() <13) return;
        } else {
            if (mQueueQuestion.size() < 4 ) return;
        }
        mAnswer = new TreeMap<String, Integer>(mAnswer);
        JSONObject json = new JSONObject(mAnswer);
        DBHelper db = new DBHelper(activity);
        db.createAnswer(AppData.getInstance().getPersonID(), json.toString());
        //go to detail
        Person person = db.getPerson(AppData.getInstance().getPersonID());
        AppData.getInstance().setPerson_selection(person);

        if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
            activity.didSelectMenuItem(new MenuItem("答卷详情", "QuestionnaireDetailFragment", "detail", 0));
        } else {
            activity.didSelectMenuItem(new MenuItem("答卷详情", "QuestionnaireDetailMarriage", "detail", 0));
        }
    }

    public void addPositionToQueue(int int_position) {
        for (int i = 0; i < mQueueQuestion.size(); i ++) {
            if ((int)(mQueueQuestion.get(i)) == int_position) {
                mQueueQuestion.remove(i);
                break;
            }
        }
        mQueueQuestion.add(int_position);
    }

    public void loadQuestion(int int_position) {
        addPositionToQueue(int_position);
        checkBackAction(int_position);
        showYesNoButton(true);
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayer = new MediaPlayer();
            btn_play_audio.setBackgroundResource(R.drawable.oval_play);
        }
        tv_question_cn.setVisibility(View.VISIBLE);
        tv_question_foreign.setVisibility(View.VISIBLE);
        if (AppData.getInstance().getLanguage().equals("vn")) {
            tv_question_foreign.setText(mListQuestion.get(int_position - 1).question_vn);
        } else if (AppData.getInstance().getLanguage().equals("my")) {
            tv_question_foreign.setText(mListQuestion.get(int_position - 1).question_my);
        } else if (AppData.getInstance().getLanguage().equals("lao")) {
            tv_question_foreign.setText(mListQuestion.get(int_position - 1).question_lao);
        } else if (AppData.getInstance().getLanguage().equals("km")) {
            tv_question_foreign.setText(mListQuestion.get(int_position - 1).question_km);
        }
        tv_question_cn.setText(mListQuestion.get(int_position - 1).question_cn);

        if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE) {
            setHeaderTitle("公安问卷 - 问题 " + Integer.toString(int_position));
        } else {
            setHeaderTitle("婚姻登记问卷 - 问题 " + Integer.toString(int_position));
        }
    }

    public void checkBackAction( int int_position ) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mQueueQuestion.size() >= 0 && int_position >= 1) {
            mainActivity.updateActionBar(true);
        } else {
            mainActivity.updateActionBar(false);
        }
    }

    public boolean c_a( int int_position ) {
        if (mAnswer.containsKey(int_position+"")) {
            return mAnswer.get(int_position+"")*1==1;
        } else {
            return false;
        }
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

}