package org.mislab.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class Test {
    private Student student;
    private Teacher teacher;

    private int courseId;
    private int examId;
    private int problemId;

    public Test() {
        student = null;
        teacher = null;
    }

    public void output(Response r) {
        System.out.println(r.getErrorCode().name());

        if (r.getContent() != null) {
            System.out.println(java.util.Arrays.toString(r.getContent().entrySet().toArray()));
        }
    }

    public void registerStudent() throws IOException {
        File file = new File("/home/yao/Desktop/User-blue-icon.png");
        BufferedImage b = ImageIO.read(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.flush();
        byte[] byteImage = baos.toByteArray();
        
        
        Response res = User.register("yuan", "yuan", "袁同學", Role.Student,
            "4027s", "scott@student.com", 105, byteImage);
        
        if (res.success()){
            System.out.println(res.getContent());
        }
        baos.close();
        
    }

    public void registerTeacher() {
        Response res = User.register("teacher", "teacher", "袁老師", Role.Teacher,
            "scott", "scott@teacher.com", 104, new byte[5]);
    }

    public void loginStudent() {
        Response res = User.login("yuan", "yuan");

        if (res.success()) {
            student =  (Student) res.getContent().get("user");
            System.out.println(res.getContent());
        }
    }

    public void loginTeacher() {
        Response res = User.login("ryan", "ryan");

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
    
    public void queryProblems() {
        output(teacher.queryProblems(1, 1));
    }
    
    public void getPhoto() {
        Response res = student.getProfilePhoto();
        System.out.println(res.getContent());
    }

    public static void main(String[] args) throws IOException {
        Test test = new Test();
       // test.registerStudent();
//        test.loginStudent();
    //    test.getPhoto();
  //      test.logout();

    }
}
