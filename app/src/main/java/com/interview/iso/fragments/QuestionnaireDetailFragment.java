package com.interview.iso.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private TextView ctrlTxtLang;
    private TextView ctrlTxtDescAddition;
    private String str_share = "";

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questionaire_detail, container, false);
        mListView = (ExpandableHeightListView) rootView.findViewById(R.id.list_questionnaire);
        mListView1 = (ExpandableHeightListView) rootView.findViewById(R.id.list_questionnaire_1);
        rdAvatar = (SelectableRoundedImageView) rootView.findViewById(R.id.image_avatar);
        TextView tvInterviewDate = (TextView) rootView.findViewById(R.id.tv_interviewDate);
        TextView tvGender = (TextView) rootView.findViewById(R.id.tv_gender);
        TextView tvLocation = (TextView) rootView.findViewById(R.id.tv_location);
        TextView tvName = (TextView) rootView.findViewById(R.id.tv_name);
        ctrlTxtLang = (TextView) rootView.findViewById(R.id.tv_userLang);
        Button tvBtnDelete = (Button) rootView.findViewById(R.id.btn_qdetail_delete);
        Button tvBtnShare = (Button) rootView.findViewById(R.id.btn_qdetail_share);
        ctrlTxtDescAddition = (TextView) rootView.findViewById(R.id.txt_addition_q_desc);

        tvBtnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }

        });

        tvBtnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder((Activity) v.getContext());
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

        tvBtnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.ShareInterview(str_share);
            }

        });

        Person person = AppData.getInstance().getPerson_selection();
        str_share += "姓名 : " + person.getStrFirstName() + " " + person.getStrLastName() + "\n";
        str_share += "电话 : " + person.getStrTelphone() + "\n";

        if (person.getAvatarPath() != null && !person.getAvatarPath().equals(""))
            setFullImageFromFilePath(person.getAvatarPath(), rdAvatar);

        tvName.setText(person.getStrFirstName() + " " + person.getStrLastName());
        tvInterviewDate.setText("时间 : " + person.getStrInterviewDate());
        str_share += "时间 : " + person.getStrInterviewDate() + "\n";
//        tvGender.setText(getString(R.string.Female));
        tvLocation.setText("地址 : " + person.getStrPosition());
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

        str_share += "\n\n" + "结果建议" + "\n\n";
        DBHelper db = new DBHelper(getActivity());
        Answer answer = db.getListQuestionByPersion(person.getnID());
        boolean b_ispotential = true;
        if (answer != null) {
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
                            if (resultQuestion.result != 1)
                                b_ispotential = false;
                        } else {
                            mList1.add(resultQuestion);
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }
        if (b_ispotential) {
            ctrlTxtDescAddition.setText(getResources().getString(R.string.police_res_desc_1));
            ctrlTxtDescAddition.setVisibility(View.VISIBLE);
            str_share += getResources().getString(R.string.police_res_desc_1) + "\n\n";
        } else {
            ctrlTxtDescAddition.setText(getResources().getString(R.string.police_res_desc_2));
            ctrlTxtDescAddition.setVisibility(View.VISIBLE);
            str_share += getResources().getString(R.string.police_res_desc_2) + "\n\n";
        }
        setQuestionDetails(person);
        QuestionDetailAdapter adapter = new QuestionDetailAdapter(getActivity(), mList, person);
        QuestionDetailAdapter1 adapter1 = new QuestionDetailAdapter1(getActivity(), mList1, person);
        mListView.setAdapter(adapter);
        mListView.setExpanded(true);
        mListView1.setAdapter(adapter1);
        mListView1.setExpanded(true);
        return rootView;
    }

    private void setQuestionDetails(Person person) {
        Map<Integer, Question> mListQuest;
        mListQuest = AppData.getInstance().getListQuestion(person.getInterview_type());
        if (mList1 != null && mList1.size() > 0) {
            Collections.sort(mList1, new MyCompare());
            for (int i = 0; i < mList1.size(); i++) {
                ResultQuestion row = (ResultQuestion) (mList1.get(i));
                Question question = mListQuest.get(Integer.parseInt(row.id) - 1);
                str_share += question.question_cn.substring(3) + (row.result == 1 ? " 是 " : " 否 ") + "\n\n";
            }
        }
        str_share += "\n问卷\n\n";

        if (mList != null) {
            Collections.sort(mList, new MyCompare());
            for (int i = 0; i < mList.size(); i++) {
                ResultQuestion row = (ResultQuestion) (mList.get(i));
                Question question = mListQuest.get(Integer.parseInt(row.id) - 1);
                str_share += question.question_cn + (row.result == 1 ? " 是 " : " 否 ") + "\n";
            }
        }
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
        Map<Integer, Question> mListQuest;

        public QuestionDetailAdapter(Context context, List<ResultQuestion> mResult, Person person) {
            mContext = context;
            this.mResult = mResult;
            mListQuest = AppData.getInstance().getListQuestion(person.getInterview_type());
            if (mResult != null)
                Collections.sort(mResult, new MyCompare());
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return mResult != null ? mResult.size() : 0;
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
                viewHoler.tvContent = (TextView) convertView.findViewById(R.id.question_content);
                viewHoler.tvID = (TextView) convertView.findViewById(R.id.quest_number);
                viewHoler.btnResult = (Button) convertView.findViewById(R.id.question_result);
                convertView.setTag(viewHoler);
            } else {
                viewHoler = (ViewHoler) convertView.getTag();
            }
            ResultQuestion resultQuestion = mResult.get(position);
            if (resultQuestion != null) {
                viewHoler.tvID.setText(resultQuestion.id);
                Question question = mListQuest.get(Integer.parseInt(resultQuestion.id) - 1);
                if (Integer.parseInt(resultQuestion.id) >= 10) {
                    viewHoler.tvContent.setText(question.question_cn.substring(3));
                } else {
                    viewHoler.tvContent.setText(question.question_cn.substring(2));
                }
                if (resultQuestion.result == 1) {
                    viewHoler.btnResult.setText("是");
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom);
                } else if (resultQuestion.result == 0) {
                    viewHoler.btnResult.setText("否");
                    viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom_green);
                } else {
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

    }

    class MyCompare implements Comparator<ResultQuestion> {

        @Override
        public int compare(ResultQuestion lhs, ResultQuestion rhs) {
            if (Integer.parseInt(lhs.id) < Integer.parseInt(rhs.id))
                return -1;
            else
                return 1;
        }
    }

    class QuestionDetailAdapter1 extends BaseAdapter {

        Context mContext;
        LayoutInflater inflater;
        List<ResultQuestion> mResult;
        Person mPerson;
        Map<Integer, Question> mListQuest;

        public QuestionDetailAdapter1(Context context, List<ResultQuestion> mResult, Person person) {
            mContext = context;
            this.mResult = mResult;
            mListQuest = AppData.getInstance().getListQuestion(person.getInterview_type());
            if (mResult != null)
                Collections.sort(mResult, new MyCompare());
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return mResult != null ? mResult.size() : 0;
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

            ResultQuestion resultQuestion = mResult.get(position);
            if (resultQuestion != null) {
                Question question = mListQuest.get(Integer.parseInt(resultQuestion.id) - 1);
                if (question != null) {
                    ViewHoler viewHoler;
                    if (convertView == null) {
                        convertView = inflater.inflate(R.layout.question_result_item_1, parent, false);
                        viewHoler = new ViewHoler();
                        viewHoler.tvContent = (TextView) convertView.findViewById(R.id.question_content_1);
                        viewHoler.btnContact = (Button) convertView.findViewById(R.id.question_contact_1);
                        viewHoler.btnResult = (Button) convertView.findViewById(R.id.question_result_1);
                        viewHoler.tvNumber = (TextView) convertView.findViewById(R.id.quest_number_additional);
                        convertView.setTag(viewHoler);
                    } else {
                        viewHoler = (ViewHoler) convertView.getTag();
                    }
                    viewHoler.tvContent.setText(question.question_cn.substring(3));
                    viewHoler.tvNumber.setText(resultQuestion.id);
                    if (resultQuestion.result == 1) {
                        viewHoler.btnResult.setText("是");
                        viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom);
                    } else if (resultQuestion.result == 0) {
                        viewHoler.btnResult.setText("否");
                        viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom_green);
                    } else {
                        viewHoler.btnResult.setText("跳过");
                        viewHoler.btnResult.setBackgroundResource(R.drawable.buttoncustom_white);
                    }
                    if (resultQuestion.id.equals("15") || resultQuestion.id.equals("17")) {
                        viewHoler.btnContact.setText("联系有关部门");
                    } else if (resultQuestion.id.equals("14")) {
                        viewHoler.btnContact.setText("联系翻译");
                    }

                    if (resultQuestion.result == 0)
                        viewHoler.btnContact.setVisibility(View.GONE);
                    else
                        viewHoler.btnContact.setVisibility(View.VISIBLE);

                    viewHoler.btnContact.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Button This = (Button) v;
                            MainActivity activity = (MainActivity) getActivity();
                            if (This.getText().equals("联系有关部门")) {
                                activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_related_section), "SectionFragment", "section", 0));
                            } else {
                                activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.menu_link_translate), "TranslatorFragment", "translate", 0));
                            }
                        }
                    });
                }
            }

            return convertView;
        }

        class ViewHoler {
            TextView tvContent;
            Button btnResult;
            Button btnContact;
            TextView tvNumber;
        }

        class MyCompare implements Comparator<ResultQuestion> {

            @Override
            public int compare(ResultQuestion lhs, ResultQuestion rhs) {
                if (Integer.parseInt(lhs.id) < Integer.parseInt(rhs.id))
                    return -1;
                else
                    return 1;
            }
        }
    }
}
