package com.project.coswara.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.coswara.R;
import com.project.coswara.util.Utils;
import com.project.coswara.model.FirestoreDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AnonymousAuthActivity extends AppCompatActivity {
    private static final String TAG = "AnonymousAuthActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (!FirestoreDB.isSignedIn()) {
            final FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirestoreDB.setFirebaseUser(user);

                                redirect();
                            } else {
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
                                Toast.makeText(AnonymousAuthActivity.this,
                                        "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        }
                    });
        } else {
            redirect();
        }
    }

    private void redirect() {
        if (Utils.isSubmitted(this, "details")) { // submitted details already
            if (Utils.isSubmitted(this, "rec_state")) { // submitted all recordings already
                startActivity(new Intent(this, FeedbackActivity.class));
            } else {
                startActivity(new Intent(this, RecordAudioActivity.class));
            }
        } else {
            startActivity(new Intent(this, DetailsActivity.class));
        }
        finish();
    }
}
