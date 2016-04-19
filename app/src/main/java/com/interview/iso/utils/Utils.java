package com.interview.iso.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * @author GT
 */
public class Utils {

    public static Uri getImageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    public static String getVersionApp(Context context) {
        try {
            return context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName + " (" + context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode + ")";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static boolean isAppInBackground(final Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);

        // Check the top Activity against the list of Activities contained in the Application's package.
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            try {
                PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_ACTIVITIES);
                for (ActivityInfo activityInfo : pi.activities) {
                    if (topActivity.getClassName().equals(activityInfo.name)) {
                        return false;
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                return false; // Never happens.
            }
        }
        return true;
    }

    public static byte[] textToPNG(String str_text) {
        float textSize = 30;
        String text = "testing";
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
        return image;
    }
}
