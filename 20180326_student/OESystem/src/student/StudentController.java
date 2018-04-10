package student;

import java.net.URL;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
//import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.UserStage;
import org.mislab.api.Student;
import org.fxmisc.richtext.CodeArea;

//import org.magiclen.magiccommand.Command;
//import org.magiclen.magiccommand.CommandListener;
//import javafx.application.Application;
import javafx.scene.control.TabPane;
import com.kodedu.terminalfx.*;
//import com.kodedu.terminalfx.TerminalBuilder;
//import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import javafx.scene.control.TextInputDialog;


/**
 * FXML Controller class
 *
 * @author Yao(2015-16),wade(2017-18)
 */
public class StudentController extends UserStage implements Initializable, Runnable {
    
    private StudentAccount student;
    private ScreenSnapshot screenSnapshot;
    private Timeline timeline;   
    private boolean snapshotFlag = false;
    private Alert alert;
    private List courses, exams;
    private final int examId = 1, courseId = 2;
    private MenuItem courseItem;
    
    private CodeAreaBox codeAreaBox;
    private String filename;
    private TerminalConfig defaultConfig;
    private TerminalBuilder terminalBuilder;
    private TerminalTab terminal;
    
    
    @FXML
    private Button logoutBtn;
    @FXML
    private Button compileBtn;
    @FXML
    private Button execBtn;
    @FXML
    private TextArea msgInputArea;
    @FXML
    private TextArea msgArea;
    @FXML
    private TextArea questionArea_A;
    @FXML
    private TextArea questionArea_B;
    @FXML
    private TextArea outputArea;
    @FXML
    private TabPane tabPane;
    @FXML
    private CodeArea keyAnswerArea;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane examPane;
    @FXML
    private Label timeLabel;
    @FXML
    private Label welcomeUserLabel;
    @FXML
    private Label pathLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private Label questionALabel;
    @FXML
    private MenuButton menuBtn;
    @FXML
    private Menu startExam;
    @FXML
    private MenuItem logoutItem;
    
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
        
        //from TerminalFX-master/.../java/...
        //Initialize terminal tabPane, default config
        defaultConfig = new TerminalConfig();
        terminalBuilder = new TerminalBuilder(defaultConfig);
        terminal = terminalBuilder.newTerminal();
        
        //for usr give cmd;
        //terminal.onTerminalFxReady(() -> {
        //  terminal.command("java -version\r");  //mac-OS:\n;
        //});
        tabPane.getTabs().add(terminal);
    }
    
    public void setStudent(StudentAccount st) {
        student = st;

        courseItem = new MenuItem("開始考試");
        courseItem.setOnAction((ActionEvent event) -> {
                    startExam(event);
                    setProblem(courseId, examId);
                });
        courseItem.setDisable(false);
        //menuBtn.getItems().add(courseItem);
        startExam.getItems().add(courseItem);

        if(getHour() < 12) {
            welcomeUserLabel.setText(student.getName() + " 早上好！");
        } else if(11 < getHour()  &  getHour() < 18) {
            welcomeUserLabel.setText(student.getName() + " 下午好！");
        } else {
            welcomeUserLabel.setText(student.getName() + " 晚上好！");
        }
        
        // Initialize codeAreaBox.
        codeAreaBox = new CodeAreaBox(keyAnswerArea);
    }

    
//** key-event function **--------------------------------------------------------------------;
    
    // Detect student keystroke. 
    public ArrayList<String> stTextRec=new ArrayList<>();
    private String keyText;
    public void keyEvent(KeyEvent e) {
        keyText = keyAnswerArea.getText();
        stTextRec.add(keyText);     
        student.getExam().sendKeyText(courseId, examId, keyText);
    }
    
    public void requestTextRec(){
        //System.out.println("@StudentController.request() => \n"+stTextRec);
        student.getExam().sendTextRec(courseId, examId, stTextRec);

    }
    
    //1.save keyAnswerArea.getText() to *.java @ ;
    public void saveCode(ActionEvent event) throws IOException {
        
        // Prompt the user for filename. Make sure the user entered something.
        /*
        do {
            filename = setFilename();
        } while(filename.isEmpty());
        */
        
        TextInputDialog dialog = new TextInputDialog("MyProgram.java");
        dialog.setTitle("另存新檔");
        dialog.setHeaderText("");
        dialog.setContentText("請輸入檔名：");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            //return result.get();
            filename=result.get();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("錯誤");
            alert.setHeaderText("");
            alert.setContentText("請輸入檔名！");
            alert.showAndWait();
            //return null;
            filename="";
        }
        
        // Retrieve code from keyAnswerArea and save to file.
        BufferedWriter javaFile = new BufferedWriter(new FileWriter(filename));
        javaFile.write(keyAnswerArea.getText());
        javaFile.flush();
        //make compileBtn able to push;
        compileBtn.setDisable(false);
        
        //System.out.println("=> print stCode: \n"+stCode);
    }
    
    
    //compile student's Code;
    //final String lsCmd = "ls";
    final String compileCmd = "javac Test.java";
    public void compileCode(ActionEvent event) throws IOException {
        
        //2.exec cmd and print output;
        
        // Issue javac <javaSource> command to terminal.
        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "javac", filename, "\r"));
        });
        
        //System.out.println("compile msg: "+tabPane);
        //System.out.println("compile msg: "+tabPane.accessibleTextProperty());
       
        //if compile without error message, then able to push "exec" btn;
        //if(terminal.){
            execBtn.setDisable(false);
        //}else{
        //    execBtn.setDisable(true);
        //}
       
    }
    
    //excute student's Code;
    final String execCmd = "java Test";
    public void execCode(ActionEvent event){
        
        //2.exec cmd and print output;
        // Issue java <javaClass> command to terminal.
        String className = filename.split("[.]")[0];
        terminal.onTerminalFxReady(() -> {
            terminal.command(String.join(" ", "java", className, "\r"));
        });
        
    }
    
//** snapshot function by  Yao/wade **--------------------------------------------------------------------;

    //開始截圖   
    public void startSnapshot(float scale,int freq) {
        screenSnapshot = new ScreenSnapshot((Student)student.getUser(), courseId, examId, student.getId() ,student.getName());
        screenSnapshot.startSnapShot(scale,freq);
        snapshotFlag = true;
    }
     
    //停止截圖   
    public void stopSnapshot() {
         screenSnapshot.stopSnapShot();
         snapshotFlag = false;
    }    
    
//** common function **--------------------------------------------------------------------;
 
    public void sendMsg(ActionEvent event){
        System.out.println("#InputMsg=> "+ student.getUser().getProfile().getContent().get("name").toString()+ " : " + msgInputArea.getText());
        student.getUser().sendMessage(courseId, examId, msgInputArea.getText());
        setMsgArea(student.getUser().getProfile().getContent().get("name").toString()+ " : " + msgInputArea.getText());
         msgInputArea.clear();
    }
    
    public void setMsgArea(String msg){
        msgArea.appendText(msg + "\n");
    }
   
   public void startExam(ActionEvent event) {
        if(courseItem.isDisable() == true) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(""); 
            alert.setContentText("您已交卷或考試尚未開始");
            final Optional<ButtonType> opt = alert.showAndWait();
        } else {
            mainPane.setVisible(true);
            examPane.setVisible(true);
            student.getExam().attendExam(courseId, examId);
        }
    }
    
//    考試暫停 _ 禁止編輯作答區域
    public void pauseExam() {
        msgInputArea.setEditable(false);
        msgInputArea.setStyle("-fx-control-inner-background: lightgray");
        setMsgArea("[系統] Exam pause");
    }
    
//    恢復考試 _ 開放編輯作答區域
    public void resumeExam() {
        msgInputArea.setEditable(true);
        msgInputArea.setStyle(null);
        setMsgArea("[系統] Exam resume");
     }
     
    public void closeExamPane(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("關閉考試視窗");
        alert.setHeaderText(""); 
        alert.setContentText("您真的要關閉考試視窗嗎？\n如尚未作答完成或交卷，就關閉此視窗\n!!將一律視同交卷!!");
        final Optional<ButtonType> opt = alert.showAndWait();
        if (opt.get() == ButtonType.OK) {
            examPane.setVisible(false);
            courseItem.setDisable(true);
        }
    }
    
    //    目前假設每一個exam只有一個problem
    private void setProblem(int courseId, int examId) {
       // pathLabel.setText(courseName + " -> " +examName);
        List problems = (List) student.getExam().queryProblems(courseId, examId).getContent().get("problems");
        questionArea_A.setText("Title: " + ((Map)problems.get(0)).get("problemName").toString() +
                "\n-----------\nDesc.: " + ((Map)problems.get(0)).get("description").toString());
        
        questionArea_B.setText("Title: " + ((Map)problems.get(0)).get("problemName").toString() +
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
    
    //  登出
    public void logout(ActionEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("登出系統");
        alert.setHeaderText(""); 
        alert.setContentText("您真的要登出嗎？\n!!如正在進行考試中選擇登出，並且作答尚未完成或交卷，將一律視同交卷!!");
        final Optional<ButtonType> opt = alert.showAndWait();
        
        try {
            if (student != null & opt.get() == ButtonType.OK) {
                if (snapshotFlag == true) {
                    stopSnapshot();
                    snapshotFlag = false;
                } 
                Stage studnetStage = (Stage) logoutBtn.getScene().getWindow();
                student.logout();
                student = null;
                System.out.println("Logout success.");
                loadLoginStage();
                studnetStage.close();
            } else {
                System.out.println("Canceled!");
            }
        } catch (Exception ioe) {
            System.out.println("\n*** IOException!!! ***");
        }
    }
    
    public void submitAnswer(ActionEvent event) {
    }
    
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
