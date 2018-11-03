/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.databaseOperation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import nour.subtitle.editor.alert.AlertMaker;
import nour.subtitle.editor.database.DatabaseOperations;
import nour.subtitle.editor.upload.mainController;
import nour.subtitle.editor.srt.utils.LoadNewWindow;

/**
 * FXML Controller class
 *
 * @author NourSoft
 */
public class SaveNewFileController implements Initializable {

    @FXML
    private JFXTextField nameOfFile;
    @FXML
    private JFXTextField typeOfPlay;
    @FXML
    private JFXButton cancelBut;
    @FXML
    private JFXButton saveBut;

    mainController main;
    Integer number = 0;
    private LoadNewWindow loader;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        main = new mainController();
        //nameOfFile.setText(main.getEnFileName().toString());
        init();
        loader = new LoadNewWindow();
    }

    private void init() {
        nameOfFile.setText(main.getFilePathName());
       
    }

    @FXML
    private void CancelOperation(ActionEvent event) {
        ((Stage) cancelBut.getScene().getWindow()).close();

    }

    @FXML
    private void SaveNewSubtitle(ActionEvent event) {
        DatabaseOperations save = new DatabaseOperations();
     
        String textName = nameOfFile.getText();
        String textType = typeOfPlay.getText();

        Integer number = 0;

        if (textName.equals("") || textType.equals("")) {
            AlertMaker.showSimpleAlert("Error occured", "File Name or Title can not be left blank. Please fill it in and try again");
        } else {
            number = save.SaveFileName(textName, textType);
          

            if (number > 0) {
                ((Stage) saveBut.getScene().getWindow()).close();
               //  main.UploadSubtitlesToDatabase(number);
            } else {
                AlertMaker.showSimpleAlert("Error occured", "The file information could not be saved.");

            }
        }

    }

    private void test() {

    }

}
