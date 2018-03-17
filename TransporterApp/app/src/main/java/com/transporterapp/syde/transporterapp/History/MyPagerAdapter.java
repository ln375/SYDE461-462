package com.transporterapp.syde.transporterapp.History;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by chari on 2018-03-16.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {
    HistOverviewMonthly overviewMonthly = new HistOverviewMonthly();
    HistOverviewFrag overviewFrag = new HistOverviewFrag();
    HistOverviewDaily overviewDaily = new HistOverviewDaily();

    private String tabTitles[] = new String[]{"Daily", "Monthly"};


    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return tabTitles.length;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return HistOverviewDaily.newInstance(0, "Daily");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return HistOverviewMonthly.newInstance(1, "Monthly");
            default:
                return new HistOverviewFrag();
        }
    }

    // Here we can finally safely save a reference to the created
    // Fragment, no matter where it came from (either getItem() or
    // FragmentManger). Simply save the returned Fragment from
    // super.instantiateItem() into an appropriate reference depending
    // on the ViewPager position.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                overviewDaily = (HistOverviewDaily) createdFragment;
                break;
            case 1:
                overviewMonthly = (HistOverviewMonthly) createdFragment;
                break;
            default:
                overviewFrag = (HistOverviewFrag) createdFragment;
        }
        return createdFragment;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    private String getFragmentTag(int viewPagerId, int fragmentPosition)
    {
        return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
    }

}
