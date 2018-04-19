   /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_jury;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import opisiame.database.Connection_db;
import java.util.Timer;
import opisiame.model.Vote;
import opisiame.dao.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
/**
 *
 * @author itzel
 */
public class Vote_Controller  implements Initializable {

    private ObservableList<Vote> votes = FXCollections.observableArrayList();

    @FXML
    private AnchorPane content;
    @FXML
    private TableView<Vote> Tableau;
    @FXML
    private TableColumn<Vote, Integer> id;
    @FXML
    private TableColumn<Vote, String> Nom;
    @FXML
    private TableColumn<Vote, String> Prenom;
    @FXML
    private TableColumn<Vote, String> Filiere;
    @FXML
    private TableColumn<Vote, Integer> Annee;
   
    @FXML
    private Label no_votants;
    
    @FXML
    private TextField Question;
    
    @FXML
    private Button Attendez;
    
    @FXML
    private Button Votez;
    
    @FXML
    private Button Arretez;
   
    private List<Integer> estudents_evaluate = new ArrayList<>();
    
    int participants;

    @FXML
    private TextField Vote_timer;
    
    private Integer timer_value;
   
    @FXML
    private Label label_error_timer;

    @FXML
    private Label label_number_timer_error;
    
    Vote_dao vote_dao = new Vote_dao();
    
    private Integer vote_id;

    public void setQuiz_id(Integer vote_id) {
        this.vote_id = vote_id;
        get_vote_by_id();
    }
    
    public void get_vote_by_id() {
        Vote vote = vote_dao.get_vote_by_id(this.vote_id);
        
        no_votants.setText((vote.getVoters()).toString());
  
    }
    
    public void setVoters(int nombre_votants) {
     this.participants = nombre_votants; 
     no_votants.setText("" + participants);
     }
    
       public void setEleve_id(List liste_id) {
        this.estudents_evaluate = liste_id;
       }
    

    @FXML
    public void timer_set_label_error_not_visible() {
        label_error_timer.setVisible(false);
        label_number_timer_error.setVisible(false);
    }
    
    //Fonction qui récupère la liste des élèves
public void getSelectedEleve() {
        try {
            this.setVoters(participants);
            ListIterator<Integer> i = this.estudents_evaluate.listIterator();
            Connection connection = Connection_db.getDatabase();
            while (i.hasNext()) {
                int supr = i.next();
                PreparedStatement requette;
            
            requette = connection.prepareStatement("SELECT participant.Part_id, participant.Part_nom, participant.Part_prenom, filiere.Filiere, filiere.Annee FROM participant \n"
                        + "LEFT JOIN filiere \n"
                        + "ON filiere.Filiere_ID = participant.Filiere_id \n"
                        + "WHERE Part_id= ?");
            requette.setInt(1, supr);
            //requette.executeUpdate();
            ResultSet res_requette = requette.executeQuery();
            while (res_requette.next()) {
                Vote etudiant = new Vote();
                etudiant.setId(res_requette.getInt(1));
                etudiant.setNom(res_requette.getString(2));
                etudiant.setPrenom(res_requette.getString(3));
                etudiant.setFiliere(res_requette.getString(4));
                etudiant.setAnnee(res_requette.getInt(5));
                votes.add(etudiant);
            }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<Vote, Integer>("id"));
        Nom.setCellValueFactory(new PropertyValueFactory<Vote, String>("Nom"));
        Prenom.setCellValueFactory(new PropertyValueFactory<Vote, String>("Prenom"));
        Filiere.setCellValueFactory(new PropertyValueFactory<Vote, String>("Filiere"));
        Annee.setCellValueFactory(new PropertyValueFactory<Vote, Integer>("Annee"));
        //System.out.println("a ver si muy machin aca la neta " + nombre_votants);
        Tableau.setItems(votes);
        Votez.setDisable(true);
        Arretez.setDisable(true);

    }

     @FXML
    public void Attendez() throws IOException {
        //Retour sur la fenetre d'identification
        Stage stage = (Stage) content.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/jury/resultats.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
    }
    
        @FXML
    public void Votez() throws IOException {
        Attendez.setDisable(true);
        //Retour sur la fenetre d'identification
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/interface_authentification.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        session.Session.Logout();
    }
    
        @FXML
    public void Arretez() throws IOException {
        //Retour sur la fenetre d'identification
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/interface_authentification.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        session.Session.Logout();
    }
    
    public void reset_null() {
        Vote_timer.setText("");
    }

    private boolean validate_number(String str) {
        return str.matches("[0-9]*");
    }
    
        @FXML
    public void btn_valider() {
        //System.out.println(t_liste_quiz.getSelectionModel().getSelectedItem().getId());
        String value_timer = Vote_timer.getText();
        Boolean champ_ok = true;
       
        if (value_timer.compareTo("") == 0) {
            label_error_timer.setVisible(true);
            champ_ok = false;
        }
        if (!validate_number(value_timer)) {
            label_number_timer_error.setVisible(true);
            champ_ok = false;
        }
        
    }
    
}
