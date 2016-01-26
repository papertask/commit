package com.interview.iso.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;

/**
 * Created by lu.nguyenvan2 on 11/4/2015.
 */
public class BookTutorialFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.list_chapter);

        String[] array_chapter = getResources().getStringArray(R.array.chapters);
        WebView wv = (WebView)rootView.findViewById(R.id.webview);
        ChapterAdapter adapter = new ChapterAdapter(getActivity(),array_chapter);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.shareActivity.loadWebview(position+1);
                //String.format("file:///android_asset/chapter%d.html", position + 1);


            }
        });

//        WebView wv = (WebView)rootView.findViewById(R.id.webview);
//        wv.loadUrl("file:///android_asset/index.html");
        return rootView;
    }

    class ChapterAdapter extends BaseAdapter {
        Context mContext;
        String[] chapters;
        public ChapterAdapter(Context context,String[] array){
            mContext = context;
            chapters = array;
        }
        @Override
        public int getCount() {
            return chapters.length;
        }

        @Override
        public Object getItem(int position) {
            return chapters[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.chapter_item, parent, false);
            TextView tvChapterNumber = (TextView)convertView.findViewById(R.id.chapter_number);
            TextView tvChapterName = (TextView)convertView.findViewById(R.id.chapter_name);
            tvChapterNumber.setText(position+1+"");
            tvChapterName.setText(chapters[position]);
            return convertView;
        }
    }
}
