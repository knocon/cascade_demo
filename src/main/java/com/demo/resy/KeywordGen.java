package com.demo.resy;

import io.github.crew102.rapidrake.RakeAlgorithm;
import io.github.crew102.rapidrake.data.SmartWords;
import io.github.crew102.rapidrake.model.RakeParams;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;

public class KeywordGen {

    public KeywordGen() {
    }

    public String generateKeywordsJob(Job j) throws IOException {
        String output="";
        String[] stopWords = new SmartWords().getSmartWords();
        String[] stopPOS = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
        int minWordChar = 1;
        boolean shouldStem = true;
        String phraseDelims = "[-,.?():;\"!/]";
        RakeParams params = new RakeParams(stopWords, stopPOS, minWordChar, shouldStem, phraseDelims);

        // Create a RakeAlgorithm object
        String POStaggerURL = "src/main/resources/com/demo/resy/en-pos-maxent.bin"; // The path to your POS tagging model
        String SentDetecURL = "src/main/resources/com/demo/resy/en-sent.bin"; // The path to your sentence detection model
        RakeAlgorithm rakeAlg = new RakeAlgorithm(params, POStaggerURL, SentDetecURL);

        // Call the rake method

        String company = j.getCompany();
        String jobdescription = j.getJobdescription();
        String jobtitle = j.getJobtitle();
        String location = j.getLocation();


        String txt = company+", "+jobdescription+", "+jobtitle+", "+location;
        io.github.crew102.rapidrake.model.Result result = rakeAlg.rake(txt);



        for(int i=0;i<result.distinct().getFullKeywords().length;i++){
            if(i==result.distinct().getFullKeywords().length-1) output+=result.distinct().getFullKeywords()[i];
            else output+=result.distinct().getFullKeywords()[i]+",";

        }
        return output;

    }

    public String generateKeywordsString(String string) throws IOException {
        String output="";
        String[] stopWords = new SmartWords().getSmartWords();
        String[] stopPOS = {"VB", "VBD", "VBG", "VBN", "VBP", "VBZ"};
        int minWordChar = 1;
        boolean shouldStem = true;
        String phraseDelims = "[-,.?():;\"!/]";
        RakeParams params = new RakeParams(stopWords, stopPOS, minWordChar, shouldStem, phraseDelims);

        // Create a RakeAlgorithm object
        String POStaggerURL = "src/main/resources/com/demo/resy/en-pos-maxent.bin"; // The path to your POS tagging model
        String SentDetecURL = "src/main/resources/com/demo/resy/en-sent.bin"; // The path to your sentence detection model
        RakeAlgorithm rakeAlg = new RakeAlgorithm(params, POStaggerURL, SentDetecURL);


        io.github.crew102.rapidrake.model.Result result = rakeAlg.rake(string);



        for(int i=0;i<result.distinct().getFullKeywords().length;i++){
            if(i==result.distinct().getFullKeywords().length-1) output+=result.distinct().getFullKeywords()[i];
            else output+=result.distinct().getFullKeywords()[i]+",";

        }
        return output;

    }




}
