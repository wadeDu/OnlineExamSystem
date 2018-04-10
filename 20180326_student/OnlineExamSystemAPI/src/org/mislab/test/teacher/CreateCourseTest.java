package org.mislab.test.teacher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.mislab.api.Response;
import org.mislab.api.Teacher;
import org.mislab.api.User;

/**
 *
 * @author Max
 */
public class CreateCourseTest {
    private Teacher t;
    
    public void loginTeacher() {
        Response res = User.login("teacher", "teacher");
        
        if (res.success()) {
            t =  (Teacher) res.getContent().get("user");
        }
    }
    
    public void createCourse() {
        List studentList = Arrays.asList("4025s","4026s");
        Response r = t.createCourse("程式設計", 2016, 1, studentList);
        if (r.success()) {
            System.out.println("程式設計' id: "+r.getContent().get("courseId"));
        } else {
            System.out.println("Error: create 程式設計 ["+ r.getErrorCode());
        }
    }
    
    public void removeCourse() {
        Response r = t.removeCourse(2);
        if (r.success()) {
            System.out.println("course 2 removed successfully");
        } else {
            System.out.println("Error! remove 程式設計 ["+ r.getErrorCode());
        }
    }
    
    public void queryCourses() {
        Response r = t.queryCourses();
        if (r.success()) {
            System.out.println("Courses: "+r.getContent());
            List courseList = (List) r.getContent().get("courses");
            Map course = (Map) courseList.get(0);
            System.out.println("CourseID: " + course.get("courseId"));
        } else {
            System.out.println("Error: queryCourses ["+r.getErrorCode()+"]");
        }
    }
    
    public static void main(String[] args) {
        CreateCourseTest cc = new CreateCourseTest();
        
        cc.loginTeacher();
//        cc.createCourse();
        cc.queryCourses();
//        cc.removeCourse();
        
    }
}
