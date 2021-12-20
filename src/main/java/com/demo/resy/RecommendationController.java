package com.demo.resy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

import static com.demo.resy.Main.jobList;
import static com.demo.resy.Main.neoDbObject;

public class RecommendationController implements Initializable {

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
    private TableColumn<Recommendation, Integer> rating;

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
            rating.setCellValueFactory(new PropertyValueFactory<Recommendation, Integer>("rating"));
            debugcode.setCellValueFactory(new PropertyValueFactory<Recommendation, String>("debugcode"));
            jobs_table.setFixedCellSize(55);

            jobList.sort(Comparator.comparing(Job::getRating));
            int bestrating = jobList.get(jobList.size()-1).getRating();
            int secondbestrating = jobList.get(jobList.size()-2).getRating();
            ObservableList<Recommendation> bestjobs = FXCollections.observableArrayList();
            for(Job job: jobList){
                /**
                 * generate list of top rated jobs.
                 */
                if(job.getRating() == bestrating || job.getRating() == secondbestrating){
                    //TODO: Duplikate entfernen.
                    Recommendation rec = new Recommendation(job.getJobtitle(), job.getCompany(), job.getLocation(), job.getExperience(), job.getSalary(), job.getJobdescription(), "NPR", job.getRating());
                    bestjobs.add(rec);
                }
            }
            System.out.println("Results finished.");
            jobs_table.setItems(bestjobs);

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
