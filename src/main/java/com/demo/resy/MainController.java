package com.demo.resy;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.ResourceBundle;

import static com.demo.resy.Main.*;


public class MainController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button anmelden_button;

    @FXML
    private TextField eingabefeld;

    @FXML
    private Button registrieren_button;

    @FXML
    private Button suchen_button;

    @FXML
    private Button kontoeinstellungen;

    @FXML
    private ImageView imageViewAura;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView cascade_logo;

    @FXML
    private ImageView wp;


    @FXML
    void popup_login(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("file:C:\\Users\\Anwender\\IdeaProjects\\resy_demo\\src\\main\\resources\\com\\demo\\resy\\ca.png"));
            stage.setTitle("Cascade - Login");
            stage.setResizable(false);
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
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("file:C:\\Users\\Anwender\\IdeaProjects\\resy_demo\\src\\main\\resources\\com\\demo\\resy\\ca.png"));
            stage.setTitle("Cascade - Registrierung");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();


        } catch (Exception e) {
            System.out.println("Cant load new Window");
        }

    }

    @FXML
    void popup_konto(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("konto.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("file:C:\\Users\\Anwender\\IdeaProjects\\resy_demo\\src\\main\\resources\\com\\demo\\resy\\ca.png"));
            stage.setTitle("Cascade - Kontoeinstellungen");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            KontoController kontoController = fxmlLoader.getController();
            kontoController.setLogStatusText();
            kontoController.setEmail();
            kontoController.setUsername();
            kontoController.setPasswordField();
            stage.show();


        } catch (Exception e) {
            System.out.println("Cant load new Window");
        }

    }

    @FXML
    void popup_jobs(MouseEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("jobverwaltung.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("file:C:\\Users\\Anwender\\IdeaProjects\\resy_demo\\src\\main\\resources\\com\\demo\\resy\\ca.png"));
            stage.setTitle("Cascade - Jobverwaltung");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cant load new Window");
        }

    }

    @FXML
    void generateRecs(MouseEvent event) throws IOException {

        /**
         * Opens a new Scene.
         */
        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    @FXML
    void generateTops(MouseEvent event) throws IOException {
        /**
         * Opens a new Scene.
         */
        Parent root = FXMLLoader.load(getClass().getResource("anything.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        neoDbObject.readJobs();
        try {
            TextFields.bindAutoCompletion(eingabefeld, keywordGen.generateKeywordsForSearch(jobList));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}