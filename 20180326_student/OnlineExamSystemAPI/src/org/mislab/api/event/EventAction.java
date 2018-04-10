package org.mislab.api.event;

public enum EventAction {
    
    //common event;
    Login,
    Logout,
    Attend,
    Register,
    NewMessage,
    Submit,
    Pause,
    Resume,
    Halt,
    Extend,
    
    //Snapshot event;
    Start,
    Stop,
    RequestSnapshot,
    SendSnapshot,
    
    //keyText event;
    KeyEvent,
    KeyText,
    RequestTextRec,
    SendTextRec

}
