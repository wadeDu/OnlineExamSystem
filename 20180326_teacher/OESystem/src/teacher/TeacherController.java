package teacher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
//import java.lang.reflect.Array;
//import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Date;
//import java.util.HashSet;
import java.util.List;
//import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
//import java.util.Set;
import java.util.TimeZone;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
//import javafx.event.Event;
//import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
//import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
//import javafx.scene.control.TabPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
//import javax.management.Notification;
import main.UserStage;
//import org.mislab.api.event.OnlineExamEvent;
import teacher.view.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

/**
 * FXML Controller class
 *
 * @author Yao(2015-16), wade(2017-18)
 */

public class TeacherController extends UserStage implements Initializable,ChangeListener{
    
    private static final Logger logger = Logger.getLogger(TeacherController.class.getSimpleName());
    
    private TeacherAccount teacher;
    private final Alert alert = new Alert(AlertType.CONFIRMATION);
    private Timeline timeline;
    private ListView listView = new ListView();
    private ArrayList studentList = new ArrayList();
    public ArrayList keyRecStList = new ArrayList();
    public ArrayList genGrpList = new ArrayList();
    private ArrayList spcGrpList = new ArrayList();
    private ObservableList<String> list = FXCollections.observableArrayList();    
    public SnapshotPane snapshot;
    public SpecialSnapshotPane specialSnapshot;
    private GridPane gridPane;
    private int examId, courseId;
    private List courses, exams;
    private String genGrpState="";
    private String spcGrpState="";
    
    public KeyEventMonitorPane keyEventMonitor;
    
    //mainControl--------------------------------------------------------------;
    @FXML
    private Pane mainCrtPane;
    @FXML
    private ScrollPane stListPane;
    @FXML
    private TextField course_name;
    @FXML
    private TextField course_year;
    @FXML
    private TextField course_semester;
    @FXML
    private TextField exam_courseId;
    @FXML
    private TextField exam_name;
    @FXML
    private TextField exam_duration;
    @FXML
    private TextField courseIdText;
    @FXML
    private TextField problem_cid;
    @FXML
    private TextField problem_eid;
    @FXML
    private TextField problem_title;
    @FXML
    private TextArea msgInputArea;
    @FXML
    private TextArea msgArea;
    @FXML
    private TextArea problem_desc;
    //@FXML
    //private TabPane monitorTabPane;
    @FXML
    private Pane courseCreatePane;
    @FXML
    private Pane examCreatePane;
    @FXML
    private Pane examQueryPane;
    @FXML
    private Pane courseQueryPane;
    @FXML
    private Pane problemCreatePane;
    @FXML
    private Label courseQueryLabel;
    @FXML
    private Label examQueryLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private Label welcomeUserLabel;
    @FXML
    private Label pathLabel;
    @FXML
    private Button logoutBtn;
    @FXML
    private MenuItem logoutItem;
    @FXML
    private Slider playRecSlider;
    
    //monitor------------------------------------------------------------------;
    @FXML
    private Pane monitorPane;
    @FXML
    private Pane paramPane;
    @FXML
    private ScrollPane snapshotPane;
    @FXML
    private ScrollPane specialSnapshotPane;
    @FXML
    private Menu monitorMenu;
    @FXML
    private ComboBox scaleBox;
    @FXML
    private ComboBox freqBox;
    @FXML
    private ComboBox specialScaleBox;
    @FXML
    private ComboBox specialFreqBox;
    @FXML
    private Button startMonitorGenGrpBtn;
    @FXML
    private Button startMonitorSpcGrpBtn;
    @FXML
    private Button stopMonitorGenGrpBtn;
    @FXML
    private Button stopMonitorSpcGrpBtn;
    @FXML
    private Button clearGeneralParamBtn;
    @FXML          
    private Button clearSpecialParamBtn;
    @FXML
    private Button switchToSpcGrpBtn;
    @FXML
    private Button switchToGenGrpBtn;
    @FXML
    private Label generalSnapshotLb;
    @FXML
    private Label specialSnapshotLb;
    //@FXML
    //private Button setParamBtn;
    
    //keyEvent-----------------------------------------------------------------;
    @FXML
    private Label stLabel;
    @FXML
    private ComboBox periodBox;
    @FXML
    private Button requestTextRecBtn;
    @FXML
    private Button playRecBtn;
    @FXML
    private Button pausePlayRecBtn;
    @FXML
    private Button stopPlayRecBtn;
    @FXML
    private Button replayRecBtn;
    @FXML
    private Button clearRecBtn;
    @FXML
    private Button setPeriodBtn;
    @FXML
    private Pane keyEventPane;
    @FXML
    private ScrollPane keyEventMonitorPane;
    @FXML
    public TextArea loadTextRecArea;



    
    
    public TeacherController() {
       // this.snapshot = new SnapshotPane();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { 
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Duration duration = Duration.millis(1000);
        KeyFrame keyFrame = new KeyFrame(duration, (ActionEvent event) -> {
            timeLabel.setText(getDateTime()); 
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();  
        
        //20171206 => for arrow key move slider;
        playRecSlider.valueProperty().addListener(this);
    } 
    
    public void setTeacher(TeacherAccount t) {
        List courses, exams;
        teacher = t;
        courses = ((List) teacher.getUser().queryCourses().getContent().get("courses"));
        exams = null;
        Menu courseItem = null;
        MenuItem examItem = null;
        for (int i = 0; i< courses.size(); i++){
            final int cid = Integer.parseInt(((Map)courses.get(i)).get("courseId").toString());
            final String courseName = ((Map)courses.get(i)).get("courseName").toString();
            exams = ((List) teacher.getUser().queryExams(cid).getContent().get("exams"));
            courseItem = new Menu(courseName);
            for (int j = 0; j< exams.size(); j++){
                final int eid = Integer.parseInt(((Map)exams.get(j)).get("examId").toString());
                final String examName = ((Map)exams.get(j)).get("examName").toString();
                examItem = new MenuItem(examName);
                examItem.setOnAction((ActionEvent event) -> {
                    courseId = cid;
                    examId = eid;
                    //setMonitorPane(event);
                    setMainCrtPane(event);
                    setProblem(courseName, examName, courseId, examId);
                    System.out.println("@TeacherController.java--> courseId:"+courseId);
                    
                    //20171003 should get the list of login students, not this way
                    //List st = ((List) teacher.getExam().queryStudents(courseId).getContent().get("students"));
                    //snapshot = new SnapshotPane(st);
                    
                });
                courseItem.getItems().add(examItem);
            }
            monitorMenu.getItems().add(courseItem);
        }
        
        if(getHour() < 12) {
            welcomeUserLabel.setText(teacher.getName() + " 早上好！");
        } else if(11 < getHour()  &  getHour() < 18) {
            welcomeUserLabel.setText(teacher.getName() + " 下午好！");
        } else {
            welcomeUserLabel.setText(teacher.getName() + " 晚上好！");
        }  
    }
    
    //登出;
    public void logout(ActionEvent event){
        alert.setTitle("登出系統");
        alert.setHeaderText(""); 
        alert.setContentText("您真的要登出嗎？");
        final Optional<ButtonType> opt = alert.showAndWait();
        
        if (opt.get() == ButtonType.OK) {          
            //logoutItem = (MenuItem) event.getTarget();
            //Stage teacherStage = (Stage) logoutItem.getParentPopup().getScene().getWindow();
            //20170926use hidden logoutBtn to getScene;
            Stage teacherStage = (Stage) logoutBtn.getScene().getWindow();
            
            teacher.logout();
            loadLoginStage();
            teacherStage.close();
        } 
    }
    
    //from TeacherAccount.java case "Attend";
    private ArrayList<String> slist = new ArrayList<>(); //student list;
    private ArrayList<ArrayList> keyTextRecorder = new ArrayList<>();
    //private String stname;
    public void loadStudentList(String name, String userId) {
        
        studentList = ((ArrayList) teacher.getExam().queryStudents(courseId).getContent().get("students"));
        //System.out.println("=> studentList("+studentList.size()+" student): ");
        
        studentList.stream().filter((st) -> (((Map)studentList.get(studentList.indexOf(st))).get("state").toString().equals("InExam"))).forEach((st) -> {
            keyRecStList.add(st);
        });
        
        keyEventMonitor = new KeyEventMonitorPane(studentList);
        
        //create arrayList for saving student keyText;
        //System.out.println("----------------------------------------------------");
        //System.out.println("@TeacherController.loadStudentList => keyTextRecorder:");
        //keyTextRecorder.stream().forEach((rec) -> { 
            //System.out.println("st: "+rec.getClass().getName());
           
        //});
        
        System.out.println("----------------------------------------------------");
        //when new student login(Attend), default add to genGrpList, and not in genGrpList;
        studentList.stream().filter((st) -> (((Map)studentList.get(studentList.indexOf(st))).get("name").toString().equals(name))).forEach((st) -> {
            genGrpList.add(st);
            //start snapshot when there newcomer in the group;
        });
        
        System.out.println("@loadStudentList when new student login(Attend):\n=> generalstList("+genGrpList.size()+" student): ");
        //genGrpList.stream().forEach((generalst) -> { System.out.println(generalst);});
        
        System.out.println("@loadSteudentList userId:"+userId);
        slist.add(userId);
        
        Platform.runLater(() -> {
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            list.add(name);
            listView.setItems(list);
            
            MenuItem[] items = {new MenuItem("Snapshot"), new MenuItem("key")};
            
            items[0].setOnAction((ActionEvent event) -> {
                //loadSnapshots(id);
                //loadSnapshots();
            });
            
            items[1].setOnAction((ActionEvent event) -> {
                setMsgArea("[系統測試]");
            });
            
            ContextMenu contextMenu = new ContextMenu(items);
            listView.setContextMenu(contextMenu);
            listView.setPrefSize(175, 715);
            stListPane.setContent(listView);
            //stOnLineListPane.setContent(listView);
        });
    }
    
    public void removeStudentFromList(String name) {
        
        Platform.runLater(() -> {
            listView.getItems().remove(name);
            //gridPane = null;
            //snapshotPane.setContent(gridPane); //what if only one student logout;
        });
        //if student logout, remove from genGrpList/specialGrpList;
        studentList.stream().filter((st) -> (((Map)studentList.get(studentList.indexOf(st))).get("name").toString().equals(name))).forEach((st) -> {
            genGrpList.remove(st);
            //genGrpState="studentLogout";
            //stopMonitorGenGrpBtn.fire();
            //startMonitorGenGrpBtn.fire();            
        });
        
        //System.out.println("@removeStudentFromList when student logout:\n=> generalGrpList("+genGrpList.size()+" student): ");
        //genGrpList.stream().forEach((st) -> { System.out.println(st);});
        
        studentList.stream().filter((st) -> (((Map)studentList.get(studentList.indexOf(st))).get("name").toString().equals(name))).forEach((st) -> {
            spcGrpList.remove(st);
        });
        
        //System.out.println("@removeStudentFromList when student logout:\n=> specialGrpList("+spcGrpList.size()+" student): ");
        //spcGrpList.stream().forEach((st) -> { System.out.println(st);});

    }
    
//** keyEvent functions by wade **---------------------------------------------------------------------------;    
    
    private double period=1,periodUesd;
    private ObservableList<String> periodOpt = FXCollections.observableArrayList("0.3","0.6","1","1.2","1.5","2");
    public void setKeyEventPane(ActionEvent event){
        keyEventPane.setVisible(true);

        periodBox.setItems(periodOpt);
    }
    
    public void setPeriodOK(ActionEvent event) throws Exception{
        
        if(null==(String)periodBox.getValue()){
            period=1; //default;
            showWarningMsg(event);
        }else{
            period=Double.parseDouble((String)periodBox.getValue());
            //System.out.println("=> setPeriod:"+period);
        }
        //textRecProcessor.setPeriod(period);
    }
    
    private boolean isStPicked;
    public void loadstKeyText(String name, String keyText) throws IOException {
        
        Platform.runLater(() -> {
            try{
                //1.update keyEventMonitorPane;
                keyEventMonitor.addstKeyText(name, keyText);
                //System.out.println("get keyText: "+keyText);
                //2.save keyText in String arrayList and database(how and when);
                
            } catch (IOException ex) {
                Logger.getLogger(TeacherController.class.getName()).log(Level.SEVERE, null, ex);
            }

            keyEventMonitorPane.setContent(keyEventMonitor);
        });
    }
    
    private String stName,userId;
    private String playRecState="stop";
    public void requestTextRec(ActionEvent event)throws InterruptedException{
        System.out.println("--------------------------------------------\n@TeacherController => requestTextRec:");
        
        System.out.println("=> playRecState: "+playRecState);
        /*if there has already stTextRec play/pause/stop...etc.;
        //in fact,this part is not used,cauz when stPicked is occupied,request btn is disabled; 
        if(!playRecState.equals("stop")){
            //clearRecBtn.fire();
            keyEventMonitor.clearStPicked();
            //playRecState="";
            loadTextRecArea.clear();
            playRecSlider.setValue(sliderVal=0);
        }*/
        
        keyEventMonitor.chkStPicked();
        stName = keyEventMonitor.getStPickedName(); 
        
        System.out.println("--------------------------------------------------------------"+stName);
        System.out.println("@TeacherController.requestTextRec =>  keyRecStList.size: "+keyRecStList.size());
        for(Object st:keyRecStList){
            
            if(((Map)keyRecStList.get(keyRecStList.indexOf(st))).get("name").toString().equals(stName)){
                
                userId = ((Map)keyRecStList.get(keyRecStList.indexOf(st))).get("userId").toString();
                System.out.println("@TeacherController.requestTextRec => getStudentPicked: "+stName+" ,userId: "+userId);
                
                stLabel.setText(stName);
                setBtnState(event);
                
                teacher.getExam().requestTextRec(courseId, examId, userId);
                
                break;
                
            }else{
                showWarningMsg(event);
            }
        }        
    }
    
    
    private ArrayList<String> stTextRec;    
    //when ">>" pushed = student picked;
    public void loadTextRec(String name, String textRec){
        //System.out.println("@TeacherController.loadTextRec from student: ");
        stTextRec=new ArrayList<String>();        
        System.out.println("@TeacherController.loadTextRec from student => "+name);
        Gson gson = new Gson();
        TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>(){};
        stTextRec = gson.fromJson(textRec, token.getType()); 
        
        //System.out.println("@TeacherController.stTextRec convert back => "+stTextRec);
        setRec4Play(stTextRec);

    }
    
    //private ArrayList<String> rec4Play;
    private double recSize,gap;
    private DecimalFormat df = new DecimalFormat("##.000");
    //private int restartIndex,toIndex;
    
    public void setRec4Play(ArrayList<String> textRec){
        //for slider;
        recSize = stTextRec.size();
        gap = Double.parseDouble(df.format(100/recSize));
        System.out.println("@loadTextRec recSize => "+recSize+" ,gap: "+gap);
        //gap is the distance of everytime slider move forward;
        playRecSlider.setBlockIncrement(gap);
    }
    
    private boolean suspended = false;
    private int countPlay=0; 
    public synchronized void playTextRec(ActionEvent event)throws InterruptedException{
        
        setBtnState(event);
        
        System.out.println("@playTextRec: "+stLabel.getText()+" ,getStPicked(): "+keyEventMonitor.getStPickedName());
        if(!keyEventMonitor.getStPickedName().isEmpty() && !stLabel.getText().isEmpty()){
            playRecState="play";
            suspended = false;
            
            System.out.println("@playTextRec => countplay: "+countPlay);
            
            if(countPlay == 0){
                System.out.println("$$ ready to play!");
                handleTextRec();
                
            }else if(countPlay > 0){ //means play after pause;
                notify();
            }
                
        }else{
           showWarningMsg(event);
        }   
    }
    
    public void pausePlayRec(ActionEvent event){
        System.out.println("!!pause pushed!!");
        setBtnState(event);
        
        countPlay++;            
        playRecState="pause";
        suspended=true;
    }
    
    public void stopPlayRec(ActionEvent event){
        System.out.println("!!stop pushed!!");
        setBtnState(event);
        playRecState="stop";
        countPlay=0;
    }
    
    public void finishPlayRec(ActionEvent event)throws InterruptedException{
        System.out.println("=> print to the end or stop, turn countPlay to 0!!");
        //when stTextRec is finished
        //playRecSlider.setValue(100);
        
        Alert alertListEmpty = new Alert(AlertType.INFORMATION);
        alertListEmpty.setTitle("提醒！！");
        alertListEmpty.setHeaderText("");
        alertListEmpty.setContentText("該員考生作答歷程播放完畢");
        alertListEmpty.showAndWait();
        
        countPlay=0;
        playRecState="stop";
        playRecBtn.setDisable(false);
        pausePlayRecBtn.setDisable(true);
        stopPlayRecBtn.setDisable(true);
    }
    
    public void replayTextRec(ActionEvent event)throws InterruptedException{
        System.out.println("!!replay pushed!!");
        setBtnState(event);
        //stopPlayRec(event);
        stopPlayRecBtn.fire();
        //playTextRec(event);
        playRecBtn.fire();
    }
    
    public void clearTextRecArea(ActionEvent event){
        System.out.println("!!clear pushed!!");
        setBtnState(event);
        
        playRecState="";
        loadTextRecArea.clear();
        stLabel.setText("");
        playRecSlider.setValue(sliderVal=0);
        keyEventMonitor.clearStPicked();
    }
    
    private double sliderVal,recIndex;
    private String text;
    public void handleTextRec(){
        
        new Thread(()->{
            
            try{
                //for(String text:stTextRec){
                for(recIndex=0; recIndex<recSize; recIndex++){
                    
                    if(playRecState.equals("stop")){ //||playRecState.equals("")
                        System.out.println("$$ ready to stop!!");
                        break;
                    }
                    
                    //text=stTextRec.get((int)recIndex);
                    //System.out.println("=> start print: "+text);
                    ///loadTextRecArea.setText(text);
                    
                    //move the slider;
                    //recIndex = stTextRec.indexOf(text);
                    System.out.println("inLoop => playRecIndex: "+recIndex+" ,recSize: "+recSize);
                    
                    if(recIndex==recSize){//means print to the last index;
                        sliderVal=100;
                    }else{
                        sliderVal = Math.round(Double.parseDouble(df.format(((recIndex*100)/recSize))));
                    }
                    System.out.println("=> playSliderVal: "+sliderVal);

                    //playRecSlider.setValue -> move the slider;
                    playRecSlider.setValue(sliderVal);
                  
                    
                    System.out.println("---Zzz...for "+period+" second!---\n");
                    Thread.sleep(Math.round(period)*1000);
                    
                    synchronized(this){
                        while(suspended){
                            System.out.println("pause => start waiting!");
                            wait();
                        }
                    }
                }
                
                //this.finishPlayRec();
                System.out.println("=> print to the end or stop, turn countPlay to 0!!");
                countPlay=0;
                playRecState="stop";
                playRecBtn.setDisable(false);
                pausePlayRecBtn.setDisable(true);
                stopPlayRecBtn.setDisable(true);
                
            }catch(InterruptedException e){
                System.out.println("### InterruptedException! ###");
                e.printStackTrace();
            }
        }).start();
    }
    
    public void moveSlider(MouseEvent event){}
    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("observable: "+observable.getClass().getName()+" ,val: "+observable.getValue());

        //situation 1: playTextRec then playRecSlider.setValue(playRecSliderVal) --> move slider;
        
        //situation 2: click mouse to move slider --> set playRecIndex then setText;
        //if(!"play".equals(playRecState)){
            
        sliderVal = Math.round(Double.parseDouble(df.format(playRecSlider.getValue())));

        recIndex = Math.round(sliderVal/gap);
        //recIndex = Math.floor(sliderVal/gap);

        System.out.println("@moveSlider => recSize: "+recSize+" ,sliderVal: "+sliderVal+" ,gap: "+gap+" ,recIndex: "+recIndex);
        //System.out.println("=> text: "+stTextRec.get((int)recIndex));
        if("".equals(playRecState)){//when clear is pushed or student changed;
            loadTextRecArea.clear();
        }else{
            loadTextRecArea.setText(stTextRec.get((int)recIndex));
        }
    }
    
    private Button btn;
    public void setBtnState(ActionEvent event){
        btn=(Button)event.getSource();
        
        if(btn.getId().equals("requestTextRecBtn")){
            //requestTextRecBtn.setDisable(true);
            playRecBtn.setDisable(false);
            
        }else if(btn.getId().equals("playRecBtn") || btn.getId().equals("replayRecBtn")){
            requestTextRecBtn.setDisable(true);
            playRecBtn.setDisable(true);
            playRecSlider.setDisable(true);
            
            pausePlayRecBtn.setDisable(false);
            stopPlayRecBtn.setDisable(false);
            replayRecBtn.setDisable(true);
            clearRecBtn.setDisable(true);
            
        }else if(btn.getId().equals("pausePlayRecBtn")){
            pausePlayRecBtn.setDisable(true);
            stopPlayRecBtn.setDisable(true);
            
            replayRecBtn.setDisable(false);
            requestTextRecBtn.setDisable(false);
            playRecSlider.setDisable(false);
            playRecBtn.setDisable(false);
            clearRecBtn.setDisable(false);
            
        }else if(btn.getId().equals("stopPlayRecBtn")){
            stopPlayRecBtn.setDisable(true);
            pausePlayRecBtn.setDisable(true);
            playRecSlider.setDisable(true);
            
            replayRecBtn.setDisable(false);
            requestTextRecBtn.setDisable(false);
            playRecBtn.setDisable(false);
            clearRecBtn.setDisable(false);
        
        }else if(btn.getId().equals("clearRecBtn")){
            playRecBtn.setDisable(true);
            pausePlayRecBtn.setDisable(true);
            stopPlayRecBtn.setDisable(true);
            replayRecBtn.setDisable(true);
            clearRecBtn.setDisable(true);
            playRecSlider.setDisable(true);
            
            requestTextRecBtn.setDisable(false);
        }
    }

    
//** monitor / snapshot functions **---------------------------------------------------------------------------;
    
    //開啟截圖畫面&啟動參數設定;
    
    private float scale,generalScale,specialScale;
    private int freq,generalFreq,specialFreq;
    private ObservableList<String> generalScaleOpt = FXCollections.observableArrayList("20","50","80");
    private ObservableList<String> generalFreqOpt = FXCollections.observableArrayList("10","15","20");
    private ObservableList<String> specialScaleOpt = FXCollections.observableArrayList("20","50","80");
    private ObservableList<String> specialFreqOpt = FXCollections.observableArrayList("2","5","8");
    public void setMonitorPane(ActionEvent event){
        //monitorTabPane.setVisible(true);
        monitorPane.setVisible(true);
        //default value;
        scale=10;
        freq=3;
        
        scaleBox.setItems(generalScaleOpt);
        freqBox.setItems(generalFreqOpt);
        
        specialScaleBox.setItems(specialScaleOpt);
        specialFreqBox.setItems(specialFreqOpt);
    }
    
    public void setParamOK(ActionEvent event) throws Exception{
        
        btn=(Button)event.getSource();
        
        if(btn.getId().equals("setParamBtn")){
            if(null==(String)scaleBox.getValue()||null==(String)freqBox.getValue()){
                showWarningMsg(event);
            }else{
                scale=Float.parseFloat((String)scaleBox.getValue());
                freq=Integer.parseInt((String)freqBox.getValue());
                System.out.println("=> setGeneralParamBtn "+scale+","+freq);
            }
        }else if(btn.getId().equals("setSpecialParamBtn")){ 
            if(null==(String)specialScaleBox.getValue()||null==(String)specialFreqBox.getValue()){
                showWarningMsg(event);
            }else{
                scale=Float.parseFloat((String)specialScaleBox.getValue());
                freq=Integer.parseInt((String)specialFreqBox.getValue());
                System.out.println("=> setSpecialParamBtn "+scale+","+freq);
            }
        }
        //paramPane.setVisible(false);
        //setParamBtn.disarm();
    }
    
    //清除參數設定;
    public void setParamClear(ActionEvent event){
        btn = (Button)event.getSource();
        if(btn.getId().equals("clearGeneralParamBtn")||btn.getId().equals("startSnapshotBtn")){
            scaleBox.getSelectionModel().clearSelection();
            freqBox.getSelectionModel().clearSelection();
        }else if(btn.getId().equals("clearSpecialParamBtn")||btn.getId().equals("startSpecialSnapshotBtn")){ 
            specialScaleBox.getSelectionModel().clearSelection();
            specialFreqBox.getSelectionModel().clearSelection();
        }
    }
    
    private Alert alertParam = new Alert(AlertType.CONFIRMATION);
    public void chkParam(ActionEvent event) throws Exception{ 
        System.out.println("-----------------------------------------------");
        //get through all the checks, then start monitor; 
        alertParam.setTitle("截圖參數須知");
        alertParam.setHeaderText("是否使用預設值(截圖大小：10% 頻率：3秒)"); 
        alertParam.setContentText("請選擇使用預設值或已設定參數值開始截圖");
        
        ButtonType btnTypeDefault = new ButtonType("預設值");
        ButtonType btnTypeSet = new ButtonType("已設定");
        ButtonType btnTypeCancel = new ButtonType("取消");
        alertParam.getButtonTypes().setAll(btnTypeDefault,btnTypeSet,btnTypeCancel);
        
        final Optional<ButtonType> optResult = alertParam.showAndWait();
        if(optResult.get() == btnTypeDefault) { 
            //use default value;
            System.out.println("@chkParam => use default scale= "+scale+",freq= "+freq);
            startMonitor(event);
            
        }else if(optResult.get() == btnTypeSet){  
            if(scale==10.0 && freq==3){ //if the scale equals to default value;
                Alert alertMsg = new Alert(Alert.AlertType.INFORMATION);
                alertMsg.setTitle("提醒通知");
                alertMsg.setHeaderText("目前參數為預設值");
                alertMsg.setContentText("請先設定參數!!");
                alertMsg.showAndWait();   
            }else{
                System.out.println("@chkParam => setting scale= "+scale+",freq= "+freq);
                startMonitor(event);
            }
        }else{//cancel -> close Dialog;
            paramPane.setVisible(false);
        }             
    }
    
    //開始截圖前先確定學生名單&參數設定;
    private ArrayList monitorList = new ArrayList();
    public void chkstListEmpty(ActionEvent event) throws Exception{
        //before snapshot -> get student List,the first genGrpList is from stList @loadstList;
        btn = (Button)event.getSource();
        System.out.println("----------------------------------------------\n"
                + "@chkList b4 startMonitor(snapshot): "+btn.getId()+"\nevent: "+event);

        //confirm which group to snapshot;
        if(btn.getId().equals("startMonitorGenGrpBtn")){
            
            if(genGrpState.equals("monitoring")){
                stopMonitorGenGrpBtn.fire();
            }
            
            System.out.println("=> generalGrpList(b4chk): "+genGrpList.size()+" student");
            printList(genGrpList);

            //remove students not login/logout, in case student logout;
            genGrpList.removeIf(st -> ((Map)genGrpList.get(genGrpList.indexOf(st))).get("state").toString().equals("logout"));

            System.out.println("=> generalstList(aft chk, b4 monitor): "+genGrpList.size()+" student");
            
            monitorList = genGrpList;
            
        }else if(btn.getId().equals("startMonitorSpcGrpBtn")){
            
            if(spcGrpState.equals("monitoring")){
                stopMonitorSpcGrpBtn.fire();
            }
            
            System.out.println("=> specialGrpList b4chk: "+spcGrpList.size()+" student");
            printList(spcGrpList);
            
            //remove students not login/logout, in case student logout;
            spcGrpList.removeIf(st -> ((Map)spcGrpList.get(spcGrpList.indexOf(st))).get("state").toString().equals("logout")); 
            
            System.out.println("=> specialGrpList(aft chk, b4 monitor): "+spcGrpList.size()+" student");
            
            monitorList = spcGrpList;
        }
        printList(monitorList);
        
        //chk parameters if there are students in the group;
        if(!monitorList.isEmpty()){
            if(genGrpState.equals("switchGroup") || spcGrpState.equals("switchGroup")){
                //pass chkParam;
                System.out.println("=> startMonitor from switchGroup");
                startMonitor(event);
            }else{
                chkParam(event);
            }
        //show warning message if no student in the group;
        }else{
            if(btn.getId().equals("startMonitorGenGrpBtn")){
                genGrpState="noStudent";
                setGroupState(event,genGrpState);
            }else if(btn.getId().equals("startMonitorSpcGrpBtn")){
                spcGrpState="noStudent";
                setGroupState(event,spcGrpState);
            }
            System.out.println("@chkstListandParam()--> there is no student in the group");
            showWarningMsg(event);
        }
    }
    
    //開始截圖;
    public void startMonitor(ActionEvent event) throws Exception{
        System.out.println("-----------------------------------------------\n@startMonitor => ");
        btn=(Button)event.getSource();
        
        if(btn.getId().equals("startMonitorGenGrpBtn")){
            
            snapshot = new SnapshotPane(genGrpList);
            setMsgArea("[系統]Start monitor: 一般組");
            
            generalScale=scale;
            generalFreq=freq;
            
            genGrpState="monitoring";
            setGroupState(event,genGrpState);
            
        }else if(btn.getId().equals("startMonitorSpcGrpBtn")){
                       
            specialSnapshot = new SpecialSnapshotPane(spcGrpList);
            setMsgArea("[系統]Start monitor: 特別組");
            
            specialScale=scale;
            specialFreq=freq;
            
            spcGrpState="monitoring";
            setGroupState(event,spcGrpState);
            
        }
        
        //startMonitor with parameters, note: userId != studentId;
        String userId,name; 
        for(Object st:monitorList){ 
            userId = ((Map)monitorList.get(monitorList.indexOf(st))).get("userId").toString();
            name = ((Map)monitorList.get(monitorList.indexOf(st))).get("name").toString();
            System.out.println("@monitor userId: "+userId+" name: "+name);
            
            teacher.getExam().startMonitor(courseId, examId, userId, scale, freq);
        }
        
        //parameters return to default;
        scale=10;
        freq=3;
        //setParamClear;
        setParamClear(event);
        
    }
   
    //停止監考; userId (e.g. 3, 1, or 2), not studentId: 學號(e.g. 4026s)
    private ArrayList stopMonitorList = new ArrayList();
    public void stopMonitor(ActionEvent event) throws Exception{
        System.out.println("---------------------------------------------------\n@stopMonitor=>");
        btn=(Button)event.getSource();
        
        if(btn.getId().equals("stopMonitorGenGrpBtn")){
                      
            stopMonitorList=genGrpList;
            setMsgArea("[系統]Stop monitor: 一般組"); 
                    
            genGrpState="stopMonitor";
            setGroupState(event,genGrpState);
            
            clearGeneralParamBtn.fire();
            
        }else if(btn.getId().equals("stopMonitorSpcGrpBtn")){
            
            stopMonitorList=spcGrpList;
            setMsgArea("[系統]Stop monitor: 特別組");
            
            spcGrpState="stopMonitor";
            setGroupState(event,spcGrpState);
            
            clearSpecialParamBtn.fire();
           
        }
        printList(stopMonitorList);
        
        String userId,name;
        for(Object st:stopMonitorList){ 
            userId = ((Map)stopMonitorList.get(stopMonitorList.indexOf(st))).get("userId").toString();
            name = ((Map)stopMonitorList.get(stopMonitorList.indexOf(st))).get("name").toString();
            System.out.println("@stopMonitor => userId: "+userId+" name: "+name);
            
            teacher.getExam().stopMonitor(courseId, examId, userId);            
        }
    }
   
    //when ">>" or "<<" btn is pushed, which chkbxbtn isSelected, the student is switch to which Group;
    //connect the chkboxbtn and the student, call function in Snapshot;
    /*1.stop monitor;
      2.chk & get student who is picked;
      3.switch to newGrpList;
      4.remove from oldGrpLsit; 
      5.update snapshot after switch*/
    private ArrayList switchList = new ArrayList();
    public void switchGroup(ActionEvent event) throws Exception{
        System.out.println("---------------------------------------------------\n@switchGroup=> ");

        //1.stop all monitor; #state have to split;
        if(genGrpState.equals("monitoring")){
            stopMonitorGenGrpBtn.fire();
        }else if(spcGrpState.equals("monitoring")){
            stopMonitorSpcGrpBtn.fire();
        }
        
        btn=(Button)event.getSource();
        
        int switchStNum;
        if(btn.getId().equals("switchToSpcGrpBtn")){
            
            switchStNum = snapshot.getNumOfStPicked();
            System.out.println("switchToSpcGrpBtn.switchStNum:"+switchStNum);
            if(switchStNum>0){
                //2.chk & get student who is picked;
                switchList = snapshot.getSwitchList();
                //3&4 switch & remove;
                switchList.stream().forEach((st) -> {
                    spcGrpList.add(st);
                    genGrpList.remove(st);
                });
                
            }else{
                showWarningMsg(event);
            }        
            
        }else if(btn.getId().equals("switchToGenGrpBtn")){
            
            switchStNum = specialSnapshot.getNumOfStPicked();
            System.out.println(" switchToGeneralBtn.switchStNum:"+switchStNum);
            if(switchStNum>0){
                //2.chk & get student who is picked;
                switchList = specialSnapshot.getSwitchList();
                //3&4 switch & remove;
                switchList.stream().forEach((st) -> {
                    genGrpList.add(st);
                    spcGrpList.remove(st);
                });
                
            }else{
                showWarningMsg(event);
            } 
        }
        System.out.println("=> switch List: "+switchList.size()+" student");
        
        System.out.println("=> generalGrpList after switch: "+genGrpList.size()+" student");
        printList(genGrpList);

        System.out.println("=> specialGrpList after switch: "+spcGrpList.size()+" student");
        printList(spcGrpList);
        
        setMsgArea("[系統]upadate Snapshot after switch");
        //update snapshoPane;
        if(genGrpList.isEmpty()){
            snapshot.contentPane.getChildren().clear();
            
            //disable the ">>" button;
            switchToSpcGrpBtn.setDisable(true);
            
            //setGroupState(event);
            genGrpState="noStudent";
            setGroupState(event,genGrpState);   
            
        }else{
            monitorList=genGrpList;

            System.out.println("@switchGroup => simulate pushed startMonitorGenGrpBtn");
            genGrpState="switchGroup";
            startMonitorGenGrpBtn.fire();
            //stopMonitorGenGrpBtn.fire();
        }
        
        if(spcGrpList.isEmpty()){
            specialSnapshot.contentPane.getChildren().clear();
               
            //disable the "<<" button;
            switchToGenGrpBtn.setDisable(true);
            
            //setGroupState(event);
            spcGrpState="noStudent";
            setGroupState(event,spcGrpState);
            
        }else{
            monitorList=spcGrpList;
            
            System.out.println("@switchGroup => simulate pused startMonitorSpcGrpBtn");
            spcGrpState="switchGroup";
            startMonitorSpcGrpBtn.fire();
            //stopMonitorSpcGrpBtn.fire();
        }       
    }
    
    //載入截圖; from TeacherAccount.java case "sendSnapshot";
    public void loadSnapshots(String sid, String name, String base64Image, String term_of_pic ,String createdTimeStr) {
        Platform.runLater(() -> {
            Date createdAt;
            String time="";
            try {
                TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdTimeStr);
                TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm:ss");
                time = sdf.format(createdAt);
            } catch (ParseException ex) {
                logger.log(Level.INFO, "cannot parse time string: {0}", createdTimeStr);
                createdAt = new Date();
            }
            
            boolean isGeneralGrp=false;
            try {
                //depend on the student name in which group;
                for(Object st:genGrpList){
                    if(((Map)genGrpList.get(genGrpList.indexOf(st))).get("name").toString().equals(name)){
                        isGeneralGrp=true;
                        System.out.println("@loadsnapshot => "+name+" go to snapshotPane, isGeneralGrp:"+isGeneralGrp);
                        //snapshot.addSnapshot(name, base64Image, time);
                        snapshot.addSnapshot(name, base64Image, generalScale, generalFreq, term_of_pic, time);
                    }
                }
                for(Object st:spcGrpList){
                    if(((Map)spcGrpList.get(spcGrpList.indexOf(st))).get("name").toString().equals(name)){
                        isGeneralGrp=false;
                        System.out.println("@loadsnapshot => "+name+" go to specialSnapshot, isGeneralGrp:"+isGeneralGrp);
                        specialSnapshot.addSnapshot(name, base64Image, specialScale, specialFreq, term_of_pic, time);
                    }
                }
                
            } catch (IOException ex) {
                Logger.getLogger(TeacherController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(isGeneralGrp==true){
                System.out.println("@loadsnapshot set snapshotPane");
                snapshotPane.setContent(snapshot);
                
                //enable the ">>" button;
                switchToSpcGrpBtn.setDisable(false);

            }else{
                System.out.println("@loadsnapshot set specialSnapshotPane");
                specialSnapshotPane.setContent(specialSnapshot);
               
                //enable the "<<" button;
                switchToGenGrpBtn.setDisable(false);
            }
        });
    }
    
    //setGroupState;
    public void setGroupState(ActionEvent event,String state){
        
        btn=(Button)event.getSource();
        System.out.println("event: "+btn.getId()+", state: "+state);
        if(btn.getId().equals("startMonitorGenGrpBtn") && state.equals("monitoring")){  
            generalSnapshotLb.setText("截圖中... 截圖大小:"+generalScale+" ％ 頻率:"+generalFreq+" 秒");
            generalSnapshotLb.setTextFill(Color.RED);
        }else if(btn.getId().equals("startMonitorSpcGrpBtn")&& state.equals("monitoring")){ 
            specialSnapshotLb.setText("截圖中... 截圖大小:"+specialScale+" ％ 頻率:"+specialFreq+" 秒");
            specialSnapshotLb.setTextFill(Color.RED);
        }else if(btn.getId().equals("stopMonitorGenGrpBtn")){
            generalSnapshotLb.setText("已停止截圖...");
            generalSnapshotLb.setTextFill(Color.BROWN);
        }else if(btn.getId().equals("stopMonitorSpcGrpBtn")){    
            specialSnapshotLb.setText("已停止截圖...");
            specialSnapshotLb.setTextFill(Color.BROWN);
        }else if((btn.getId().equals("switchToSpcGrpBtn") || btn.getId().equals("startMonitorGenGrpBtn")) && state.equals("noStudent")){
            generalSnapshotLb.setText("待命中...目前無學生");
            generalSnapshotLb.setTextFill(Color.BLUE);
        }else if((btn.getId().equals("switchToGenGrpBtn") || btn.getId().equals("startMonitorSpcGrpBtn")) && state.equals("noStudent")){
            specialSnapshotLb.setText("待命中...目前無學生");
            specialSnapshotLb.setTextFill(Color.BLUE);
        }
    }
    
    //showList;
    public void showList(ActionEvent event){
        
        System.out.println("@TeacherController.java");
        
        System.out.println("=> studentList ("+studentList.size()+" student):");
        studentList.stream().forEach((st) -> { System.out.println(st);});
        
        System.out.println("=> generalGrpList ("+genGrpList.size()+" student):");
        genGrpList.stream().forEach((st) -> { System.out.println(st);});
        
        System.out.println("=> specialGrpList ("+spcGrpList.size()+" student):");
        spcGrpList.stream().forEach((st) -> { System.out.println(st);});
        
        //private ArrayList switchList;
        //switchList = snapshot.getStSwitched();
        //System.out.println("=> spcGrpList ("+spcGrpList.size()+" student):");
        //specialGrpList.stream().forEach((st) -> { System.out.println(st);});
    }
   
    
//** common control functions **---------------------------------------------------------------------------;
    
    //選擇要監考的考試才觸發;
    public void setMainCrtPane(ActionEvent event) {
        //System.out.println(event);
        mainCrtPane.setVisible(true);
        //monitorPane.setVisible(true);
    }
    
    //送出訊息;
    public void sendMsg(ActionEvent event){
        teacher.getUser().sendMessage(courseId, examId, msgInputArea.getText());
        msgInputArea.clear();
    }
    
    //顯示訊息;
    public void setMsgArea(String msg){
        msgArea.appendText(msg + "\n");
    }
   
    //考試暫停;
    public void pauseExam(ActionEvent event) {
        teacher.getExam().pauseExam(courseId, examId);
    }
    
    //繼續考試;
    public void resumeExam(ActionEvent event) {
        teacher.getExam().resumeExam(courseId, examId);
    }

    //新增課程OK - 顯示新增課程畫面;
    public void setCreateCoursePane(ActionEvent event){
        courseCreatePane.setVisible(true);
    }
    
    //查詢課程OK;
    public void queryCourse(ActionEvent event){
        String str = "";
        courseQueryPane.setVisible(true);
        int len= courses.size();
        
        for (int i = 0; i < len; i++) {
            str += "Course Name: " + 
                    ((Map)courses.get(i)).get("courseName").toString() + "\n";
        }
        courseQueryLabel.setText(str);
           //System.out.println(tch.getUser().queryCourses().getContent().toString());
    }
    
    //新增課程 - 送出鈕，同時新增至資料庫;
    public void sendCourse(ActionEvent event){
        List<String> studentIds = new ArrayList<String>() {{
            add("4025s");//???
        }};
        teacher.getExam().createCourse(course_name.getText(), Integer.parseInt(course_year.getText()), 
                Integer.parseInt(course_semester.getText()), studentIds);
        courseCreatePane.setVisible(false);
    }
    
    //新增考試OK - 顯示新增考試畫面;
    public void setCreateExamPane(ActionEvent event){
        examCreatePane.setVisible(true);
    }
    
    //查詢考試OK - 顯示查詢畫面;
    public void queryExam(ActionEvent event){
        examQueryPane.setVisible(true);
    }
    
    //查詢考試OK - 送出鈕，同時從資料庫抓取資料並顯示;
    public void sendQueryExam(){
        String str = "";

        int len= exams.size();
        
        for (int i = 0; i < len; i++) {
            str += "Name: " + 
                ((Map)exams.get(i)).get("examName").toString() + "\n";
        }
        examQueryLabel.setText(str);        
    }
    
    //新增考試OK - 送出鈕，同時新增至資料庫;
    public void sendExam(ActionEvent event){
        Date date = new Date();       
        Timestamp timestamp = new Timestamp(date.getTime());
        teacher.getExam().createExam(Integer.parseInt(exam_courseId.getText()), exam_name.getText(), timestamp, Integer.parseInt(exam_duration.getText()));
        examCreatePane.setVisible(false);
    }

    //新增題目 - 顯示新增題目畫面;
    public void createProblem(ActionEvent event){
        problemCreatePane.setVisible(true);
    }
    
    //新增題目OK - 送出鈕，同時新增至資料庫
    public void sendProblem(ActionEvent event){
        int cid = Integer.parseInt(problem_cid.getText());
        int eid = Integer.parseInt(problem_eid.getText());
        org.mislab.api.Problem problem = new org.mislab.api.Problem(problem_title.getText(),  problem_desc.getText());
        problem.addTestData("input for p1.1", "output1.1");
        problem.addTestData("input for p1.2", "output222");
        
        teacher.getExam().createProblem(cid, eid, problem);
        problemCreatePane.setVisible(false);
    }
    
    //目前假設每一個exam只有一個problem;
    private void setProblem(String courseName, String examName, int courseId, int examId) {
        pathLabel.setText(courseName + " -> " +examName);
        List problems = (List) teacher.getExam().queryProblems(courseId, examId).getContent().get("problems");
        questionLabel.setText("Title: " + ((Map)problems.get(0)).get("problemName").toString() +
                "\n-----------\nDesc.: " + ((Map)problems.get(0)).get("description").toString());
        
//       當一個exam有多個problem
//        switch(problems.size()) {
//            case 1:
//                questionLabel.setText(((Map)problems.get(0)).get("problemName").toString());
//                break;
//            default:
//                for (int i = 0; i < problems.size(); i++){
//                    problemStr += ((Map)problems.get(i)).get("problemName").toString() + "\n";
//                }
//                questionLabel.setText(problemStr);
//        }
    }
    
    //關閉畫面（課程查詢、考試查詢）;
    public void close(ActionEvent event){
        courseQueryPane.setVisible(false);
        examQueryPane.setVisible(false);
        courseCreatePane.setVisible(false);
        examCreatePane.setVisible(false);
        problemCreatePane.setVisible(false);
        //monitorPane.setVisible(false);
    }
    
    //關閉參數設定畫面 也可以寫在close裡面 用if else判斷;
    public void closeParamPane(ActionEvent event){
        paramPane.setVisible(false);
    }
    
//** shered functions **---------------------------------------------------------------------------;
    
    //show warning msg;
    public void showWarningMsg(ActionEvent event){
        btn = (Button)event.getSource();
        
        String msg="";
        
        if(btn.getId().equals("setParamBtn")||btn.getId().equals("setSpecialParamBtn")||btn.getId().equals("setPeriodBtn")){
            msg="請先設定參數!!";
        }else if(btn.getId().equals("startMonitorGenGrpBtn")||btn.getId().equals("startMonitorSpcGrpBtn")){
            msg="目前群組內沒有學生！！";
        }else if(btn.getId().equals("switchToSpcGrpBtn")||btn.getId().equals("switchToGenGrpBtn")){
            msg="請選擇要變更群組的學生！！";
        }else if(btn.getId().equals("playRecBtn")||btn.getId().equals("requestTextRecBtn")){
            msg="請選擇要播放作答歷程的學生！！(未選擇學生或該員尚未登入)";
        }else if(btn.getId().equals("pausePlayRecBtn")){
            msg="目前並無作答歷程播放中！！";
        }
        
        Alert alertListEmpty = new Alert(AlertType.INFORMATION);
        alertListEmpty.setTitle("警告！！");
        alertListEmpty.setHeaderText("");
        alertListEmpty.setContentText(msg);
        alertListEmpty.showAndWait();
    }
    
    
    public void showWarningMsg_old(ActionEvent event){
        btn = (Button)event.getSource();
        
        if(btn.getId().equals("setParamBtn")||btn.getId().equals("setSpecialParamBtn")||btn.getId().equals("setPeriodBtn")){
            Alert alertParamMsg = new Alert(Alert.AlertType.INFORMATION);
            alertParamMsg.setTitle("提醒！！");
            alertParamMsg.setHeaderText("");
            alertParamMsg.setContentText("請先設定參數!!");
            alertParamMsg.showAndWait(); 
            
        }else if(btn.getId().equals("startMonitorGenGrpBtn")||btn.getId().equals("startMonitorSpcGrpBtn")){
            Alert alertListEmpty = new Alert(AlertType.INFORMATION);
            alertListEmpty.setTitle("警告！！");
            alertListEmpty.setHeaderText("");
            alertListEmpty.setContentText("目前群組內沒有學生！！");
            alertListEmpty.showAndWait();
    
        }else if(btn.getId().equals("switchToSpcGrpBtn")||btn.getId().equals("switchToGenGrpBtn")){
            Alert alertNoPick = new Alert(AlertType.INFORMATION);
            alertNoPick.setTitle("警告！！");
            alertNoPick.setHeaderText("");
            alertNoPick.setContentText("請選擇要變更群組的學生！！");
            alertNoPick.showAndWait();
        
        }else if(btn.getId().equals("playTextRecBtn")||btn.getId().equals("requestTextRecBtn")){
            Alert alertListEmpty = new Alert(AlertType.INFORMATION);
            alertListEmpty.setTitle("警告！！");
            alertListEmpty.setHeaderText("");
            alertListEmpty.setContentText("請選擇要播放作答歷程的學生！！");
            alertListEmpty.showAndWait();
            
        }else if(btn.getId().equals("pauseTextRecBtn")){
            Alert alertListEmpty = new Alert(AlertType.INFORMATION);
            alertListEmpty.setTitle("警告！！");
            alertListEmpty.setHeaderText("");
            alertListEmpty.setContentText("目前並無作答歷程播放中！！");
            alertListEmpty.showAndWait();
        }
    }
    
    public void printList(ArrayList list){
        list.stream().forEach((st) -> { System.out.println(st); });
    }

}

