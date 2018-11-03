/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.important.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author isacv
 */
public class SubtitlesManager {
    private List<Subtitle> subtitles;
    
    private String filePath;
    private String encoding;
    
    public SubtitlesManager(){
        subtitles = new ArrayList<Subtitle>();
    }
    

    public void setFilePath(String newFilePath){
        filePath = newFilePath;
    }
    
    public String getFilePath(){
        return filePath;
    }
    
    /**
     * Sets the current subtitle manager encoding. This encoding will only be used on the next file action (load/save).
     * More often than not it is advisable to load from file again so that the load is already done with the correct encoding.
     * @param newEncoding New Encoding to be used as a String
     */
    public void setEncoding(String newEncoding){
        encoding = newEncoding;
    }
    
    public void writeToFile(){
        if (filePath != null){
            try {
                //PrintWriter pw = new PrintWriter(filePath); //writer to file without considering the encoding
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), encoding));
                
                for (Subtitle s : subtitles){
                    pw.print(s.toString());
                }
                
                pw.flush();
                pw.close();
                
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                         Logger.getLogger(SubtitlesManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else {
        }
    }
    

    
    /**
     * A hard notification to all registered notifiees
     * @param message the notification message
     */
//    private void severeNotifyAction(String message){
//        for (Notifier n: notifiers){
//            n.severeNotifyAction(message);
//        }
//    }
    
    private ReadingType updateCurrentRead(ReadingType rt){
        switch(rt){
            case number: return ReadingType.time;
            case time: return ReadingType.text;
            case text: return ReadingType.emptyline;
            case emptyline: return ReadingType.number;
        }
        
        return null;
    }
    
    public void readFromFile(){
        ReadingType currRead = ReadingType.number;
        
        BufferedReader br = null;
        
        try {
            //br = new BufferedReader(new FileReader(new File(filePath))); //load without encoding
            
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)),encoding));

          

            String textLine = br.readLine();

            subtitles.clear();
            
            Subtitle currentSubtitle = null;

            while (textLine != null){
                switch(currRead){
                    case number:
                        if (currentSubtitle!= null){
                            subtitles.add(currentSubtitle);
                        }

                        currentSubtitle = new Subtitle();
                        currentSubtitle.number = Integer.parseInt(textLine);
                        currRead = updateCurrentRead(currRead);
                        break;
                    case time:
                        String[] blocks = textLine.split(" --> ");

                        currentSubtitle.parseStartTime(blocks[0]);
                        currentSubtitle.parseEndTime(blocks[1]);

                        currRead = updateCurrentRead(currRead);                            
                        break;
                    case text:
                        if (textLine.equals("")){
                            currRead = ReadingType.number;
                        }
                        else {
                            currentSubtitle.addText(textLine);
                        }
                        break;
                }

                textLine = br.readLine(); 
            }
            subtitles.add(currentSubtitle);

           
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                if (br != null){
                    br.close();    
                }                
            } catch (IOException ex) {
                Logger.getLogger(SubtitlesManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Updates all the subtitles according to the given parameters.
     * Positive values move all the subtitles forward(later) whereas negative values move them backwards(earlier)
     * @param minutes minutes to advance/delay
     * @param seconds seconds to advance/delay
     * @param milliseconds milliseconds to advance/delay
     */

    /**
     * Updates the subtitles by moving the forward(later) or backwards (earlier) for a given range of subtitles.
     * Positive values move all the subtitles forward(later) whereas negative values move them backwards(earlier).
     * @param minutes minutes to advance/delay
     * @param seconds seconds to advance/delay
     * @param milliseconds milliseconds to advance/delay
     * @param min subtitle where the update starts (inclusive)
     * @param max subtitle where the update ends (inclusive)
     */

    
    /**
     * Updates the subs based on the target subtitle. It rearranges the target subtitle to a specific time
     * and readjusts all previous times proportionaly.
     * Positive values move the subtitle forward(later) whereas negative values move it backwards(earlier).
     * @param minutes minutes to advance/delay
     * @param seconds seconds to advance/delay
     * @param milliseconds milliseconds to advance/delay
     * @param targetSub target subtitle to adjust from towards the first subtitle
     */
//    public void updateSubsProprortionally(int minutes, int seconds, int milliseconds, int targetSub){
//        int totalChange = (minutes * 60000) + (seconds * 1000) + milliseconds;
//        
//        Integer subIndex = getSubtitleIndex(targetSub);
//        
//        if (subIndex == null){ //unexisting sub, unable to update
//            severeNotifyAction("The subtitle selected for the update doesn't exist");
//            return;
//        }
//        
//        long perSubChangeBackward = totalChange / subIndex; //sub index is the amount of elements - 1, which is what is neccessary for distributed update
//        
//        
//        int change = totalChange;
//        
//        //makes the proportional adjustment for all the subtitles up to the selected subtitle
//        for (int i = subIndex - 1; i >= 1; --i){ //update all subs backwards with the proportional change
//            
//            subtitles.get(i).updateTimes(change);
//            change -= perSubChangeBackward;
//        }
//        
//        //updates all the remaining forward subtitles so that they keep the same distance among themselves maintaining
//        //the same subtitle sync
//        for (int i = subIndex ; i < subtitles.size(); ++i){
//            subtitles.get(i).updateTimes(totalChange);
//        }
//        
//        String notifyStr = "Subtitle " + targetSub;
//        
//        if (minutes > 0 || seconds > 0 || milliseconds > 0){
//            notifyStr += " delayed for ";
//        }
//        else {
//            notifyStr += " advanced for ";
//        }
//        
//        if (minutes != 0){
//            notifyStr += " " + Misc.plurify(Math.abs(minutes), "minute") + " ";
//        }
//        
//        if (seconds != 0){
//            notifyStr += " " + Misc.plurify(Math.abs(seconds), "second") + " ";
//        }
//        
//        if (milliseconds != 0 ){
//            notifyStr += " " + Misc.plurify(Math.abs(milliseconds), "millisecond") + " ";
//        }
//        
//        notifyAction(notifyStr + " and remaining subtitles adjusted proportionally");
//    }
    
    private Subtitle getSubtitle(int subNumber){
        for (Subtitle sub: subtitles){
            if (sub.number == subNumber){
                return sub;
            }
        }
        
        return null;
    }
    
    private Integer getSubtitleIndex(int subNumber){
        for (int i = 0; i < subtitles.size(); ++i){
            Subtitle sub = subtitles.get(i);
            
            if (sub.number == subNumber){
                return i;
            }
        }
        
        return null;
    }
    
    /**
     * Gets the text that represents all the subs and should match a perfect subtitle file
     * @return The String representation of all subs in order
     */
    public String subsToText(){
        StringBuilder sb = new StringBuilder(5000);
        
        for (Subtitle sub : subtitles){
            sb.append(sub.toString());
        }
        
        return sb.toString();
    }
    
    public int getFirstSubNumber(){
        if (subtitles != null){
            return subtitles.get(0).number;
        }
        
        return 0;
    }
    
    public int getLastSubNumber(){
        if (subtitles != null || subtitles.size() >= 1){
            return subtitles.get(subtitles.size() - 1).number;
        }
        
        return 0;
    }
}
