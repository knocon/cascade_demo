package com.demo.resy;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.demo.resy.Main.neoDbObject;

public class KontoController implements Initializable {

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
    private TableView<Skill> skills_table;

    @FXML
    private TableColumn<Skill, String> skills_column;

    @FXML
    private TableView<?> skills_table2;

    @FXML
    private TextField username;

    public void setUsername(){
        username.setText(Main.activeUser.getUsername());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        neoDbObject.fillTable2();
        System.out.println("OMEGALUL");
        //skills_column.setCellValueFactory(new PropertyValueFactory<Skill, String>("skillname"));
        //skills_table.getItems().setAll(parseSkillList());
    }

    @FXML
    private Button parseButton;
}
