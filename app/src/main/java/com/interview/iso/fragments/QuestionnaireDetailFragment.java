package com.interview.iso.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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
 * Created by lu.nguyenvan2 on 11/3/2015.
 */
public class QuestionnaireDetailFragment extends BaseFragment {
    private List<ResultQuestion> mList;
    private List<ResultQuestion> mList1;
    private ExpandableHeightListView mListView;
    private ExpandableHeightListView mListView1;
    SelectableRoundedImageView rdAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questionaire_detail, container, false);
        mListView = (ExpandableHeightListView) rootView.findViewById(R.id.list_questionnaire);
        mListView1 = (ExpandableHeightListView) rootView.findViewById(R.id.list_questionnaire_1);
        rdAvatar = (SelectableRoundedImageView) rootView.findViewById(R.id.image_avatar);
        TextView tvInterviewDate = (TextView)rootView.findViewById(R.id.tv_interviewDate);
        TextView tvGender = (TextView)rootView.findViewById(R.id.tv_gender);
        TextView tvLocation = (TextView)rootView.findViewById(R.id.tv_location);
        TextView tvName = (TextView)rootView.findViewById(R.id.tv_name);

        Person person = AppData.getInstance().getPerson_selection();
        if(person.getAvatarPath()!=null && !person.getAvatarPath().equals(""))
            setFullImageFromFilePath(person.getAvatarPath(), rdAvatar);

        tvName.setText(person.getFirstName()+" "+person.getLastName());
        tvInterviewDate.setText(person.getTime());
        if(person.getGender().equals("Male"))
            tvGender.setText(getString(R.string.Male));
        else
            tvGender.setText(getString(R.string.Female));
        tvLocation.setText(person.getAdd());

        DBHelper db = new DBHelper(getActivity());
        Answer answer = db.getListQuestionByPersion(person.getID());
        if(answer!= null) {
            JSONObject object = answer.convertToJsonArray();
//        Map<String , Boolean> mResult;
            if (object != null) {
                Iterator<String> key = object.keys();
                mList = new ArrayList<>();
                mList1 = new ArrayList<>();
                while (key.hasNext()) {
                    try {
                        ResultQuestion resultQuestion = new ResultQuestion();
                        resultQuestion.id = key.next();
                        resultQuestion.result = object.getInt(resultQuestion.id);
                        if (Integer.parseInt(resultQuestion.id) > 0 && Integer.parseInt(resultQuestion.id) < 14) {
                            mList.add(resultQuestion);
                        } else {
                            mList1.add(resultQuestion);
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }
        QuestionDetailAdapter adapter = new QuestionDetailAdapter(getActivity(),mList,person);
        QuestionDetailAdapter1 adapter1 = new QuestionDetailAdapter1(getActivity(),mList1,person);
        mListView.setAdapter(adapter);
        mListView.setExpanded(true);
        mListView1.setAdapter(adapter1);
        mListView1.setExpanded(true);
        return rootView;
    }
    class ResultQuestion {
        public String id;
        public int result;
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
                viewHoler.btnResult = (Button)convertView.findViewById(R.id.question_result);
                convertView.setTag(viewHoler);
            }else {
                viewHoler = (ViewHoler)convertView.getTag();
            }
            ResultQuestion resultQuestion = mResult.get(position);
            if(resultQuestion!=null){
                viewHoler.tvID.setText(resultQuestion.id);
                Question question = mListQuest.get(Integer.parseInt(resultQuestion.id)-1);
                viewHoler.tvContent.setText(question.question_cn);
                if(resultQuestion.result == 1) {
                    viewHoler.btnResult.setText("是");
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom);
                }else if(resultQuestion.result == 0) {
                    viewHoler.btnResult.setText("否");
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom_green);
                }else {
                    viewHoler.btnResult.setText("跳过");
                    viewHoler.btnResult.setTextColor(Color.BLACK);
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom_black);
                }
            }

            return convertView;
        }
        class ViewHoler {
            TextView tvContent;
            TextView tvID;
            Button btnResult;
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

    class QuestionDetailAdapter1 extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;
        List<ResultQuestion> mResult;
        Person mPerson;
        Map<Integer,Question> mListQuest;
        public QuestionDetailAdapter1(Context context , List<ResultQuestion> mResult,Person person) {
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
                convertView = inflater.inflate(R.layout.question_result_item_1, parent, false);
                viewHoler = new ViewHoler();
                viewHoler.tvContent = (TextView)convertView.findViewById(R.id.question_content_1);
                viewHoler.btnContact =(Button)convertView.findViewById(R.id.question_contact_1);
                viewHoler.btnResult = (Button)convertView.findViewById(R.id.question_result_1);
                convertView.setTag(viewHoler);
            }else {
                viewHoler = (ViewHoler)convertView.getTag();
            }
            ResultQuestion resultQuestion = mResult.get(position);
            if(resultQuestion!=null){
                Question question = mListQuest.get(Integer.parseInt(resultQuestion.id)-1);
                viewHoler.tvContent.setText(question.question_cn);
                if(resultQuestion.result == 1) {
                    viewHoler.btnResult.setText("是");
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom);
                }else if(resultQuestion.result == 0) {
                    viewHoler.btnResult.setText("否");
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom_green);
                }else {
                    viewHoler.btnResult.setText("跳过");
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom_white);
                }
                if (resultQuestion.id.equals("15") || resultQuestion.id.equals("17")) {
                    viewHoler.btnContact.setText("联系有关部门");
                    viewHoler.btnContact.setVisibility(View.VISIBLE);
                } else if (resultQuestion.id.equals("14")) {
                    viewHoler.btnContact.setText("联系翻译");
                    viewHoler.btnContact.setVisibility(View.VISIBLE);
                } else {
                    viewHoler.btnContact.setVisibility(View.GONE);
                }

                viewHoler.btnContact.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        ViewHoler viewHolder = (ViewHoler)v.getParent();
                    }
                });
            }

            return convertView;
        }

        class ViewHoler {
            TextView tvContent;
            Button btnResult;
            Button btnContact;
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
