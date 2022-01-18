package com.demo.resy;

import javafx.collections.ObservableList;
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
            int[] likes = calculateRatings(jobList);
            for(Job job: jobList){
                /**
                 * generate list of top rated jobs.
                 */
                if(job.getLikes() == likes[0] || job.getLikes() == likes[1]  || job.getLikes() == likes[2] ){
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
            alert.setContentText("Liked job.");
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
            /**
             * Best to worst ratings
             */
            int[] ratings = calculateRatings(jobList);

            for(Job job: jobList){
                /**
                 * generate list of top rated jobs.
                 */
                if(job.getLikes() == ratings[0] || job.getLikes() == ratings[1] || job.getLikes() == ratings[2] || job.getLikes() == ratings[3] || job.getLikes() == ratings[4] || job.getLikes() == ratings[5] || job.getLikes() == ratings[6] || job.getLikes() == ratings[7] || job.getLikes() == ratings[8] || job.getLikes() == ratings[9]){
                    job.setDebugcode("NPR");
                    bestJobs.add(job);
                }
            }
            System.out.println("Results finished.");
            System.out.println("Non-personalized recommendations:"+bestJobs.size());

            jobs_table.setItems(bestJobs);
            likes.setSortType(TableColumn.SortType.DESCENDING);
            jobs_table.getSortOrder().add(likes);
            likes.setSortable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[] calculateRatings(ObservableList<Job> input){
        int[] erg ={0,0,0,0,0,0,0,0,0,0};
        erg[0] = input.get(input.size()-1).getLikes();

        for(int i=jobList.size()-1;i>0;i--){

            if(jobList.get(i).getLikes()<erg[0]) {
                erg[1] = jobList.get(i).getLikes();
                break;
            }

        }

        for(int i=jobList.size()-1;i>0;i--){

            if(jobList.get(i).getLikes()<erg[1]) {
                erg[2] = jobList.get(i).getLikes();
                break;
            }

        }

        for(int i=jobList.size()-1;i>0;i--){

            if(jobList.get(i).getLikes()<erg[2]) {
                erg[3] = jobList.get(i).getLikes();
                break;
            }

        }

        for(int i=jobList.size()-1;i>0;i--){

            if(jobList.get(i).getLikes()<erg[3]) {
                erg[4] = jobList.get(i).getLikes();
                break;
            }

        }

        for(int i=jobList.size()-1;i>0;i--){

            if(jobList.get(i).getLikes()<erg[4]) {
                erg[5] = jobList.get(i).getLikes();
                break;
            }

        }

        for(int i=jobList.size()-1;i>0;i--){

            if(jobList.get(i).getLikes()<erg[5]) {
                erg[6] = jobList.get(i).getLikes();
                break;
            }

        }

        for(int i=jobList.size()-1;i>0;i--){

            if(jobList.get(i).getLikes()<erg[6]) {
                erg[7] = jobList.get(i).getLikes();
                break;
            }

        }

        for(int i=jobList.size()-1;i>0;i--){

            if(jobList.get(i).getLikes()<erg[7]) {
                erg[8] = jobList.get(i).getLikes();
                break;
            }

        }

        for(int i=jobList.size()-1;i>0;i--){

            if(jobList.get(i).getLikes()<erg[8]) {
                erg[9] = jobList.get(i).getLikes();
                break;
            }

        }


        System.out.println("Calculated Ratings: r1["+erg[0]+"], r2["+erg[1]+"], r3["+erg[2]+"] r4["+erg[3]+"] r5["+erg[4]+"] r6["+erg[5]+"] r7["+erg[6]+"] r8["+erg[7]+"] r9["+erg[8]+"] r10["+erg[9]+"]");

        return erg;
    }
}
