package com.project.coswara.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.coswara.util.LoadCountryStateData;
import com.project.coswara.R;
import com.project.coswara.util.Utils;
import com.project.coswara.model.FirestoreDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import static com.project.coswara.util.Constants.READ_MORE_URL;

public class IntroActivity extends AppCompatActivity {

    private static final String TAG = IntroActivity.class.getName();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);

        mAuth = FirebaseAuth.getInstance();

        LoadCountryStateData.loadDataMap(IntroActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setLocale(String localeName) {
        Locale myLocale = new Locale(localeName);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(this, IntroActivity.class);
        Utils.updateLanguage(IntroActivity.this, localeName);

        finish();
        overridePendingTransition( 0, 0);
        startActivity(refresh);
        overridePendingTransition( 0, 0);
    }

    private void languageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(IntroActivity.this);
        builder.setTitle("Choose Language")
                .setItems(R.array.languages_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0: setLocale("en"); break;
                            case 1: setLocale("hi"); break;
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_translate) {
            languageDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) FirestoreDB.setFirebaseUser(currentUser);
    }

    public void onProceedClick(View view) {
        startActivity(new Intent(this, AnonymousAuthActivity.class));
    }

    public void onReadMoreClick(View view) {
        Utils.openInBrowser(this, READ_MORE_URL);
    }
}
