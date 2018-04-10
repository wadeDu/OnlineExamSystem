package org.mislab.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mislab.api.event.OnlineExamEvent;
import org.mislab.api.event.OnlineExamEventManager;

public class SocketServer extends Thread {
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(SocketServer.class.getName());
    }
    
    private ServerSocket server;
    private final OnlineExamEventManager eventManager;
    
    public SocketServer() {
        try {
            server = new ServerSocket(Config.PORT);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        eventManager = OnlineExamEventManager.getInstance();
    }
    
    @Override
    public void run() {
        
        System.out.println("start socket server...");
        while (!server.isClosed()) {
            System.out.println("server is about to accept!");
            try {
                Socket socket = server.accept();
                handleConnection(socket);
 
            } catch (SocketException se) {
                System.out.println("socket is closed!");
                LOGGER.log(Level.INFO, "[logout]", se);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            } 
        }
        System.out.println("socketserver is closed!");
    }
    
    MessageProducer mp;
    private void handleConnection(Socket socket) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            String jsonStr = br.readLine();
            JsonObject json = new JsonParser().parse(jsonStr).getAsJsonObject();
            //System.out.println("Got an event: " );
//            + json.toString()

//            if(jsonStr.contains("content")){
//                System.out.println("tellll");
//                String msg="name: ";
//                mp = new MessageProducer(msg, output);
//                
//            } else {
//                System.out.println("nottt equals");
//            }
            OnlineExamEvent event = new OnlineExamEvent(json);

            eventManager.fireEvent(event);  
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
  
    protected void closeServer() {
        try {
            server.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
}
  