package com.demo.resy;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class MainController {

    @FXML
    private Button anmelden_button;

    @FXML
    private TextField eingabefeld;

    @FXML
    private Button registrieren_button;

    @FXML
    private Button suchen_button;

    @FXML
    void popup_login(MouseEvent event) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Cascade - Login");
            stage.setScene(new Scene(root1));
            stage.show();


        }
        catch (Exception e){
            System.out.println("Cant load new Window");
        }

    }

    @FXML
    void popup_register(MouseEvent event) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Cascade - Registrierung");
            stage.setScene(new Scene(root1));
            stage.show();


        }
        catch (Exception e){
            System.out.println("Cant load new Window");
        }

    }

}