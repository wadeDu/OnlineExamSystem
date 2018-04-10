package org.mislab.test.event;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Max
 */
public class UserConsole {
    private int timeInSec;
    
    private final static ScheduledExecutorService scheduler = 
            Executors.newScheduledThreadPool(1);
    
    public UserConsole() {
        timeInSec = 0;
    }
    
    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }
    
    /**
     * schedule a runnable task in absolute time specified in sec
     * @param r a Runnable
     * @param sec absolute time in seconds
     */
    public void scheduledTaskAbsTime(Runnable r, int sec) {
        scheduler.schedule(r, sec, TimeUnit.SECONDS);
    }
    
    /**
     * schedule a runnable task in relative time. That is, 1 means 1 sec
     * from now on.
     * @param r a Runnable
     * @param inc time increment in seconds
     */
    public void scheduledTaskRelTime(Runnable r, int inc) {
        timeInSec += inc;
        scheduler.schedule(r, timeInSec, TimeUnit.SECONDS);
    }
}