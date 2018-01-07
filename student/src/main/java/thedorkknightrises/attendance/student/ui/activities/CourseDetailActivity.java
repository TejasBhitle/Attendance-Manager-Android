package thedorkknightrises.attendance.student.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import thedorkknightrises.attendance.student.Constants;
import thedorkknightrises.attendance.student.R;
import thedorkknightrises.attendance.student.models.Course;
import thedorkknightrises.attendance.student.util.RestClient;

/**
 * Created by tejas on 5/1/18.
 */

public class CourseDetailActivity extends AppCompatActivity {

    SharedPreferences userPrefs;
    private Course course;
    private boolean enrolled, isEnrollmentOn = true;
    private Button enrollButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        userPrefs = getSharedPreferences(Constants.USER_PREFS, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            course = bundle.getParcelable(Constants.COURSE);
            enrolled = bundle.getBoolean(Constants.IS_ENROLLED);
        }

        ((TextView) findViewById(R.id.course_name)).setText(course.getName());
        ((TextView) findViewById(R.id.course_info)).setText(course.getInfoText(CourseDetailActivity.this));
        TextView descTextView = findViewById(R.id.course_desc);
        enrollButton = findViewById(R.id.enrollButton);
        String desc = course.getDescription();
        if (desc.equals("")) descTextView.setVisibility(View.GONE);
        else descTextView.setText(desc);

        if (enrolled) {
            enrollButton.setText(R.string.enrolled);
            enrollButton.setEnabled(false);
        } else {
            enrollButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Header[] headers = new Header[]{new BasicHeader("Authorization", "JWT " + userPrefs.getString(Constants.TOKEN, ""))};

                    RequestParams params = new RequestParams();
                    int student_id = userPrefs.getInt(Constants.ID, 0);
                    params.put("course_id", course.getCourse_id());
                    params.put(Constants.ID, student_id);
                    RestClient.post("course/enrollStudentInCourse/", headers, params, new JsonHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            enrollButton.setText(R.string.please_wait);
                            enrollButton.setEnabled(false);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            enrollButton.setText(R.string.enrolled);
                            Toast.makeText(CourseDetailActivity.this, R.string.enrolled, Toast.LENGTH_SHORT).show();
                            Log.e("Enrolment", response.toString());
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            enrollButton.setText(R.string.enrolled);
                            Toast.makeText(CourseDetailActivity.this, R.string.enrolled, Toast.LENGTH_SHORT).show();
                            Log.e("Enrolment", response.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            enrollButton.setText(R.string.enroll);
                            enrollButton.setEnabled(true);
                            try {
                                Toast.makeText(CourseDetailActivity.this, "Failed to enroll\n(" + errorResponse.getString("detail") + ")", Toast.LENGTH_SHORT).show();
                                Log.e("Enrolment", errorResponse.toString());
                            } catch (JSONException | NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
