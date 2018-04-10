package student;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
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
    private String file_path, studentId, studentName;
    private boolean snapshot_start = true;
    private byte[] imageInByte;
    private Student student;
    private int courseId;
    private int examId;
    private final Base64.Encoder encoder = Base64.getEncoder();
    private float snapshotScale;
    private int snapshotFreq;
    private int term_of_pic;
    
    public SnapshotProcessor(){
        
    }
    
    public SnapshotProcessor(Student s, int c_Id, int e_Id, String s_Id, String s_Name, float scale, int freq){
        student = s;
        courseId = c_Id;
        examId = e_Id;
        studentId = s_Id;
        studentName = s_Name;
        snapshotScale = scale;
        snapshotFreq = freq;
    }
    
    @Override
    public void run(){
        System.out.println("**** Screen snapshot Begin ****");
        System.out.println("@SnapshotProcess.run() => scale= "+snapshotScale+",freq= "+snapshotFreq);
        
        term_of_pic=1;
        while (snapshot_start) {   
            
            try{
                //System.out.println("@SnapshotProcessor.run() "+count+" => snapshot_start: "+snapshot_start);
                // 更改截圖時間
                Thread.sleep(snapshotFreq*1000);
                
                file_path = getDateTime()+"_pic";     
                sendSnapshot(snapshotScreen(file_path));   
                
                //System.out.println(file_path);
                
            } catch (InterruptedException ie) {
                System.out.println("\n*** InterruptedException!!! ( ImageProcessor.run ) ***");
            } catch (NullPointerException npe) {
                System.out.println("\n*** NullPointerException!!! ( ImageProcessor.run ) ***");
            } catch (Exception e) {
                    Logger.getLogger(SnapshotProcessor.class.getName()).log(Level.SEVERE, null, e);
            }
            term_of_pic++;
        }
        System.out.println("@SnapshotProcessor => Screen snapshot Stop");
        
    }
    private int getTermOfPic(){
        return term_of_pic;
    } 
    
    public BufferedImage snapshotScreen(String fileName) throws Exception {
        Robot robot = new Robot();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle rect = new Rectangle(0, 0, d.width, d.height);
        BufferedImage image = robot.createScreenCapture(rect);
        //file_path = fileName + "pic";
        
        int lastIndex = System.getProperty("user.dir").lastIndexOf("OESystem");
        String path = System.getProperty("user.dir").substring(0, lastIndex+8);
        //System.out.println("path: "+path);
        ImageIO.write(image, "png", new File(path+"/stSnapshot/" + fileName + ".png"));
        //you can add mkdir of the date here later;
        return image;
    }
   
    private void sendSnapshot(BufferedImage i) throws IOException {
        BufferedImage image = thumbnail(i);
        byte[] b = convet2byte(image);
        
        
        System.out.println("\n=================================== "+
                           "\nterm: "+getTermOfPic()+
                           "\nname: "+ studentName + " ,id: "+ studentId +
                           "\nscale = "+snapshotScale + "% ,freq= "+snapshotFreq + " sec"+
                           "\nWidth: "+ image.getWidth() + " ,Height: " + image.getHeight() + 
                           "\nImage: "+getImgSize()+ " KB"+
                           "\nTime: " + getTime() + 
                           "\n=================================== " );
        
        student.sendSnapshot(courseId, examId, studentId, b, getTermOfPic());
    }
    
    private String getTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss:SSSS");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }
        
    public BufferedImage thumbnail(BufferedImage i) throws IOException {
        //snapshotScale=50;
        //try {
          i = Thumbnails.of(i).scale(snapshotScale/100).asBufferedImage();
       // } catch (IOException ioe) {
       //     Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, null, ioe);
        //}
        
        return i;
    }
    
    private double imgSize;
    public byte[] convet2byte(BufferedImage i) {
        try{
            ByteArrayOutputStream baos = null;
   
            baos = new ByteArrayOutputStream();
            ImageIO.write(i, "png", baos);
            
            System.out.println("=====\nBytes: " + baos.size());
            
            DecimalFormat df = new DecimalFormat("#.00");
            imgSize = Double.parseDouble(df.format((double)baos.size()/1024));  //1KB=1024 Bytes;
            
            System.out.println("KB: " + imgSize +"\n=====");
            baos.flush();
          
            imageInByte = encoder.encode(baos.toByteArray());    
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        return imageInByte;
    }
    
    private double getImgSize(){
        return imgSize;
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
        //System.out.println("---------------------------------------------------");
        //System.out.println("@SnapshotProcessor.stop() => snapshot_start: "+snapshot_start);
        snapshot_start = false;
        //System.out.println("@SnapshotProcessor.stop() => snapshot_start: "+snapshot_start);
    }
}
