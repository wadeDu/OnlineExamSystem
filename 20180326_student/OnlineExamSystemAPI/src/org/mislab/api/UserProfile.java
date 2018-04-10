package org.mislab.api;

public class UserProfile {
    private final String userName;
    private final String studentId;
    private final String email;
    private final int graduateYear;
    
    public UserProfile(String userName, String studentId, String email,
            int graduateYear) {
        this.userName = userName;
        this.studentId = studentId;
        this.email = email;
        this.graduateYear = graduateYear;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public int getGraudateYear() {
        return graduateYear;
    }
}
