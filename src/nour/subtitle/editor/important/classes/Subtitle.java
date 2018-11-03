/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.important.classes;

/**
 *
 * @author isacv
 */
public class Subtitle {
    public int number;
    public long start; //start of the subtitles in milliseconds
    public long end; //end of the subtitles in milliseconds
    public String text;  
    
    @Override
    public String toString(){
        return "" + number + "\n" + formatTime(start) + " --> " + formatTime(end) + "\n" + text + "\n\n";
    }
    
    public static String formatTime(long t){
        int millis = (int) (t%1000);
        long seconds = t/1000;
        long minutes = seconds/60;
        int hours = (int)minutes/60;

        if (seconds >= 60){
            seconds = seconds % 60;
        }
  
        if (minutes >= 60){
            minutes = minutes % 60;
        }
   
        String textMillis = padLeftZeros(Integer.toString(millis),3);
        String textSeconds = padLeftZeros(Long.toString(seconds),2);
        String textMinutes = padLeftZeros(Long.toString(minutes),2);
        String textHours = padLeftZeros(Integer.toString(hours),2);
        
        return textHours + ":" + textMinutes + ":" + textSeconds + "," + textMillis;
    }
    
    public static String padLeftZeros(String inputStr, int places){
        while (inputStr.length() < places){
            inputStr = "0" + inputStr;
        }
        
        return inputStr;
    }
    
    /**
     * Updates a subtitle by displacing it foward(later) or backward(earlier).
     * Backward movements are indicated with negative signs on all the required parameters.
     * @param minutes Minutes to advance/delay the subtitle
     * @param seconds Seconds to advance/delay the subtitle
     * @param milliseconds Milliseconds to advance/delay the subtitle
     */
    public void updateTimes(int minutes, int seconds, int milliseconds){
        start += minutes * 60000;
        start += seconds * 1000;
        start += milliseconds;
        
        end += minutes * 60000;
        end += seconds * 1000;
        end += milliseconds;
    }
    
    public void updateTimes(TimeUpdateValues tuv){
        updateTimes(tuv.getMinutes(), tuv.getSeconds(), tuv.getMilliseconds());
    }
    
    /**
     * Updates the current subtitle forward(later) if the millis are positive or backward(earlier) 
     * if they are negative
     * @param milliseconds Milliseconds to advance/delay
     */
    public void updateTimes(long milliseconds){
        start += milliseconds;
        end += milliseconds;
    }
    
    /**
     * Parses a String time in the format hh:mm:ss,uuu --> hh:mm:ss,uuu
     * Where u represents the milliseconds of the subtitle.
     * The parsed result will be stored as milliseconds in the start time
     * @param inputTime Formatted String that represents the starting time
     */
    public void parseStartTime(String inputTime){
        start = parseTime(inputTime);
    }
    
    /**
     * Parses a String time in the format hh:mm:ss,uuu --> hh:mm:ss,uuu
     * Where u represents the milliseconds of the subtitle.
     * The parsed result will be stored as milliseconds in the end time
     * @param inputTime Fromatted String that represents the ending time
     */
    public void parseEndTime(String inputTime){
        end = parseTime(inputTime);
    }
    
    private long parseTime(String inputTime){
        String[] blocks = inputTime.split(":");
        
        long time = 0;
        
        time += Integer.parseInt(blocks[0]) * 3600000; //hours
        time += Integer.parseInt(blocks[1]) * 60000; //minutes
        
        String[] secondBlock = blocks[2].split(",");
        
        time += Integer.parseInt(secondBlock[0]) * 1000; //seconds
        time += Integer.parseInt(secondBlock[1]);
        
        return time;
    }
    
    /**
     * Adds a new line of text to this subtitle
     * @param newText The new line of text to be added to this subtitle
     */
    public void addText(String newText){
        if (text == null){
            text = newText;
        }
        else {
            text += "\n" + newText;    
        }
    }
}
