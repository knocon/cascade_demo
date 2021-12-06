package com.demo.resy;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class MainController {

    @FXML
    private Button anmelden_button;

    @FXML
    private TextField eingabefeld;

    @FXML
    private Button registrieren_button;

    @FXML
    private Button suchen_button;


    Image image = new Image("C:\\Users\\Anwender\\IdeaProjects\\resy_demo\\src\\main\\resources\\com\\demo\\resy\\logo_uni_siegen.png");

    @FXML
    private ImageView imageView = new ImageView();



    @FXML
    void popup_login(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Cascade - Login");
            stage.setScene(new Scene(root1));
            stage.show();


        } catch (Exception e) {
            System.out.println("Cant load new Window");
        }

    }

    @FXML
    void popup_register(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Cascade - Registrierung");
            stage.setScene(new Scene(root1));
            stage.show();


        } catch (Exception e) {
            System.out.println("Cant load new Window");
        }

    }

    @FXML
    void popup_konto(MouseEvent event) {

    }


}