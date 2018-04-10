/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teacher.view;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Base64;
//import java.util.Collections;
import java.util.Date;
//import java.util.List;
import java.util.Map;
//import java.util.logging.Level;
import java.util.logging.Logger;
//import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.Border;
//import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
//import javafx.util.Pair;
//import org.mislab.api.event.OnlineExamEvent;
//import teacher.ImageUtils;

/**
 *
 * @author wade
 */

public class KeyEventMonitorPane extends ScrollPane {
    public teacher.TeacherController teacherCrt;
    
    private static final Logger LOGGER = Logger.getLogger(KeyEventMonitorPane.class.getSimpleName());
    
    private static final int HEIGHT = 150;
    private static final int WIDTH = 180;
    
    private int count;
    
    public GridPane contentPane;
    //public FlowPane contentPane;
    private final RadioButton[] stPickBtn;
    private final TextArea[] stTextArea;
    private final Label[] nameLabel;
    private ArrayList monitorList = new ArrayList();

    public KeyEventMonitorPane(ArrayList studentList) {
        //System.out.println("---------------------------------\n@ KeyEventMonitorPane");
        monitorList = studentList;
        count = studentList.size();
        //System.out.println("=>count: "+count);
        
        contentPane = new GridPane();
        
        if(count==0){ contentPane.getChildren().clear();}
     
        contentPane.setHgap(5.0f);
        contentPane.setVgap(5.0f);
        contentPane.setPadding(new Insets(10,10,10,10));
        contentPane.setStyle("-fx-border-color:silver");
        stPickBtn = new RadioButton[count];
        
        stTextArea = new TextArea[count];
        nameLabel = new Label[count]; 
        ToggleGroup toggleGroup = new ToggleGroup();
        
        for (int i = 0; i < count; i++) {
            
            stTextArea[i] = new TextArea();
            stTextArea[i].setPrefHeight(HEIGHT);
            stTextArea[i].setPrefWidth(WIDTH);
            stTextArea[i].setEditable(false);
            stTextArea[i].setFont(Font.font("System",10));
            //System.out.println("=> studentList.get(i): "+((Map)studentList.get(i)).get("name").toString());
            
            nameLabel[i] = new Label(((Map)studentList.get(i)).get("name").toString());
            //System.out.println("@KeyEventPane => nameLabel: "+nameLabel[i].getText());
            stPickBtn[i] = new RadioButton();
            stPickBtn[i].setToggleGroup(toggleGroup);
            
            //1070301 add by wade;
            int x = i/4; //every 4 student change line;
            int y = i%4;
            
            contentPane.add(nameLabel[i], x*2, y*2);
            contentPane.add(stPickBtn[i], x*2+1, y*2);
            contentPane.add(stTextArea[i], x*2, y*2+1, 2, 1);  //2 column, 1 row;
            
        }
        setContent(contentPane);
    }
    
    public void addstKeyText(String name, String keyText) throws IOException {       
        //System.out.println("@addstKeyText: ");            
        for (int i = 0; i < count; i++) {
            //System.out.println("@addStKeyChar => nameLabel: "+nameLabel[i].getText()+", keyChar: "+keyChar);
            if(nameLabel[i].getText().equals(name)){
                //stTextArea[i].clear();
                stTextArea[i].setText(keyText);
            }
        }
        System.gc();
    }
    
    private TextArea tmpTxtArea;
    private String stPickedName="";
    public void chkStPicked(){
        
        System.out.println("@KeyEventMonitorPane => monitorList.size: "+monitorList.size());
        for(Object st:monitorList){
 
            System.out.println("@KeyEventMonitorPane => st: "+st);
            if (stPickBtn[monitorList.indexOf(st)].isSelected()==true){                 
                //System.out.println("@KeyEventMonitorPane => nameLabel[monitorList.indexOf(st)].getText(): "+nameLabel[monitorList.indexOf(st)].getText());
                stPickedName = nameLabel[monitorList.indexOf(st)].getText();
                System.out.println("@KeyEventMonitorPane => getStudentPicked: "+stPickedName);
                
                tmpTxtArea = stTextArea[monitorList.indexOf(st)];
                //System.out.println("@KeyEventMonitorPane => tmpTxtArea: "+tmpTxtArea);
                //System.out.println("=> stTextArea: "+stTextArea[monitorList.indexOf(st)].getText());
                //end=tmpTxtArea.getLength();
                //System.out.println("=> end: "+end);
                //System.out.println("=> tmpTxtArea: "+tmpTxtArea.getText(0, end));
                
                break;
            }
        }
    }
    
    public void clearStPicked(){
        stPickedName="";
        System.out.println("@KeyEventMonitorPane => clearStPicked: "+stPickedName);
    }
    
    public String getStPickedName(){
        return stPickedName;
    }
    
    public TextArea getStTxtArea(){
        return tmpTxtArea;
    }
    
    /*
    //when btn ">>" is pushed, get student that is switch to special group;
    public ArrayList getSwitchList(){

        System.out.println("@snapshotPane => switchList");
        switchList.stream().forEach((st) -> { System.out.println(st);});
        return switchList;
    }*/

    /*
    private void write2file(Image image, String base64Image) throws IOException {
        FileWriter fw = new FileWriter("record.txt", true);
        fw.write("Width: " + image.getWidth() +  
                 "\nHeight: " + image.getHeight() + 
                 "\nKB: " + (DECODER.decode(base64Image)).length/1024 +"\n=====");
        fw.close();
    }
    Base64.Decoder DECODER = Base64.getDecoder();
    */
}
