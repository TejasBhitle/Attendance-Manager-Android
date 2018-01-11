package thedorkknightrises.attendance.student.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import thedorkknightrises.attendance.student.Constants;
import thedorkknightrises.attendance.student.R;
import thedorkknightrises.attendance.student.models.Course;
import thedorkknightrises.attendance.student.ui.fragments.CalendarFragment;
import thedorkknightrises.attendance.student.ui.fragments.CourseListFragment;
import thedorkknightrises.attendance.student.ui.fragments.CoursesFragment;
import thedorkknightrises.attendance.student.ui.fragments.PreferenceFragment;
import thedorkknightrises.attendance.student.util.RestClient;

public class MainActivity extends AppCompatActivity
        implements CourseListFragment.OnListFragmentInteractionListener,
        CalendarFragment.OnCalendarFragmentInteractionListener,
        PreferenceFragment.OnPreferenceFragmentInteractionListener {
    SharedPreferences preferences, userPrefs;
    BottomNavigationView bottomNavigationView;
    private boolean backPressFlag = false;

    private static final String LOG = "MainActivity";

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
        } if (!preferences.getBoolean(Constants.IS_VIDEO_ADDED, false)) {
            // TODO: Uncomment when server capable of face recognition
            //startActivity(new Intent(MainActivity.this, FaceRecordActivity.class));
            //finish();
        } else {
            final FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.frameLayout, new CoursesFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.bottom_nav_home:
                            fm.beginTransaction()
                                    .replace(R.id.frameLayout, new CoursesFragment())
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .commit();
                            break;
                        case R.id.bottom_nav_calendar:
                            fm.beginTransaction()
                                    .replace(R.id.frameLayout, new CalendarFragment())
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
        sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
    }

    private void sendRegistrationToServer(String token){
        Log.e(LOG,"registration token :- "+token);
        SharedPreferences userPrefs = this.getSharedPreferences(Constants.USER_PREFS, Context.MODE_PRIVATE);
        String JWT = userPrefs.getString(Constants.TOKEN, "");
        Header[] headers = new Header[]{new BasicHeader("Authorization", "JWT "+JWT)};

        RequestParams params = new RequestParams();
        params.put("registration_id",token);
        params.put("cloud_message_type","FCM");
        Log.e(LOG,"sendRegistrationToServer :- "+token);
        RestClient.post("token/create/",headers,params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.e(LOG,response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e(LOG,response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
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

    @Override
    public void onListFragmentInteraction(Course item, boolean enrolled) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.COURSE, item);
        bundle.putBoolean(Constants.IS_ENROLLED, enrolled);
        Intent intent = new Intent(MainActivity.this, CourseDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCalendarInteraction() {
        Toast.makeText(this, "Calendar tapped!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogout() {
        userPrefs.edit().clear().apply();
        preferences.edit().putBoolean(Constants.LOGGED_IN, false).apply();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

}
