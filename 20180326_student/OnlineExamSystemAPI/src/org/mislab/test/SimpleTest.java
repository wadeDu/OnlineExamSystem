package org.mislab.test;

/**
 *
 * @author Max
 */
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mislab.api.Problem;
import org.mislab.api.Response;
import org.mislab.api.Role;
import org.mislab.api.Student;
import org.mislab.api.Teacher;
import org.mislab.api.User;

public class SimpleTest {
    private Student student;
    private Teacher teacher;
    
    private int courseId;
    private int examId;
    private int problemId;
    
    public SimpleTest() {
        student = null;
        teacher = null;
    }
    
    public void output(Response r) {
        System.out.println(r.getErrorCode().name());
        
        if (r.getContent() != null) {
            System.out.println(java.util.Arrays.toString(r.getContent().entrySet().toArray()));
        }
    }
    
    public void registerStudent() {
        Response res = User.register("student", "student", "袁同學", Role.Student,
            "4025s", "scott@student.com", 104, new byte[5]);
        
        if (res.success()) {
            System.out.println("student registers successful");
        } else {
            System.out.println("Error [" +res.getErrorCode()+"] student register FAILED");
        }
    }
    
    public void registerTeacher() {
        Response res = User.register("teacher", "teacher", "袁老師", Role.Teacher,
            "scott", "scott@teacher.com", 104, new byte[5]);
        
        if (res.success()) {
            System.out.println("teacher registers successful");
        } else {
            System.out.println(res.getErrorCode());
            System.out.println("Error! teacher registered fail!");
        }
    }
    
    public void loginStudent() {
        Response res = User.login("student", "student");
        
        if (res.success()) {
            student =  (Student) res.getContent().get("user");
        }
    }
    
    public void loginTeacher() {
        Response res = User.login("teacher", "teacher");
        
        if (res.success()) {
            teacher =  (Teacher) res.getContent().get("user");
        }
    }
    
    public void logout() {
        if (student != null) {
            student.logout();
            student = null;
        }
        
        if (teacher != null) {
            teacher.logout();
            teacher = null;
        }
    }
    
    public void createCourseAndExamAndProblems() {
        List<String> studentIds = new ArrayList<String>() {{
            add("4025s");
        }};
        
        Response res = teacher.createCourse("course1", 104, 1, studentIds);
        
        if (res.success()) {
            courseId = (int) res.getContent().get("courseId");
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            
            Response res2 = teacher.createExam(courseId, "exam1", timestamp, 10);
            
            if (res2.success()) {
                examId = (int) res2.getContent().get("examId");
                
                Problem problem = new Problem("p1", "desc for p1");
                problem.addTestData("input for p1.1", "output1.1");
                problem.addTestData("input for p1.2", "output222");
                
                Response res3 = teacher.createProblem(courseId, examId, problem);
                
                output(res3);
            }
            
            output(res2);
        } else {
            output(res);
        }
    }
    
    public void queryCourses() {
        output(teacher.queryCourses());
    }
    
    public void queryExam(int cid) {
        Response res = teacher.queryExams(cid);
        
        String time = ((Map) ((List) res.getContent().get("exams")).get(0)).get("startTime").toString();
        
        Long t = Double.valueOf(time).longValue();
        
        Timestamp ts = new Timestamp(t);
        
        System.out.println(ts);
    }
    
    public void queryCourseStudents(int cid) {
        output(teacher.queryStudents(cid));
    }
    
    public static void main(String[] args) {
        SimpleTest test = new SimpleTest();
        
        test.loginTeacher();
        
//        test.logout();
    }
}
