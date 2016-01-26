package com.interview.iso.fragments;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 */
public class QuestionFragment extends BaseFragment {
    private int radio_id;
    RadioGroup radioGroup;

    Map<Integer, Question> mListQuestion;
    int current_question = 0;
    TextView tv_question_en, tv_question_cn,tv_question_basic_cn,tv_question_basic_en,tv_header;
    Button btn_play_audio;
    public static MediaPlayer mPlayer;
    Map<String, Boolean> mAnswer;
    List<Integer> mQueueQuestion;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_question, container, false);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.answer_radio_group);
        //Button btn_fullScreen = (Button) rootView.findViewById(R.id.btn_zoom);
        Button btn_share = (Button) rootView.findViewById(R.id.btn_share_intent);
        btn_play_audio = (Button) rootView.findViewById(R.id.btn_play_audio);
        Button btn_next = (Button) rootView.findViewById(R.id.next);
        tv_question_cn = (TextView) rootView.findViewById(R.id.tv_question_cn);
        tv_question_en = (TextView) rootView.findViewById(R.id.tv_question_en);
        tv_question_basic_cn = (TextView) rootView.findViewById(R.id.basic_query_cn);
        tv_question_basic_en = (TextView) rootView.findViewById(R.id.basic_query_en);
        tv_header = (TextView)rootView.findViewById(R.id.header);
        if(AppData.getInstance().getLanguage().equals("lao"))
        {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lao.ttf");
            tv_question_en.setTypeface(tf_mm3);
            tv_question_basic_en.setTypeface(tf_mm3);
            tv_question_cn.setTypeface(tf_mm3);
        }else if (AppData.getInstance().getLanguage().equals("my"))
        {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/mm.ttf");
            tv_question_en.setTypeface(tf_mm3);
            tv_question_basic_en.setTypeface(tf_mm3);
            tv_question_cn.setTypeface(tf_mm3);
        }else if (AppData.getInstance().getLanguage().equals("km")){
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/khmerOS.ttf");
            tv_question_en.setTypeface(tf_mm3);
            tv_question_basic_en.setTypeface(tf_mm3);
            tv_question_cn.setTypeface(tf_mm3);
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
        if(AppData.getInstance().getApptype()==Constants.POLICY_TYPE) {
            if (AppData.getInstance().getLanguage().equals("lao")) {
                tv_question_en.setText(getString(R.string.question_policy_lao1));
                tv_question_basic_en.setText(getString(R.string.question_policy_lao));
            } else if (AppData.getInstance().getLanguage().equals("vn")) {
                tv_question_en.setText(getString(R.string.question_policy_vn1));
                tv_question_basic_en.setText(getString(R.string.question_policy_vn));
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                tv_question_en.setText(getString(R.string.question_policy_km1));
                tv_question_basic_en.setText(getString(R.string.question_policy_km));
            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tv_question_en.setText(getString(R.string.question_policy_my1));
                tv_question_basic_en.setText(getString(R.string.question_policy_my));
            }

            tv_question_basic_cn.setText(getString(R.string.question_policy_cn));
            tv_question_cn.setText(getString(R.string.question_policy_cn1));
        }else {
            tv_header.setVisibility(View.VISIBLE);
            if (AppData.getInstance().getLanguage().equals("lao")) {
                tv_question_en.setText(getString(R.string.quetion_gov_lao));
                tv_question_basic_en.setText(getString(R.string.quetion_gov_cn1));
                tv_question_basic_cn.setText(getString(R.string.quetion_gov_cn));
                tv_question_cn.setText(getString(R.string.quetion_gov_lao1));
            } else if (AppData.getInstance().getLanguage().equals("vn")) {

                tv_question_basic_cn.setText(getString(R.string.quetion_gov_cn));
                tv_question_cn.setText(getString(R.string.quetion_gov_vn));
                tv_question_en.setText(getString(R.string.quetion_gov_vn1));
                tv_question_basic_en.setText(getString(R.string.quetion_gov_cn1));
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                tv_question_en.setText(getString(R.string.quetion_gov_km));
                tv_question_basic_en.setText(getString(R.string.quetion_gov_cn1));
                tv_question_basic_cn.setText(getString(R.string.quetion_gov_cn));
                tv_question_cn.setText(getString(R.string.quetion_gov_km1));
            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tv_question_en.setText(getString(R.string.quetion_gov_my));
                tv_question_basic_en.setText(getString(R.string.quetion_gov_cn1));
                tv_question_basic_cn.setText(getString(R.string.quetion_gov_cn));
                tv_question_cn.setText(getString(R.string.quetion_gov_my1));
            }


        }
        RadioButton rdNo = (RadioButton) rootView.findViewById(R.id.rd_no);

        RadioButton rdYes = (RadioButton) rootView.findViewById(R.id.rd_yes);
        rdYes.setChecked(true);
        rdYes.setTextSize(10f);
        if(AppData.getInstance().getLanguage().equals("vn"))
        {
            rdYes.setText("是(Có)");
            rdNo.setText("不是(Không)");
        }
        else if(AppData.getInstance().getLanguage().equals("lao"))
        {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/lao.ttf");
            rdNo.setTypeface(tf_mm3);
            rdYes.setTypeface(tf_mm3);
            rdYes.setText("是(ແມ່ນ)");
            rdNo.setText("不是(ບໍ່ແມ່ນ)");
        }else if (AppData.getInstance().getLanguage().equals("my"))
        {
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/mm.ttf");
            rdNo.setTypeface(tf_mm3);
            rdYes.setTypeface(tf_mm3);
            rdYes.setText("是(ဟုတ္ပါသည္ ။)");
            rdNo.setText("不是(မဟုတ္ပါ။)");
        }else if (AppData.getInstance().getLanguage().equals("km")){
            Typeface tf_mm3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/khmerOS.ttf");
            rdNo.setTypeface(tf_mm3);
            rdYes.setTypeface(tf_mm3);
            rdYes.setText("是(យល់ព្រម)");
            rdNo.setText("不是(គ្មាន)");
        }
        rdNo.setVisibility(View.GONE);
        current_question = -1;
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.updateActionBar(false);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.next:
                    if(current_question>=0){
                        MainActivity mainActivity = (MainActivity)getActivity();
                        mainActivity.updateActionBar(true);
                    }
                    if(mPlayer!=null && mPlayer.isPlaying()) {
                        mPlayer.reset();
                        Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                        btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    }
                    if (radioGroup.getCheckedRadioButtonId() == 0) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                        builder1.setMessage("您必须选择“是”或者“否”。");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                        return;
                    }
                    if (AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                        loadNextStep_Policy();
                    else
                        loadNextStep_Government();
                    break;
                case R.id.btn_play_audio:
                    if(mPlayer==null)
                        mPlayer = new MediaPlayer();
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                        mPlayer = new MediaPlayer();
                        Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                        btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    } else {
                        Drawable img = getContext().getResources().getDrawable(R.drawable.icon_pause);
                        btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                        String apptype = "";
                        if(AppData.getInstance().getApptype() == Constants.POLICY_TYPE)
                            apptype = "policy";
                        else
                            apptype = "government";
                        String file_name = "";
                        if(current_question>-1)
                            file_name = String.format("Sound/%s/%s/%d.mp3",apptype,AppData.getInstance().getLanguage(), current_question+1);//1
                        else
                            file_name = String.format("Sound/%s/%s/%s.mp3",apptype,AppData.getInstance().getLanguage(), "introduction");
                        Play(file_name);
                    }
                    break;
                case R.id.btn_share_intent:
                    shareIntent();
                    break;
                default:
                    break;
            }
        }
    };

    public void Play(String fileName) {

        try {
            if (mPlayer==null)
                mPlayer= new MediaPlayer();
            mPlayer.reset();
            AssetFileDescriptor descriptor = getActivity().getAssets().openFd(fileName);
            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();
            if(start == end)
            {
                Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                return;
            }
            //update paused

            mPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
            mPlayer.prepare();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                    btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                }
            });
            mPlayer.start();

        } catch (Exception ex) {
            Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
            btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            if(mPlayer!=null && mPlayer.isPlaying())
                mPlayer.pause();
        }
    }
    public void Pause(){
        if(mPlayer!=null && mPlayer.isPlaying())
            mPlayer.pause();
    }


    private boolean isFirstYes = false;

    public void loadNextStep_Policy() {
        tv_question_basic_en.setText("");
        tv_question_basic_cn.setVisibility(View.GONE);
        if (mAnswer == null)
            mAnswer = new HashMap<>();
        if (current_question > -1) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes)
                mAnswer.put(current_question + 1 + "", true);
            else
                mAnswer.put(current_question + 1 + "", false);
        }else {
            RadioButton rdNo = (RadioButton) rootView.findViewById(R.id.rd_no);
            rdNo.setVisibility(View.VISIBLE);
        }
        if (current_question >= 12) {
            Policy_Extra_Process();
            return;
        }
        if(mPlayer==null)
            mPlayer = new MediaPlayer();
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
            btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
        if (current_question == 0) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_no)
                current_question += 3;
            else
                current_question++;
        } else {
            ++current_question;
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
        }else if (AppData.getInstance().getLanguage().equals("km")){
            tv_question_en.setText(mListQuestion.get(current_question).question_km);
        }
        tv_question_cn.setText(current_number + ". " + mListQuestion.get(current_question).question_cn);
    }

    public void loadNextStep_Government() {
        tv_header.setVisibility(View.GONE);
        if(current_question<1){
            RadioButton rdNo = (RadioButton) rootView.findViewById(R.id.rd_no);
            rdNo.setVisibility(View.VISIBLE);
        }
        if (current_question > 3) {
            MainActivity activity = (MainActivity) getActivity();
            DBHelper db = new DBHelper(activity);
            Person person = db.getPerson(AppData.getInstance().getPersonID());
            AppData.getInstance().setPerson_selection(person);
            Pause();
            activity.didSelectMenuItem(new MenuItem("问卷内容", 0, "QuestionnaireDetailMarriage", "detail"));
            return;
        }
        if (current_question == 3) {
            //store current and play audio
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
                {
                    mAnswer.put(current_question + 1 + "", true);
                }
            } else {
                mAnswer.put(current_question + 1 + "", false);
            }
            MainActivity activity = (MainActivity) getActivity();
            mAnswer = new TreeMap<String, Boolean>(mAnswer);
            JSONObject json = new JSONObject(mAnswer);
            DBHelper db = new DBHelper(activity);
            db.createAnswer(AppData.getInstance().getPersonID(), json.toString());
            String filename = String.format("Sound/government/%s/%d.mp3",AppData.getInstance().getLanguage(),current_question);
            Play(filename);
            //Drawable img = getContext().getResources().getDrawable(R.drawable.icon_pause);
            // btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            current_question++;
            mQueueQuestion.add(current_question);
            loadNextStep_Government();
            return;
        }
        if (mPlayer!=null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayer = new MediaPlayer();
            Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
            btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
        if (mAnswer == null)
            mAnswer = new HashMap<>();
        if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
            {
                mAnswer.put(current_question + 1 + "", true);
            }
        } else {
            mAnswer.put(current_question + 1 + "", false);
        }
        radioGroup.check(0);
        current_question++;
        mQueueQuestion.add(current_question);
        if (AppData.getInstance().getLanguage().equals("vn")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_vn);

        } else if (AppData.getInstance().getLanguage().equals("my")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_my);
        } else if (AppData.getInstance().getLanguage().equals("lao")) {
            tv_question_en.setText(mListQuestion.get(current_question).question_lao);
        }else if (AppData.getInstance().getLanguage().equals("km")){
            tv_question_en.setText(mListQuestion.get(current_question).question_km);
        }
        tv_question_cn.setText(mListQuestion.get(current_question).question_cn);

    }

    private void Policy_Extra_Process() {
        //check list answer
        Integer current_number = current_question + 1;
        if (current_question == 12) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
                mAnswer.put(current_question + 1 + "", true);
            } else {
                mAnswer.put(current_question + 1 + "", false);
            }
            if ((mAnswer.containsKey("2")&& mAnswer.get("2") && mAnswer.get("6")) || (mAnswer.containsKey("2")&&mAnswer.get("6") && mAnswer.get(7 + ""))
                    || (mAnswer.containsKey("2")&&mAnswer.get("2") && mAnswer.get(8 + "")) || (mAnswer.containsKey("2")&&mAnswer.get(2 + "") && mAnswer.get(9 + ""))
                    || (mAnswer.containsKey("2")&&mAnswer.get("2") && mAnswer.get(10 + "")) || (mAnswer.containsKey("2")&&mAnswer.get(2 + "") && mAnswer.get(11 + ""))
                    || (mAnswer.containsKey("3")&&mAnswer.get("3") && mAnswer.get(6 + "")) || (mAnswer.containsKey("3")&&mAnswer.get(3 + "") && mAnswer.get(7 + ""))
                    || (mAnswer.containsKey("3")&&mAnswer.get("3") && mAnswer.get(8 + "")) || (mAnswer.containsKey("3")&&mAnswer.get(3 + "") && mAnswer.get(9 + ""))
                    || (mAnswer.containsKey("3")&&mAnswer.get("3") && mAnswer.get(10 + "")) || (mAnswer.containsKey("3")&&mAnswer.get(3 + "") && mAnswer.get(11 + "")
                    || mAnswer.get("4") || mAnswer.get("5")
                    || (mAnswer.get(7 + "") && mAnswer.get(9 + "")) || (mAnswer.get(7 + "") && mAnswer.get(10 + ""))
                    || (mAnswer.get(7 + "") && mAnswer.get(11 + "")) || (mAnswer.get(8 + "") && mAnswer.get(9 + ""))
                    || (mAnswer.get(8 + "") && mAnswer.get(10 + "")) || (mAnswer.get(8 + "") && mAnswer.get(11 + ""))
                    || (mAnswer.get(12 + "")))) {
                current_question++;
            }else
                current_question+=2;

            radioGroup.check(0);
            if (AppData.getInstance().getLanguage().equals("vn")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_vn);

            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_my);
            } else if (AppData.getInstance().getLanguage().equals("lao")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_lao);
            }else if (AppData.getInstance().getLanguage().equals("km")){
                tv_question_en.setText(mListQuestion.get(current_question).question_km);
            }


            tv_question_cn.setText(current_number + ". " + mListQuestion.get(current_question).question_cn);
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                mPlayer = new MediaPlayer();
            }

            mQueueQuestion.add(current_question);
            return;

        } else if (current_question >= 13 && current_question <16) {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
                mAnswer.put(current_question + 1 + "", true);
            } else {
                mAnswer.put(current_question + 1 + "", false);
            }

            if (current_question==14 && mAnswer.containsKey(13+"")&& mAnswer.get(13+""))
                current_question++;
            else {
                if (current_question == 14)
                     current_question+=2;
                else
                    current_question++;
            }


            radioGroup.check(0);
            if (AppData.getInstance().getLanguage().equals("vn")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_vn);

            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_my);
            } else if (AppData.getInstance().getLanguage().equals("lao")) {
                tv_question_en.setText(mListQuestion.get(current_question).question_lao);
            }else if (AppData.getInstance().getLanguage().equals("km")){
                tv_question_en.setText(mListQuestion.get(current_question).question_km);
            }
            tv_question_cn.setText(current_number + ". " + mListQuestion.get(current_question).question_cn);

            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                mPlayer = new MediaPlayer();
            }
            mQueueQuestion.add(current_question);
            return;
        }
        if (radioGroup.getCheckedRadioButtonId() == R.id.rd_yes) {
            mAnswer.put(current_question + 1 + "", true);
        } else {
            mAnswer.put(current_question + 1 + "", false);
        }
        MainActivity activity = (MainActivity) getActivity();
        mAnswer = new TreeMap<String, Boolean>(mAnswer);
        JSONObject json = new JSONObject(mAnswer);
        DBHelper db = new DBHelper(activity);
        db.createAnswer(AppData.getInstance().getPersonID(), json.toString());
        //go to detail
        Person person = db.getPerson(AppData.getInstance().getPersonID());
        AppData.getInstance().setPerson_selection(person);

        activity.didSelectMenuItem(new MenuItem("问卷内容", 0, "QuestionnaireDetailFragment", "NewQuestionnaire"));
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
        if (size > 1) {
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
            }else if (AppData.getInstance().getLanguage().equals("km")){
                tv_question_en.setText(mListQuestion.get(current_question).question_km);
            }
            tv_question_cn.setText(current_number + ". " + mListQuestion.get(current_question).question_cn);

            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                mPlayer = new MediaPlayer();
            }
        }else {
            MainActivity mainActivity = (MainActivity)getActivity();
            mainActivity.updateActionBar(false);
        }

    }
}