package com.demo.resy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class JobCreationController implements Initializable {




    @FXML
    private Button create;

    @FXML
    private TextArea description;



    @FXML
    private TextField jobname;

    @FXML
    private TextField company;




    @FXML
    private TextField experience;

    @FXML
    private TextField jobtitle;

    @FXML
    private TextField location;

    @FXML
    private TextField salary;


    @FXML
    void createOffer(MouseEvent event) {

        //TODO: REGEX BEI EXPERIENCE&SALARY

        if(jobtitle.getText()!="" && company.getText()!="" && location.getText()!="" && experience.getText()!="" && salary.getText()!="" && description.getText()!=""){
            Job newJob = new Job(jobtitle.getText(), company.getText(), location.getText(), experience.getText(), salary.getText(), description.getText(), 0);
            Main.neoDbObject.createOffer(newJob);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Important information!");
            alert.setContentText("Done.");
            alert.showAndWait();
            System.out.println(newJob.getLikes());
        }
        else{Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Missing data.");
            alert.showAndWait();}


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<String> marshal(ObservableList<Skill> input){
        ObservableList<String> output = FXCollections.observableArrayList();
        for(Skill item : input){
            output.add(item.getSkillname());
        }
        return output;
    }


}
