package org.mislab.api.event;

public enum EventAction {
    //common event;
    Login,
    Logout,
    Register,
    NewMessage,
    Submit,
    Pause,
    Resume,
    Halt,
    Extend,
    Attend,
    
    //Snapshot event;
    Start,
    Stop,
    RequestSnapshot,
    SendSnapshot,
    
    //key event;
    KeyEvent,
    KeyText,
    RequestTextRec,
    SendTextRec

}
