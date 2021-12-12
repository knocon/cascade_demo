package com.demo.resy;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.demo.resy.Main.neoDbObject;
import static com.demo.resy.Main.skillsList;

public class KontoController implements Initializable {

    @FXML
    private TextField email;
    @FXML
    private Text logStatusText;
    @FXML
    private PasswordField password;
    @FXML
    private Button save;
    @FXML
    private TableView<Skill> skills_table;
    @FXML
    private TableColumn<Skill, String> skills_column;
    @FXML
    private TableColumn<Skill, String> category_column;
    @FXML
    private TableColumn<Skill, String> description_column;
    @FXML
    private TableView<?> skills_table2;
    @FXML
    private TextField username;
    @FXML
    private TextField filter;
    @FXML
    private Button parseButton;

    public void setEmail() {
        email.setText(Main.activeUser.getEmail());
    }

    public void setLogStatusText() {
        logStatusText.setText("Sie sind eingeloggt als: \n" + Main.activeUser.getUsername() + "\n" + Main.activeUser.getEmail() + "\n");
    }

    public void setPasswordField() {
        password.setText("dummypwdummypw");
    }

    public void setUsername() {
        username.setText(Main.activeUser.getUsername());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        neoDbObject.readSkills();
        skills_column.setCellValueFactory(new PropertyValueFactory<Skill, String>("skillname"));
        category_column.setCellValueFactory(new PropertyValueFactory<Skill, String>("category"));
        description_column.setCellValueFactory(new PropertyValueFactory<Skill, String>("description"));
        skills_table.getItems().setAll(skillsList);

        FilteredList<Skill> filteredData = new FilteredList<>(skillsList, p -> true);
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(skill -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }


                String lowerCaseFilter = newValue.toLowerCase();

                if (skill.getSkillname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (skill.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Skill> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(skills_table.comparatorProperty());
        skills_table.setItems(sortedData);
    }
}
