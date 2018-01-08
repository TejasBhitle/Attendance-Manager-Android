package thedorkknightrises.attendance.teacher.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import thedorkknightrises.attendance.teacher.Constants;
import thedorkknightrises.attendance.teacher.R;
import thedorkknightrises.attendance.teacher.models.Lecture;

/**
 * Created by tejas on 8/1/18.
 */

public class LectureDetailActivity extends AppCompatActivity {

    private Lecture lecture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            lecture = bundle.getParcelable(Constants.LECTURE);
            setTitle("Lecture "+lecture.getLect_no());
        }
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
