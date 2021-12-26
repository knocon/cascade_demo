package com.demo.resy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class JobDetailedController implements Initializable {




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




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JobHolder holder = JobHolder.getInstance();
        Job j = holder.getJob();
        try {
            jobtitle.setText(j.getJobtitle());
            experience.setText(j.getExperience());
            this.location.setText(j.getLocation());
            salary.setText(j.getSalary());
            company.setText(j.getCompany());
            description.setText(j.getJobdescription());
            description.setWrapText(true);
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
