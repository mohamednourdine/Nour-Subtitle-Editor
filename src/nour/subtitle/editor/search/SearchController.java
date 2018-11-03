/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.search;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import nour.subtitle.editor.database.DatabaseOperations;
import nour.subtitle.editor.showSrt.Subtitle;

/**
 * FXML Controller class
 *
 * @author NourSoft
 */
public class SearchController implements Initializable {

    @FXML
    private TextArea searchTextArea;
   // private ListView<String> searchResultList;
    @FXML
    private ChoiceBox<String> subLanguage;
    @FXML
    private JFXButton searchBut;
    @FXML
    private JFXButton cancelBut;
    
    ObservableList<Subtitle> getTheList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Subtitle, String> englishSearchResult;
    @FXML
    private TableColumn<Subtitle, String> turkishSearchResult;
    @FXML
    private TableView<Subtitle> subtileSearch;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SetSearchLanguages();
        initSTR();
    }
    private void initSTR() {        
        englishSearchResult.setCellValueFactory(new PropertyValueFactory<>("text"));
        turkishSearchResult.setCellValueFactory(new PropertyValueFactory<>("translation"));
    }
    private void SetSearchLanguages() {
        ObservableList<String> options = FXCollections.observableArrayList("English", "Turkish");

        subLanguage.setValue("English"); // this statement shows default value 

        subLanguage.setItems(options); // this statement adds all values in choiceBox
    }

    @FXML
    private void SearchForSubtile(ActionEvent event) {
        //System.out.println("Key pressed");
        DatabaseOperations save = new DatabaseOperations();
       // List<String> matchingStrings = new ArrayList<>();
        String language = subLanguage.getValue();
        getTheList = save.SearchForSubtitle(searchTextArea.getText(), language);

        
        for (Subtitle subtitle : getTheList) {
            System.out.println(getTheList.get(0).getTranslation());
        }
        
    
        subtileSearch.getItems().setAll(getTheList);
    }

    @FXML
    private void cancelOperation(ActionEvent event) {
        ((Stage) cancelBut.getScene().getWindow()).close();
    }

}
