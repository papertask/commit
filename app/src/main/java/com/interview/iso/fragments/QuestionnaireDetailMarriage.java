package com.interview.iso.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.base.MenuItem;
import com.interview.iso.models.Answer;
import com.interview.iso.models.Person;
import com.interview.iso.models.Question;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.DBHelper;
import com.interview.iso.view.ExpandableHeightListView;
import com.joooonho.SelectableRoundedImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;

/**
 * Created by lu.nguyenvan2 on 11/11/2015.
 */
public class QuestionnaireDetailMarriage extends Fragment {

    private List<ResultQuestion> mList;
    private ExpandableHeightListView mListView;
    SelectableRoundedImageView rdAvatar;
    TextView ctrlTxtLang;
    Map<String, Integer> mAnswer=null;
    private String str_share = "";
    private  int number;

    private IWXAPI api;

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questionaire_detail_marrige, container, false);
        mListView = (ExpandableHeightListView) rootView.findViewById(R.id.list_questionnaire);
        rdAvatar = (SelectableRoundedImageView) rootView.findViewById(R.id.image_avatar);
        TextView tvInterviewDate = (TextView)rootView.findViewById(R.id.tv_interviewDate);
        TextView tvExtraText = (TextView)rootView.findViewById(R.id.extra_information);
        TextView tvLocation = (TextView)rootView.findViewById(R.id.tv_location);
        TextView tvName = (TextView)rootView.findViewById(R.id.tv_name);
        TextView lblResult = (TextView) rootView.findViewById(R.id.lbl_result);
        TextView tvResultCn = (TextView) rootView.findViewById(R.id.tv_result_cn);
        TextView tvResult = (TextView) rootView.findViewById(R.id.tv_result);
        TextView lblResult_1 = (TextView) rootView.findViewById(R.id.lbl_result_1);
        TextView tvResultCn_1 = (TextView) rootView.findViewById(R.id.tv_result_cn_1);
        TextView tvResult_1 = (TextView) rootView.findViewById(R.id.tv_result_1);
        ImageView imgPlayAnounce = (ImageView) rootView.findViewById(R.id.img_play_anounce);
        Button btnDelete = (Button) rootView.findViewById(R.id.btn_qdetail_ma_delete);
        Button btnShare = (Button) rootView.findViewById(R.id.btn_qdetail_ma_share);
        ctrlTxtLang = (TextView) rootView.findViewById(R.id.tv_gender);
        mAnswer = new HashMap<>();

        api = AppData.getInstance().getWeChatAPI();

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = str_share;
                WXTextObject textObj = new WXTextObject();
                textObj.text = text;
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObj;
                msg.description = text;
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("text");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                api.sendReq(req);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb =	 new AlertDialog.Builder((Activity) v.getContext());
                adb.setTitle("删除");
                adb.setMessage("确定删除这个问卷？");
                //final int positionToRemove = position;
                adb.setNegativeButton("取消", null);
                adb.setPositiveButton("确定", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Person person = AppData.getInstance().getPerson_selection();
                        MainActivity activity = (MainActivity) getActivity();
                        DBHelper db = new DBHelper(activity);
                        db.deletePerson(person.getnID());
                        activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_question_list), "ListNameFragment", "list_interviewer", 0));
                    }
                });
                adb.show();
            }

        });

        Button btnGotoTranslate = (Button) rootView.findViewById(R.id.btn_marrage_translate);
        Button btnGotoSection = (Button) rootView.findViewById(R.id.btn_marrage_section);
        Button btnGotoEmbassy = (Button) rootView.findViewById(R.id.btn_marrage_embassy);

        btnGotoEmbassy.setOnClickListener(gotoButtonClickListener);
        btnGotoSection.setOnClickListener(gotoButtonClickListener);
        btnGotoTranslate.setOnClickListener(gotoButtonClickListener);

        imgPlayAnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView This = (ImageView) v;
                if(QuestionFragment.mPlayer==null)
                    QuestionFragment.mPlayer = new MediaPlayer();
                if (QuestionFragment.mPlayer.isPlaying()) {
                    QuestionFragment.mPlayer.pause();
                    QuestionFragment.mPlayer = new MediaPlayer();
                    // Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                    //btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                    This.setImageDrawable(getResources().getDrawable(R.drawable.oval_play));
                } else {
                    String file_name;
                    if (mAnswer.get("2")*1==1 && mAnswer.get("3")*1 == 0 && mAnswer.get("4")*1==0) {
                        file_name = String.format("Sound/%s/%s/r1.mp3", "government", AppData.getInstance().getLanguage());//1
                    } else {
                        file_name = String.format("Sound/%s/%s/r2.mp3", "government", AppData.getInstance().getLanguage());//1
                    }
                    Play(file_name);
                    This.setImageDrawable(getResources().getDrawable(R.drawable.button_pause));
                }
            }
        });

        Person person = AppData.getInstance().getPerson_selection();
        if(person.getAvatarPath()!=null && !person.getAvatarPath().equals(""))
            setFullImageFromFilePath(person.getAvatarPath(), rdAvatar);

        str_share += "姓名 : " + person.getStrFirstName()+" "+person.getStrLastName() + "\n";
        str_share += "电话 : " + person.getStrTelphone() + "\n";
        str_share += "时间 : " + person.getStrInterviewDate() + "\n";
        str_share += "地址 : " + person.getStrPosition() + "\n";

        if (person.getLang().equals("vn")) {
            ctrlTxtLang.setText("语言 : 越南");
            str_share += "语言 : 越南" + "\n";
        } else if (person.getLang().equals("km")) {
            ctrlTxtLang.setText("语言 : 柬埔寨");
            str_share += "语言 : 柬埔寨" + "\n";
        } else if (person.getLang().equals("lao")) {
            ctrlTxtLang.setText("语言 : 老挝");
            str_share += "语言 : 老挝" + "\n";
        } else if (person.getLang().equals("my")) {
            ctrlTxtLang.setText("语言 : 缅甸");
            str_share += "语言 : 缅甸" + "\n";
        }

        tvInterviewDate.setText("时间 : " + person.getStrInterviewDate());
        tvName.setText(person.getStrFirstName()+" "+person.getStrLastName());
        tvLocation.setText("地址 : " + person.getStrPosition());

        DBHelper db = new DBHelper(getActivity());
        Answer answer = db.getListQuestionByPersion(person.getnID());
        AppData.getInstance().setLanguage(person.getLang());
        number = 0;

        if(answer!= null) {
            JSONObject object = answer.convertToJsonArray();
//        Map<String , Boolean> mResult;
            if (object != null) {
                Iterator<String> key = object.keys();
                mList = new ArrayList<>();
                while (key.hasNext()) {
                    try {
                        ResultQuestion resultQuestion = new ResultQuestion();
                        resultQuestion.id = key.next();
                        resultQuestion.result = object.get(resultQuestion.id) == 1 ? true : false;

                        if (resultQuestion.id.equals("0") == false) {
                            mList.add(resultQuestion);
                            mAnswer.put(resultQuestion.id, (int)(object.get(resultQuestion.id)));
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }

        str_share += "\n结果建议\n\n";

        if(mAnswer.get("2")*1==1 && mAnswer.get("3")*1 == 0 && mAnswer.get("4")*1==0){
            //tvExtraText.setText(getString(R.string.quetion_gov_cn));
            lblResult.setText("同意办证：");
            str_share += "同意办证: \n\n";
            str_share += getString(R.string.marriage_res_approve_cn) + "\n\n";
            tvResultCn.setText(getString(R.string.marriage_res_approve_cn));
            if (AppData.getInstance().getLanguage().equals("vn")) {
                tvResult.setText(getString(R.string.marriage_res_approve_vn));
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                tvResult.setText(getString(R.string.marriage_res_approve_km));
            } else if (AppData.getInstance().getLanguage().equals("lao")) {
                tvResult.setText(getString(R.string.marriage_res_approve_lao));
            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tvResult.setText(getString(R.string.marriage_res_approve_my));
            } else {
                tvResult.setText("");
            }
        } else  {//tvExtraText.setText(getString(R.string.quetion_gov_cn));

            lblResult.setText("疑似拐卖: ");
            str_share += "疑似拐卖: \n\n";
            str_share += getString(R.string.marriage_res_potential_cn) + "\n\n";
            tvResultCn.setText(getString(R.string.marriage_res_potential_cn));
            if (AppData.getInstance().getLanguage().equals("vn")) {
                tvResult.setText(getString(R.string.marriage_res_potential_vn));
            } else if (AppData.getInstance().getLanguage().equals("km")) {
                tvResult.setText(getString(R.string.marriage_res_potential_km));
            } else if (AppData.getInstance().getLanguage().equals("lao")) {
                tvResult.setText(getString(R.string.marriage_res_potential_lao));
            } else if (AppData.getInstance().getLanguage().equals("my")) {
                tvResult.setText(getString(R.string.marriage_res_potential_my));
            } else {
                tvResult.setText("");
            }
        }

        str_share += "\n问卷\n\n";

        Map<Integer,Question> mListQuest;
        mListQuest = AppData.getInstance().getListQuestion(person.getInterview_type());
        if (mList != null && mList.size() > 0) {
            Collections.sort(mList,new MyCompare());
            for (int i = 0; i < mList.size(); i ++) {
                ResultQuestion row = (ResultQuestion)(mList.get(i));
                Question question = mListQuest.get(Integer.parseInt(row.id)-1);
                str_share += question.question_cn + (row.result ? " 是 " : " 否 ") + "\n";
            }
        }

        QuestionDetailAdapter adapter = new QuestionDetailAdapter(getActivity(),mList,person);
        mListView.setAdapter(adapter);
        mListView.setExpanded(true);

        return rootView;
    }
    public void Play(String fileName) {

        try {
            if (QuestionFragment.mPlayer==null)
                QuestionFragment.mPlayer= new MediaPlayer();
            QuestionFragment.mPlayer.reset();
            AssetFileDescriptor descriptor = getActivity().getAssets().openFd(fileName);
            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();
            if(start == end)
            {
                Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                return;
            }
            //update paused

            QuestionFragment.mPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
            QuestionFragment.mPlayer.prepare();
            QuestionFragment.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                }
            });
            QuestionFragment.mPlayer.start();

        } catch (Exception ex) {
            Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
            if(QuestionFragment.mPlayer!=null && QuestionFragment.mPlayer.isPlaying())
                QuestionFragment.mPlayer.pause();
        }
    }
    public void Pause(){
        if(QuestionFragment.mPlayer!=null && QuestionFragment.mPlayer.isPlaying())
            QuestionFragment.mPlayer.pause();
    }

    class ResultQuestion {
        public String id;
        public boolean result;
    }
    private void setFullImageFromFilePath(final String imagePath, final ImageView imageView) {

        int targetW = (int) convertDpToPixel(80.0f, getActivity());
        int targetH = (int) convertDpToPixel(80.0f, getActivity());

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
        imageView.setImageBitmap(bitmap);

    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public View.OnClickListener gotoButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            MainActivity activity = (MainActivity) getActivity();
            switch (v.getId()) {
                case R.id.btn_marrage_translate:
                    activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_translate), "TranslatorFragment", "translate", 0));
                    break;
                case R.id.btn_marrage_embassy:
                    activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_embassy), "CamEmbassyFragment", "submenu_shiguan", 0));
                    break;
                case R.id.btn_marrage_section:
                    activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_related_section), "SectionFragment", "section", 0));
                    break;
                default:
                    break;
            }
        }
    };

    class QuestionDetailAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;
        List<ResultQuestion> mResult;
        Person mPerson;
        Map<Integer,Question> mListQuest;
        public QuestionDetailAdapter(Context context , List<ResultQuestion> mResult,Person person) {
            mContext = context;
            this.mResult = mResult;
            mListQuest = AppData.getInstance().getListQuestion(person.getInterview_type());
            if(mResult!=null)
                Collections.sort(mResult, new MyCompare());
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return mResult!=null? mResult.size():0;
        }

        @Override
        public Object getItem(int position) {
            return mResult.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = null;
            ViewHoler viewHoler;
            convertView = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.question_result_item, parent, false);
                viewHoler = new ViewHoler();
                viewHoler.tvContent = (TextView)convertView.findViewById(R.id.question_content);
                viewHoler.tvID =(TextView)convertView.findViewById(R.id.quest_number);
                viewHoler.btnResult =(Button)convertView.findViewById(R.id.question_result);
                convertView.setTag(viewHoler);
            }else {
                viewHoler = (ViewHoler)convertView.getTag();
            }

            ResultQuestion resultQuestion = mResult.get(position);
            if(resultQuestion!=null){
                viewHoler.tvID.setText(resultQuestion.id);

                Question question = mListQuest.get(Integer.parseInt(resultQuestion.id)-1);

                if (question != null ) {
                    if (resultQuestion.result) {
                        viewHoler.tvContent.setText(question.question_cn.substring(2));
                        viewHoler.btnResult.setText("是");
                        viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom);
                    } else {
                        viewHoler.tvContent.setText(question.question_cn.substring(2));
                        viewHoler.btnResult.setText("否");
                        viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom_green);
                    }
                }
            }

            return convertView;
        }
        class ViewHoler {
            TextView tvContent;
            TextView tvID;
            Button btnResult;
        }
    }
    class MyCompare implements Comparator<ResultQuestion> {

        @Override
        public int compare(ResultQuestion lhs, ResultQuestion rhs) {
            if(Integer.parseInt(lhs.id) <Integer.parseInt(rhs.id))
                return -1;
            else
                return 1;
        }
    }
}
