package org.mislab.api;

import com.google.gson.JsonObject;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import javafx.scene.input.KeyEvent;

public class Student extends User {

    public Student(int uid) {
        super(uid);
    }

    public Response attendExam(int courseId, int examId) {
        String uri = String.format("/course/%d/exam/%d/student/%d/attend",
                courseId, examId, super.userId);

        return Utils.get(CLIENT, uri);
    }

    public Response submitSourceCode(int courseId, int examId, int problemId,
            String sourceCode, String sourceCodeFileName) {
        
        String uri = String.format("/course/%d/exam/%d/problem/%d/student/%s/source-code",
                courseId, examId, problemId, super.profile.getStudentId());

        JsonObject json = new JsonObject();

        json.addProperty("sourceCode", sourceCode);
        json.addProperty("sourceCodeFileName", sourceCodeFileName);

        return Utils.post(CLIENT, uri, json);
    }

    public Response sendSnapshot(int courseId, int examId, String studentId, byte[] snapshot) {
        String uri = String.format("/course/%d/exam/%d/student/%s/snapshot",
                courseId, examId, studentId);

        JsonObject json = new JsonObject();

        try {
            json.addProperty("snapshot", new String(snapshot, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            json.addProperty("snapshot", "");

            LOGGER.log(Level.SEVERE, null, ex);
        }

        return Utils.post(CLIENT, uri, json);
    }

    public Response sendTestResult(int courseId, int examId, int problemId,
            String result) {
        String uri = String.format("/course/%d/exam/%d/problem/%d/student/%s/test-result",
                courseId, examId, problemId, super.profile.getStudentId());

        return Utils.get(CLIENT, uri);
    }

    public Response sendKeyEvent(int courseId, int examId, KeyEvent keyEvent) {
        String uri = String.format("/course/%d/exam/%d/student/%s/monitor/send-key-event",
                courseId, examId, super.profile.getStudentId());
        JsonObject json = new JsonObject();

        json.addProperty("keyCode", keyEvent.getCode().ordinal());
        json.addProperty("keyEventType", keyEvent.getEventType().getName());
        json.addProperty("keyChar", keyEvent.getText());
        

        return Utils.post(CLIENT, uri, json);
    }

    public Response queryExamResults(int courseId, int examId) {
       String uri = String.format("/course/%d/exam/%d/student/%s/score-and-comment",
                courseId, examId, super.profile.getStudentId());

        return Utils.get(CLIENT, uri);
    }
}
