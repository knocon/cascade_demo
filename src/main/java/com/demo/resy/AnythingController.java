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

import static com.demo.resy.Main.*;

public class AnythingController implements Initializable {

    private  Stage stage;
    private  Scene scene;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Job, String> company;

    @FXML
    private TableColumn<Job, String> debugcode;

    @FXML
    private Button dislikeButton;

    @FXML
    private Button likeButton;

    @FXML
    private TableColumn<Job, String> experience;

    @FXML
    private TextField filter;

    @FXML
    private TableColumn<Job, String> jobdescription;


    @FXML
    private TableView<Job> jobs_table;

    @FXML
    private TableColumn<Job, String> jobtitle;

    @FXML
    private TableColumn<Job, String> location;

    @FXML
    private TableColumn<Job, Integer> likes;

    @FXML
    private Button refresh;

    @FXML
    private TableColumn<Job, String> salary;

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
        try {
            bestJobs.removeAll(bestJobs);
            neoDbObject.readJobs();
            jobtitle.setCellValueFactory(new PropertyValueFactory<Job, String>("jobtitle"));
            company.setCellValueFactory(new PropertyValueFactory<Job, String>("company"));
            this.location.setCellValueFactory(new PropertyValueFactory<Job, String>("location"));
            experience.setCellValueFactory(new PropertyValueFactory<Job, String>("experience"));
            jobdescription.setCellValueFactory(new PropertyValueFactory<Job, String>("jobdescription"));
            salary.setCellValueFactory(new PropertyValueFactory<Job, String>("salary"));
            likes.setCellValueFactory(new PropertyValueFactory<Job, Integer>("likes"));
            debugcode.setCellValueFactory(new PropertyValueFactory<Job, String>("debugcode"));
            jobs_table.setFixedCellSize(57);

            jobList.sort(Comparator.comparing(Job::getLikes));
            int bestrating = jobList.get(jobList.size()-1).getLikes();
            int secondbestrating = jobList.get(jobList.size()-2).getLikes();
            for(Job job: jobList){
                /**
                 * generate list of top rated jobs.
                 */
                if(job.getLikes() == bestrating || job.getLikes() == secondbestrating){
                    //TODO: Duplikate entfernen.
                    job.setDebugcode("NPR");
                    bestJobs.add(job);
                }
            }
            System.out.println("Results finished.");
            jobs_table.setItems(bestJobs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void likeJob(MouseEvent event) {

        try{

            neoDbObject.likeOffer(jobs_table.getSelectionModel().getSelectedItem(), activeUser);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Important information!");
            alert.setContentText("Joboffer deleted.");
            alert.showAndWait();
            refresh(event);
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Tupel nicht ausgewählt!");
            alert.showAndWait();
        }

    }

    @FXML
    void dislikeJob(MouseEvent event) {

        try{

            neoDbObject.dislikeOffer(jobs_table.getSelectionModel().getSelectedItem(), activeUser);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Important information!");
            alert.setContentText("Like deleted.");
            alert.showAndWait();
            refresh(event);
        }catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information!");
            alert.setHeaderText("Missing information!");
            alert.setContentText("Tupel nicht ausgewählt!");
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            bestJobs.removeAll(bestJobs);
            neoDbObject.readJobs();
            jobtitle.setCellValueFactory(new PropertyValueFactory<Job, String>("jobtitle"));
            company.setCellValueFactory(new PropertyValueFactory<Job, String>("company"));
            this.location.setCellValueFactory(new PropertyValueFactory<Job, String>("location"));
            experience.setCellValueFactory(new PropertyValueFactory<Job, String>("experience"));
            jobdescription.setCellValueFactory(new PropertyValueFactory<Job, String>("jobdescription"));
            salary.setCellValueFactory(new PropertyValueFactory<Job, String>("salary"));
            likes.setCellValueFactory(new PropertyValueFactory<Job, Integer>("likes"));

            debugcode.setCellValueFactory(new PropertyValueFactory<Job, String>("debugcode"));
            jobs_table.setFixedCellSize(57);

            jobList.sort(Comparator.comparing(Job::getLikes));
            int bestrating = jobList.get(jobList.size()-1).getLikes();
            int secondbestrating = jobList.get(jobList.size()-2).getLikes();
            for(Job job: jobList){
                /**
                 * generate list of top rated jobs.
                 */
                if(job.getLikes() == bestrating || job.getLikes() == secondbestrating){
                    //TODO: Duplikate entfernen.
                    job.setDebugcode("NPR");
                    bestJobs.add(job);
                }
            }
            System.out.println("Results finished.");

            jobs_table.setItems(bestJobs);
            likes.setSortType(TableColumn.SortType.DESCENDING);
            jobs_table.getSortOrder().add(likes);
            likes.setSortable(true);
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
