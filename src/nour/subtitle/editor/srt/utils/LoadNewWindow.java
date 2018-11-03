/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.srt.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author NourSoft
 */
public class LoadNewWindow {
        
    public void loadWindow(String location, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
             stage.setResizable(false);
            //stage.setAlwaysOnTop(true);
           // stage.initModality(Modality.NONE);
           // stage.initModality(Modality.APPLICATION_MODAL);
         
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/nour/subtitle/editor/images/noursoft.png")));
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            System.out.println("Could not load the new window NourKas +" + ex.getMessage());
            System.err.println("ERROR: "+ ex.getLocalizedMessage());
        }

    }
    
    public void loadsetResizableFrame(String location, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setResizable(true);
            //stage.setAlwaysOnTop(true);
           // stage.initModality(Modality.NONE);
           // stage.initModality(Modality.APPLICATION_MODAL);
         
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/nour/subtitle/editor/images/noursoft.png")));
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            System.out.println("Could not load the new window NourKas +" + ex.getMessage());
            System.err.println("ERROR: "+ ex.getLocalizedMessage());
        }
    }
    
        public void loadDisplayFrame(String location, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setResizable(false);
            //stage.setAlwaysOnTop(true);
           // stage.initModality(Modality.NONE);
            stage.initModality(Modality.APPLICATION_MODAL);
         
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/nour/subtitle/editor/images/noursoft.png")));
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            System.out.println("Could not load the new window NourKas +" + ex.getMessage());
            System.err.println("ERROR: "+ ex.getLocalizedMessage());
        }
    }
    
    
        public void loadLoader(String location, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.resizableProperty();
            stage.setResizable(false);
            //stage.setAlwaysOnTop(true);
           // stage.initModality(Modality.NONE);
            stage.initModality(Modality.APPLICATION_MODAL);
         
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/nour/subtitle/editor/images/noursoft.png")));
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
           
            stage.showAndWait();
            
        } catch (IOException ex) {
            System.out.println("Could not load the new window NourKas +" + ex.getMessage());
            System.err.println("ERROR: "+ ex.getLocalizedMessage());
        }

    }
}
