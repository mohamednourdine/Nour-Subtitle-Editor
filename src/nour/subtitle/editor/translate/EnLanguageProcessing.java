/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.translate;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author NourSoft
 */
public class EnLanguageProcessing {

//    public static String text = "Joe Smith was born in California.";

    public static void main(String[] args) throws IOException {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        /**
         * tokenize / TokenizerAnnotator - Tokenizes the text. This splits the
         * text into roughly “words”, using rules or methods suitable for the
         * language being processed.
         *
         * ssplit / WordsToSentencesAnnotator - Splits a sequence of tokens into
         * sentences.
         *
         * pos / POSTaggerAnnotator - Labels tokens with their POS tag.
         *
         * lemma / MorphaAnnotator - Generates the word lemmas for all tokens in
         * the corpus.
         *
         * ner / NERClassifierCombiner - Recognizes named (PERSON, LOCATION,
         * ORGANIZATION, MISC), numerical (MONEY, NUMBER, ORDINAL, PERCENT), and
         * temporal (DATE, TIME, DURATION, SET) entities.
         *
         * parse / ParserAnnotator - Provides full syntactic analysis, using
         * both the constituent and the dependency representations. The
         * constituent-based output is saved in TreeAnnotation.
         *
         * dcoref / DeterministicCorefAnnotator - Implements both pronominal and
         * nominal coreference resolution.
         */
   
        // read some text from the file..
        //File inputFile = new File("src/test/resources/sample-content.txt");
     
        String text = "Mohamed is an engineer and he was born in Foumban precisely in Cameroon!";

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                
                System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
            }

            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            System.out.println("parse tree:\n" + tree);

            // this is the Stanford dependency graph of the current sentence
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            System.out.println("dependency graph:\n" + dependencies);
        }

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
//        Map<Integer, CorefChain> graph
//                = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);

    }
}
