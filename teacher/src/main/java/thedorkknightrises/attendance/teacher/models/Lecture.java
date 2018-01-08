package thedorkknightrises.attendance.teacher.models;

/**
 * Created by tejas on 7/1/18.
 */

public class Lecture {

    private String comment, start_time, end_time;
    private int lect_no, lect_id, course_id;

    public Lecture(String start_time, String end_time, int lect_id, int course_id, String comment, int lect_no ) {
        this.comment = comment;
        this.start_time = start_time;
        this.end_time = end_time;
        this.lect_no = lect_no;
        this.lect_id = lect_id;
        this.course_id = course_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getLect_no() {
        return lect_no;
    }

    public void setLect_no(int lect_no) {
        this.lect_no = lect_no;
    }

    public int getLect_id() {
        return lect_id;
    }

    public void setLect_id(int lect_id) {
        this.lect_id = lect_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }
}
