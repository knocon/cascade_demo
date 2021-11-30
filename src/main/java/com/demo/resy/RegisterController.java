package com.demo.resy;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class RegisterController {

    //TODO: Label, der entsprechende Fehler wirft, ob Username, Email oder Passwort fehlerhaft ist.


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
        }
        if (string_email == "") {
            System.out.println("Fehlende E-Mail");
        }
        if (string_password == "") {
            System.out.println("Fehlendes Passwort");
        }

        if (string_username != "" && string_email != "" && string_password != "") {
            User newUser = new User(string_username, string_email, string_password);
                if(Database.checkDuplicateonEmail(newUser)==false){
                    Database.insert_User(newUser);
                    System.out.print("Duplicates not found. Import done.");
                }
                else System.out.print("Duplicates found. Import not possible.");


        }


    }

}