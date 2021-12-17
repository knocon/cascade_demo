package com.demo.resy;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.demo.resy.Main.*;

public class JobController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Job,String> category_column;

    @FXML
    private TableColumn<Job,String> company_column;

    @FXML
    private Button delButton;

    @FXML
    private TableColumn<Job,String> description_column;

    @FXML
    private TableColumn<Job,String> duration_column;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<Job,String> email_column;

    @FXML
    private TextField filter;

    @FXML
    private TableColumn<Job,String> jobid_column;

    @FXML
    private TableColumn<Job,String> jobname_column;

    @FXML
    private TableView<Job> jobs_table;

    @FXML
    private TableColumn<Job,String> nachname_column;

    @FXML
    private TableColumn<Job,String> plzort_column;

    @FXML
    private TableColumn<Job,String> skillset_column;

    @FXML
    private TableColumn<Job,String> street_column;

    @FXML
    private TableColumn<Job,String> telnr_column;

    @FXML
    private TableColumn<Job,String> vorname_column;

    @FXML
    private Button refresh;

    @FXML
    void addJob(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("jobcreation.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("file:C:\\Users\\Anwender\\IdeaProjects\\resy_demo\\src\\main\\resources\\com\\demo\\resy\\ca.png"));
            stage.setTitle("Cascade - Job hinzufügen");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cant load new Window");
        }

    }

    @FXML
    void delJob(MouseEvent event) {


        try{

            neoDbObject.deleteOffer(jobs_table.getSelectionModel().getSelectedItem());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Important information!");
            alert.setContentText("Joboffer deleted.");
            alert.showAndWait();
            refresh();
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Tupel nicht ausgewählt!");
            alert.showAndWait();
        }

    }

    @FXML
    void editJob(MouseEvent event) {
        //TODO: Bearbeiten ermöglichen.
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        neoDbObject.readJobs();
        jobid_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobid"));
        jobname_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobname"));
        description_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobdescription"));
        duration_column.setCellValueFactory(new PropertyValueFactory<Job, String>("durationOfactivity"));
        street_column.setCellValueFactory(new PropertyValueFactory<Job, String>("strnr"));
        plzort_column.setCellValueFactory(new PropertyValueFactory<Job, String>("plzort"));
        telnr_column.setCellValueFactory(new PropertyValueFactory<Job, String>("telnr"));
        nachname_column.setCellValueFactory(new PropertyValueFactory<Job, String>("name"));
        vorname_column.setCellValueFactory(new PropertyValueFactory<Job, String>("vorname"));
        company_column.setCellValueFactory(new PropertyValueFactory<Job, String>("companyname"));
        skillset_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobskills"));
        category_column.setCellValueFactory(new PropertyValueFactory<Job, String>("categorys"));
        email_column.setCellValueFactory(new PropertyValueFactory<Job, String>("email"));
        jobs_table.getItems().setAll(jobList);

        FilteredList<Job> filteredData = new FilteredList<>(jobList, p -> true);
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(job -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }


                String lowerCaseFilter = newValue.toLowerCase();

                if (job.getJobname().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (job.getJobdescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Job> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(jobs_table.comparatorProperty());
        jobs_table.setItems(sortedData);



    }
    //TODO: Filter
    public void refresh(){
        neoDbObject.readJobs();
        jobid_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobid"));
        jobname_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobname"));
        description_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobdescription"));
        duration_column.setCellValueFactory(new PropertyValueFactory<Job, String>("durationOfactivity"));
        street_column.setCellValueFactory(new PropertyValueFactory<Job, String>("strnr"));
        plzort_column.setCellValueFactory(new PropertyValueFactory<Job, String>("plzort"));
        telnr_column.setCellValueFactory(new PropertyValueFactory<Job, String>("telnr"));
        nachname_column.setCellValueFactory(new PropertyValueFactory<Job, String>("name"));
        vorname_column.setCellValueFactory(new PropertyValueFactory<Job, String>("vorname"));
        company_column.setCellValueFactory(new PropertyValueFactory<Job, String>("companyname"));
        skillset_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobskills"));
        category_column.setCellValueFactory(new PropertyValueFactory<Job, String>("categorys"));
        email_column.setCellValueFactory(new PropertyValueFactory<Job, String>("email"));
        jobs_table.getItems().setAll(jobList);

    }

    @FXML
    void refresh(MouseEvent event) {
        refresh();
    }
}
