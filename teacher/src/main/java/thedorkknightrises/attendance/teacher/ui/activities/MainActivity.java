package thedorkknightrises.attendance.teacher.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import thedorkknightrises.attendance.teacher.Constants;
import thedorkknightrises.attendance.teacher.R;
import thedorkknightrises.attendance.teacher.models.Course;
import thedorkknightrises.attendance.teacher.ui.fragments.CourseFragment;
import thedorkknightrises.attendance.teacher.ui.fragments.PreferenceFragment;
import thedorkknightrises.attendance.teacher.util.RestClient;

public class MainActivity extends AppCompatActivity implements CourseFragment.OnListFragmentInteractionListener, PreferenceFragment.OnPreferenceFragmentInteractionListener {
    SharedPreferences preferences, userPrefs;
    BottomNavigationView bottomNavigationView;
    private boolean backPressFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(Constants.APP_PREFS, MODE_PRIVATE);
        userPrefs = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        if (preferences.getBoolean(Constants.DEBUG_SERVER_ENABLED, false)) {
            RestClient.setBaseUrl(preferences.getString(Constants.DEBUG_SERVER_URL, ""));
        }

        if (!preferences.getBoolean(Constants.LOGGED_IN, false)) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            final FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.frameLayout, CourseFragment.newInstance(1))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.bottom_nav_home:
                            fm.beginTransaction()
                                    .replace(R.id.frameLayout, CourseFragment.newInstance(1))
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();
                            break;
                        case R.id.bottom_nav_settings:
                            fm.beginTransaction()
                                    .replace(R.id.frameLayout, new PreferenceFragment())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();
                            break;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void onListFragmentInteraction(Course item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogout() {
        userPrefs.edit().clear().apply();
        preferences.edit().putBoolean(Constants.LOGGED_IN, false).apply();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressFlag) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.back_press_prompt, Toast.LENGTH_SHORT).show();
            backPressFlag = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressFlag = false;
                }
            }, 2000);
        }
    }
}
