package thedorkknightrises.attendance.teacher.ui.activities;

import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

import thedorkknightrises.attendance.teacher.BuildConfig;
import thedorkknightrises.attendance.teacher.R;
import thedorkknightrises.attendance.teacher.ui.fragments.DebugConfigFragment;

/**
 * Created by Samriddha on 04-01-2018.
 */

public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.app_name)
                .description("Welcome")
                .image(R.mipmap.ic_launcher)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        if (BuildConfig.DEBUG) {
            addSlide(new FragmentSlide.Builder()
                    .background(R.color.colorPrimary)
                    .backgroundDark(R.color.colorPrimaryDark)
                    .fragment(new DebugConfigFragment())
                    .build());
        }

    }
}
