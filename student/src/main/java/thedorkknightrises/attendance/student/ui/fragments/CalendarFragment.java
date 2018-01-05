package thedorkknightrises.attendance.student.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.co.recruit_mp.android.lightcalendarview.LightCalendarView;
import jp.co.recruit_mp.android.lightcalendarview.MonthView;
import thedorkknightrises.attendance.student.R;

public class CalendarFragment extends Fragment {
    private OnCalendarFragmentInteractionListener mListener;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        TextView titleText = view.findViewById(R.id.toolbar_title);
        titleText.setText(R.string.calendar);

        final TextView header = view.findViewById(R.id.calendar_header);
        LightCalendarView calendarView = view.findViewById(R.id.calendarView);
        final SimpleDateFormat format = new SimpleDateFormat("MMMM YYYY", Locale.getDefault());
        calendarView.setOnStateUpdatedListener(new LightCalendarView.OnStateUpdatedListener() {
            @Override
            public void onMonthSelected(Date date, MonthView monthView) {
                header.setText(format.format(date));
            }

            @Override
            public void onDateSelected(Date date) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCalendarFragmentInteractionListener) {
            mListener = (OnCalendarFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCalendarFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCalendarFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCalendarInteraction();
    }
}
