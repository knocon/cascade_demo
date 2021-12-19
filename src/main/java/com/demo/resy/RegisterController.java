package com.demo.resy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.neo4j.driver.Config;
import org.neo4j.driver.exceptions.ClientException;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.demo.resy.Main.neoDbObject;

public class RegisterController implements Initializable {

    @FXML
    private TextField country;

    @FXML
    private TextField email;

    @FXML
    private TextField firstname;

    @FXML
    private ChoiceBox<String> gender;

    @FXML
    private TextField lastname;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField password2;

    @FXML
    private TextField postcode;

    @FXML
    private Button register_button;

    @FXML
    private TextField town;

    @FXML
    private TextField username;


    private neoDB dbSession() {
        String uri = "neo4j+s://94c89272.databases.neo4j.io";
        String user = "neo4j";
        String password = "TwrZkJZ7UXqLmRYsytRGU0iJEVGo6o9OGhDqLPiiHOU";
        try (neoDB db = new neoDB(uri, user, password, Config.defaultConfig())) {
            System.out.print("session");
            return db;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    @FXML
    void register_User(MouseEvent event) throws SQLException {

        String string_username = username.getText().toString();
        String string_email = email.getText().toString();
        String string_password = password.getText().toString();
        String string_password2 = password2.getText().toString();
        String string_firstname = firstname.getText().toString();
        String string_lastname = lastname.getText().toString();
        String string_gender = gender.getSelectionModel().getSelectedItem();
        String string_country = country.getText().toString();
        String string_town = town.getText().toString();
        String string_postcode = postcode.getText().toString();


        if (string_username != "" && string_email != "" && string_password != "" && string_password2 != "" && string_firstname != "" && string_lastname != "" && string_gender != "" && string_country != "" && string_town != "" && string_postcode != "") {
            if (!string_password.equals(string_password2)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information!");
                alert.setHeaderText("Missing information!");
                alert.setContentText("Password missing.");
                alert.showAndWait();
            } else {

                User newUser = new User(string_username, string_email, string_password, string_firstname, string_lastname, string_gender, string_country, string_town, string_postcode);

                try {
                    neoDbObject.registerUser(newUser);
                    System.out.print("STATUS neoDB: new User created.");
                } catch (ClientException ce) {
                    System.out.print("STATUS neoDB: new User NOT created.");
                }
            }

        } else {
            System.out.println("Missing data");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Missing information!");
            alert.showAndWait();

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> genders = FXCollections.observableArrayList();
        genders.add("Male");
        genders.add("Female");
        genders.add("?");
        gender.setItems(genders);
    }
}