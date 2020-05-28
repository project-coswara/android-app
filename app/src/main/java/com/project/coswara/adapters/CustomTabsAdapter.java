package com.project.coswara.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
    private final DetailsActivity.SubmitForm submitFormCallback;
    private final String[] tabTitles;
    private final String[] countries;
    private final String[] genders;
    private final String defaultState;

    public CustomTabsAdapter(@NonNull FragmentManager fm, int behavior,
                             Metadata metadata, HealthData healthData,
                             DetailsActivity.MetadataUpdate metadataCallback,
                             DetailsActivity.HealthDataUpdate healthDataCallback,
                             DetailsActivity.NavigateTabs switchTabsCallback,
                             DetailsActivity.SubmitForm submitFormCallback,
                             String[] tabTitles, String[] countries, String[] genders,
                             String defaultState) {
        super(fm, behavior);
        this.numOfTabs = behavior;
        this.metadataCallback = metadataCallback;
        this.healthDataCallback = healthDataCallback;
        this.switchTabsCallback = switchTabsCallback;
        this.metadata = metadata;
        this.healthData = healthData;
        this.submitFormCallback = submitFormCallback;
        this.tabTitles = tabTitles;
        this.countries = countries;
        this.genders = genders;
        this.defaultState = defaultState;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new MetadataFragment(metadataCallback, switchTabsCallback, metadata,
                        countries, genders, defaultState);
            case 2:
                return new HealthStatusFragment(healthDataCallback, switchTabsCallback, healthData,
                        submitFormCallback);
            case 0:
            default:
                return new DisclaimerFragment(switchTabsCallback);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return this.numOfTabs;
    }
}
