package com.project.coswara.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.project.coswara.BuildConfig;
import com.project.coswara.R;
import com.project.coswara.Utils;
import com.project.coswara.adapters.CustomExpandableListAdapter;
import com.project.coswara.model.FirestoreDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;

import static com.project.coswara.Constants.INSTRUCTIONS_LIST;
import static com.project.coswara.Constants.OPTIONS_LIST;
import static com.project.coswara.Constants.VALUES_LIST;

public class RecordAudioActivity extends AppCompatActivity {

    private static final String TAG = RecordAudioActivity.class.getName();
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private final Handler handler = new Handler();

    private static int success = 0;

    public interface FinishRecordInterface {
        void handleDone();
    }

    public interface ExpandGroupInterface {
        void expandNextGroup(int currGroupPos);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted) finish();

    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_record_audio);

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        boolean[] statusList = Utils.getRecCompletion(this, VALUES_LIST.length);

        FirebaseUser user = FirestoreDB.getFirebaseUser();
        final String uid = user.getUid();

        System.out.println("auth: " + uid);

        final ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        ExpandableListAdapter expandableListAdapter = new CustomExpandableListAdapter(this,
                Arrays.asList(OPTIONS_LIST), Arrays.asList(VALUES_LIST), Arrays.asList(INSTRUCTIONS_LIST),
                statusList, getExternalCacheDir().getAbsolutePath(), storageRef,
                uid, db, new ExpandGroupInterface() {

            @Override
            public void expandNextGroup(int currGroupPos) {
                Utils.updateRecCompletion(RecordAudioActivity.this, currGroupPos, VALUES_LIST.length);
                expandableListView.collapseGroup(currGroupPos);
                expandableListView.expandGroup(currGroupPos + 1);
            }
        }, new FinishRecordInterface() {

            @Override
            public void handleDone() {
                HashMap<String, Object> updateAppData = new HashMap<>();
                updateAppData.put("cS", "done");
                updateAppData.put("cSD", Utils.getISODate());
                updateAppData.put("lV", BuildConfig.VERSION_CODE);

                HashMap<String, Object> userAppData = Utils.getStoredHashMap(RecordAudioActivity.this,
                        "userAppData");
                String dateStr = null;
                if (userAppData != null) {
                    dateStr = (String) userAppData.get("dS");
                }

                WriteBatch batch = db.batch();
                batch.update(
                        db.collection("USER_APPDATA")
                                .document(uid),
                        updateAppData
                );
                if (dateStr != null) {
                    batch.update(
                            db.collection("USER_METADATA")
                                    .document(dateStr)
                                    .collection("DATA")
                                    .document(uid),
                            "iF",
                            true
                    );
                }
                batch.commit()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "batch update success");
                                success++;
                                if (success == 2) {
                                    afterSubmit();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "batch update failed: ", e);
                                Toast.makeText(RecordAudioActivity.this,
                                        "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        expandableListView.setAdapter(expandableListAdapter);

        int firstFalsePos = Utils.getFirstMatchingPos(statusList, false, 0);
        expandableListView.expandGroup(firstFalsePos);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    private void afterSubmit() {
        Utils.clearRecCompletion(this);
        Utils.updateBooleanFlag(this, true, "rec_state");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(RecordAudioActivity.this, FeedbackActivity.class));
                finish();
            }
        }, 1500);
    }
}
