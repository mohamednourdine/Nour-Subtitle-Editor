/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.others;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import nour.subtitle.editor.alert.AlertMaker;

/**
 * FXML Controller class
 *
 * @author NourSoft
 */
public class LanguageSettingController implements Initializable {

    @FXML
    private ChoiceBox<String> appLangOptions;
    @FXML
    private ChoiceBox<String> defaultLanOptions;
    @FXML
    private ChoiceBox<String> transLangOptions;
    @FXML
    private JFXButton saveSettingsBut;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        applicationAppLanguageOptions();
        applicationDefaultLanguageOptions();
        applicationDefaultTranslationOptions();
    }

    @FXML
    private void saveSettings(ActionEvent event) {
        AlertMaker.showSimpleAlert("Settings Saved!", "Your default settings has been successfully saved");
        saveSettingsBut.getScene().getWindow().hide();

    }

    private void applicationAppLanguageOptions() {
        ObservableList<String> options = FXCollections.observableArrayList("English");
        appLangOptions.setValue("English"); // this statement shows default value 
        appLangOptions.setItems(options); // this statement adds all values in choiceBox
    }

    private void applicationDefaultLanguageOptions() {
        ObservableList<String> options = FXCollections.observableArrayList("English");
        defaultLanOptions.setValue("English"); // this statement shows default value 
        defaultLanOptions.setItems(options); // this statement adds all values in choiceBox
    }

    private void applicationDefaultTranslationOptions() {
        ObservableList<String> options = FXCollections.observableArrayList("Translate to English", "Translate to Turkish");
        transLangOptions.setValue("Translate to Turkish"); // this statement shows default value 
        transLangOptions.setItems(options); // this statement adds all values in choiceBox
    }

}
