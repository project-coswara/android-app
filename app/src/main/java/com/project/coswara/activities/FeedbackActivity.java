package com.project.coswara.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.coswara.util.Constants;
import com.project.coswara.R;
import com.project.coswara.util.Utils;
import com.project.coswara.model.FirestoreDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {

    private static final String TAG = FeedbackActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final EditText feedbackEdit = (EditText) findViewById(R.id.edittext_feedback);
        final Button feedbackSubmitBtn = (Button) findViewById(R.id.button_send_feedback);
        final TextView feedbackThanks = (TextView) findViewById(R.id.text_feedback_thanks);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_feedback);
        final Button newSessionBtn = (Button) findViewById(R.id.button_start_new_session);
        ImageView orgLeap = (ImageView) findViewById(R.id.image_leap_org);
        ImageView orgIISc = (ImageView) findViewById(R.id.image_iisc_org);
        ImageView orgCogknit = (ImageView) findViewById(R.id.image_cogknit_org);

        orgLeap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openInBrowser(FeedbackActivity.this, Constants.LEAP_URL);
            }
        });

        orgIISc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openInBrowser(FeedbackActivity.this, Constants.IISC_URL);
            }
        });

        orgCogknit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openInBrowser(FeedbackActivity.this, Constants.COGKNIT_URL);
            }
        });

        //check if feedback already given
        boolean givenFeedback = Utils.isSubmitted(this, "feedback");
        if(givenFeedback){
            feedbackEdit.setVisibility(View.GONE);
            feedbackSubmitBtn.setVisibility(View.GONE);
            feedbackThanks.setVisibility(View.VISIBLE);
        }

        newSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.clearAllPrefs(FeedbackActivity.this);
                FirestoreDB.setFirebaseUser(null);
                mAuth.signOut();
                finish();
            }
        });

        feedbackEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s != null && s.length() != 0){
                    feedbackSubmitBtn.setVisibility(View.VISIBLE);
                }else{
                    feedbackSubmitBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        feedbackSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackText = feedbackEdit.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                feedbackEdit.setVisibility(View.GONE);
                feedbackSubmitBtn.setVisibility(View.GONE);

                HashMap<String, Object> map = new HashMap<>();
                map.put("fb", feedbackText);
                map.put("uid", FirestoreDB.getFirebaseUser().getUid());
                if(Utils.isDebugMode(FeedbackActivity.this)) map.put("test", true);

                db.collection("FEEDBACK")
                        .add(map)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "feedback upload success");

                                //add to share prefs
                                Utils.updateBooleanFlag(FeedbackActivity.this, true, "feedback");

                                progressBar.setVisibility(View.GONE);
                                feedbackThanks.setVisibility(View.VISIBLE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "feedback upload failed", e);
                            }
                        });
            }
        });
    }

    public void onFacebookClick(View view) {
        Utils.openInBrowser(FeedbackActivity.this, Constants.FB_URL);
    }

    public void onTwitterClick(View view) {
        Utils.openInBrowser(FeedbackActivity.this, Constants.TWITTER_URL);
    }
}
