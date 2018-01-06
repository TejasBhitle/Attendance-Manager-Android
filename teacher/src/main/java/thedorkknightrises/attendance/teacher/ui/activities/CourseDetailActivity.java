package thedorkknightrises.attendance.teacher.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import thedorkknightrises.attendance.teacher.Constants;
import thedorkknightrises.attendance.teacher.R;
import thedorkknightrises.attendance.teacher.data.AttendanceDbHelper;
import thedorkknightrises.attendance.teacher.models.BiMap;
import thedorkknightrises.attendance.teacher.models.Course;

/**
 * Created by tejas on 5/1/18.
 */

public class CourseDetailActivity extends AppCompatActivity {

    private Course course;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            course = bundle.getParcelable(Constants.COURSE);
            if(course != null)
                Toast.makeText(this, course.getName(), Toast.LENGTH_SHORT).show();
        }

        setTitle("");

        ((TextView)findViewById(R.id.course_name)).setText(course.getName());
        ((TextView)findViewById(R.id.course_info)).setText(course.getInfoText(this));
        ((TextView)findViewById(R.id.course_desc)).setText(course.getDescription());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
