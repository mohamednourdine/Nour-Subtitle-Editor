/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.upload;

import javafx.scene.image.Image;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author NourSoft
 */
public class main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/nour/subtitle/editor/splash/splashfxml.fxml"));
        //        load.loadWindow("/nour/subtitle/editor/dictionary/translateDictionary.fxml", "Nour Automatic Dictionary");
//        Parent root = FXMLLoader.load(getClass().getResource("/nour/subtitle/editor/loader/loader.fxml"));

        stage = new Stage(StageStyle.UNDECORATED);
//        stage = new Stage(StageStyle.DECORATED);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Nour Automatic Subtitles Translator");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/nour/subtitle/editor/images/noursoft.png")));
        // Scene scene = new Scene(root, 1350, 800);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
