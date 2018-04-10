package org.mislab.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
    public static final MediaType JSON;
    public static final Logger LOGGER;
    
    static {
        JSON = MediaType.parse("application/json; charset=utf-8");
        LOGGER = Logger.getLogger(Utils.class.getName());
    }
    
    private static Response sendRequest(OkHttpClient client, Request request) {
        Response response;
        
        try {
            com.squareup.okhttp.Response r = client.newCall(request).execute();
            
            Gson gson = new Gson();
            String msg = r.body().string();
//            System.out.println("RESP: "+msg);
//            File file = new File("/Users/Max/Desktop/tmp.html");
//            file.createNewFile();
//            try (FileWriter fw = new FileWriter(file)) {
//                fw.write(msg);
//            }
            JsonObject json = gson.fromJson(msg, JsonObject.class);
//            JsonObject json = gson.fromJson(r.body().string(), JsonObject.class);
            response = new Response(json);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            
            response = new Response(ErrorCode.NetworkError);
        }
        
        return response;
    }
    
    public static Response post(OkHttpClient client, String url, JsonObject json) {
        RequestBody body = RequestBody.create(JSON, json.toString());
        
        Request request = new Request.Builder()
                .url(Config.HOST + url)
                .post(body)
                .build();
        
        return sendRequest(client, request);
    }
    
    public static Response get(OkHttpClient client, String url) {
        Request request = new Request.Builder()
                .url(Config.HOST + url)
                .build();
        
        return sendRequest(client, request);
    }
    
    private static String[] getHostAddresses() {
        Set<String> hostAddresses = new HashSet<>();
        try {
            for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (!ni.isLoopback() && ni.isUp() && ni.getHardwareAddress() != null) {
                    for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                        if (ia.getBroadcast() != null) {  //If limited to IPV4
                            hostAddresses.add(ia.getAddress().getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, null, e);
       }

        return hostAddresses.toArray(new String[0]);
    }
    
    private static String getLocalIPv4Addr() {
        String[] haddrs = getHostAddresses();
        String localIP = "127.0.0.1";
        
        for (String addr : haddrs) {
            if (addr.startsWith("192.168.0")) {
                localIP = addr;
                break;
            }
            if (addr.startsWith("192.168.1")) {
                localIP = addr;
                break;
            }
            if (addr.startsWith("192.168")) {
                localIP = addr;
                break;
            }
            if (addr.startsWith("172.16")) {
                localIP = addr;
            }
        }
        return localIP;
    }
        
    
    public static String getIPAddress() {
//        try {
//            return InetAddress.getLocalHost().getHostAddress();
            return getLocalIPv4Addr();
//        } catch (UnknownHostException ex) {
//            LOGGER.log(Level.SEVERE, null, ex);
//            
//            return "localhost";
//        }
    }
    
    public static Map<String, Object> convert2Integer(Map<String, Object> map) {
        for (String key: map.keySet()) {
            Object value = map.get(key);
            
            if (value instanceof Map) {
                map.put(key, convert2Integer((Map) value));
            } else if (value instanceof List) {
                map.put(key, convert2Integer((List) value));
            } else if (value instanceof Double) {
                double d = (Double) value;
                
                if (Math.round(d) == (int) d) {
                    map.put(key, (int) d);
                }
            }
        }
        
        return map;
    }
    
    public static List convert2Integer(List list) {
        List newList = new ArrayList();
        
        for (Object value: list) {
            if (value instanceof Map) {
                newList.add(convert2Integer((Map) value));
            } else if (value instanceof Double) {
                double d = (Double) value;
                
                if (Math.round(d) == (int) d) {
                    newList.add((int) d);
                } else {
                    newList.add(d);
                }
            } else {
                newList.add(value);
            }
        }
        
        return newList;
    }
}
