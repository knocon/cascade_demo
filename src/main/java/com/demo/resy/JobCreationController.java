package com.demo.resy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class JobCreationController implements Initializable {
    //TODO: Categorys fixen. Sobald Jobdatenbestand 0 => EXCEPTION

    @FXML
    private TextField companyname;

    @FXML
    private Button create;

    @FXML
    private TextArea description;

    @FXML
    private TextField duration;

    @FXML
    private TextField email;


    @FXML
    private TextField jobname;

    @FXML
    private ListView<String> skillsView;

    @FXML
    private ListView<String> categorysView;

    @FXML
    private TextField name;

    @FXML
    private TextField plzort;

    @FXML
    private TextField streetnr;

    @FXML
    private TextField telnr;

    @FXML
    private TextField vorname;


    @FXML
    void createOffer(MouseEvent event) {

        final String jn = jobname.getText();
        final String dur = duration.getText();
        final String area = description.getText();
        final ObservableList<String> skills_selected =  skillsView.getSelectionModel().getSelectedItems();
        final ObservableList<String> categorys_selected = categorysView.getSelectionModel().getSelectedItems();
        final String un = companyname.getText();
        final String na = name.getText();
        final String vn = vorname.getText();
        final String em = email.getText();
        final String tel = telnr.getText();
        final String str = streetnr.getText();
        final String plz = plzort.getText();

        if(jn!="" && dur!="" && area!="" && un!="" && na!="" && vn!="" && em!="" && tel!="" && str!="" && plz!="" &&skills_selected.size()>0 && categorys_selected.size()>0 ){
            Job newJob = new Job(jn, dur, area, skills_selected, categorys_selected, un, na, vn, em , tel , str, plz);
            Main.neoDbObject.createOffer(newJob);
            Main.neoDbObject.createOfferRelationship(newJob);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Important information!");
            alert.setContentText("Done.");
            alert.showAndWait();
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
            Main.neoDbObject.readSkills();
            skillsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            skillsView.setItems(marshal(Main.skillsList));
            Main.neoDbObject.returnCategories();
            categorysView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            categorysView.setItems(Main.skillCategorys);
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
