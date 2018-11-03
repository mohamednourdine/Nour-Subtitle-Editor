/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.srt.utils;
/**
 *
 * @author NourSoft
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class translationapi {
    
    	public static void main(String[] args){
		
		// Get list of .srt files
		List<String> file_list = new ArrayList<>();
		try (Stream<Path> stream = Files.list(Paths.get(""))) {
			file_list = stream
		        .map(String::valueOf)
		        .filter(path -> path.endsWith(".srt"))
		        .collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Get each row - phrase in a file
		List<String> phrases = new ArrayList<>();
		for(String file : file_list)
			try (Stream<String> stream = Files.lines(Paths.get(file))){
				phrases = stream
						.filter(line -> line.matches("\\D*") && !line.isEmpty())
						.map(line -> line.replaceAll("[^A-Za-z']", " "))
						.collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			};
		
		// Form words Set from all files in the directory
		HashSet<String> words = new HashSet<String>();
		for (String phrase : phrases){
			String[] phrase_arr = phrase.split(" ");
			for (String word : phrase_arr)
				words.add(word.replace("''", "").toLowerCase());
		}
		words.remove("");
		
		// Form dictionary map
		TreeMap<String, String> dict = new TreeMap<>();
		words.forEach(word -> {
			try {
				dict.put(word, YandexTranslate.translate("ru", word));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		/*
		phrases.forEach(phrase -> {
			try {
				dict.put(phrase, YandexTranslate.translate("ru", phrase));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		*/
		
		// Write result to file
		Path path = Paths.get("result.txt");
		try {
			Files.write(path, () -> dict.entrySet().stream()
				    .<CharSequence>map(e -> e.getKey() + "	" + e.getValue())
				    .iterator());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
    
    
    
}
