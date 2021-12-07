package com.demo.resy;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

import static com.demo.resy.Main.*;

public class LoginController {


    //TODO: Fenster sperren & schließen.


    @FXML
    private Label forgot_password;

    @FXML
    private Button login_button;

    @FXML
    private PasswordField password;

    @FXML
    private TextField email;

    @FXML
    private TextField username;

    @FXML
    void login(MouseEvent event) throws SQLException {
        String email_string = email.getText().toString();
        String password_string = password.getText().toString();
        String username_string = username.getText().toString();


        if (neoDbObject.loginUser(username_string, email_string, password_string)[0] == true) {
            System.out.println("User found. Logged in.");
            activeUser.setUsername(username_string);
            activeUser.setEmail(email_string);
            activeUser.setPassword(password_string);
            Main.setLogStatus(true);

            Stage stage = (Stage) login_button.getScene().getWindow();
            stage.close();


        } else System.out.println("User not found. Login failed.");


    }


}
