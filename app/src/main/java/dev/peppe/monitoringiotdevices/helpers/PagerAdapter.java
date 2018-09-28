package dev.peppe.monitoringiotdevices.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dev.peppe.monitoringiotdevices.fragments.ChronologyFragment;
import dev.peppe.monitoringiotdevices.fragments.PublishFragment;
import dev.peppe.monitoringiotdevices.fragments.SubscribeFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ChronologyFragment tab1 = new ChronologyFragment();
                return tab1;
            case 1:
                PublishFragment tab2 = new PublishFragment();
                return tab2;
            case 2:
                SubscribeFragment tab3 = new SubscribeFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
