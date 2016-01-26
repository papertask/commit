package com.interview.iso.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.interview.iso.activity.MainActivity;
import com.interview.iso.models.Person;
import com.interview.iso.models.Question;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by lu.nguyenvan2 on 10/27/2015.
 */
public class AppData {

    private static AppData _instance;

    public int getApptype() {
        return apptype;
    }

    public void setApptype(int apptype) {
        this.apptype = apptype;
    }

    int apptype;

    public String getLanguage() {
        return _language;
    }

    public void setLanguage(String _language) {
        this._language = _language;
    }

    private String _language = "cn";

    private long personID = 0L;

    public long getPersonID() {
        return personID;
    }

    public void setPersonID(long personID) {
        this.personID = personID;
    }

    public static AppData getInstance() {
        if (_instance == null) {
            _instance = new AppData();
        }
        return _instance;
    }


    public Person getPerson_selection() {
        return person_selection;
    }

    public void setPerson_selection(Person person_id) {
        this.person_selection = person_id;
    }

    private Person person_selection;

    public Map<Integer, Question> getListQuestion(int apptype) {
        if (apptype == Constants.POLICY_TYPE) {
            if (mListQuestion == null)
                loadQuestion(apptype);
            return mListQuestion;
        } else {
            if(mListQuestion1 == null)
                loadQuestion(apptype);
                return mListQuestion1;
            }
    }

    public Map<String, Boolean> getmAnswer() {
        return mAnswer;
    }

    public void setmAnswer(Map<String, Boolean> mAnswer) {
        this.mAnswer = mAnswer;
    }

    private Map<String, Boolean> mAnswer = null;

    public int getCurrQuestion() {
        return currQuestion;
    }

    public void setCurrQuestion(int currQuestion) {
        this.currQuestion = currQuestion;
    }

    private int currQuestion;

    public int getCurrQuestionStatus() {
        return currQuestionStatus;
    }

    public void setCurrQuestionStatus(int currQuestionStatus) {
        this.currQuestionStatus = currQuestionStatus;
    }

    private int currQuestionStatus;

    public void setListQuestion(Map<Integer, Question> mListQuestion) {
        this.mListQuestion = mListQuestion;
    }

    private Map<Integer, Question> mListQuestion1 = null;
    private Map<Integer, Question> mListQuestion = null;

    private void loadQuestion(int apptype) {
        try {

            String strJson = loadJSONFromAsset();
            JSONObject jsonObject = new JSONObject(strJson);
            if (apptype == Constants.POLICY_TYPE) {
                mListQuestion = new TreeMap<Integer, Question>();
                JSONArray array = jsonObject.getJSONArray("policy");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Question question = new Question(object.getString("question_cn"), object.getString("question_vn"),
                            object.getString("question_my"),object.getString("question_lo"),object.getString("question_km"));
                    mListQuestion.put(i, question);
                }
            } else {
                mListQuestion1 =  new TreeMap<Integer, Question>();
                JSONArray array = jsonObject.getJSONArray("government");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Question question = new Question(object.getString("question_cn"), object.getString("question_vn"),
                            object.getString("question_my"),object.getString("question_lo"),object.getString("question_km"));
                    mListQuestion1.put(i, question);
                }
            }

        } catch (Exception ex) {
            mListQuestion = null;
        }

    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = MainActivity.shareActivity.getAssets().open("question.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
