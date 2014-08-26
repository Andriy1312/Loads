package com.android.loads.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.android.loads.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerSactionsAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private ArrayList<String> mFiles;
    private List<Fragment> fragments;

    public PagerSactionsAdapter(FragmentManager fm, Context context, List<Fragment> fragments) {
        super(fm);
        mContext = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if(fragments != null) {
            if(position < fragments.size()) {
                fragment = fragments.get(position);
                if(position == 0 && mFiles != null){
                    Bundle args = new Bundle();
                    args.putStringArrayList("key", mFiles);
                    fragment.setArguments(args);
                }
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
        case 0:
            return mContext.getString(R.string.title_section1).toUpperCase(l);
        case 1:
            return mContext.getString(R.string.title_section2).toUpperCase(l);
        }
        return null;
    }

    public void setFilesArray(ArrayList<String> fileNames) {
        mFiles = fileNames;
    }
}