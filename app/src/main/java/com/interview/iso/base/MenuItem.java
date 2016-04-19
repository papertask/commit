package com.interview.iso.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by lu.nguyenvan2 on 10/26/2015.
 */
public class MenuItem  {
    public String mTitle;
    public String mFragmentClass;
    public String identifier;
    public boolean hasSubItems;

    public MenuItem(String title , String fragmentClass, String identifier, int subitems) {
        mTitle = title;
        this.identifier = identifier;
        mFragmentClass = fragmentClass;

        if (subitems != 0) {
            hasSubItems = false;
        } else {
            hasSubItems = true;
        }
    }

    public String getTitle(){
        return mTitle;
    }

    public boolean hasSubItem() {
        return hasSubItems;
    }

    public Fragment fragment(Context context) {
        if (mFragmentClass == null) {
            return null;
        }
        return Fragment.instantiate(context, "com.interview.iso.fragments." + mFragmentClass);
    }

    public static Fragment getFragment(Context context, String mFragmentClass) {
        if (mFragmentClass == null) {
            return null;
        }
        return Fragment.instantiate(context, "com.interview.iso.fragments." + mFragmentClass);
    }

}
