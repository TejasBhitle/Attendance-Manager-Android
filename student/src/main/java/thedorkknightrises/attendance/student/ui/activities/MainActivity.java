package thedorkknightrises.attendance.student.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import thedorkknightrises.attendance.student.Constants;
import thedorkknightrises.attendance.student.R;
import thedorkknightrises.attendance.student.models.Course;
import thedorkknightrises.attendance.student.ui.fragments.CourseFragment;
import thedorkknightrises.attendance.student.util.RestClient;

public class MainActivity extends AppCompatActivity implements CourseFragment.OnListFragmentInteractionListener {
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(Constants.APP_PREFS, MODE_PRIVATE);

        if (preferences.getBoolean(Constants.DEBUG_SERVER_ENABLED, false)) {
            RestClient.setBaseUrl(preferences.getString(Constants.DEBUG_SERVER_URL, ""));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!preferences.getBoolean(Constants.LOGGED_IN, false)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).start();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frameLayout, CourseFragment.newInstance(1))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();

    }

    @Override
    public void onListFragmentInteraction(Course item) {
        Toast.makeText(this, item.getName(), Toast.LENGTH_SHORT).show();
    }
}
