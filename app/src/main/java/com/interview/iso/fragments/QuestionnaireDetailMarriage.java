package com.interview.iso.fragments;

import android.content.Context;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lu.nguyenvan2 on 11/11/2015.
 */
public class QuestionnaireDetailMarriage extends Fragment {

    private List<ResultQuestion> mList;
    private ExpandableHeightListView mListView;
    SelectableRoundedImageView rdAvatar;
    private  int number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questionaire_detail_marrige, container, false);
        mListView = (ExpandableHeightListView) rootView.findViewById(R.id.list_questionnaire);
        rdAvatar = (SelectableRoundedImageView) rootView.findViewById(R.id.image_avatar);
        TextView tvInterviewDate = (TextView)rootView.findViewById(R.id.tv_interviewDate);
        TextView tvGender = (TextView)rootView.findViewById(R.id.tv_gender);
        TextView tvExtraText = (TextView)rootView.findViewById(R.id.extra_information);
        TextView tvLocation = (TextView)rootView.findViewById(R.id.tv_location);
        TextView tvName = (TextView)rootView.findViewById(R.id.tv_name);
        TextView lblResult = (TextView) rootView.findViewById(R.id.lbl_result);
        TextView tvResultCn = (TextView) rootView.findViewById(R.id.tv_result_cn);
        TextView tvResult = (TextView) rootView.findViewById(R.id.tv_result);
        ImageView imgPlayAnounce = (ImageView) rootView.findViewById(R.id.img_play_anounce);
        imgPlayAnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuestionFragment.mPlayer==null)
                    QuestionFragment.mPlayer = new MediaPlayer();
                if (QuestionFragment.mPlayer.isPlaying()) {
                    QuestionFragment.mPlayer.pause();
                    QuestionFragment.mPlayer = new MediaPlayer();
                    Drawable img = getContext().getResources().getDrawable(R.drawable.button_broadcast);
                    //btn_play_audio.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                } else {
                    String file_name;
                    if (number == 1) {
                        file_name = "1.mp3";
                    } else {
                        file_name = "2.mp3";
                    }
                    Play(file_name);
                }
            }
        });

        Person person = AppData.getInstance().getPerson_selection();
        if(person.getAvatarPath()!=null && !person.getAvatarPath().equals(""))
            setFullImageFromFilePath(person.getAvatarPath(), rdAvatar);

        tvInterviewDate.setText(person.getTime());
        if(person.getGender().equals("Male"))
            tvGender.setText(getString(R.string.Male));
        else
            tvGender.setText(getString(R.string.Female));
        tvName.setText(person.getFirstName()+" "+person.getLastName());
        tvLocation.setText(person.getAdd());

        DBHelper db = new DBHelper(getActivity());
        Answer answer = db.getListQuestionByPersion(person.getID());
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
                        resultQuestion.result = object.getBoolean(resultQuestion.id);
                        if (!resultQuestion.id.equals("0"))
                        mList.add(resultQuestion);
                    } catch (Exception ex) {
                    }
                }
            }
        }

        if(number == 1) {
            //tvExtraText.setText(getString(R.string.quetion_gov_cn));
            lblResult.setText("同意办证：");
            tvResultCn.setText(getString(R.string.quetion_gov_cn));
            tvResult.setText(getString(R.string.quetion_gov_vn));
        }
        else {
            lblResult.setText("疑似拐卖: ");
            tvResultCn.setText(getString(R.string.quetion_gov_cn1));
            tvResult.setText(getString(R.string.quetion_gov_vn1));
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
                if (resultQuestion.result) {
                    viewHoler.tvContent.setText(question.question_cn);
                    viewHoler.btnResult.setText("是");
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom);
                } else {
                    viewHoler.tvContent.setText(question.question_cn);
                    viewHoler.btnResult.setText("否");
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom_green);
                }

            }

            return convertView;
        }
        class ViewHoler {
            TextView tvContent;
            TextView tvID;
            Button btnResult;
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
}
