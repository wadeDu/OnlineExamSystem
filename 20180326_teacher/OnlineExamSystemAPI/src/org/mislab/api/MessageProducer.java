package org.mislab.api;


import java.io.PrintStream;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author user
 */


public class MessageProducer {
    public String msg;
    String s;
    Vector v;
    public MessageProducer(String message, Vector output){
        s= message;
        msg= message;
        v=output;
    }
    
    public MessageProducer(){}
            ////
    public void tellApiece(){
        //產生iterator可以存取集合內的元素資料    
//        Iterator it = v.iterator(); 
//        //向下讀取元件   
//        while(it.hasNext()){          

                System.out.println("tellApiece");
                //取集合內資料
//                PrintStream writer = (PrintStream) it.next();  
                //印出
                System.out.println("******");
//                System.out.println(message);
                msg = s;
//                writer.println(s);   
                System.out.println("******");
                //刷新該串流的緩衝。
//                writer.flush();           
//
//        }
   }

    public String getMsg(){
        System.out.println("getttt || " + msg);
        return msg;
    }
    ////
        
}