package org.mislab.test.event;

import org.mislab.api.Response;
import org.mislab.api.Teacher;
import org.mislab.api.User;
import org.mislab.api.event.EventAction;
import org.mislab.api.event.EventType;
import org.mislab.api.event.OnlineExamEvent;
import org.mislab.api.event.OnlineExamEventListener;
import org.mislab.api.event.OnlineExamEventManager;

/**
 *
 * @author Max
 */
public class LoginNotify implements OnlineExamEventListener {
    private Teacher tcher = null;
    private OnlineExamEventManager evMgr = null;
    
    public LoginNotify() {
        evMgr = OnlineExamEventManager.getInstance();
        evMgr.addEventListener(this, EventType.User, EventAction.Login);
        evMgr.addEventListener(this, EventType.User, EventAction.Logout);
    }
    
    public void handleOnlineExamEvent(OnlineExamEvent e) {
        System.out.println(String.format("rcv: %s", e));
    }
    
    public void loginTeacher() {
        Response res = User.login("max", "max");
        
        if (res.success()) {
            System.out.println("max login success!");
            tcher =  (Teacher) res.getContent().get("user");
        } else {
            System.out.println("max login FAILED!!!");
        }
    }

    public void logout() {
        if (tcher != null) {
            tcher.logout();
            tcher = null;
        } else {
            System.out.println("T: max - logout error");
        }
    }
    
    public static void main(String[] args) {
        LoginNotify ln = new LoginNotify();
        ln.loginTeacher();
    }
}
