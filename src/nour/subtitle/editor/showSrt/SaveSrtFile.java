/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.showSrt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;

/**
 *
 * @author NourSoft
 */
public class SaveSrtFile {

    private List<Subtitle> subtitles;
    List<String> translations = new ArrayList<String>();

    public boolean SaveSrtFile(List<String> translations, String formername, String language) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.srt)", "*.srt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(formername+" - "+language);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            saveTextToFile(translations, file);
             return true;
        }
        return false;
    }

    public void saveTextToFile(List<String> translations, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);

            for (String translation : translations) {
                writer.println(translation);
            }
            writer.close();
        } catch (IOException ex) {
        }
    }

    public String prepareFirstMilliSeconds(String time) {
        String ms;
        ms = time.substring(9, 12);
        if (ms.length() == 2) {
            return "0" + ms;
        } else {
            return ms;
        }
    }

    public String prepareSecondMilliSeconds(String time) {
        String ms;
        ms = time.substring(26, 29);
        if (ms.length() == 2) {
            return "0" + ms;
        } else {
            return ms;
        }
    }

    public String prepareText(BufferedReader reader) throws IOException {
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

    public TimeCode prepareStart(String line) {
        String string = line.substring(0, 12);
        int milisecond = parseTime(string);
        return new TimeCode(string.substring(0, 8), milisecond);
    }

    public TimeCode prepareStop(String line) {
        String string = line.substring(line.length() - 12, line.length());
        int milisecond = parseTime(string);
        return new TimeCode(string.substring(0, 8), milisecond);
    }

    public int parseTime(String time) {
        int h, m, s, ms;
        h = Integer.parseInt(time.substring(0, 2));
        m = Integer.parseInt(time.substring(3, 5));
        s = Integer.parseInt(time.substring(6, 8));
        ms = Integer.parseInt(time.substring(9, 12));
        return ms + (s * 1000) + (m * 60000) + (h * 360000);
    }

}
