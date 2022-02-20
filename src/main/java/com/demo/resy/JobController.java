package com.demo.resy;

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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.demo.resy.Main.*;

public class JobController implements Initializable {

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Job, String> jobtitle;

    @FXML
    private TableColumn<Job, String> company;

    @FXML
    private Button delButton;

    @FXML
    private TableColumn<Job, String> location;

    @FXML
    private TableColumn<Job, String> experience;


    @FXML
    private TableColumn<Job, String> salary;

    @FXML
    private TextField filter;

    @FXML
    private TableColumn<Job, String> jobid_column;

    @FXML
    private TableColumn<Job, String> jobdescription;

    @FXML
    private TableView<Job> jobs_table;

    @FXML
    private TableColumn<Job, Integer> likes;


    @FXML
    void addJob(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("jobcreation.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("file:src/main/resources/com/demo/resy/ca.png"));
            stage.setTitle("RESY - Add Job");
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


        try {

            neoDbObject.deleteOffer(jobs_table.getSelectionModel().getSelectedItem());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Important information!");
            alert.setContentText("Joboffer deleted.");
            alert.showAndWait();
            refresh(event);
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Tupel nicht ausgewählt!");
            alert.showAndWait();
        }

    }

    @FXML
    void genKey(MouseEvent event) throws IOException {
        neoDbObject.readJobs();
        neoDbObject.updateAllOffers(jobList);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        neoDbObject.readJobs();
        jobid_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobid"));
        jobtitle.setCellValueFactory(new PropertyValueFactory<Job, String>("jobtitle"));
        company.setCellValueFactory(new PropertyValueFactory<Job, String>("company"));
        this.location.setCellValueFactory(new PropertyValueFactory<Job, String>("location"));
        experience.setCellValueFactory(new PropertyValueFactory<Job, String>("experience"));
        jobdescription.setCellValueFactory(new PropertyValueFactory<Job, String>("jobdescription"));
        salary.setCellValueFactory(new PropertyValueFactory<Job, String>("salary"));
        likes.setCellValueFactory(new PropertyValueFactory<Job, Integer>("likes"));
        jobs_table.getItems().setAll(jobList);
        jobs_table.setFixedCellSize(25);
        likes.setSortType(TableColumn.SortType.DESCENDING);
        FilteredList<Job> filteredData = new FilteredList<>(jobList, p -> true);
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(job -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }


                String lowerCaseFilter = newValue.toLowerCase();

                if (job.getJobtitle().toLowerCase().contains(lowerCaseFilter)) {
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

    @FXML
    void refresh(MouseEvent event) {
        neoDbObject.readJobs();
        jobid_column.setCellValueFactory(new PropertyValueFactory<Job, String>("jobid"));
        jobtitle.setCellValueFactory(new PropertyValueFactory<Job, String>("jobtitle"));
        company.setCellValueFactory(new PropertyValueFactory<Job, String>("company"));
        this.location.setCellValueFactory(new PropertyValueFactory<Job, String>("location"));
        experience.setCellValueFactory(new PropertyValueFactory<Job, String>("experience"));
        jobdescription.setCellValueFactory(new PropertyValueFactory<Job, String>("jobdescription"));
        salary.setCellValueFactory(new PropertyValueFactory<Job, String>("salary"));
        likes.setCellValueFactory(new PropertyValueFactory<Job, Integer>("likes"));
        jobs_table.setItems(jobList);

    }

}
