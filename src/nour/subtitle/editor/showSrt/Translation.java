/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.showSrt;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author NourSoft
 */
public class Translation {
    private SimpleIntegerProperty nr;
    private SimpleStringProperty  text;
    private SimpleStringProperty  start;
    private SimpleStringProperty  stop;
    private SimpleStringProperty  translation;

    public Translation(int nr, String start, String stop, String text, String trans) {
        this.nr = new SimpleIntegerProperty(nr);
        this.start = new SimpleStringProperty(start);
        this.stop = new SimpleStringProperty(stop);
        this.text = new SimpleStringProperty(text);
        this.translation = new SimpleStringProperty(trans);
    }

  

    public int getNr() {
        return nr.get();
    }

    public void setNr(int number) {
        nr.set(number); 
    }

    public String  getText() {
        return text.get();
    }

    public void setText(String texts) {
         text.set(texts); 
    }

    public String  getStart() {
        return start.get();
    }

    public void setStart(String st) {     
        start.set(st);
    }

    public String  getStop() {
        return stop.get();
    }

    public void setStop(String sp) {
        stop.set(sp);
    }
    
    public String getTranslation() {
        return translation.get();
    }

    public void setTranslation(String trans) {
        translation.set(trans);
    }
}
