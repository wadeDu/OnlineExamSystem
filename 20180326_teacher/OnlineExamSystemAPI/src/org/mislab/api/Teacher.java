package org.mislab.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Timestamp;
import java.util.List;
import org.mislab.api.Problem.TestData;

public class Teacher extends User {
    public Teacher(int uid) {
        super(uid);
    }
    
    public Response createCourse(String courseName, int year, int semester,
            List<String> studentIds) {
        JsonObject json = new JsonObject();
        
        json.addProperty("userId", super.userId);
        json.addProperty("courseName", courseName);
        json.addProperty("year", year);
        json.addProperty("semester", semester);
        
        JsonArray studentIdsJson = new JsonArray();
        
        for (String studentId: studentIds) {
            studentIdsJson.add(studentId);
        }
        
        json.add("studentIds", studentIdsJson);
        
        return Utils.post(CLIENT, "/course/create", json);
    }
    
    public Response removeCourse(int courseId) {
        String uri = String.format("/course/%d/remove", courseId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response createExam(int courseId, String examName, Timestamp begin,
        int durationInMinutes) {
        String uri = String.format("/course/%d/exam/add", courseId);
        
        JsonObject json = new JsonObject();
        
        json.addProperty("examName", examName);
        json.addProperty("beginTime", begin.toString());
        json.addProperty("duration", durationInMinutes);
        
        return Utils.post(CLIENT, uri, json);
    }
    
    public Response removeExam(int courseId, int examId) {
        String uri = String.format("/course/%d/exam/%d/remove", courseId, examId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response createProblem(int courseId, int examId, Problem problem) {
        String uri = String.format("/course/%d/exam/%d/problem/add",
            courseId, examId);
        
        JsonObject json = new JsonObject();
        
        json.addProperty("title", problem.getTitle());
        json.addProperty("description", problem.getDescription());
        
        JsonArray tdArr = new JsonArray();
        
        for (TestData td: problem.getTestData()) {
            JsonObject tdJson = new JsonObject();
            
            tdJson.addProperty("input", td.input);
            tdJson.addProperty("output", td.output);
        }
        
        json.add("testdata", tdArr);
        
        return Utils.post(CLIENT, uri, json);
    }
    
    public Response queryStudents(int courseId) {
        String uri = String.format("/course/%d/student", courseId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response getSnapshots(int courseId, int examId, String studentId) {
        String uri = String.format("/course/%d/exam/%d/student/%s/snapshot",
                courseId, examId, studentId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response requestCurrentSnapshot(int courseId, int examId,
            String studentId) {
        String uri = String.format("/course/%d/exam/%d/student/%s/request-current-snapshot",
                courseId, examId, studentId);
        
        return Utils.get(CLIENT, uri);
    }
    
    //20170915 add on snapshot parameters;
    public Response startMonitor(int courseId, int examId, String studentId, float scale, int freq) {
    //public Response startMonitor(int courseId, int examId, String studentId) {
        System.out.println("@Teacher.startMonitor() send startMonitor uri to Django with parameters");
        
        String uri = String.format("/course/%d/exam/%d/student/%s/monitor/start",
                courseId, examId, studentId);
        
        JsonObject json = new JsonObject();
        
        //json.addProperty("userName", super.userName);
        System.out.println("@Teacher.startMonitor() teacher.userId= "+super.userId);
        json.addProperty("proctorId", super.userId);
        json.addProperty("scale", scale);
        json.addProperty("freq", freq);
        
        return Utils.post(CLIENT, uri, json);
    }
    
    public Response stopMonitor(int courseId, int examId, String userId) {
        String uri = String.format("/course/%d/exam/%d/student/%s/monitor/stop",
                courseId, examId, userId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response requestTextRec(int courseId, int examId, String userId) {
        String uri = String.format("/course/%d/exam/%d/student/%s/monitor/requestTextRec",
                courseId, examId, userId);
        System.out.println("@Teacher.requestTextRec() userId= "+userId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response pauseExam(int courseId, int examId) {
        String uri = String.format("/course/%d/exam/%d/pause",
                courseId, examId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response resumeExam(int courseId, int examId) {
        String uri = String.format("/course/%d/exam/%d/resume",
                courseId, examId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response haltExam(int courseId, int examId) {
        String uri = String.format("/course/%d/exam/%d/halt",
                courseId, examId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response extendExam(int courseId, int examId, int extendMinutes) {
        String uri = String.format("/course/%d/exam/%d/extend",
                courseId, examId);
        
        JsonObject json = new JsonObject();
        
        json.addProperty("extendMinutes", extendMinutes);
        
        return Utils.post(CLIENT, uri, json);
    }
    
    public Response getSourceCode(int courseId, int examId, int problemId,
            String studentId) {
        String uri = String.format("/course/%d/exam/%d/problem/%d/student/%s/source-code",
                courseId, examId, problemId, studentId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response getStudentResult(int courseId, int examId, int problemId,
            String studentId) {
        String uri = String.format("/course/%d/exam/%d/problem/%d/student/%s/test-result",
                courseId, examId, problemId, studentId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response scoreAndComment(int courseId, int examId, int problemId,
            String studentId, int score, String comment) {
        String uri = String.format("/course/%d/exam/%d/problem/%d/student/%s/score-and-comment",
                courseId, examId, problemId, studentId);
        
        JsonObject json = new JsonObject();
        
        json.addProperty("score", score);
        json.addProperty("comment", comment);
        
        return Utils.post(CLIENT, uri, json);
    }
    
    public Response finishScoring(int courseId, int examId) {
        String uri = String.format("/course/%d/exam/%d/finish-scoring",
                courseId, examId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response queryExamAllResults(int courseId, int examId) {
        String uri = String.format("/course/%d/exam/%d/result",
                courseId, examId);
        
        return Utils.get(CLIENT, uri);
    }
}
