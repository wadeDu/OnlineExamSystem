package org.mislab.test.event;

import org.mislab.api.User;
import org.mislab.api.event.OnlineExamEventListener;

/**
 *
 * @author Max
 */
public interface UserAccountInt extends OnlineExamEventListener {
    //public User login();
    public void logout();
    
    public void setupEventListener();
    
    public int getCourseId();
    public int getExamId(int cid);
    
    public String getName();
    
}
