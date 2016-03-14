package com.interview.iso.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.Image;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.Activity;

import com.interview.iso.R;
import com.interview.iso.models.Person;
import com.interview.iso.utils.Constants;
import com.interview.iso.utils.DBHelper;
import com.joooonho.SelectableRoundedImageView;

import org.w3c.dom.Text;

import java.util.List;


/**
 * Created by lu.nguyenvan2 on 10/30/2015.
 */
public class InterviewerAdapter extends BaseAdapter {
    private List<Person> mPersons;
    private Context mContext;
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
                            db.deletePerson(person.getID());
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
        holder.tvName.setText(person.getFirstName() + " " + person.getLastName());
        holder.tvDesc.setText(person.getTime());

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

}
