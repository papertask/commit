package com.interview.iso.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.adapters.InterviewerAdapter;
import com.interview.iso.base.MenuItem;
import com.interview.iso.models.Person;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.Constants;
import com.interview.iso.utils.DBHelper;

import java.util.List;

/**
 * Created by lu.nguyenvan2 on 10/28/2015.
 */
public class ListNameFragment extends BaseFragment {
    private ListView mListView;
    private InterviewerAdapter mAdapter;
    private List<Person> mListPerson;
    DBHelper db;
    private EditText edtSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_name,container,false);
        mListView =(ListView)v.findViewById(R.id.lv_interviewer);
        edtSearch = (EditText)v.findViewById(R.id.ed_search);
        DBHelper db = new DBHelper(getActivity());
        mListPerson = db.getAllPerson();
        mAdapter = new InterviewerAdapter(getActivity(),mListPerson);
        mListView.setAdapter(mAdapter);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //string here
                DBHelper dbHelper = new DBHelper(getActivity());
                List<Person> list = dbHelper.searchPerson(s.toString());
                mAdapter.updateListPerson(list);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                /view.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                Person person = (Person)mAdapter.getItem(position);
                AppData.getInstance().setPerson_selection(person);
                MainActivity activity = (MainActivity)getActivity();
                if(person.getInterview_type()== Constants.POLICY_TYPE)
                    activity.didSelectMenuItem(new MenuItem("问卷内容",0,"QuestionnaireDetailFragment","NewQuestionnaire"));
                else
                    activity.didSelectMenuItem(new MenuItem("问卷内容",0,"QuestionnaireDetailMarriage","NewQuestionnaire"));
            }
        });
        return v;
    }
    private boolean isSearch = false;
    public void updateSearch(){
        isSearch =!isSearch;
        if(isSearch) {
            edtSearch.setVisibility(View.VISIBLE);
            edtSearch.setFocusable(true);
        }else {
            edtSearch.setVisibility(View.GONE);
            mAdapter.updateListPerson(mListPerson);
        }

    }
    @Override
    public void onAttach(Context activity) {
        db = new DBHelper(activity);
        super.onAttach(activity);
    }
    @Override
    public void onDestroy() {
        db.close();
        super.onDestroy();
    }

}
