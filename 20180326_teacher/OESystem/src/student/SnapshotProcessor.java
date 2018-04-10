package student;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.mislab.api.Student;

/**
 *
 * @author Yao
 */
public class SnapshotProcessor implements Runnable {
    private String file_path, studentId;
    private boolean flag_stop = true;
    private byte[] imageInByte;
    private Student student;
    private int courseId;
    private int examId;
    private final Base64.Encoder encoder = Base64.getEncoder();
    private float snapshotScale;
    private int snapshotFreq;
    
    public SnapshotProcessor(){
        
    }
    
    public SnapshotProcessor(Student s, int c_Id, int e_Id, String s_Id, float scale, int freq){
        student = s;
        courseId = c_Id;
        examId = e_Id;
        studentId = s_Id;
        snapshotScale = scale;
        snapshotFreq = freq;
    }
 
    @Override
    public void run(){
        System.out.println("**** Screen snapshot Begin ****");
        System.out.println("@SnapshotProcess.java -->scale= "+snapshotScale+",freq= "+snapshotFreq);
        while (flag_stop) {            
            try{
                // 更改截圖時間
                //Thread.sleep(5000);
                Thread.sleep(snapshotFreq*1000);
                file_path = getDateTime();     
                sendSnapshot(snapshotScreen(file_path));   
                System.out.println(file_path);
            } catch (InterruptedException ie) {
                System.out.println("\n*** InterruptedException!!! ( ImageProcessor.run ) ***");
            } catch (NullPointerException npe) {
                System.out.println("\n*** NullPointerException!!! ( ImageProcessor.run ) ***");
            } catch (Exception e) {
                    Logger.getLogger(SnapshotProcessor.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        System.out.println("Screen Shot Stop");
    }
    
    public BufferedImage snapshotScreen(String fileName) throws Exception {
        Robot robot = new Robot();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = new Rectangle(0, 0, d.width, d.height);
        BufferedImage image = robot.createScreenCapture(rect);
        file_path = fileName + "pic";
        ImageIO.write(image, "png", new File("../OESystem/student screen shot/" + file_path + ".png"));
        return image;
    }
   
    private void sendSnapshot(BufferedImage i) throws IOException {
        BufferedImage image = thumbnail(i);
        byte[] b = convet2byte(image);
        System.out.println("\nTime: " + getTime() + "\nWidth: " +
                image.getWidth() + "\nHeight: " + image.getHeight() + "\n===== " + studentId);
        
        student.sendSnapshot(courseId, examId, studentId, b);
    }
    
    private String getTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss:SSSS");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }
        
    public BufferedImage thumbnail(BufferedImage i) throws IOException {
        snapshotScale=50;
        //try {
          i = Thumbnails.of(i).scale(snapshotScale/100).asBufferedImage();
       // } catch (IOException ioe) {
       //     Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ioe);
        //}
        
        return i;
    }

    public byte[] convet2byte(BufferedImage i) {
        try{
            ByteArrayOutputStream baos = null;
   
            baos = new ByteArrayOutputStream();
            ImageIO.write(i, "png", baos);
            System.out.println("=====\nKB: " + baos.size()/1000);
            baos.flush();
            
            imageInByte = encoder.encode(baos.toByteArray());    
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        return imageInByte;
    }
    
    
    public String getDateTime(){
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy_MM_dd hh_mm_ss");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }
    
    public String getFileName() {
        return file_path;
    }
    
    public void stop() {
        flag_stop = false;
    }
}
