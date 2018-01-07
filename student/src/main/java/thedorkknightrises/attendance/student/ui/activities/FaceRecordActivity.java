package thedorkknightrises.attendance.student.ui.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import thedorkknightrises.attendance.student.Constants;
import thedorkknightrises.attendance.student.R;
import thedorkknightrises.attendance.student.util.RestClient;

public class FaceRecordActivity extends AppCompatActivity {
    CameraView cameraView;
    FloatingActionButton fab;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_record);

        cameraView = findViewById(R.id.camera);
        fab = findViewById(R.id.captureButton);
        progress = findViewById(R.id.progress);

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(File video) {
                super.onVideoTaken(video);
                upload(video);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                fab.setEnabled(false);
                fab.setBackgroundColor(Color.DKGRAY);
                File file = new File(getCacheDir().getPath() + File.pathSeparator + "face");
                cameraView.startCapturingVideo(file, 6000);
                CountDownTimer timer = new CountDownTimer(6000, 1000) {
                    @Override
                    public void onTick(long l) {
                        progress.setProgress((int) (l/1000));
                    }

                    @Override
                    public void onFinish() {
                        progress.setVisibility(View.GONE);
                    }
                };
                timer.start();
            }
        });
    }

    private void upload(File video) {

        SharedPreferences userPrefs = getSharedPreferences(Constants.USER_PREFS, MODE_PRIVATE);
        RequestParams params = new RequestParams();
        Header[] headers = new Header[]{new BasicHeader("Authorization", "JWT " + userPrefs.getString(Constants.TOKEN, ""))};

        try {
            params.put(Constants.VIDEO, video);
            int student_id = userPrefs.getInt(Constants.ID, 0);
            params.put(Constants.ID, student_id);

            final ProgressDialog progressDialog = new ProgressDialog(FaceRecordActivity.this);
            progressDialog.setCancelable(false);

            RestClient.setTimeOut(30);

            RestClient.post("student/upload_data/", headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    progressDialog.setMessage(getString(R.string.uploading));
                    progressDialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    progressDialog.dismiss();
                    Log.d(getLocalClassName(), response.toString());
                    finish();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }
}
