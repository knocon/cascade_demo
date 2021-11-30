package com.demo.resy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1234, 671);
        stage.setTitle("Cascade - Wissensgraph-basierendes Empfehlungssystem");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) throws SQLException {
        Database cascade_db = new Database("jdbc:mysql://localhost:3306/cascade_db?", "root", "");

        User newUser = new User("test", "test", "test");
        Database.insert_User(newUser);
        System.out.println("Java-FX Launch pending...");
        launch();
        System.out.println("Java-FX Launch successful!");
    }
}