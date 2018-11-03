/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.translate;

import java.util.ArrayList;
import java.util.List;
import nour.subtitle.editor.dictionary.Languages;
import nour.subtitle.editor.dictionary.Translator;
import nour.subtitle.editor.showSrt.Subtitle;

/**
 *
 * @author NourSoft
 */
public class onlinetranslation {

    public Boolean onlineGetTranslation(List<Subtitle> translations, Languages sl, Languages dl) {

        String hostname = "";
        int port = -1;

        String responce = null;
        List<Subtitle> lines = new ArrayList<>();
        Translator translate = new Translator();

        for (Subtitle translation : translations) {

            responce = translate.execute(translation.getText(), sl, dl);
            System.out.println(responce);
            translation.setTranslation(responce);
        }

        return true;
    }

    public String onlineGetSentenceTranslation(String sentence, Languages sl, Languages dl) {

        String hostname = "";
        int port = -1;

        String responce = null;
        Translator translate = new Translator();

        responce = translate.execute(sentence, sl, dl);
       // System.out.println(responce);

        return responce;
    }

}
