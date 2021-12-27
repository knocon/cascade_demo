package com.demo.resy;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static com.demo.resy.Main.*;

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
    private TableView<Skill> skillsuser_table;
    @FXML
    private TableColumn<Skill, String> skills_column2;
    @FXML
    private TableColumn<Skill, String> category_column2;
    @FXML
    private TableColumn<Skill, String> description_column2;
    @FXML
    private TextField username;
    @FXML
    private TextField filter;
    @FXML
    private TextField filter2;
    @FXML
    private Button parseButton;

    @FXML
    private Button addButton;

    @FXML
    private Button delButton;

    @FXML
    private Button fetchData;

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

                }
                return false;
            });
        });

        SortedList<Skill> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(skills_table.comparatorProperty());
        skills_table.setItems(sortedData);

        /**
         * First table above
         * Second table below
         */
        neoDbObject.readUserSkills();
        skills_column2.setCellValueFactory(new PropertyValueFactory<Skill, String>("skillname"));
        description_column2.setCellValueFactory(new PropertyValueFactory<Skill, String>("description"));
        skillsuser_table.getItems().setAll(userSkillsList);

        FilteredList<Skill> filteredData2 = new FilteredList<>(userSkillsList, p -> true);
        filter2.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData2.setPredicate(skill -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }


                String lowerCaseFilter = newValue.toLowerCase();

                if (skill.getSkillname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Skill> sortedData2 = new SortedList<>(filteredData2);
        sortedData2.comparatorProperty().bind(skillsuser_table.comparatorProperty());
        skillsuser_table.setItems(sortedData2);


    }

    @FXML
    void addSkill(MouseEvent event) {

        try {
            Skill skill = skills_table.getSelectionModel().getSelectedItem();
            System.out.println(skill.getSkillname());
            neoDbObject.createSkillRelationship(activeUser, skill.getSkillname());
            refresh(event);
        } catch (Exception npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Please select a row!");
            alert.showAndWait();
        }

    }

    @FXML
    void delSkill(MouseEvent event) {
        try {
            Skill skill = skillsuser_table.getSelectionModel().getSelectedItem();
            System.out.println(skill.getSkillname());
            neoDbObject.deleteSkillUserRelationships(activeUser, skill.getSkillname());
            refresh(event);
        } catch (Exception npe) {
            npe.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Please select a row!");
            alert.showAndWait();
        }
    }

    @FXML
    void refresh(MouseEvent event) {
        neoDbObject.readSkills();
        skills_column.setCellValueFactory(new PropertyValueFactory<Skill, String>("skillname"));
        description_column.setCellValueFactory(new PropertyValueFactory<Skill, String>("description"));
        skills_table.setItems(skillsList);
        /**
         * First table above
         * Second table below
         */
        neoDbObject.readUserSkills();
        skills_column2.setCellValueFactory(new PropertyValueFactory<Skill, String>("skillname"));
        description_column2.setCellValueFactory(new PropertyValueFactory<Skill, String>("description"));
        skillsuser_table.setItems(userSkillsList);


    }

}
