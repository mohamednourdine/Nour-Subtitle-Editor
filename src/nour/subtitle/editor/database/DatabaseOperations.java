/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nour.subtitle.editor.dictionary.Languages;
import nour.subtitle.editor.showSrt.Subtitle;
import nour.subtitle.editor.srt.utils.DBConnection;
import nour.subtitle.editor.translate.OfflineTranslationOperation;
import nour.subtitle.editor.translate.onlinetranslation;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author NourSoft
 */
public class DatabaseOperations {

    Connection connection = DBConnection.getConnection();
    private static Statement statement = null;
    ObservableList<Subtitle> list = FXCollections.observableArrayList();

    public Integer SaveFileName(String SubName, String SubType) {
        int id = 0;
        String SaveFileInfo = "INSERT INTO fileinfo(name,type) VALUES (?,?)";

        try {
            PreparedStatement ps = connection.prepareStatement(SaveFileInfo);

            ps.setString(1, SubName);
            ps.setString(2, SubType);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
                System.out.println("Saved with id: " + id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return id;
    }

    public void UploadSubtitle(Subtitle subtitle, Integer id) {

        try {

            PreparedStatement ps = connection.prepareStatement("INSERT INTO subtitletexts(sub_id,number,startTime,endTime,enSub,trSub)"
                    + "values(?,?,?,?,?,?) ");

            ps.setInt(1, id);
            ps.setInt(2, subtitle.getNr());
            ps.setString(3, subtitle.getStart());
            ps.setString(4, subtitle.getStop());
            ps.setString(5, subtitle.getText());
            ps.setString(6, subtitle.getTranslation());

            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ObservableList<Subtitle> SearchForSubtitle(String text, String language) {
        PreparedStatement ps = null;
        List<String> matching = new ArrayList<>();
        Integer number;
        String startTime, endTime, trSub, enSub;

        try {

            if (language.equals("English")) {
                String getTrSub = "SELECT * FROM subtitletexts WHERE enSub LIKE '%" + text + "%'";
                // SELECT * FROM Customers WHERE CustomerName NOT LIKE 'a%';
                ps = connection.prepareStatement(getTrSub);

            } else if (language.equals("Turkish")) {
                String getEnSub = "SELECT * FROM subtitletexts WHERE trSub LIKE '%" + text + "%'";
                ps = connection.prepareStatement(getEnSub);
            }
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (language.equals("English")) {
                    number = rs.getInt("number");
                    startTime = rs.getString("startTime");
                    endTime = rs.getString("endTime");
                    enSub = rs.getString("enSub");
                    trSub = rs.getString("trSub");

                    Subtitle subtitle = new Subtitle(number, startTime, endTime, enSub, trSub);

                    list.add(subtitle);

                } else if (language.equals("Turkish")) {
                    number = rs.getInt("number");
                    startTime = rs.getString("startTime");
                    endTime = rs.getString("endTime");
                    enSub = rs.getString("enSub");
                    trSub = rs.getString("trSub");

                    Subtitle subtitle = new Subtitle(number, startTime, endTime, enSub, trSub);

                    list.add(subtitle);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    public String getTranslation(String text, String fromLan) throws IOException {
        PreparedStatement ps = null;
        String result = null;
        String escapedSQL = StringEscapeUtils.escapeSql(text);

     //   result = offlineTranslate(text, fromLan);

        try {

            if (fromLan.equals("English")) {
                String getTrSub = "SELECT trSub FROM  subtitletexts WHERE enSub LIKE '%" + escapedSQL + "%'";

                ps = connection.prepareStatement(getTrSub);

            } else if (fromLan.equals("Turkish")) {
                String getEnSub = "SELECT enSub FROM  subtitletexts WHERE trSub LIKE '%" + escapedSQL + "%'";
                ps = connection.prepareStatement(getEnSub);
            }
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                result = offlineTranslate(text, fromLan);
            } else {

                do {
                    if (fromLan.equals("English")) {
                        result = rs.getString("trSub");

                    } else if (fromLan.equals("Turkish")) {
                        result = rs.getString("enSub");
                    }
                } while (rs.next());
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private String offlineTranslate(String text, String fromLan) throws IOException {
        PreparedStatement ps = null;
        String result = null;
        String escapedSQL = StringEscapeUtils.escapeSql(text);
        List<String> offlineresult = new ArrayList<String>();

        boolean dot = false;
        boolean exist = false;
        boolean exist2 = false;

        String words[] = text.split(" ");
        int size = words.length;
        String sentences[] = null;

//        for (int i = 0; i < test.length(); i++) {
//            sentences[i] = "in";
//            System.out.println(sentences[i]);
//        }
        OfflineTranslationOperation offline = new OfflineTranslationOperation();

        offlineresult = offline.analyseNLP(text);

        onlinetranslation onlinetranslate = new onlinetranslation();

//        onlinetranslate.onlineGetSentenceTranslation(text, Languages.TURKISH, Languages.ENGLISH);
        result = onlinetranslate.onlineGetSentenceTranslation(text, Languages.ENGLISH, Languages.TURKISH);

        System.out.println(text + "\n");
        System.out.println(onlinetranslate.onlineGetSentenceTranslation(text, Languages.ENGLISH, Languages.TURKISH));

        for (String string : offlineresult) {
            System.out.print(onlinetranslate.onlineGetSentenceTranslation(string, Languages.ENGLISH, Languages.TURKISH) + "<-->");
        }

        return result;
    }

    public ResultSet execQuery(String query) {
        ResultSet result = null;

        try {
            statement = connection.createStatement();
            result = statement.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at the executeQuery function " + ex.getLocalizedMessage());
            Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
        }
        return result;
    }

    public boolean execAction(String query) {
        ResultSet result;

        try {
            statement = connection.createStatement();
            statement.execute(query);
            return true;
        } catch (SQLException ex) {
            System.out.println("Exception at the executeQuery function " + ex.getLocalizedMessage());
            Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
        }
    }

    public List getDictionaryWords() {
        PreparedStatement ps = null;
        String st = null;

        List<String> result = new ArrayList<>();

        try {
            String getEnWords = "SELECT * FROM  dictionary";
            ps = connection.prepareStatement(getEnWords);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                st = rs.getString("english_word");
                result.add(st);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void SaveTranslation(String request, String anwser) {
        PreparedStatement ps = null;

        String queery = "UPDATE dictionary SET turkish_word = ? WHERE english_word = ?";

        try {
            ps = connection.prepareStatement(queery);

            // set the preparedstatement parameters
//            if(anwser.isEmpty())
//                ps.setString(1, request);
//            else
            ps.setString(1, anwser);

            ps.setString(2, request);

            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
