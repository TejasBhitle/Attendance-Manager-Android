package thedorkknightrises.attendance.teacher.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import thedorkknightrises.attendance.teacher.Constants;
import thedorkknightrises.attendance.teacher.R;
import thedorkknightrises.attendance.teacher.models.Course;

/**
 * Created by tejas on 5/1/18.
 */

public class CourseDetailActivity extends AppCompatActivity {

    private Course course;
    private boolean isEnrollmentOn = true;
    private CardView enrollment_on_view;
    private RelativeLayout enrollment_off_view;
    private TextView lectures_empty_view;
    private RecyclerView lecturesRecyclerView;
    private FloatingActionButton lect_add_fab;

    private LinearLayout bottomSheet;
    private TextView date_textview,start_time_textview,end_time_textview;
    private Calendar startTime,endTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            course = bundle.getParcelable(Constants.COURSE);
        }

        ((TextView)findViewById(R.id.course_name)).setText(course.getName());
        ((TextView)findViewById(R.id.course_info)).setText(course.getInfoText(this));
        TextView descTextView = findViewById(R.id.course_desc);
        String desc = course.getDescription();
        if(desc.equals("")) descTextView.setVisibility(View.GONE);
        else descTextView.setText(desc);

        enrollment_on_view = findViewById(R.id.enrollment_on_view);
        enrollment_off_view = findViewById(R.id.enrollment_off_view);
        lectures_empty_view = findViewById(R.id.lectures_empty_view);
        lecturesRecyclerView = findViewById(R.id.lecturesRecyclerView);
        lect_add_fab = findViewById(R.id.lect_add_fab);
        bottomSheet = findViewById(R.id.bottom_sheet);

        lect_add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showButtonSheetDialog();
            }
        });

        findViewById(R.id.enrolledStudentsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_LECTURE_SPECIFIC,false);
                bundle.putInt(Constants.ID,Integer.parseInt(course.getCourse_id()));
                Intent intent = new Intent(CourseDetailActivity.this,StudentListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        findViewById(R.id.stop_enrollment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CourseDetailActivity.this)
                        .setTitle(getString(R.string.confirm_stop_enrollment))
                        .setMessage(getString(R.string.action_undone))
                        .setPositiveButton(getString(R.string.stop_enrollment), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                stopEnrollment();
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //do nothing
                            }
                        })
                .create()
                .show();

            }
        });
    }

    private void showButtonSheetDialog(){
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_lecture_create, null);

        startTime = Calendar.getInstance();
        endTime = Calendar.getInstance();

        date_textview = view.findViewById(R.id.date_textview);
        view.findViewById(R.id.date_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                date_textview.setText("Date: "+dayOfMonth+"/"+monthOfYear+1+"/"+year);
                                startTime.set(year,monthOfYear,dayOfMonth,0,0);
                                endTime.set(year,monthOfYear,dayOfMonth,0,0);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "DatePickerDialog");
            }
        });

        start_time_textview = view.findViewById(R.id.start_time_textview);
        view.findViewById(R.id.start_time_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd1 = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                startTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                startTime.set(Calendar.MINUTE,minute);
                                start_time_textview.setText("Start: "+hourOfDay+"/"+minute);
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd1.show(getFragmentManager(),"TimePickerDialog1");

            }
        });


        end_time_textview = view.findViewById(R.id.end_time_textview);
        view.findViewById(R.id.end_time_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd2 = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                endTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                endTime.set(Calendar.MINUTE,minute);
                                end_time_textview.setText("End: "+hourOfDay+"/"+minute);
                            }
                        },
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true
                );
                tpd2.show(getFragmentManager(),"TimePickerDialog1");

            }
        });

        view.findViewById(R.id.createLectureButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String LOG = "DateTime";
                Log.e(LOG,startTime.toString());
                Log.e(LOG,endTime.toString());
            }
        });

        BottomSheetDialog dialog = new BottomSheetDialog(CourseDetailActivity.this);
        dialog.setContentView(view);
        dialog.show();
    }

    //TODO: add API call
    private void stopEnrollment(){
        Toast.makeText(this,"TODO",Toast.LENGTH_SHORT).show();
        isEnrollmentOn = false;
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLayout();
    }

    private void updateLayout(){
        if (isEnrollmentOn) {
            enrollment_on_view.setVisibility(View.VISIBLE);
            enrollment_off_view.setVisibility(View.GONE);
            lect_add_fab.setVisibility(View.GONE);
        }
        else{
            enrollment_on_view.setVisibility(View.GONE);
            enrollment_off_view.setVisibility(View.VISIBLE);
            lect_add_fab.setVisibility(View.VISIBLE);
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
