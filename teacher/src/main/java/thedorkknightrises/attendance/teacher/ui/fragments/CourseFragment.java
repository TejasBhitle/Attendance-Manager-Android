package thedorkknightrises.attendance.teacher.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import thedorkknightrises.attendance.teacher.Constants;
import thedorkknightrises.attendance.teacher.R;
import thedorkknightrises.attendance.teacher.models.Course;
import thedorkknightrises.attendance.teacher.ui.activities.CreateCourseActivity;
import thedorkknightrises.attendance.teacher.ui.adapters.CourseRecyclerViewAdapter;
import thedorkknightrises.attendance.teacher.util.RestClient;

public class CourseFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    SharedPreferences userPrefs;
    Context context;
    private View view;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ProgressBar progress;
    private ArrayList<Course> courses = new ArrayList<>();

    public CourseFragment() {
    }

    public static CourseFragment newInstance(int columnCount) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course_list, container, false);
        recyclerView = view.findViewById(R.id.list);
        progress = view.findViewById(R.id.progress);
        context = view.getContext();

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                CourseRecyclerViewAdapter adapter = (CourseRecyclerViewAdapter) recyclerView.getAdapter();
                if (!"".equals(newText)) {
                    ArrayList<Course> temp = new ArrayList<>();
                    for (Course c : courses) {
                        if (c.getName().toLowerCase().contains(newText.toLowerCase())
                                || c.getDescription().toLowerCase().contains(newText.toLowerCase())) {
                            temp.add(c);
                        }
                    }
                    adapter.updateList(temp);
                } else {
                    // Show all results is search bar is empty
                    adapter.updateList(courses);
                }
                return false;
            }
        });

        userPrefs = view.getContext().getSharedPreferences(Constants.USER_PREFS, Context.MODE_PRIVATE);

        getCourses();

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(getActivity(),CreateCourseActivity.class));
            }
        });

        return view;
    }

    public void getCourses() {
        Header[] headers = new Header[]{new BasicHeader("Authorization", "JWT " + userPrefs.getString(Constants.TOKEN, ""))};

        RequestParams params = new RequestParams();
        params.put("teacher_id", userPrefs.getInt(Constants.ID, 0));
        RestClient.get("course/getByTeacherId/", headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                progress.setVisibility(View.GONE);

                courses.clear();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject item = response.getJSONObject(i);
                        Course course = new Course(item.getString("course_id"),
                                item.getString("dept_id"),
                                item.getString("teacher_id"),
                                item.getString("name"),
                                item.getString("description"),
                                item.getString("academic_yr"),
                                item.getString("year"),
                                item.getString("updated"),
                                item.getString("created"));
                        courses.add(course);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (!courses.isEmpty()) {

                    if (mColumnCount <= 1) {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                    }
                    recyclerView.setAdapter(new CourseRecyclerViewAdapter(courses, mListener));

                    view.findViewById(R.id.emptyText).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.emptyText).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progress.setVisibility(View.GONE);
                try {
                    Toast.makeText(getActivity(), "Failed to fetch courses\n(" + errorResponse.getString("detail") + ")", Toast.LENGTH_SHORT).show();
                    Log.e("CourseFragment", errorResponse.toString());
                } catch (JSONException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Course item);
    }
}
