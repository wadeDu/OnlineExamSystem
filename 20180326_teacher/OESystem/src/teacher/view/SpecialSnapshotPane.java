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
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import teacher.ImageUtils;

/**
 *
 * @author wade
 */
public class SpecialSnapshotPane extends ScrollPane {
    public teacher.TeacherController teacherCrt;
    
    private static final Logger LOGGER = Logger.getLogger(SpecialSnapshotPane.class.getSimpleName());
    
    private static final int SNAPSHOT_HEIGHT = 120;
    private static final int SNAPSHOT_WIDTH = 180;
    
    private int count;
    
    public GridPane contentPane;
    private final CheckBox[] stChkBx;
    private final ImageView[] imageViews;
    private final Label[] nameLabels;
    private final Label[] timelabels;
    private ArrayList specialGrpList = new ArrayList();
    
    public SpecialSnapshotPane(ArrayList monitorList) {
        System.out.println("@@ SpecialSnapshotPane");
        specialGrpList = monitorList;
        count = monitorList.size(); 
        contentPane = new GridPane();
        
        if(count==0){
            contentPane.getChildren().clear();
        }
     
        contentPane.setHgap(5.0f);
        contentPane.setVgap(5.0f);
        contentPane.setPadding(new Insets(10,10,10,10));
        contentPane.setStyle("-fx-border-color:silver");
        
        imageViews = new ImageView[count];
        stChkBx = new CheckBox[count];
        nameLabels = new Label[count];
        timelabels = new Label[count];      
        
        for (int i = 0; i < count; i++) {
            
            imageViews[i] = new ImageView();
            imageViews[i].setFitHeight(SNAPSHOT_HEIGHT);
            imageViews[i].setFitWidth(SNAPSHOT_WIDTH);
            imageViews[i].setStyle("-fx-border-color:blue");
            stChkBx[i] = new CheckBox();
            nameLabels[i] = new Label(((Map)specialGrpList.get(i)).get("name").toString());
            
            timelabels[i] = new Label();
            
            //1070301 19:59 add by wade;
            int x = i/4; //every 4 student change line;
            int y = i%4;
            
            contentPane.add(nameLabels[i], x*3, y*3);
            contentPane.add(stChkBx[i], x*3+1, y*3);
            contentPane.add(imageViews[i], x*3, y*3+1, 2, 1); // 2 column 1 row; 
            contentPane.add(timelabels[i], x*3, y*3+2);
            
        }
        setContent(contentPane);
    }
        
    public void addSnapshot(String name, String base64Image, float scale, int freq, String term_of_pic, String createdAt) throws IOException {
        System.out.println("@specialSnapshotPane => here comes "+name);
        Image image = ImageUtils.decode(base64Image, SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT);
        write2file(term_of_pic, name, scale, freq, image, base64Image, createdAt);
        for (int i = 0; i < count; i++) {
            if (nameLabels[i].getText().equals(name)){
                imageViews[i].setImage(image);
                timelabels[i].setText(createdAt);
                break;
            } else {
                continue;
            }
        }
        System.gc();
    }
    
    private ArrayList switchList = new ArrayList();
    public int getNumOfStPicked(){
        
        for(Object st:specialGrpList){
            //if the chkBox is selected(true);            
            if (stChkBx[specialGrpList.indexOf(st)].isSelected()==true){ 

                //System.out.println("pick: "+st);
                switchList.add(st);
            }
        }
        return switchList.size();
    }
    
    //when btn "<<" is pushed, get student that is switch to special group;
    public ArrayList getSwitchList(){
        
        return switchList;
    }
    
    /*//1061013 when btn "<<" is pushed;
    public void switchToGeneralGroup(ActionEvent event) throws Exception{

        for(Object st:specialGrpList){
            //System.out.println("generalstList b4 switch:"+generalst);
            //if the chkBox is selected(true);            
            if (stChkBx[specialGrpList.indexOf(st)].isSelected()==true){ 
                //1.switch: add student to specialGroup 
                //System.out.println("name:"+nameLabels[generalstList.indexOf(generalst)].getText());
                System.out.println("pick: "+st);
                generalGrpList.add(st);
            }   
        }
        //2.remove from generalList = stop
        specialGrpList.removeIf(st -> (stChkBx[specialGrpList.indexOf(st)].isSelected()==true));
        
        if(specialGrpList.isEmpty()){
            this.contentPane.getChildren().clear();
        }
        
        System.out.println("@SpecialSnapshotPane => generalstList after switch("+generalGrpList.size()+"student):");
        generalGrpList.stream().forEach((st) -> { System.out.println(st);});
        
        //3.update generalstList;
        //teacherCrt.generalstList = generalstList;
        //teacherCrt.updGenGpList(generalstList);
        //teacherCrt.updSpcGpList(specialstList);
        
        System.out.println("@SpecialSnapshotPane => specialstList after switch("+specialGrpList.size()+"student):");
        specialGrpList.stream().forEach((st) -> { System.out.println(st);});
        
    }*/
    
    private void write2file(String term_of_pic, String name, float scale, int freq, Image image, String base64Image, String time) throws IOException {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        FileWriter fw = new FileWriter("snapshot_log_SpeGrp-"+dateFormat.format(date)+".txt", true);
        
        fw.write("====================================================\n"+
                 "term: "+ term_of_pic+ " , Name: "+name+
                 "\nScale: "+scale+ "% , freq: "+freq+ "sec"+
                 "\nWidth: " + image.getWidth() + " , Height: " + image.getHeight() +
                 "\nKB: " + (DECODER.decode(base64Image)).length/1024 +
                 "\nTime: "+time+ "\n");
        fw.close();
    }
    Base64.Decoder DECODER = Base64.getDecoder();
 
}
