package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import static javafx.application.Application.launch;
import org.mislab.api.Config;

/**
 *
 * @author Yao
 */
public class Main extends Application {

    @FXML
    TextField id;
    
    /**
     * 
     * @param primaryStage 主要場景
     */
    @Override
    public void start(Stage primaryStage) {
        try{
            // 載入fxml
            Parent root = FXMLLoader.load(getClass().getResource("/main/MainFXML.fxml"));
            Scene scene = new Scene(root);
            
            // 設定主場景之畫面
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
