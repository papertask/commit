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
 * Created by Castorim on 03/07/2016.
 */
public class HelpFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, container,false);

        return v;
    }

}
