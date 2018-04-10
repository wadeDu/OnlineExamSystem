package student;

import org.mislab.api.Student;
import org.mislab.api.event.EventAction;
import org.mislab.api.event.EventType;
import org.mislab.api.event.OnlineExamEvent;
import org.mislab.test.event.UserAccount;

/**
 *
 * @author Max
 */
public class StudentAccount extends UserAccount {
    private String msgName, msg, action, proctor;
    private StudentController stStage;
    private float scale;
    private int freq,proctor_id;

    public StudentAccount(Student student) {
        super(student);
    }

    @Override
    public void setupEventListener() {
        evMgr.addEventListener(this, EventType.User, EventAction.Login);
        evMgr.addEventListener(this, EventType.User, EventAction.Logout);
        evMgr.addEventListener(this, EventType.Chat, EventAction.NewMessage);
        
        evMgr.addEventListener(this, EventType.Exam, EventAction.Halt);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Extend);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Pause);
        evMgr.addEventListener(this, EventType.Exam, EventAction.Resume);
        
        evMgr.addEventListener(this, EventType.Monitor, EventAction.Start);
        evMgr.addEventListener(this, EventType.Monitor, EventAction.Stop);
        evMgr.addEventListener(this, EventType.Monitor, EventAction.RequestTextRec);
    }
    
    @Override
    public void handleOnlineExamEvent(OnlineExamEvent e) {
        try{
            String sname = (String) e.getContent().get("name");
            System.out.println(String.format("%s got an event %s from %s", this.getName(), e, sname));
            switch ((String) e.getAction().name()){
                case "NewMessage":
                    msgName = (String) e.getContent().get("name");
                    msg = (String) e.getContent().get("message");
                    System.out.println("*" + msgName + " : " + msg);
                    stStage.setMsgArea(msgName + " : " + msg);
                    break;
                //20170918 catch snapshot parameters from Django;
                case "Start":
                    //System.out.println("@studentAccount start from Django");
                    //proctor_id = Integer.parseInt(e.getContent().get("proctor_id"));
                    //proctor = (String) e.getContent().get("proctor");
                    //System.out.println("@studentAccount"+proctor_id+","+proctor);
                    
                    scale = Float.parseFloat(e.getContent().get("scale"));
                    freq = Integer.parseInt(e.getContent().get("freq"));
                    System.out.println("@studentAccount from Django:scale= "+scale+", freq= "+freq);
                    
                    //20170919 catech prameters from Django;
                    //stStage.startSnapshot();
                    stStage.startSnapshot(scale,freq);
                    break;

                    
            }
        } catch (NullPointerException ex){
           // System.out.println(String.format("%s got an event %s", this.getName(), e));
            switch ((String) e.getAction().name()){
                case "Pause":
                    System.out.println("*Exam pause...");
                    stStage.pauseExam();
                    break;
                case "Resume":
                    System.out.println("*Exam resume...");
                    stStage.resumeExam();
                    break;
                case "Stop":
                    stStage.stopSnapshot();
                    break;
                //20171127 requestTextRec from teacher=>Server=>student;
                case "RequestTextRec":
                    System.out.println("@StudentAcoount => requestTextRec");
                    stStage.requestTextRec();    
                    break;
            }
            
        }              
    }
    
    public Student getExam(){
        return (Student)user;
    }
    
    public void setStage(StudentController ss){
        stStage = ss;
        stStage.setStudent(this);
    }
}
