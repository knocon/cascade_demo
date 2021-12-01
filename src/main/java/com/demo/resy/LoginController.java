package com.demo.resy;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class LoginController {


    //TODO: Fenster sperren & schließen.
    //TODO: Feedback-Elemente.

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

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information!");
            alert.setHeaderText("Login successfull!");
            alert.setContentText("You are now logged in as: "+Database.getUser(email_string,password_string).getUsername());
            alert.showAndWait();


        } catch (NullPointerException e) {
            System.out.println("Login-Failed.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Login failed!");
            alert.setContentText("Wrong login data.");
            alert.showAndWait();
        }

    }

}
