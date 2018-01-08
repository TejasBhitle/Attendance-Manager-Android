package thedorkknightrises.attendance.teacher.ui.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import thedorkknightrises.attendance.teacher.R;
import thedorkknightrises.attendance.teacher.models.Lecture;

/**
 * Created by tejas on 7/1/18.
 */

public class LectureRecyclerViewAdapter extends RecyclerView.Adapter<LectureRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Lecture> lectures;

    public LectureRecyclerViewAdapter(Context context, ArrayList<Lecture> lectures){
        this.context = context;
        this.lectures = lectures;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView lect_num;
        ViewHolder(View view){
            super(view);
                lect_num = view.findViewById(R.id.lecture_num);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_lecture,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lecture lecture = lectures.get(position);
        holder.lect_num.setText(lecture.getLect_no());
    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }
}
