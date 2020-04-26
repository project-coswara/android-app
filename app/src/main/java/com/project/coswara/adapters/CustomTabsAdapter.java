package com.project.coswara.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.project.coswara.Constants;
import com.project.coswara.activities.DetailsActivity;
import com.project.coswara.fragments.DisclaimerFragment;
import com.project.coswara.fragments.HealthStatusFragment;
import com.project.coswara.fragments.MetadataFragment;
import com.project.coswara.model.HealthData;
import com.project.coswara.model.Metadata;

public class CustomTabsAdapter extends FragmentStatePagerAdapter {
    private final int numOfTabs;

    private final DetailsActivity.MetadataUpdate metadataCallback;
    private final DetailsActivity.HealthDataUpdate healthDataCallback;
    private final DetailsActivity.NavigateTabs switchTabsCallback;
    private final Metadata metadata;
    private final HealthData healthData;

    public CustomTabsAdapter(@NonNull FragmentManager fm, int behavior,
                             DetailsActivity.MetadataUpdate metadataCallback,
                             DetailsActivity.HealthDataUpdate healthDataCallback,
                             DetailsActivity.NavigateTabs switchTabsCallback,
                             Metadata metadata, HealthData healthData) {
        super(fm, behavior);
        this.numOfTabs = behavior;
        this.metadataCallback = metadataCallback;
        this.healthDataCallback = healthDataCallback;
        this.switchTabsCallback = switchTabsCallback;
        this.metadata = metadata;
        this.healthData = healthData;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new MetadataFragment(metadataCallback, switchTabsCallback, metadata);
            case 2:
                return new HealthStatusFragment(healthDataCallback, switchTabsCallback, healthData);
            case 0:
            default:
                return new DisclaimerFragment(switchTabsCallback);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Constants.TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        return this.numOfTabs;
    }
}
