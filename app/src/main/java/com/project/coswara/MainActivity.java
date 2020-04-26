package com.project.coswara;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.coswara.activities.AnonymousAuthActivity;
import com.project.coswara.model.FirestoreDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.project.coswara.Constants.INTRO_CONTENT;
import static com.project.coswara.Constants.READ_MORE_URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Constants.isProd) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.inflateMenu(R.menu.menu_main);
        }

        mAuth = FirebaseAuth.getInstance();

        TextView introText = (TextView) findViewById(R.id.intro_text_view);
        Utils.enableLinksInText(introText, INTRO_CONTENT);

        LoadCountryStateData.loadDataMap(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!Constants.isProd) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);

            MenuItem item = (MenuItem) menu.getItem(0);
            if (item != null)
                item.setChecked(Utils.isTestModeOn(MainActivity.this));

            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!Constants.isProd) {
            if (item.getItemId() == R.id.menu_item_enable_test) {
                item.setChecked(!item.isChecked());
                Utils.setTestMode(MainActivity.this, item.isChecked());

                Log.d(TAG, "menu item checked: " + item.isChecked());
                return true;
            }
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
