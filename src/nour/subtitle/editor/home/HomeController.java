/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.home;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import nour.subtitle.editor.alert.AlertMaker;
import nour.subtitle.editor.srt.utils.LoadNewWindow;

/**
 * FXML Controller class
 *
 * @author NourSoft
 */
public class HomeController implements Initializable {

    @FXML
    private JFXButton TranslateBut;
    @FXML
    private JFXButton uploadBut;

    /**
     * Initializes the controller class.
     */
    LoadNewWindow load;
    @FXML
    private JFXButton aboutBut;
    @FXML
    private JFXButton settingBut;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        load = new LoadNewWindow();
    }

    @FXML
    private void OpenTranslateView(ActionEvent event) {
        uploadBut.getScene().getWindow().hide();
        load.loadsetResizableFrame("/nour/subtitle/editor/translate/Translate.fxml", "Nour Automatic Subtitles Translator");
    }

    @FXML
    private void OpenUploadView(ActionEvent event) {
        AlertMaker.showSimpleAlert("Edit Options Comming Soon", "The edit option is not available in this version of our software. Follow our updates and do not miss any new release.");
    }

    @FXML
    private void OpenAboutScene(ActionEvent event) {
        load.loadDisplayFrame("/nour/subtitle/editor/others/aboutFrame.fxml", "Nour Automatic Subtitles Translator");
    }

    @FXML
    private void OpenSettingsScene(ActionEvent event) {
        load.loadDisplayFrame("/nour/subtitle/editor/others/languageSetting.fxml", "Nour Automatic Subtitles Translator");

    }

}
