package org.mislab.api.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OnlineExamEventManager {
 
   class OnlineExamEventType {
        EventType evType;
        EventAction evAction;
        
        public OnlineExamEventType(EventType t, EventAction a) {
            evType = t; evAction = a;
        }
        
        @Override
        public boolean equals(Object o) {
            OnlineExamEventType ev = (OnlineExamEventType) o;
            return this.evType == ev.evType && this.evAction == ev.evAction;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + Objects.hashCode(this.evType);
            hash = 29 * hash + Objects.hashCode(this.evAction);
            return hash;
        }
        
        @Override
        public String toString() {
            return String.format("<k:%s:%s>", evType, evAction);
        }
    }
     
    private static OnlineExamEventManager INSTANCE = null;
    
    public static OnlineExamEventManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OnlineExamEventManager();
        }
        
        return INSTANCE;
    }
    
    private final Map< OnlineExamEventType, List<OnlineExamEvent> > listenerMgr;

    /**
     * Initializes the listenerMgr <event->listener(ArrayList)> table
     */
    private OnlineExamEventManager() {
        listenerMgr = new HashMap<>();
        
        listenerMgr.put( new OnlineExamEventType(EventType.User, EventAction.Login),new ArrayList<>() );
        listenerMgr.put( new OnlineExamEventType(EventType.User, EventAction.Logout),new ArrayList<>() );
        listenerMgr.put( new OnlineExamEventType(EventType.User, EventAction.Register),new ArrayList<>() );
        
        listenerMgr.put( new OnlineExamEventType(EventType.Chat, EventAction.NewMessage), new ArrayList<>());
        
        listenerMgr.put( new OnlineExamEventType(EventType.Exam, EventAction.Extend), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Exam, EventAction.Halt), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Exam, EventAction.Pause), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Exam, EventAction.Resume), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Exam, EventAction.Start), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Exam, EventAction.Stop), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Exam, EventAction.Submit), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Exam, EventAction.Attend), new ArrayList<>());        

        listenerMgr.put( new OnlineExamEventType(EventType.Monitor, EventAction.Start), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Monitor, EventAction.Stop), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Monitor, EventAction.RequestSnapshot), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Monitor, EventAction.SendSnapshot), new ArrayList<>());
                
        listenerMgr.put( new OnlineExamEventType(EventType.Monitor, EventAction.KeyEvent), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Monitor, EventAction.KeyText), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Monitor, EventAction.RequestTextRec), new ArrayList<>());
        listenerMgr.put( new OnlineExamEventType(EventType.Monitor, EventAction.SendTextRec), new ArrayList<>());

    }
    
    /**
     * This method is for the client to receive those interested events from 
     * the server.
     * @param listener the destination for incoming events
     * @param evType event action
     * @param evAction event type 
     */
    public synchronized void addEventListener(OnlineExamEventListener listener, 
            EventType evType, EventAction evAction) {
        
        OnlineExamEventType ev = new OnlineExamEventType(evType, evAction);
        ((ArrayList)listenerMgr.get(ev)).add(listener);
    }

    /**
     * Client uses this method to stop receiving events from the server.
     * @param listener
     * @param evAct
     * @param evType 
     */
    public synchronized void removeEventListener(OnlineExamEventListener listener,
            EventAction evAct, EventType evType) {
        
        OnlineExamEventType ev = new OnlineExamEventType(evType, evAct);
        ((ArrayList)listenerMgr.get(ev)).remove(listener);
    }

    /**
     * OnlineExamEventManager delivers events for each listener subscribes 
     * the specified eventAction.
     * @param e the incoming event
     */
    public synchronized void fireEvent(OnlineExamEvent e) {
//        get listeners subscribed for e
        OnlineExamEventType ev = new OnlineExamEventType(e.getType(), e.getAction());

        ArrayList<OnlineExamEventListener> listeners = (ArrayList) listenerMgr.get(ev);
        
        for (OnlineExamEventListener listener: listeners) {
//            System.out.println("***fire: " + listener);
            listener.handleOnlineExamEvent(e);
        }
    }    
}
