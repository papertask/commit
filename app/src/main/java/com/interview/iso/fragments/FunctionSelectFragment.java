package com.interview.iso.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.base.MenuItem;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.Constants;
import com.interview.iso.utils.DataPreferenceManager;

/**
 * Created by lu.nguyenvan2 on 11/3/2015.
 */
public class FunctionSelectFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_function_selection,container,false);
        CardView ivPolicy = (CardView)rootView.findViewById(R.id.police);
        CardView ivGoverment = (CardView)rootView.findViewById(R.id.government);
        ivPolicy.setOnClickListener(onClickListener);
        ivGoverment.setOnClickListener(onClickListener);

        return rootView;
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),MainActivity.class);
            switch (v.getId()){
                case R.id.police:
                    AppData.getInstance().setApptype(Constants.POLICY_TYPE);
                    DataPreferenceManager.getInstance(getActivity()).writeIntData(Constants.SELECT_TYPE, Constants.POLICY_TYPE);
                    AppData.getInstance().setApptype(Constants.POLICY_TYPE);

                    break;
                case R.id.government:
                    AppData.getInstance().setApptype(Constants.GOVERNMENT_TYPE);
                    DataPreferenceManager.getInstance(getActivity()).writeIntData(Constants.SELECT_TYPE, Constants.GOVERNMENT_TYPE);
                    AppData.getInstance().setApptype(Constants.GOVERNMENT_TYPE);
                    break;
                default:
                    break;
            }
            DataPreferenceManager.getInstance(getActivity()).writeBooleanData(Constants.APP_USED,true);
            MainActivity activity = (MainActivity)getActivity();
            //if(v.getId() == R.id.police)
                activity.didSelectMenuItem(new MenuItem(getResources().getString(R.string.addnew_header), "NewQuestionnaireFragment","add_new", 0));
            //else
             //   activity.didSelectMenuItem(new MenuItem("添加新问卷",0,"NewQuestionnaireFragment","add_new"));
        }
    };
}
