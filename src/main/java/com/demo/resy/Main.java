package com.demo.resy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.neo4j.driver.Config;
import io.github.crew102.rapidrake.RakeAlgorithm;
import io.github.crew102.rapidrake.data.SmartWords;
import io.github.crew102.rapidrake.model.RakeParams;
import io.github.crew102.rapidrake.model.Result;
import java.io.IOException;
import java.sql.SQLException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main extends Application {
    public static String db_uri = "neo4j+s://94c89272.databases.neo4j.io";
    public static String db_user = "neo4j";
    public static String db_pw = "TwrZkJZ7UXqLmRYsytRGU0iJEVGo6o9OGhDqLPiiHOU";
    public static neoDB neoDbObject = new neoDB(db_uri, db_user, db_pw, Config.defaultConfig());
    public static User activeUser = new User();
    public static KeywordGen keywordGen = new KeywordGen();
    public static ObservableList<Skill> skillsList = FXCollections.observableArrayList();
    public static ObservableList<Skill> userSkillsList = FXCollections.observableArrayList();
    public static ObservableList<Job> jobList = FXCollections.observableArrayList();
    public static ObservableList<String> skillCategorys = FXCollections.observableArrayList();
    public static ArrayList<String> keywords = new ArrayList<>();


    public static boolean isLogStatus() {
        return logStatus;
    }

    public static void setLogStatus(boolean logStatus) {
        Main.logStatus = logStatus;
    }

    public static boolean logStatus = false;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1234, 671);
        stage.setTitle("Cascade - Wissensgraph-basierendes Empfehlungssystem");
        stage.setScene(scene);
        stage.getIcons().add(new Image("file:C:\\Users\\Anwender\\IdeaProjects\\resy_demo\\src\\main\\resources\\com\\demo\\resy\\ca.png"));
        stage.setResizable(false);
        stage.show();


    }


    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Java-FX Launch pending...");
        launch();
        System.out.println("Java-FX exit.");
    }

}