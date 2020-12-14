package com.ketekmall.ketekmall.adapter;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ketekmall.ketekmall.pages.Fragment_Buying;
import com.ketekmall.ketekmall.R;
import com.ketekmall.ketekmall.pages.Fragment_Selling;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static final int[] TAB_TITLES = new int[]{R.string.buying, R.string.selling};
    public SectionsPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }
    private final Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NotNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Fragment_Buying();
                break;
            case 1:
                fragment = new Fragment_Selling();
                break;
        }
        return Objects.requireNonNull(fragment);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}