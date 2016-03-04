package com.interview.iso.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.interview.iso.R;
import com.interview.iso.activity.CameraActivity;
import com.interview.iso.activity.MainActivity;
import com.interview.iso.adapters.InterviewerAdapter;
import com.interview.iso.base.MenuItem;
import com.interview.iso.models.Person;
import com.interview.iso.utils.AppData;
import com.interview.iso.utils.DBHelper;
import com.interview.iso.utils.Utils;
import com.interview.iso.view.RoundedImageView;
import com.joooonho.SelectableRoundedImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewQuestionnaireFragment extends BaseFragment {


    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter_birth,dateFormatter_interview ;
    private Calendar newDate;
    private Button btnCreateAccount;
    private SelectableRoundedImageView imgCamera;
    private MediaRecorder recorder;

    private String date="";
    private String time="";
    private DBHelper db;
    private EditText edtFirstName,edtLastName, edtAdd;
    private TextView tvTime,tvDate;
    private RadioGroup rdoGender;
    int selectDatefrom = -1;
    static final int REQUEST_TAKE_PHOTO = 11111;
    public NewQuestionnaireFragment(){}
    private boolean hasChange = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_newperson, container, false);

        btnCreateAccount =(Button)rootView.findViewById(R.id.btn_next);
        imgCamera =(SelectableRoundedImageView)rootView.findViewById(R.id.img_avatar);
        imgCamera.setOnClickListener(myAddClickListener);
        mContentResolver = getActivity().getContentResolver();
//        imgCamera.getLayoutParams().height = (int)InterviewerAdapter.convertDpToPixel(80,getActivity());
//        imgCamera.getLayoutParams().width = (int)InterviewerAdapter.convertDpToPixel(80,getActivity());
//        imgCamera.requestLayout();
        btnCreateAccount.setOnClickListener(myAddClickListener);

        edtFirstName = (EditText)rootView.findViewById(R.id.edtFirstName);
        edtLastName = (EditText)rootView.findViewById(R.id.edtLastName);

        tvDate = (TextView)rootView.findViewById(R.id.tvInterviewDate);
        tvTime = (TextView)rootView.findViewById(R.id.tvBirthday);
        edtAdd = (EditText)rootView.findViewById(R.id.edtaddnew_l5);
        edtAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasChange = true;
            }
        });
        edtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hasChange = true;
            }
        });
        rdoGender = (RadioGroup)rootView.findViewById(R.id.rdog_addnew_gender);
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter_birth = new SimpleDateFormat("yyyy年MM月dd天");
        dateFormatter_interview = new SimpleDateFormat("yyyy年MM月dd天, HH点mm分");//2015年11月1日，15点51分
        Date date = new Date();
        tvTime.setText(dateFormatter_birth.format(date.getTime()));
        tvDate.setText(dateFormatter_interview.format(date.getTime()));
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                hasChange = true;
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (selectDatefrom ==1)
                    tvDate.setText(dateFormatter_interview.format(newDate.getTime()));
                else
                    tvTime.setText(dateFormatter_birth.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDatefrom = 1;
                fromDatePickerDialog.show();
            }
        });
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDatefrom = 2;
                fromDatePickerDialog.show();
            }
        });
        return rootView;
    }

    View.OnClickListener myAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                switch (v.getId()){
                    case R.id.btn_next:
                        if(edtLastName.getText().length()>0 && edtFirstName.getText().length()>0 && tvDate.getText().length()>0 & tvTime.getText().length()>0 & edtAdd.getText().length()>0)
                        {
                            int idxgender_selected = rdoGender.indexOfChild(rdoGender.findViewById(rdoGender.getCheckedRadioButtonId()));
                            String strGender="";
                            if(idxgender_selected==0) strGender="Female";
                            else strGender="Male";
                            Person pr = new Person();
                            pr.setLastName(edtLastName.getText().toString());
                            pr.setFirstName(edtFirstName.getText().toString());
                            pr.setBirthDay(tvTime.getText().toString());
                            pr.setTime(tvDate.getText().toString());
                            pr.setAdd(edtAdd.getText().toString());
                            pr.setGender(strGender);
                            CameraActivity cameraActivity = (CameraActivity)getActivity();
                            pr.setAvatar(cameraActivity.getCurrentPhotoPath());
                            if (!isStored && hasChange)
                                AppData.getInstance().setPersonID(db.createPerson(pr));
                            Log.d("current Person",AppData.getInstance().getPersonID() + "");
                            //next question
                            MainActivity activity = (MainActivity) getActivity();
                            activity.didSelectMenuItem(new MenuItem(getString(R.string.language), "LanguageChooseFragment","Language", 0));

                        }else{ ShowMessage("请填写所有信息。",0); return;}
                        break;
                    case R.id.img_avatar:
                        dispatchTakePictureIntent();
                        break;
                    default:
                        break;
                }
            }catch(Exception e){e.printStackTrace();}
        }
    };
    /*
        status = 0 fail
        status = 1 done
     */
    private void ShowMessage(String strmessage, int status){
        AlertDialog.Builder dl=	new AlertDialog.Builder(getActivity());
        if(status == 0) {
            dl.setTitle("警告！");
            dl.setIcon(R.drawable.ic_bell);
        }else if (status == 1){
            dl.setTitle("Success!");
            dl.setIcon(R.drawable.icon_mark);
        }
        dl.setMessage(strmessage);
        dl.setNeutralButton("OK",null);
        dl.show();
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

    protected void dispatchTakePictureIntent() {

        // Check if there is a camera.
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        CameraActivity activity = (CameraActivity)getActivity();
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri fileUri = Uri.fromFile(photoFile);
                activity.setCapturedImageURI(fileUri);
                activity.setCurrentPhotoPath(fileUri.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        activity.getCapturedImageURI());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        CameraActivity activity = (CameraActivity)getActivity();
        activity.setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("restoreState", true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            addPhotoToGallery();
            CameraActivity activity = (CameraActivity)getActivity();

            // Show the full sized image.
            //setFullImageFromFilePath(activity.getCurrentPhotoPath(), imgCamera);
            Uri ImageUri = Utils.getImageUri(activity.getCurrentPhotoPath());
            mCurrentAvatar = getBitmap(ImageUri);
            if(mCurrentAvatar!=null){
                Bitmap b = mCurrentAvatar;
                Drawable bitmap = new BitmapDrawable(getResources(), b);

                imgCamera.setImageDrawable(bitmap);

                int targetW = imgCamera.getWidth();
                int targetH = imgCamera.getHeight();

                // Get the dimensions of the bitmap
//                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                bmOptions.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(imagePath, bmOptions);
//                int photoW = bmOptions.outWidth;
//                int photoH = bmOptions.outHeight;
//
//                // Determine how much to scale down the image
//                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//                // Decode the image file into a Bitmap sized to fill the View
//                bmOptions.inJustDecodeBounds = false;
//                bmOptions.inSampleSize = scaleFactor;
//                bmOptions.inPurgeable = true;
//
//                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
//                imgCamera.setImageBitmap(bitmap);
            }
            hasChange = true;
            // setFullImageFromFilePath(activity.getCurrentPhotoPath(), imgCamera);
        } else {
            Toast.makeText(getActivity(), "系统拍照失败，请联系管理员", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    protected void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        CameraActivity activity = (CameraActivity)getActivity();
        File f = new File(activity.getCurrentPhotoPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);
    }
    private Bitmap mCurrentAvatar = null;
    private void setFullImageFromFilePath(String imagePath, ImageView imageView) {
        // Get the dimensions of the View
        if(imgCamera.getWidth()==0){
            imgCamera.getLayoutParams().height = (int)InterviewerAdapter.convertDpToPixel(80,getActivity());
            imgCamera.getLayoutParams().width = (int)InterviewerAdapter.convertDpToPixel(80,getActivity());
            imgCamera.requestLayout();
        }
        int targetW = imgCamera.getWidth();
        int targetH = imgCamera.getHeight();

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
        imgCamera.setImageBitmap(bitmap);
    }
    boolean isStored = false;
    public void storeInterviewer(){
        if(edtFirstName.getText().length()>0 && edtLastName.getText().length()>0 && tvDate.getText().length()>0 & tvTime.getText().length()>0 & edtAdd.getText().length()>0)
        {
            int idxgender_selected = rdoGender.indexOfChild(rdoGender.findViewById(rdoGender.getCheckedRadioButtonId()));
            String strGender="";
            if(idxgender_selected==0) strGender="Female";
            else strGender="Male";
            Person pr = new Person();
            pr.setFirstName(edtFirstName.getText().toString());
            pr.setLastName(edtLastName.getText().toString());
            pr.setBirthDay(tvTime.getText().toString());
            pr.setTime(tvDate.getText().toString());
            pr.setAdd(edtAdd.getText().toString());
            pr.setGender(strGender);
            CameraActivity activity = (CameraActivity)getActivity();
            pr.setAvatar(activity.getCurrentPhotoPath());
            if(hasChange)
            {
                db.createPerson(pr);
                hasChange = false;
                isStored = true;
            }


            ShowMessage("person information stored ", 1);
        }else{ ShowMessage("请您填写全部信息",0); return;}
    }
    private ContentResolver mContentResolver;
    private final int IMAGE_MAX_SIZE = 512;
    private Bitmap getBitmap(Uri uri) {
        InputStream in = null;
        Bitmap returnedBitmap = null;
        try {
            in = mContentResolver.openInputStream(uri);
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = mContentResolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            //First check
            ExifInterface ei = new ExifInterface(uri.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    returnedBitmap = rotateImage(bitmap, 90);
                    //Free up the memory
                    bitmap.recycle();
                    bitmap = null;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    returnedBitmap = rotateImage(bitmap, 180);
                    //Free up the memory
                    bitmap.recycle();
                    bitmap = null;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    returnedBitmap = rotateImage(bitmap, 270);
                    //Free up the memory
                    bitmap.recycle();
                    bitmap = null;
                    break;
                default:
                    returnedBitmap = bitmap;
            }
            return returnedBitmap;
        } catch (FileNotFoundException e) {
            Log.e("ImageCrop", e.getMessage());
        } catch (IOException e) {
            Log.e("ImageCrop", e.getMessage());
        }
        return null;
    }
    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}

