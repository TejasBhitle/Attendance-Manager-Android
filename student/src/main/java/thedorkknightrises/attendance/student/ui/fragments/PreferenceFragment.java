package thedorkknightrises.attendance.student.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import thedorkknightrises.attendance.student.R;
import thedorkknightrises.attendance.student.ui.activities.IntroActivity;

public class PreferenceFragment extends Fragment {
    private OnPreferenceFragmentInteractionListener mListener;

    public PreferenceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference, container, false);

        TextView intro = view.findViewById(R.id.replay_intro_button);
        TextView about = view.findViewById(R.id.about_button);
        TextView logout = view.findViewById(R.id.logout_button);

        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IntroActivity.class));
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LibsBuilder()
                        .withAboutAppName(getString(R.string.app_name))
                        .withAboutDescription(getString(R.string.app_description))
                        .withAboutIconShown(true)
                        .withAboutVersionShown(true)
                        .withActivityStyle(Libs.ActivityStyle.LIGHT)
                        .start(getActivity());
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLogout();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPreferenceFragmentInteractionListener) {
            mListener = (OnPreferenceFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPreferenceFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPreferenceFragmentInteractionListener {
        void onLogout();
    }

}
