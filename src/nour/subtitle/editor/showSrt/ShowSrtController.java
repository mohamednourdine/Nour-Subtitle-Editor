/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.showSrt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


/**
 * FXML Controller class
 *
 * @author NourSoft
 */
public class ShowSrtController implements Initializable {

    ObservableList<Subtitle> list = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Subtitle, String> number;
    @FXML
    private TableColumn<Subtitle, String> startTime;
    @FXML
    private TableColumn<Subtitle, String> endTime;
    @FXML
    private TableColumn<Subtitle, String> english;
    @FXML
    private TableColumn<Subtitle, String> turkish;
    @FXML
    private TableView<Subtitle> tableView;
    @FXML
    private Button open;

    private Path path;
    @FXML
    private Text filePath;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initCol();
        // loadData();

        System.out.println("This data is retrived!");

    }

    private void initCol() {
        number.setCellValueFactory(new PropertyValueFactory<>("nr"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("stop"));
        english.setCellValueFactory(new PropertyValueFactory<>("text"));
        turkish.setCellValueFactory(new PropertyValueFactory<>("text"));
    }

    @FXML
    private void loadData() throws IOException, ParseException {
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

        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(open.getScene().getWindow());

        if (file != null) {
            path = file.toPath();
            filePath.setText(file.getAbsolutePath());
        }

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
                Subtitle subtitle = new Subtitle(index, text, st, sp, sp);
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

  
}
