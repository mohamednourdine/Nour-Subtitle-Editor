/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.translate;

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
import javafx.scene.control.MenuItem;
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
import nour.subtitle.editor.dictionary.DictionaryController;
import nour.subtitle.editor.dictionary.Languages;
import nour.subtitle.editor.showSrt.SaveSrtFile;
import nour.subtitle.editor.showSrt.Subtitle;
import nour.subtitle.editor.showSrt.TimeCode;
import nour.subtitle.editor.showSrt.Translation;
import nour.subtitle.editor.srt.utils.LoadNewWindow;
import nour.subtitle.editor.srt.utils.RingProgressIndicator;
import nour.subtitle.editor.upload.mainController;


/**
 * FXML Controller class
 *
 * @author NourSoft
 */
public class TranslateController implements Initializable {

    @FXML
    private JFXButton choiceFileBut;
    @FXML
    private ChoiceBox<String> fromLan;
    @FXML
    private ChoiceBox<String> toLang;
    @FXML
    private JFXButton translateBut;
    @FXML
    private JFXTextField startTime;
    @FXML
    private JFXTextField endTime;
    @FXML
    private TextArea enSub;
    @FXML
    private TextArea trSub;
    @FXML
    private JFXButton saveSub;
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
    @FXML
    private JFXButton homeBut;

    private Path path;
    List<String> translations = new ArrayList<String>();

    LoadNewWindow load;
    private Subtitle subtitle;
    ObservableList<Subtitle> list = FXCollections.observableArrayList();
    List<String> getTranslations = new ArrayList<String>();
    
    public static List<Subtitle> subtitles = new ArrayList<>();

    @FXML
    private JFXButton exportBut;
    @FXML
    private JFXButton newTransBut;

    SaveSrtFile saveSrtFile;

    String FileName;

    @FXML
    private ChoiceBox<String> fileType;
    @FXML
    private JFXButton uploadBut;
    @FXML
    private JFXButton seachForSub;
    @FXML
    private JFXButton choiceFileBut1;
    @FXML
    private JFXButton choiceFileBut2;
    @FXML
    private JFXButton choiceFileBut11;
    @FXML
    private JFXButton choiceFileBut21;
    @FXML
    private JFXButton choiceFileBut111;
    @FXML
    private JFXButton newBut;
    @FXML
    private Text enFileName;
    @FXML
    private Text trFileName;

    public static String filePathName;
    @FXML
    private MenuItem newOperationMenuitem;
    @FXML
    private MenuItem translateMenuitem;
    @FXML
    private MenuItem saveMenuitem;
    @FXML
    private MenuItem cancelMenuitem;
    @FXML
    private MenuItem exitMenuitem;
    @FXML
    private MenuItem undoMenuitem;
    @FXML
    private MenuItem redoMenuitem;
    @FXML
    private MenuItem copyMenuitem;
    @FXML
    private MenuItem pasteMenuitem;
    @FXML
    private MenuItem settingsMenuitem;
    @FXML
    private MenuItem aboutMenuitem;
    @FXML
    private MenuItem helpMenuitem;
    @FXML
    private MenuItem contributeMenuitem;
    @FXML
    private MenuItem donateMenuitem;
    @FXML
    private MenuItem contactMenuItem;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public String getFilePathName() {
        return filePathName;
    }

    public void setFilePathName(String filePathName) {
        this.filePathName = filePathName;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        load = new LoadNewWindow();
        initSTR();
        setFromLang();
        setToLang();
        getSelectedItemsData();

        saveSrtFile = new SaveSrtFile();
        //  transtlateDictionary();
    }

    private void initSTR() {
        number.setCellValueFactory(new PropertyValueFactory<>("nr"));
        startTimes.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimes.setCellValueFactory(new PropertyValueFactory<>("stop"));
        english.setCellValueFactory(new PropertyValueFactory<>("text"));
        turkish.setCellValueFactory(new PropertyValueFactory<>("translation"));
    }

    private void setFromLang() {
        ObservableList<String> options = FXCollections.observableArrayList("English", "Turkish");
        fromLan.setValue("English"); // this statement shows default value 
        fromLan.setItems(options); // this statement adds all values in choiceBox
    }

    private void setToLang() {
        ObservableList<String> options = FXCollections.observableArrayList("English", "Turkish");
        toLang.setValue("Turkish"); // this statement shows default value 
        toLang.setItems(options); // this statement adds all values in choiceBox
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
    private void choiceFileToTranslate(ActionEvent event) throws IOException, ParseException {
        if ("English".equals(fromLan.getValue()) && "English".equals(toLang.getValue()) || "Turkish".equals(fromLan.getValue()) && "Turkish".equals(toLang.getValue())) {
            AlertMaker.showSimpleAlert("Same languages selected", "Please select different languages and continue");
            return;
        }

        if (tableView.getItems().size() > 0) {
            AlertMaker.showSimpleAlert("Files already selected", "Finish with the on going operation first");
            return;
        }
//        
        String string = "";
        String[] number;
        String startTime = "";
        String endTime = "";
        String enLine = "";
        String trLine = "";

        //selecting the txt file gui
        FileChooser fileChooser = new FileChooser();
        //only accept text files
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("SRT files (*.srt)", "*.srt");

        fileChooser.setTitle("Select the english Subtitle File");

        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(choiceFileBut.getScene().getWindow());

        if (file != null) {

//            Set the fileName for global use
            setFileName(file.getName());

            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            Map<Integer, Subtitle> subtitleMap = new TreeMap<>();

            String line = reader.readLine();
            int index = 1;

            while (line != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    line = line.replaceAll("\\s+", "");

                    line = reader.readLine();

                    TimeCode start = saveSrtFile.prepareStart(line);
                    String st = start.getText();
                    String milest = saveSrtFile.prepareFirstMilliSeconds(line);
                    String startT = st + "," + milest;

                    TimeCode stop = saveSrtFile.prepareStop(line);
                    String sp = stop.getText();
                    String milesp = saveSrtFile.prepareSecondMilliSeconds(line);
                    String startS = sp + "," + milesp;

                    String text = saveSrtFile.prepareText(reader);

                    if ("English".equals(fromLan.getValue())) {
                        subtitle = new Subtitle(index, startT, startS, text, "");
                    } else {
                        subtitle = new Subtitle(index, startT, startS, "", text);
                    }

                    subtitleMap.put(start.getMiliSecond(), subtitle);

                    list.add(subtitle);

                    index++;
                    line = reader.readLine();
                }

                tableView.getItems().setAll(list);
                choiceFileBut.isDisable();
            }
        } else {
            System.out.println("No file selected");
        }

    }

    @FXML
    private void translateTheSubtitleFile(ActionEvent event) throws IOException {

        subtitles = tableView.getItems();
        if (subtitles.size() < 1) {
            AlertMaker.showSimpleAlert("No data to Translate!", "Select the subtitle file you want to translate.");
            enSub.setText("");
            trSub.setText("");
        } else if ("English".equals(fromLan.getValue()) && "English".equals(toLang.getValue()) || "Turkish".equals(fromLan.getValue()) && "Turkish".equals(toLang.getValue())) {
            AlertMaker.showSimpleAlert("Same languages selected", "Please select different languages and continue");
            return;
        } else {

            DatabaseOperations translate = new DatabaseOperations();
            List<Subtitle> subs = new ArrayList<>();
            String translations = null;
            ObservableList<Subtitle> list = FXCollections.observableArrayList();

            String language = fromLan.getValue();

            subs = tableView.getItems();
            String translation = null;

            if ("English".equals(fromLan.getValue())) {
                for (Subtitle sub : subs) {
                    translations = translate.getTranslation(sub.getText(), language);

                    subtitle = new Subtitle(sub.getNr(), sub.getStart(), sub.getStop(), sub.getText(), translations);

                    //lets see the tranlation result with this variable
                    translation = subtitle.toString(sub.getNr(), sub.getStart(), sub.getStop(), translations);
                    getTranslations.add(translation);

                    list.add(subtitle);
                    System.out.println(translation);
                  
                }
            } else {
                for (Subtitle sub : subs) {
                    translations = translate.getTranslation(sub.getTranslation(), language);
                    subtitle = new Subtitle(sub.getNr(), sub.getStart(), sub.getStop(), translations, sub.getTranslation());

                    //lets see the tranlation result with this variable
                    translation = subtitle.toString(sub.getNr(), sub.getStart(), sub.getStop(), translations);
                    getTranslations.add(translation);

                    list.add(subtitle);
                    System.out.println(translation);
                    exportBut.setDisable(false);
                }

            }

            tableView.getItems().setAll(list);
              exportBut.setDisable(false);

        }
    }

    @FXML
    private void editSubTitles(ActionEvent event) {
        subtitles = tableView.getItems();
        String enText = enSub.getText();
        String trText = trSub.getText();

        if (subtitles.size() < 1) {
            AlertMaker.showSimpleAlert("No data to save!", "Select the subtitle file and its translation first before trying to save it.");
            enSub.setText("");
            trSub.setText("");
        } else {
            subtitle = tableView.getSelectionModel().getSelectedItem();
            subtitle.setText(enText);
            subtitle.setTranslation(trText);

            tableView.refresh();
            startTime.setText("");
            endTime.setText("");
            enSub.setText("");
            trSub.setText("");
        }
    }

    @FXML
    private void goToHomeFrame(ActionEvent event) {
        choiceFileBut.getScene().getWindow().hide();
        load.loadWindow("/nour/subtitle/editor/home/home.fxml", "Nour Automatic Subtitles Translator");
    }

    @FXML
    private void exportSubtitles(ActionEvent event) {
        if (tableView.getItems().size() < 1) {
            AlertMaker.showSimpleAlert("No data to Export!", "No data to export");
            enSub.setText("");
            trSub.setText("");
        } else {
            String fileOriginalName = getFileName();
            String tranlationLang = toLang.getValue();
            boolean isSaved = saveSrtFile.SaveSrtFile(getTranslations, fileOriginalName, tranlationLang);

            if (isSaved) {
                AlertMaker.showSimpleAlert("Success", "Your file is sucessfully saved. Thanks for using Nour Subtitle Editor");
                tableView.getItems().clear();
                exportBut.setDisable(true);

            }
        }
    }

    @FXML
    private void newTranslationOperation(ActionEvent event) {
        System.out.println("I am ready");
        tableView.getItems().clear();
    }

//Here we are Translating the srt online using Google Translation API
    private void googleTranslator(ActionEvent event) {

        if (tableView.getItems().size() < 1) {
            AlertMaker.showSimpleAlert("No data to Translate!", "Please choose your file and before trying to translate");
            enSub.setText("");
            trSub.setText("");
        } else {
            boolean isTranslated = false;
            String fileOriginalName = getFileName();
            String tranlationLang = toLang.getValue();
            onlinetranslation onlinetranslate = new onlinetranslation();

            List<Subtitle> subs = new ArrayList<>();
            List<Subtitle> Translations = new ArrayList<>();

            subs = tableView.getItems();

            if (tranlationLang.equalsIgnoreCase("English")) {
                System.out.println("-----------------********TR EN********---------------");

                isTranslated = onlinetranslate.onlineGetTranslation(subs, Languages.TURKISH, Languages.ENGLISH);
            } else {
                System.out.println("-----------------*******EN TR*********---------------");

                isTranslated = onlinetranslate.onlineGetTranslation(subs, Languages.ENGLISH, Languages.TURKISH);
            }

            tableView.refresh();

            if (isTranslated) {
                AlertMaker.showSimpleAlert("Success", "Your file is has been translated successfully. Thanks for using Nour Subtitle Editor");
                exportBut.setDisable(false);
            }
        }

        //  load.loadWindow("/nour/subtitle/editor/dictionary/Main.fxml", "Nour Automatic Subtitles Translator");
    }

    //HERE I AM GOING TO TRANSLATE NourDictiory using this function.
    //Everything will be fine Inchallah
    //I AM REALLY ENJOYING MYSELF HERE, SEING MYSELF AT THIS LEVEL IS REALLY SOMETHING GREAT.
    private void transtlateDictionary() {

        DatabaseOperations translate = new DatabaseOperations();
        DictionaryController translationController = new DictionaryController();

        List<String> enWords = new ArrayList<>();

        enWords = translate.getDictionaryWords();

        String result;

        for (String enWord : enWords) {
            result = translationController.translateString(enWord, Languages.ENGLISH, Languages.TURKISH);
            translate.SaveTranslation(enWord, result);
            System.out.println(" " + enWord + " --- " + result);

        }

    }

    @FXML
    private void OpenSaveWindow(ActionEvent event) {

        subtitles = tableView.getItems();
        int fileID;
        SaveSubtitles();
        // System.out.println(Subs.size());
        if (subtitles.size() < 1) {
            AlertMaker.showSimpleAlert("No data to save!", "Select the subtitle file and its translation first before trying to save it.");
        } else {
            // load.loadWindow("/nour/subtitle/editor/loader/loader.fxml", "loading ...");

            UploadSubtitlesToDatabase();

            AlertMaker.showSimpleAlert("Success", "The files has ben seuccessfully uploaded to the database. Thanks for using Nour Subtitle Editor");
            tableView.getItems().clear();
            subtitles.clear();
            enFileName.setText("");
            trFileName.setText("");

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
            enFileName.setText("English File Name: " + file.getName());
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

            setFileTypes();

            fileType.setDisable(false);
            uploadBut.setDisable(false);
        } else {
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
            trFileName.setText("Turkish File Name: " + file.getName());

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

    @FXML
    private void saveTheActivity(ActionEvent event) {
    }

    @FXML
    private void cancelOperation(ActionEvent event) {
    }

    @FXML
    private void exitSoftware(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void undoTheOperation(ActionEvent event) {
    }

    @FXML
    private void redoTheOperation(ActionEvent event) {
    }

    @FXML
    private void copyTheSubtitle(ActionEvent event) {
    }

    @FXML
    private void pasteTheSubtitle(ActionEvent event) {
    }

    @FXML
    private void openApplicationSettings(ActionEvent event) {
         load.loadDisplayFrame("/nour/subtitle/editor/others/languageSetting.fxml", "Nour Automatic Subtitles Translator");
    }

    @FXML
    private void openAboutView(ActionEvent event) {
         load.loadDisplayFrame("/nour/subtitle/editor/others/aboutFrame.fxml", "Nour Automatic Subtitles Translator");
    }

    @FXML
    private void openHelpView(ActionEvent event) {
        
    }

    @FXML
    private void contributeProject(ActionEvent event) {
    }

    @FXML
    private void donateFrame(ActionEvent event) {
         load.loadDisplayFrame("/nour/subtitle/editor/others/donate.fxml", "Nour Automatic Subtitles Translator");
    }

    @FXML
    private void contactUsFrame(ActionEvent event) {
    }

    
    
}
