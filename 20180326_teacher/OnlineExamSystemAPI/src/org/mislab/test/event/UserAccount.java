package org.mislab.test.event;

import java.util.ArrayList;
import java.util.Map;
import org.mislab.api.Response;
import org.mislab.api.Student;
import org.mislab.api.Teacher;
import org.mislab.api.User;
import org.mislab.api.event.OnlineExamEventManager;

/**
 *
 * @author Max
 */
public abstract class UserAccount implements UserAccountInt {
    protected String name, password;
    protected User user;
    
    protected OnlineExamEventManager evMgr = null; 
    
    public UserAccount(Teacher teacher) {
        user= teacher;
        evMgr = OnlineExamEventManager.getInstance();
        setupEventListener();
    }   
    
    public UserAccount(Student student) {
        this.user= student;
        evMgr = OnlineExamEventManager.getInstance();
        setupEventListener();
    }  
 
//    public UserAccount(String n, String pw) {
//        name = n; password = pw;
//        
//        evMgr = OnlineExamEventManager.getInstance();
//        setupEventListener();
//    }

//    public User login() {
//        Response res = User.login(name, password);
//        
//        if (res.success()) {
//            System.out.println(name+" login success!");
//            user = (User) res.getContent().get("user");
//        } else {
//            System.out.println(name+" login FAILED!!!");
//        }
//        return user;
//    }
    
    public void logout() {
        if (user != null) {
            user.logout();
            user = null;
        } else {
            System.out.println(String.format("%s logout FAILS", name));
        }
    }

    /**
     * get course id
     * @return course id
     */
    @Override
    public int getCourseId() {
        Response resp = user.queryCourses();
        int courseId = -1;
        
        if (resp.success()) {
            ArrayList courses = (ArrayList) resp.getContent().get("courses");
            
            courseId = (int) ((Map)courses.get(0)).get("courseId");
        } else {
            System.out.println("query course FAILED!");            
        }
        return courseId;
    }

    /**
     * get exam id by courseId (cid)
     * @param cid Course id
     * @return exam id
     */
    @Override
    public int getExamId(int cid) {
        Response resp = user.queryExams(cid);
        int examId = -1;
        
        if (resp.success()) {
            ArrayList exams = (ArrayList) resp.getContent().get("exams");
            examId = (int) ((Map)exams.get(0)).get("examId");
        } else {
            System.out.println("query course FAILED!");            
        }
        return examId;
    }

    public User getUser() { return user; }
    
    public String getName() { 
        name = user.getProfile().getContent().get("name").toString();
        return name; 
    }
    public String getPassword() { return password; }
    
    public String getId() {
        String id = user.getProfile().getContent().get("id").toString();
        return id;
    }
    
}
