package com.demo.resy;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    void login(MouseEvent event) {
        String email_string = email.toString();
        String password_string = password.toString();



    }

}
