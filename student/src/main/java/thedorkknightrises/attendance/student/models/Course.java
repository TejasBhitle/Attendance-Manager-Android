package thedorkknightrises.attendance.student.models;

/**
 * Created by Samriddha on 03-01-2018.
 */

public class Course {
    private String course_id, dept_id, teacher_id, name, description, academic_yr, year, updated, created;

    public Course(String course_id, String dept_id, String teacher_id, String name, String description, String academic_yr, String year, String updated, String created) {
        this.course_id = course_id;
        this.dept_id = dept_id;
        this.teacher_id = teacher_id;
        this.name = name;
        this.description = description;
        this.academic_yr = academic_yr;
        this.year = year;
        this.updated = updated;
        this.created = created;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAcademic_yr() {
        return academic_yr;
    }

    public void setAcademic_yr(String academic_yr) {
        this.academic_yr = academic_yr;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
