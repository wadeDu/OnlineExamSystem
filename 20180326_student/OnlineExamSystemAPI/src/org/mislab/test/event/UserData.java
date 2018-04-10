package org.mislab.test.event;

/**
 *
 * @author Max
 */
public class UserData {
    public String name;
    public String passwd;
    public boolean isAdmin;

    public UserData(String n, String pw, boolean adm) {
        name = n; passwd = pw; isAdmin = isAdmin;
    }
    public String toString() {
        return String.format("[%s, %s]", name, isAdmin ? "T" : "S");
    }
}

class StData extends UserData {
    public StData(String n, String pw) {
        super(n, pw, false);
    }
}

class TData extends UserData {
    public TData(String n, String pw) {
        super(n, pw, true);
    }
}

