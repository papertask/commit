package com.interview.iso.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

import com.interview.iso.R;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.models.Answer;
import com.interview.iso.models.Person;
import com.interview.iso.models.Question;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.Constants;
import com.interview.iso.utils.DBHelper;
import com.joooonho.SelectableRoundedImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;

import org.json.JSONObject;

/**
 * Created by lu.nguyenvan2 on 10/30/2015.
 */
public class InterviewerAdapter extends BaseAdapter {
    private List<Person> mPersons;
    private Context mContext;
    private IWXAPI api;

    public InterviewerAdapter(Context context, List<Person> list){
        mContext = context;
        mPersons = list;
    }
    public void updateListPerson(List<Person> lsPerson){
        mPersons = lsPerson;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mPersons!=null ? mPersons.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mPersons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final Person person = mPersons.get(position);
        convertView = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_choice_name,parent,false);
            holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            holder.tvDesc = (TextView)convertView.findViewById(R.id.tvDesc);
            holder.rdCheck =(SelectableRoundedImageView)convertView.findViewById(R.id.image_avatar);
            holder.ivDelete = (TextView)convertView.findViewById(R.id.btn_qlist_delete);
            holder.ivShare = (TextView) convertView.findViewById(R.id.btn_qlist_share);
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder adb =	 new AlertDialog.Builder((Activity) v.getContext());
                    adb.setTitle("删除");
                    adb.setMessage("确定删除这个问卷？");
                    //final int positionToRemove = position;
                    adb.setNegativeButton("取消", null);
                    adb.setPositiveButton("确定", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DBHelper db = new DBHelper(mContext);
                            db.deletePerson(person.getnID());
                            List<Person> lsPerson = db.getAllPerson();
                            updateListPerson(lsPerson);
                        }
                    });
                    adb.show();
                }
            });
            holder.ivShare.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder adb =	 new AlertDialog.Builder((Activity) v.getContext());
                    adb.setTitle("分享");
                    adb.setMessage("确定分享这个问卷？");
                    //final int positionToRemove = position;
                    adb.setNegativeButton("取消", null);
                    adb.setPositiveButton("确定", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            api = AppData.getInstance().getWeChatAPI();
                            String text = getShareString(person);

                            float textSize = 30;
                            TextPaint tp = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
                            tp.setColor(Color.WHITE);
                            tp.setTextSize(textSize);
                            Rect bounds = new Rect();
                            tp.getTextBounds(text , 0, text.length(), bounds);
                            StaticLayout sl = new StaticLayout(text , tp, bounds.width()+5,
                                    Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false);

                            Bitmap bmp = Bitmap.createBitmap(bounds.width()+5, bounds.height()+5,
                                    Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bmp);
                            sl.draw(canvas);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] image = stream.toByteArray();

                            WXImageObject imgObj = new WXImageObject(bmp);

                            WXMediaMessage msg = new WXMediaMessage();
                            msg.mediaObject = imgObj;
                            msg.thumbData = image;
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            req.transaction = "share_image" + System.currentTimeMillis();
                            req.message = msg;

                            req.scene = SendMessageToWX.Req.WXSceneSession;
                            api.sendReq(req);
                        }
                    });
                    adb.show();
                }
            });
            convertView.setTag(holder);
        }else
            holder = (ViewHolder)convertView.getTag();
        /* if(person.getInterview_type()== Constants.POLICY_TYPE)
            holder.tvName.setText("警方排查");
        else
            holder.tvName.setText("外籍人员在华登记询问"); */
        holder.tvName.setText(person.getStrFirstName() + " " + person.getStrLastName());
        holder.tvDesc.setText(person.getStrPosition() + ", " + person.getStrInterviewDate());

        //holder.rdCheck.setChecked(true);
        if(person.getAvatarPath()!=null && !person.getAvatarPath().equals("")){
            setFullImageFromFilePath(person.getAvatarPath(),holder.rdCheck,100*position);
        }

        return convertView;
    }
    private class ViewHolder {
        TextView tvName;
        TextView tvDesc;
        SelectableRoundedImageView rdCheck;
        TextView ivDelete;
        TextView ivShare;
    }
    private void setFullImageFromFilePath(final String imagePath,final ImageView imageView,long delay) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int targetW = (int)convertDpToPixel(80.0f,mContext);
                int targetH = (int)convertDpToPixel(80.0f,mContext);

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imagePath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
                imageView.setImageBitmap(bitmap);
            }
        },delay);

    }
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public String getShareString(Person person) {
        MainActivity activity = (MainActivity)mContext;
        String str_share = "";
        str_share += "姓名 : " + person.getStrFirstName()+" "+person.getStrLastName() + "\n";
        str_share += "电话 : " + person.getStrTelphone() + "\n";
        str_share += "时间 : " + person.getStrInterviewDate() + "\n";
        str_share += "地址 : " + person.getStrPosition() + "\n";

        if (person.getLang().equals("vn")) {
            str_share += "语言 : 越南" + "\n";
        } else if (person.getLang().equals("km")) {
            str_share += "语言 : 柬埔寨" + "\n";
        } else if (person.getLang().equals("lao")) {
            str_share += "语言 : 老挝" + "\n";
        } else if (person.getLang().equals("my")) {
            str_share += "语言 : 缅甸" + "\n";
        }

        str_share += "\n结果建议\n\n";

        DBHelper db = new DBHelper(activity);
        Answer answer = db.getListQuestionByPersion(person.getnID());
        List<ResultQuestion1> mList = null;
        List<ResultQuestion1> mList1 = null;
        Map<String, Integer> mAnswer=new HashMap<>();
        if(answer!= null) {
            JSONObject object = answer.convertToJsonArray();
//        Map<String , Boolean> mResult;
            if (object != null) {
                Iterator<String> key = object.keys();
                mList = new ArrayList<>();
                mList1 = new ArrayList<>();
                while (key.hasNext()) {
                    try {
                        ResultQuestion1 resultQuestion = new ResultQuestion1();
                        resultQuestion.id = key.next();
                        resultQuestion.result = object.getInt(resultQuestion.id);
                        if (Integer.parseInt(resultQuestion.id) > 0 && Integer.parseInt(resultQuestion.id) < 14) {
                            mList.add(resultQuestion);
                            mAnswer.put(resultQuestion.id, (int) (object.get(resultQuestion.id)));
                        } else {
                            mList1.add(resultQuestion);
                        }

                    } catch (Exception ex) {
                    }
                }
            }
        }

        Map<Integer,Question> mListQuest;
        mListQuest = AppData.getInstance().getListQuestion(person.getInterview_type());

        if (person.getInterview_type() == Constants.GOVERNMENT_TYPE) {
            if(mAnswer.get("2")*1==1 && mAnswer.get("3")*1 == 0 && mAnswer.get("4")*1==0) {
                str_share += "同意办证: \n\n";
                str_share += activity.getResources().getString(R.string.marriage_res_approve_cn) + "\n\n";
            } else {
                //tvExtraText.setText(getString(R.string.quetion_gov_cn));

                str_share += "疑似拐卖: \n\n";
                str_share += activity.getResources().getString(R.string.marriage_res_potential_cn) + "\n\n";
            }

            str_share += "\n问卷\n\n";


            if (mList != null && mList.size() > 0) {
                Collections.sort(mList, new MyCompare1());
                for (int i = 0; i < mList.size(); i ++) {
                    ResultQuestion1 row = (ResultQuestion1)(mList.get(i));
                    Question question = mListQuest.get(Integer.parseInt(row.id)-1);
                    str_share += question.question_cn + (row.result == 1 ? " 是 " : " 否 ") + "\n";
                }
            }
        } else {
            boolean b_ispotential = true;
            String str_additional = "";
            if (mList1 != null && mList1.size() > 0) {
                Collections.sort(mList1,new MyCompare1());
                for (int i = 0; i < mList1.size(); i ++) {
                    ResultQuestion1 row = (ResultQuestion1)(mList1.get(i));
                    Question question = mListQuest.get(Integer.parseInt(row.id)-1);
                    str_additional += question.question_cn.substring(3) + (row.result == 1 ? " 是 " : " 否 ") + "\n\n";
                }
            }

            str_additional += "\n问卷\n\n";

            if (mList != null) {
                Collections.sort(mList,new MyCompare1());
                for (int i = 0; i < mList.size(); i ++) {
                    ResultQuestion1 row = (ResultQuestion1)(mList.get(i));
                    Question question = mListQuest.get(Integer.parseInt(row.id)-1);
                    if (row.result != 1)
                        b_ispotential = false;
                    str_additional += question.question_cn + (row.result == 1 ? " 是 " : " 否 ") + "\n";
                }
            }

            if (b_ispotential)
                str_share += activity.getResources().getString(R.string.police_res_desc_1) + "\n";
            else
                str_share += activity.getResources().getString(R.string.police_res_desc_2) + "\n";

            str_share += str_additional;
        }

        return str_share;
    }

    class ResultQuestion1 {
        public String id;
        public int result;
    }

    class MyCompare1 implements Comparator<ResultQuestion1> {

        @Override
        public int compare(ResultQuestion1 lhs, ResultQuestion1 rhs) {
            if(Integer.parseInt(lhs.id) <Integer.parseInt(rhs.id))
                return -1;
            else
                return 1;
        }
    }
}
