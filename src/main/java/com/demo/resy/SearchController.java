package com.demo.resy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static com.demo.resy.Main.jobList;
import static com.demo.resy.Main.neoDbObject;

public class SearchController implements Initializable {

    private  Stage stage;
    private  Scene scene;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Recommendation, String> company;

    @FXML
    private TableColumn<Recommendation, String> debugcode;

    @FXML
    private Button delButton;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<Recommendation, String> experience;

    @FXML
    private TextField filter;

    @FXML
    private TableColumn<Recommendation, String> jobdescription;


    @FXML
    private TableView<Recommendation> jobs_table;

    @FXML
    private TableColumn<Recommendation, String> jobtitle;

    @FXML
    private TableColumn<Recommendation, String> location;

    @FXML
    private TableColumn<Recommendation, Integer> likes;

    @FXML
    private Button refresh;

    @FXML
    private TableColumn<Recommendation, String> salary;

    @FXML
    void addJob(MouseEvent event) {

    }

    @FXML
    void backToHome(MouseEvent event) throws IOException {

        /**
         * Returns home.
         */
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void editJob(MouseEvent event) {

    }

    @FXML
    void refresh(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            neoDbObject.readJobs();
            jobtitle.setCellValueFactory(new PropertyValueFactory<Recommendation, String>("jobtitle"));
            company.setCellValueFactory(new PropertyValueFactory<Recommendation, String>("company"));
            this.location.setCellValueFactory(new PropertyValueFactory<Recommendation, String>("location"));
            experience.setCellValueFactory(new PropertyValueFactory<Recommendation, String>("experience"));
            jobdescription.setCellValueFactory(new PropertyValueFactory<Recommendation, String>("jobdescription"));
            salary.setCellValueFactory(new PropertyValueFactory<Recommendation, String>("salary"));
            likes.setCellValueFactory(new PropertyValueFactory<Recommendation, Integer>("rating"));
            debugcode.setCellValueFactory(new PropertyValueFactory<Recommendation, String>("debugcode"));
            jobs_table.setFixedCellSize(57);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
