package com.demo.resy;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class LoginController {

    @FXML
    private Label forgot_password;

    @FXML
    private Button login_button;

    @FXML
    private PasswordField password;

    @FXML
    private TextField email;

    @FXML
    void login(MouseEvent event) throws SQLException {
        String email_string = email.getText().toString();
        String password_string = password.getText().toString();
        try {
            Database.login(Database.getUser(email_string,password_string));
            System.out.println("Login successfull.");
        } catch (NullPointerException e) {
            System.out.println("Login-Failed.");
        }

    }

}
