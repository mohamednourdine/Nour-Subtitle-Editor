/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.splash;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nour.subtitle.editor.upload.main;
import nour.subtitle.editor.srt.utils.LoadNewWindow;

/**
 * FXML Controller class
 *
 * @author NourSoft
 */
public class SplashController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private LoadNewWindow load;
    main mainApplication;
    @FXML
    private AnchorPane splashAnchorPane;
    @FXML
    private ImageView image;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new splash().start();
        load = new LoadNewWindow();
    }

    class splash extends Thread {

        @Override
        public void run() {
            try {
                Thread.sleep(2500);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                          // splashAnchorPane.getScene().getWindow().hide();
                         // get a handle to the stage
                        Stage stage = (Stage) splashAnchorPane.getScene().getWindow();                    
                        stage.close();
                        load.loadWindow("/nour/subtitle/editor/home/home.fxml", "Nour Automatic Subtitles Translator");
                    }
                });

            } catch (InterruptedException ex) {
                Logger.getLogger(SplashController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
