package com.project.coswara.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.coswara.BuildConfig;
import com.project.coswara.Constants;
import com.project.coswara.R;
import com.project.coswara.Utils;
import com.project.coswara.adapters.CustomTabsAdapter;
import com.project.coswara.adapters.CustomViewPager;
import com.project.coswara.model.FirestoreDB;
import com.project.coswara.model.HealthData;
import com.project.coswara.model.Metadata;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getName();
    private Button submitBtn;
    private FirebaseFirestore db;
    private String uid;

    private Metadata metadata = null;
    private HealthData healthData = null;

    private ProgressBar progressBar;
    private CustomViewPager viewPager;
    private LinearLayout tabStrip;

    public interface MetadataUpdate {
        void updateMetadata(Metadata metadata);
    }

    public interface HealthDataUpdate {
        void updateHealthData(HealthData data);
    }

    public interface NavigateTabs {
        void changeTab(int currPos, int direction);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        metadata = Utils.getStoredMetadata(this);
        if (metadata == null) metadata = new Metadata();

        healthData = Utils.getStoredHealthData(this);
        if (healthData == null) healthData = new HealthData();

        db = FirebaseFirestore.getInstance();
        uid = FirestoreDB.getFirebaseUser().getUid();

        Log.d(TAG, "uid: " + uid);
        Toast.makeText(this, Constants.INFO_CONTEXT, Toast.LENGTH_LONG).show();

        progressBar = (ProgressBar) findViewById(R.id.progress_details_submit);
        viewPager = (CustomViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        CustomTabsAdapter tabsAdapter = new CustomTabsAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(), new MetadataUpdate() {
            @Override
            public void updateMetadata(Metadata updatedData) {
                metadata = updatedData;
                toggleSubmit();
            }
        }, new HealthDataUpdate() {

            @Override
            public void updateHealthData(HealthData data) {
                healthData = data;
                toggleSubmit();
            }
        }, new NavigateTabs(){

            @Override
            public void changeTab(int currPos, int direction) {
                int newPos = currPos + direction; //direction is +1 is move ahead, -1 if move backwards
                viewPager.setCurrentItem(newPos);
            }
        }, metadata, healthData);

        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabStrip = (LinearLayout) tabLayout.getChildAt(0);

        submitBtn = (Button) findViewById(R.id.meta_data_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    submitBtn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);

                    sendToFirebase();
                }
            }
        });
    }

    //false = enable, true = disable
    private void toggleTabLayoutHeader(final boolean enable){
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return !enable;
                }
            });
        }
    }

    private boolean validate() {
        return metadata.isComplete() && healthData.isComplete();
    }

    private void toggleSubmit() {
        viewPager.setPagingEnabled(validate());
        toggleTabLayoutHeader(validate());
        submitBtn.setEnabled(validate());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Utils.storeMetadata(this, metadata);
        Utils.storeHealthData(this, healthData);
    }

    private void afterSubmit() {
        //clean up shared prefs before redirect
        Utils.clearMetadata(this);
        Utils.clearHealthData(this);
        Utils.updateBooleanFlag(this, true, "details"); //submitted details

        startActivity(new Intent(DetailsActivity.this, RecordAudioActivity.class));
        finish();
    }

    private void sendToFirebase() {
        final HashMap<String, Object> userMetadata = new HashMap<>();
        userMetadata.put("dT", "android");
        userMetadata.put("fV", BuildConfig.VERSION_CODE);
        userMetadata.put("a", metadata.getAge());
        userMetadata.put("g", metadata.getGender());
        userMetadata.put("ep", metadata.getEnglishProficient() == 1 ? "y" : "n");
        userMetadata.put("rU", metadata.getReturningUser() == 1 ? "y" : "n");
        userMetadata.put("l_c", metadata.getCountry());
        userMetadata.put("l_s", metadata.getState());
        userMetadata.put("covid_status", healthData.getCurrentStatus());
        if (metadata.getLocality() != null && !metadata.getLocality().isEmpty())
            userMetadata.put("l_l", metadata.getLocality());

        if(Utils.isTestModeOn(DetailsActivity.this)) userMetadata.put("test", true);

        for (Map.Entry mapElement : healthData.getHealthMap().entrySet()) {
            String key = (String) mapElement.getKey();
            Boolean val = (Boolean) mapElement.getValue();
            if (val) userMetadata.put(key, true);
        }

        String dateString = Utils.getDateString();

        final HashMap<String, Object> userAppData = new HashMap<>();
        userAppData.put("fMD", true);
        userAppData.put("dS", dateString);
        userAppData.put("fV", BuildConfig.VERSION_CODE);
        if(Utils.isTestModeOn(DetailsActivity.this)) userAppData.put("test", true);

        WriteBatch batch = db.batch();
        batch.set(
                db.collection("USER_METADATA")
                        .document(dateString)
                        .collection("DATA")
                        .document(uid),
                userMetadata
        );
        batch.set(
                db.collection("USER_APPDATA")
                        .document(uid),
                userAppData
        );
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "details batch upload success");

                        //store in shared prefs
                        Utils.storeHashMap(DetailsActivity.this, userAppData, "userAppData");

                        afterSubmit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "details batch upload failed: ", e);
                    }
                });
    }
}
