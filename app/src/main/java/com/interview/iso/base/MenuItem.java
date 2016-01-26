package com.interview.iso.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by lu.nguyenvan2 on 10/26/2015.
 */
public class MenuItem  {
    public String mTitle;
    public int mIconResource;
    public String mFragmentClass;
    public String identifier;
    public MenuItem(String title , int iconResource,  String fragmentClass,String identifier) {
        mTitle = title;
        mIconResource = iconResource;
        this.identifier = identifier;
        mFragmentClass = fragmentClass;
    }
    public String getTitle(){
        return mTitle;
    }
    public Fragment fragment(Context context) {
        if (mFragmentClass == null) {
            return null;
        }
        return Fragment.instantiate(context, "com.interview.iso.fragments." + mFragmentClass);
    }
    public static Fragment getFragment(Context context,String mFragmentClass) {
        if (mFragmentClass == null) {
            return null;
        }
        return Fragment.instantiate(context, "com.interview.iso.fragments." + mFragmentClass);
    }

}
