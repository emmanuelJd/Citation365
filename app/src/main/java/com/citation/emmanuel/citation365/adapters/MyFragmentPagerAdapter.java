package com.citation.emmanuel.citation365.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by emmanuel on 05/10/2015.
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> listFragment;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return this.listFragment.get(position);
    }

    @Override
    public int getCount() {
        return this.listFragment.size();
    }


}
