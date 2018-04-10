package org.mislab.test.student;

import java.util.List;
import org.mislab.api.Response;
import org.mislab.api.Role;
import org.mislab.api.User;

/**
 *
 * @author Max
 */
public class CreateStudentTest {

    public void registerStudent(String username, String passwd, String name, Role role, 
            String id, String email, int year, byte[] img) {
        
        Response res = User.register(username, passwd, name, role, id, email, year, img);
        
        if (res.success()) {
            System.out.println("student "+ username+" registers successful");
        } else {
            System.out.println("student "+ username+", Error [" +res.getErrorCode()+"] student register FAILED");
        }
    }
    
    public void createCourse(String name, List<String> studentList) {
        
    }
        
    public static void main(String[] args) {
        CreateStudentTest cst = new CreateStudentTest();

        cst.registerStudent("alan", "alan", "Alan Walker", Role.Student,
            "100", "alan@student.com", 104, new byte[5]);
        cst.registerStudent("bob", "bob", "Bob Bush", Role.Student,
            "101", "bob@student.com", 104, new byte[5]);
        cst.registerStudent("cann", "cann", "Cann Curry", Role.Student,
            "102", "cann@student.com", 104, new byte[5]);
        
    }
}
