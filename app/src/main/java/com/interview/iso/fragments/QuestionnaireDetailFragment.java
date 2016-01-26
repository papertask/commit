package com.interview.iso.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by lu.nguyenvan2 on 11/3/2015.
 */
public class QuestionnaireDetailFragment extends BaseFragment {
    private List<ResultQuestion> mList;
    private ExpandableHeightListView mListView;
    SelectableRoundedImageView rdAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questionaire_detail, container, false);
        mListView = (ExpandableHeightListView) rootView.findViewById(R.id.list_questionnaire);
        rdAvatar = (SelectableRoundedImageView) rootView.findViewById(R.id.image_avatar);
        TextView tvInterviewDate = (TextView)rootView.findViewById(R.id.tv_interviewDate);
        TextView tvGender = (TextView)rootView.findViewById(R.id.tv_gender);

        Person person = AppData.getInstance().getPerson_selection();
        if(person.getAvatarPath()!=null && !person.getAvatarPath().equals(""))
            setFullImageFromFilePath(person.getAvatarPath(), rdAvatar);

        tvInterviewDate.setText(person.getTime());
        if(person.getGender().equals("Male"))
            tvGender.setText(getString(R.string.Male));
        else
            tvGender.setText(getString(R.string.Female));

        DBHelper db = new DBHelper(getActivity());
        Answer answer = db.getListQuestionByPersion(person.getID());
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
                        mList.add(resultQuestion);
                    } catch (Exception ex) {
                    }
                }
            }
        }
        QuestionDetailAdapter adapter = new QuestionDetailAdapter(getActivity(),mList,person);
        mListView.setAdapter(adapter);
        mListView.setExpanded(true);
        return rootView;
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
                Collections.sort(mResult,new MyCompare());
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
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.question_result_item, parent, false);
                viewHoler = new ViewHoler();
                viewHoler.tvContent = (TextView)convertView.findViewById(R.id.question_content);
                viewHoler.tvID =(TextView)convertView.findViewById(R.id.quest_number);
                convertView.setTag(viewHoler);
            }else {
                viewHoler = (ViewHoler)convertView.getTag();
            }
            ResultQuestion resultQuestion = mResult.get(position);
            if(resultQuestion!=null){
                viewHoler.tvID.setText(resultQuestion.id + ". ");
                Question question = mListQuest.get(Integer.parseInt(resultQuestion.id)-1);
                if(resultQuestion.result)
                    viewHoler.tvContent.setText(question.question_cn +"   -是");
                else
                    viewHoler.tvContent.setText(question.question_cn +"   -否");
            }

            return convertView;
        }
        class ViewHoler {
            TextView tvContent;
            TextView tvID;
        }
        class MyCompare implements Comparator<ResultQuestion>{

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
