package com.demo.resy;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class SearchController implements Initializable {

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
    private Button detailsButton;

    @FXML
    private TableView<Job> jobs_table;

    @FXML
    private TableColumn<Job, String> jobtitle;

    @FXML
    private TableColumn<Job, String> location;

    @FXML
    private TableColumn<Job, Integer> likes;



    @FXML
    private TableColumn<Job, String> salary;



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
    void likeJob(MouseEvent event) {

        try{

            neoDbObject.likeOffer(jobs_table.getSelectionModel().getSelectedItem(), activeUser);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Important information!");
            alert.setContentText("Joboffer deleted.");
            alert.showAndWait();
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
            likedJobList.removeAll(likedJobList);
            neoDbObject.readJobs();
            neoDbObject.readLikedJobs();
            jobtitle.setCellValueFactory(new PropertyValueFactory<Job, String>("jobtitle"));
            company.setCellValueFactory(new PropertyValueFactory<Job, String>("company"));
            this.location.setCellValueFactory(new PropertyValueFactory<Job, String>("location"));
            experience.setCellValueFactory(new PropertyValueFactory<Job, String>("experience"));
            jobdescription.setCellValueFactory(new PropertyValueFactory<Job, String>("jobdescription"));
            salary.setCellValueFactory(new PropertyValueFactory<Job, String>("salary"));
            likes.setCellValueFactory(new PropertyValueFactory<Job, Integer>("likes"));
            debugcode.setCellValueFactory(new PropertyValueFactory<Job, String>("debugcode"));
            jobs_table.setFixedCellSize(57);
            System.out.println("Looking for recommendations. This might take a while!");
            jobs_table.setItems(generateRecommendations(likedJobList, Main.jobList));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     *
     * @param
     * @return
     */


    public ObservableList<Job> generateRecommendations(ObservableList<Job> liked, ObservableList<Job> joblist) throws IOException {
        NormalizedLevenshtein nl = new NormalizedLevenshtein();                 //calculates string distance
        ObservableList<Job> collection = FXCollections.observableArrayList();   //holds the result
        ObservableList<Job> results = FXCollections.observableArrayList();      //can be used to remove duplicates
        ObservableList<User> similarUsersl1 = FXCollections.observableArrayList();// LEVEL 1
        similarUsersl1.addAll(neoDbObject.findSimilarUsersToActiveUserBySkills());        //holds all similar users to active user.
        ObservableList<User> similarUsersl2 = FXCollections.observableArrayList();// LEVEL 2
        similarUsersl2.addAll(neoDbObject.findSimilarUsersToActiveUserL2());        //holds all similar users to active user.
        int cb =0;
        int cfl1 =0;
        int cfl2 =0;
        double sharpness = 0.15;
        /**
         * CB-Recommendation
         */
        for(Job j : liked){

            for(Job jL : joblist){

                /*
                 Search for job with similar keywords.
                 keywords are generated by: company, jobtitle, location
                 */
                double similarity_keywords = nl.distance(j.getKeywords().toString(), jL.getKeywords().toString());
                if(similarity_keywords < sharpness){
                    jL.setDebugcode("CBR_K");
                    collection.add(jL);
                    cb+=1;
                }
            }
        }
        /**
         * CF-Recommendation
         * Very low-level.
         */
        ObservableList<Job> potentialJobs = FXCollections.observableArrayList();
        for(User u : similarUsersl1){
            potentialJobs.addAll(neoDbObject.readLikedJobsWithReturn(u));
        }
        for(Job j : potentialJobs){
            j.setDebugcode("CF_S_L1");
        }
        cfl1 += potentialJobs.size();
        collection.addAll(potentialJobs);

        /**
         * CF-Recommendation
         * Mid level. Finding better clusters!
         */

        ObservableList<Job> potentialJobs2 = FXCollections.observableArrayList();
        for(User u : similarUsersl2){
            potentialJobs2.addAll(neoDbObject.readLikedJobsWithReturn(u));
        }
        for(Job j : potentialJobs2){
            j.setDebugcode("CF_S_L2");
        }
        cfl2 += potentialJobs2.size();
        collection.addAll(potentialJobs2);


        /**
         * remove duplicates.
         */
        /*for(Job j : collection){
            if(!results.contains(j)){
                results.add(j);
            }
        }*/

        System.out.println("I've found some recommendations for you.\n"+cb+" Content based recommendations[sharpness set to:"+sharpness+"].");
        System.out.println(cfl1 +" Collaborative filtering recommendations[Level 1].");
        System.out.println(cfl2 +" Collaborative filtering recommendations[Level 2].");
        return collection;
    }

    @FXML
    void openJob(MouseEvent event) {

        try {
            JobHolder holder = JobHolder.getInstance();
            holder.setJob(jobs_table.getSelectionModel().getSelectedItem());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("jobdetailed.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("file:src/main/resources/com/demo/resy/ca.png"));
            stage.setTitle("Cascade - Job");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cant load new Window");
        }

    }


}
