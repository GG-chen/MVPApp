package com.chen.mvp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by chen on 2017/9/7.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<String> mTitles;
    private List<Fragment> fragments;


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mTitles = new ArrayList<>();
        fragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }


    public void setItems(List<Fragment> fragments, List<String> mTitles) {
        this.fragments = fragments;
        this.mTitles = mTitles;
        notifyDataSetChanged();
    }

    public void setItems(List<Fragment> fragments, String[] mTitles) {
        this.fragments = fragments;
        this.mTitles = Arrays.asList(mTitles);
        notifyDataSetChanged();
    }

    public void addItem(Fragment fragment, String title) {
        fragments.add(fragment);
        mTitles.add(title);
        notifyDataSetChanged();
    }

    public void delItem(int position) {
        mTitles.remove(position);
        fragments.remove(position);
        notifyDataSetChanged();
    }

    public int delItem(String title) {
        int index = mTitles.indexOf(title);
        if (index != -1) {
            delItem(index);
        }
        return index;
    }

    public void swapItems(int fromPos, int toPos) {
        Collections.swap(mTitles, fromPos, toPos);
        Collections.swap(fragments, fromPos, toPos);
        notifyDataSetChanged();
    }

    public void modifyTitle(int position, String title) {
        mTitles.set(position, title);
        notifyDataSetChanged();
    }
}
