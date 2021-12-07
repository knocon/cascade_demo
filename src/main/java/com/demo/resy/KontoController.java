package com.demo.resy;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class KontoController {

    @FXML
    private TextField email;

    public void setEmail(){
        email.setText(Main.activeUser.getEmail());
    }

    @FXML
    private Text logStatusText;

    public void setLogStatusText(){
        logStatusText.setText("Sie sind eingeloggt als: \n"+Main.activeUser.getUsername() +"\n"+Main.activeUser.getEmail() +"\n");
    }

    @FXML
    private PasswordField password;

    public void setPasswordField(){
        password.setText("dummypwdummypw");
    }

    @FXML
    private Button save;

    @FXML
    private TableView<?> skills_table;

    @FXML
    private TextField username;

    public void setUsername(){
        username.setText(Main.activeUser.getUsername());
    }

}
