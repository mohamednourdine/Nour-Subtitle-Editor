/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.upload;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nour.subtitle.editor.alert.AlertMaker;
import nour.subtitle.editor.database.DatabaseOperations;
import nour.subtitle.editor.showSrt.TimeCode;
import nour.subtitle.editor.showSrt.Subtitle;
import nour.subtitle.editor.showSrt.Translation;
import nour.subtitle.editor.srt.utils.LoadNewWindow;
import nour.subtitle.editor.srt.utils.RingProgressIndicator;

/**
 * FXML Controller class
 *
 * @author NourSoft
 */
public class mainController implements Initializable {

    @FXML
    private JFXButton newBut;
    @FXML
    private JFXTextField startTime;
    @FXML
    private JFXTextField endTime;
    @FXML
    private TextArea enSub;
    @FXML
    private TextArea trSub;
    @FXML
    private TableView<Subtitle> tableView;
    @FXML
    private TableColumn<Subtitle, String> number;
    @FXML
    private TableColumn<Subtitle, String> startTimes;
    @FXML
    private TableColumn<Subtitle, String> endTimes;
    @FXML
    private TableColumn<Subtitle, String> english;
    @FXML
    private TableColumn<Subtitle, String> turkish;

    private Path path;
    private LoadNewWindow load;
    private Subtitle subtitle;
    ObservableList<Subtitle> list = FXCollections.observableArrayList();
    List<String> translations = new ArrayList<String>();

    public static List<Subtitle> mainsubtitles = new ArrayList<>();

    @FXML
    private Text enFileName;
    @FXML
    private Text trFileName;

    public static String filePathName;

    @FXML
    private JFXButton uploadBut;
    @FXML
    private JFXButton seachForSub;
    @FXML
    private ChoiceBox<String> fileType;
    @FXML
    private JFXButton saveSub;
    @FXML
    private JFXButton homeBut;

    public String getFilePathName() {
        return filePathName;
    }

    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        initSTR();
        load = new LoadNewWindow();
        getSelectedItemsData();
        setFileTypes();

    }

    private void initSTR() {
        number.setCellValueFactory(new PropertyValueFactory<>("nr"));
        startTimes.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimes.setCellValueFactory(new PropertyValueFactory<>("stop"));
        english.setCellValueFactory(new PropertyValueFactory<>("text"));
        turkish.setCellValueFactory(new PropertyValueFactory<>("translation"));
    }

    @FXML
    public void loadData() throws IOException, ParseException {
        String string = "";
        String[] number;
        String startTime = "";
        String endTime = "";
        String enLine = "";
        String trLine = "";
        List<String> getTranslations = new ArrayList<String>();

        //selecting the txt file gui
        FileChooser fileChooser = new FileChooser();
        //only accept text files
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("SRT files (*.srt)", "*.srt");

        fileChooser.setTitle("Select the english Subtitle File");

        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(newBut.getScene().getWindow());

        if (file != null) {
            path = file.toPath();
            enFileName.setText(file.getName());
            setFilePathName(file.getName());
            
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            Map<Integer, Subtitle> subtitleMap = new TreeMap<>();

            String line = reader.readLine();
            int index = 1;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
            }

            getTranslations = addTranslation();

            while (line != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    line = line.replaceAll("\\s+", "");
                    // Number numbers = NumberFormat.getInstance().parse(line);
                    //  int num = (Integer.parseInt(line));
//                int num;
//                num = (int) line;
                    line = reader.readLine();
                    TimeCode start = prepareStart(line);
                    String st = start.getText();

                    TimeCode stop = prepareStop(line);
                    String sp = stop.getText();

                    String text = prepareText(reader);
                    subtitle = new Subtitle(index, st, sp, text, getTranslations.get(index - 1));
                    subtitleMap.put(start.getMiliSecond(), subtitle);

                    list.add(subtitle);

//                System.out.println(subtitle.getNr());
//                System.out.println(subtitle.getStart());
//                System.out.println(subtitle.getStop());
//                System.out.println(subtitle.getText());
//                          
                    index++;
                    line = reader.readLine();
                }

                tableView.getItems().setAll(list);

            }
        }else{
            System.out.println("No file selected");
        }

    }

    private List addTranslation() throws IOException, ParseException {
        ObservableList<String> list2 = FXCollections.observableArrayList();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("SRT files (*.srt)", "*.srt");
        fileChooser.setTitle("Select the Turkish File");

        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(newBut.getScene().getWindow());

        if (file != null) {
            path = file.toPath();
            trFileName.setText(file.getName());
            
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            Map<Integer, Translation> TranslationMap = new TreeMap<>();

            String line = reader.readLine();
            int index = 1;
            while (line != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    line = line.replaceAll("\\s+", "");
                    line = reader.readLine();
                    TimeCode start = prepareStart(line);
                    String st = start.getText();

                    TimeCode stop = prepareStop(line);
                    String sp = stop.getText();

                    String text = prepareText(reader);

                    translations.add(text);

                    index++;
                    line = reader.readLine();

                }
            }

        }
        return translations;
    }

    private String prepareText(BufferedReader reader) throws IOException {
        StringBuffer sb = new StringBuffer();
        String text = "";
        String line = reader.readLine();
        while (line != null && !line.isEmpty()) {
//            if (sb.length() > 1) {
//                sb.append()
//            }
            sb.append(line);
            line = reader.readLine();
        }
        return sb.toString();
    }

    private TimeCode prepareStart(String line) {
        String string = line.substring(0, 12);
        int milisecond = parseTime(string);
        return new TimeCode(string.substring(0, 8), milisecond);
    }

    private TimeCode prepareStop(String line) {
        String string = line.substring(line.length() - 12, line.length());
        int milisecond = parseTime(string);
        return new TimeCode(string.substring(0, 8), milisecond);
    }

    private int parseTime(String time) {
        int h, m, s, ms;
        h = Integer.parseInt(time.substring(0, 2));
        m = Integer.parseInt(time.substring(3, 5));
        s = Integer.parseInt(time.substring(6, 8));
        ms = Integer.parseInt(time.substring(9, 12));
        return ms + (s * 1000) + (m * 60000) + (h * 360000);
    }

    private void getSelectedItemsData() {

        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Subtitle sb = tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex());
                startTime.setText(sb.getStart());
                endTime.setText(sb.getStop());
                enSub.setText(sb.getText());
                trSub.setText(sb.getTranslation());

            }

        });
    }

    @FXML
    private void OpenSaveWindow(ActionEvent event) {
        mainsubtitles = tableView.getItems();
        int fileID;
        SaveSubtitles();
        // System.out.println(Subs.size());
        if (mainsubtitles.size() < 1) {
            AlertMaker.showSimpleAlert("No data to save!", "Select the subtitle file and its translation first before trying to save it.");
        } else {
            // load.loadWindow("/nour/subtitle/editor/loader/loader.fxml", "loading ...");

            UploadSubtitlesToDatabase();
            
          
        }

    }

    public void SaveSubtitles() {

    }

    public void UploadSubtitlesToDatabase() {

        RingProgressIndicator ringProgressIndicator = new RingProgressIndicator();
        ringProgressIndicator.setRingWidth(200);
        ringProgressIndicator.makeIndeterminate();
        // Parent parent = FXMLLoader.load(getClass().getResource(location));
        Stage stage = new Stage(StageStyle.DECORATED);
        StackPane root = new StackPane();
        root.getChildren().add(ringProgressIndicator);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root, 300, 300);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/nour/subtitle/editor/images/noursoft.png")));
        stage.setTitle("Saving ...");
        stage.setScene(scene);

        int number = 0;
        String textType = fileType.getValue();
        String textName = getFilePathName();

        DatabaseOperations save = new DatabaseOperations();

        List<Subtitle> subs = new ArrayList<>();
        subs = tableView.getItems();
        System.out.println("Type: " + textType + " File Name: " + textName);

        if (textName.equals("") || textType.equals("")) {
            AlertMaker.showSimpleAlert("Error occured", "File Name or Title can not be left blank. Please fill it in and try again");
        } else {
            number = save.SaveFileName(textName, textType);

            if (number > 0) {
                System.out.println("File Information saved successfully");
                System.out.println("file id is: " + number + "Files number: " + subs.size());

               // loadProgressBar();

                stage.show();

                for (Subtitle subtitle : subs) {
                    System.out.println(subtitle.getNr() + " " + subtitle.getStart() + " " + subtitle.getStop() + " " + subtitle.getText() + " " + subtitle.getTranslation());
                    save.UploadSubtitle(subtitle, number);

                }

                stage.close();
            } else {
                AlertMaker.showSimpleAlert("Error occured", "The file information could not be saved.");

            }
        }

    }

    public void loadProgressBar() {

        //stage.setAlwaysOnTop(true);
        // stage.initModality(Modality.NONE);
    }

    @FXML
    private void SeachForSubtitle(ActionEvent event) {
        load.loadWindow("/nour/subtitle/editor/search/search.fxml", "Search");
    }

    private void setFileTypes() {
        ObservableList<String> options = FXCollections.observableArrayList("Film", "Serie");

        fileType.setValue("Film"); // this statement shows default value 

        fileType.setItems(options); // this statement adds all values in choiceBox
    }

    @FXML
    private void editSubTitles(ActionEvent event) {
        mainsubtitles = tableView.getItems();
        String enText = enSub.getText();
        String trText = trSub.getText();
        int fileID;
        SaveSubtitles();
        // System.out.println(Subs.size());
        if (mainsubtitles.size() < 1) {
            AlertMaker.showSimpleAlert("No data to save!", "Select the subtitle file and its translation first before trying to save it.");
            enSub.setText("");
            trSub.setText("");
        } else {
            subtitle = tableView.getSelectionModel().getSelectedItem();
            subtitle.setText(enText);
            subtitle.setTranslation(trText);

            tableView.refresh();
            enSub.setText("");
            trSub.setText("");
        }
    }

    @FXML
    private void goToHomeFrame(ActionEvent event) {
        newBut.getScene().getWindow().hide();
        load.loadWindow("/nour/subtitle/editor/home/home.fxml", "Nour Automatic Subtitles Translator");
    }

}
