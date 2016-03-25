package com.interview.iso.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.base.MenuItem;
import com.interview.iso.models.Person;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.Constants;
import com.interview.iso.utils.DBHelper;

import org.json.JSONObject;

import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by lu.nguyenvan2 on 11/3/2015.
 */
public class LanguageChooseFragment extends BaseFragment {

    private ListView lsvCountry;
    private CountriesListAdapter adpCountry;
    private String[] strCountry;
    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.language, container, false);
        lsvCountry =(ListView)rootView.findViewById(R.id.lsvlanguage);
        init();
        mainActivity = (MainActivity)getActivity();
        lsvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = adpCountry.getItem(position);
                String code = item.split(",")[0];
                AppData.getInstance().setLanguage(code.toLowerCase());

                MainActivity activity = (MainActivity) getActivity();
                DBHelper db = new DBHelper(activity);
                Person person = db.getPerson(AppData.getInstance().getPersonID());
                person.setLang(code.toLowerCase());
                db.updatePerson(person);
                
                if(AppData.getInstance().getApptype()== Constants.POLICY_TYPE)
                    mainActivity.didSelectMenuItem(new MenuItem("公安问卷 - 基本询问","QuestionFragment","question", 0));
                else
                    mainActivity.didSelectMenuItem(new MenuItem("婚姻登记问卷", "QuestionFragment","question", 0));
            }
        });
        return rootView;
    }
    private void init(){
        strCountry = getActivity().getResources().getStringArray(R.array.CountryCodes);
        adpCountry = new CountriesListAdapter(getActivity(), strCountry);
        lsvCountry.setAdapter(adpCountry);
    }
    public class CountriesListAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public CountriesListAdapter(Context context, String[] values) {
            super(context,R.layout.country_list_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public String getItem(int position) {
            return values[position];
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.country_list_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.txtViewCountryName);
            TextView textView_cn = (TextView) rowView.findViewById(R.id.txtViewCountryName_cn);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.imgViewFlag);

            String[] g= values[position].split(",");
            textView.setText(g[1]);
            textView_cn.setText(g[2]);
            String pngName = g[0].trim().toLowerCase();
            imageView.setImageResource(context.getResources().getIdentifier("drawable/" + pngName, null, context.getPackageName()));
            return rowView;
        }

    }
}
