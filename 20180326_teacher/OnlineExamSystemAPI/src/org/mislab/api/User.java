package org.mislab.api;

import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class User {
    protected static final Logger LOGGER;
    protected final static OkHttpClient CLIENT;
    
    protected final int userId;
    protected UserProfile profile;
    protected SocketServer socketServer;
    
    static {
        LOGGER = Logger.getLogger(JsonObject.class.getName());
        CLIENT = new OkHttpClient();
    }
    
    public User(int uid) {
        this.userId = uid;
        this.socketServer = new SocketServer();
        
        initProfile();
    }
    
    private void initProfile() {
        Response res = getProfile();
        
        if (res.success()) {
            Map content = res.getContent();
            
            profile = new UserProfile(
                    content.get("userName").toString(),
                    content.get("studentId").toString(),
                    content.get("email").toString(),
                    (int) content.get("graduateYear"));
        }
    }
    
    private void startSocketServer() {
        socketServer.start();
    }
    
    public static Response login(String userName, String password) {
        JsonObject json = new JsonObject();
        
        json.addProperty("userName", userName);
        json.addProperty("password", password);
        json.addProperty("ip", Utils.getIPAddress());
        
        Response res = Utils.post(CLIENT, "/user/login", json);
        
        if (res.success()) {
            Map content = res.getContent();
            int uid = (int) content.get("userId");
            
            User user = null;
            
            switch (content.get("role").toString()) {
                case "teacher":
                    res.addContent("user", user = new Teacher(uid));
                    break;
                case "student":
                    res.addContent("user", user = new Student(uid));
                    break;
            }
            
            if (user != null) {
                user.startSocketServer();
            }
        }
        
        return res;
    }
    
    public static Response register(String userName, String password, String name,
        Role role, String studentId, String email, int graduateYear, byte[] image) {
        JsonObject json = new JsonObject();
        
        json.addProperty("userName", userName);
        json.addProperty("password", password);
        json.addProperty("name", name);
        json.addProperty("role", role.toString());
        json.addProperty("studentId", studentId);
        json.addProperty("email", email);
        json.addProperty("graduateYear", graduateYear);
        
        try {
            json.addProperty("profilePhoto", new String(image, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            json.addProperty("profilePhoto", "");
            
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        return Utils.post(CLIENT, "/user/register", json);
    }
    
    public static Response forgetPassword(String userName) {
        JsonObject json = new JsonObject();
        
        json.addProperty("userName", userName);
        
        return Utils.post(CLIENT, "/user/forget-password", json);
    }
    
    public Response resetPassword(String oldPassword, String newPassword) {
        JsonObject json = new JsonObject();
        
        //yao start
        String uri = String.format("/user/%d/reset-password",
                userId);
        //yao end
        
        json.addProperty("userId", userId);
        json.addProperty("oldPassword", oldPassword);
        json.addProperty("newPassword", newPassword);
        
        return Utils.post(CLIENT, uri, json);
    }
    
    public Response getProfile() {
        String uri = String.format("/user/%d", userId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response getProfilePhoto() {
        String uri = String.format("/user/%d/photo", userId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response resetProfilePhoto(byte[] image) {
        String uri = String.format("/user/%d/photo", userId);
        
        JsonObject json = new JsonObject();
        
        json.addProperty("userId", userId);
        
        try {
            json.addProperty("profilePhoto", new String(image, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            json.addProperty("profilePhoto", "");
            
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        return Utils.post(CLIENT, uri, json);
    }
    
    public Response logout() {
        String uri = String.format("/user/%d/logout", userId);
        
        socketServer.closeServer();
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response queryCourses() {
        return Utils.get(CLIENT, "/course");
    }
    
    public Response queryExams(int courseId) {
        String uri = String.format("/course/%d/exam", courseId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response queryProblems(int courseId, int examId) {
        String uri = String.format("/course/%d/exam/%d/problem", 
            courseId, examId);
        
        return Utils.get(CLIENT, uri);
    }
    
    public Response queryHistoryMessages(int courseId, int examId) {
        String uri = String.format("/course/%d/exam/%d/chat/history",
                courseId, examId);
        
        return Utils.get(CLIENT, uri);
    }

    public Response sendMessage(int courseId, int examId, String message) {
        String uri = String.format("/course/%d/exam/%d/chat/send-message",
                courseId, examId);
        
        JsonObject json = new JsonObject();
        
        json.addProperty("userId", userId);
        //System.out.println("＠User.java userId= "+userId);
        json.addProperty("message", message);
        
        return Utils.post(CLIENT, uri, json);
    }
    
    public Response getTestData(int courseId, int examId, int problemId) {
        String uri = String.format("/course/%d/exam/%d/problem/%d/testdata",
                courseId, examId, problemId);
        
        return Utils.get(CLIENT, uri);
    }
}
