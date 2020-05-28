package com.project.coswara.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.coswara.BuildConfig;
import com.project.coswara.util.Constants;
import com.project.coswara.R;
import com.project.coswara.util.Utils;
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
    private FirebaseFirestore db;
    private String uid;

    private Metadata metadata = null;
    private HealthData healthData = null;

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

    public interface SubmitForm {
        void submit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String[] COUNTRIES = new String[]{getResources().getString(R.string.country), "India", "China", "France", "Italy", "Spain", "United Kingdom", "United States", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua And Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas The", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Republic Of The Congo", "Democratic Republic Of The Congo", "Cook Islands", "Costa Rica", "Cote DIvoire (Ivory Coast)", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "External Territories of Australia", "Falkland Islands", "Faroe Islands", "Fiji Islands", "Finland", "France", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia The", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guernsey and Alderney", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and McDonald Islands", "Honduras", "Hong Kong S.A.R.", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea North", "Korea South", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau S.A.R.", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Man (Isle of)", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands Antilles", "Netherlands The", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestinian Territory Occupied", "Panama", "Papua new Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Island", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russia", "Rwanda", "Saint Helena", "Saint Kitts And Nevis", "Saint Lucia", "Saint Pierre and Miquelon", "Saint Vincent And The Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Smaller Territories of the UK", "Solomon Islands", "Somalia", "South Africa", "South Georgia", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard And Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad And Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks And Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City State (Holy See)", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (US)", "Wallis And Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"};

        metadata = Utils.getStoredMetadata(this);
        if (metadata == null) metadata = new Metadata();

        healthData = Utils.getStoredHealthData(this);
        if (healthData == null) healthData = new HealthData();

        db = FirebaseFirestore.getInstance();
        uid = FirestoreDB.getFirebaseUser().getUid();

        Log.d(TAG, "uid: " + uid);
        Toast.makeText(this, Constants.INFO_CONTEXT, Toast.LENGTH_LONG).show();

        viewPager = (CustomViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        CustomTabsAdapter tabsAdapter = new CustomTabsAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(), metadata, healthData, new MetadataUpdate() {
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
        }, new SubmitForm(){

            @Override
            public void submit() {
                if (validate()) {
                    sendToFirebase();
                }else{
                    Toast.makeText(DetailsActivity.this, "Please fill all required data",
                            Toast.LENGTH_SHORT).show();
                }
            }
        },  getResources().getStringArray(R.array.tab_titles),
                COUNTRIES,
                getResources().getStringArray(R.array.genders),
                getResources().getString(R.string.state));

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
    }

    //return false = enable, true = disable
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
    }

    @Override
    protected void onStop() {
        super.onStop();

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

        for (Map.Entry mapElement : healthData.getHealthMap().entrySet()) {
            String key = (String) mapElement.getKey();
            Boolean val = (Boolean) mapElement.getValue();
            if (val) userMetadata.put(key, true);
        }

        if(Utils.isDebugMode(DetailsActivity.this)) userMetadata.put("test", true);

        String dateString = Utils.getDateString();

        final HashMap<String, Object> userAppData = new HashMap<>();
        userAppData.put("fMD", true);
        userAppData.put("dS", dateString);
        userAppData.put("fV", BuildConfig.VERSION_CODE);
        if(Utils.isDebugMode(DetailsActivity.this)) userAppData.put("test", true);

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
