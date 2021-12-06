package com.demo.resy;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.neo4j.driver.Config;
import org.neo4j.driver.exceptions.ClientException;

import java.sql.SQLException;

import static com.demo.resy.Main.neoDbObject;

public class RegisterController {


    //TODO: Regex
    //TODO: Passwort hash
    //TODO: Fenster sperren & schließen.

    private neoDB dbSession(){
        String uri = "neo4j+s://94c89272.databases.neo4j.io";
        String user = "neo4j";
        String password = "TwrZkJZ7UXqLmRYsytRGU0iJEVGo6o9OGhDqLPiiHOU";
        try (neoDB db = new neoDB(uri, user, password, Config.defaultConfig())){
            System.out.print("session");
            return db;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button register_button;

    @FXML
    private TextField username;

    @FXML
    void register_User(MouseEvent event) throws SQLException {

        String string_username = username.getText().toString();
        String string_email = email.getText().toString();
        String string_password = password.getText().toString();

        if (string_username == "") {
            System.out.println("Fehlender Username.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Username missing.");
            alert.showAndWait();
        }
        if (string_email == "") {
            System.out.println("Fehlende E-Mail");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Email missing.");
            alert.showAndWait();
        }
        if (string_password == "") {
            System.out.println("Fehlendes Passwort");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Password missing.");
            alert.showAndWait();
        }

        if (string_username != "" && string_email != "" && string_password != "") {
            User newUser = new User(string_username, string_email, string_password);

            try{
                neoDbObject.registerUser(newUser);
                System.out.print("STATUS neoDB: new User created.");
            }catch(ClientException ce){
                System.out.print("STATUS neoDB: new User NOT created.");
            }



    }

}}