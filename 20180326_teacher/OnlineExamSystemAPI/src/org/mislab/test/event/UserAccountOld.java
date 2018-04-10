package org.mislab.test.event;

import java.util.ArrayList;
import java.util.Map;
import org.mislab.api.Response;
import org.mislab.api.Teacher;
import org.mislab.api.User;
import org.mislab.api.event.OnlineExamEventListener;
import org.mislab.api.event.OnlineExamEventManager;

/**
 *
 * @author Max
 */
public abstract class UserAccountOld implements OnlineExamEventListener {
    protected String name, password;
    protected UserAccountOld user;
    
    protected OnlineExamEventManager evMgr = null;    
    
    public UserAccountOld(String n, String pw) {
        name = n; password = pw;
        
        evMgr = OnlineExamEventManager.getInstance();
        setupEventListener();
    }

    public UserAccountOld login() {
        Response res = User.login(name, password);
        
        if (res.success()) {
            System.out.println(name+" login success!");
            user = (UserAccountOld) res.getContent().get("user");
        } else {
            System.out.println(name+" login FAILED!!!");
        }
        return user;
    }
    
    public void logout() {
        if (user != null) {
            user.logout();
            user = null;
        } else {
            System.out.println(String.format("%s logout FAILS", name));
        }
    }

    public int getCourseId(Teacher t) {
        Response resp = t.queryCourses();
        int courseId = -1;
        
        if (resp.success()) {
            ArrayList courses = (ArrayList) resp.getContent().get("courses");
            
            courseId = (int) ((Map)courses.get(0)).get("courseId");
        } else {
            System.out.println("query course FAILED!");            
        }
        return courseId;
    }
    
    public int getExamId(Teacher t, int cid) {
        Response resp = t.queryExams(cid);
        int examId = -1;
        
        if (resp.success()) {
            ArrayList exams = (ArrayList) resp.getContent().get("exams");
            examId = (int) ((Map)exams.get(0)).get("examId");
        } else {
            System.out.println("query course FAILED!");            
        }
        return examId;
    }
        
    public String getName() { return name; }
    public abstract void setupEventListener();
    
    public String toString() { return "<"+name+">"; }
}
