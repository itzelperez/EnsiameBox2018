   /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_jury;

import static com.lowagie.text.pdf.PdfFileSpecification.url;
import com.rapplogic.xbee.XBeePin;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import opisiame.model.Vote;
import opisiame.dao.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import opisiame.controller.com.ListenToRemoteVotingThread;
import opisiame.controller.gestion_eleve.Link_eleve_teleController;
import opisiame.controller.gestion_quiz.Lancer_questionController;
import opisiame.model.Eleve;
import opisiame.dao.Voting_poll_dao;
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
    private Label no_votes;
    
    @FXML
    private TextField Question;
    
    @FXML
    private Button Attendez;
    
    @FXML
    private Button Votez;
    
    @FXML
    private Button Arretez;
    
    private Integer pkVotingPoll = 0;
    private Integer votesMade = 0;
    
    @FXML
    private Button valider;
    
    @FXML
    private Button commencer_votation;
   
    private List<Integer> estudents_evaluate = new ArrayList<>();
    
    int participants;

    int votesmade; 
    
    @FXML
    private TextField Vote_timer;
    
    private Integer timer_value;
   
    @FXML
    private Label label_error_timer;

    @FXML
    private Label label_number_timer_error;
    
    @FXML
    private Label label_minuteur;
    
    Vote_dao vote_dao = new Vote_dao();
    
    Vote current_vote; 
            
    private Integer vote_id;
    
    private Integer value_timer_integer = 30;
    
    
    private Integer first_digit = 0, second_digit = 30;
    
    ListenToRemoteVotingThread listenRemote;
    
    private XBee xbee;
    
    @FXML
    private TextField motifTextField;
    
    @FXML
    private Button validation;
    
    @FXML
    private Button redoublement;
    
    @FXML
    private Button exclusion;

    String led_yellow = "D7";
    String led_green = "D5";
    String led_red = "D4";
    
    public void setXBee(XBee xbee){
        System.out.println("opisiame.controller.gestion_jury.Vote_Controller.setXBee() is it getting here");
        System.out.println("ENDERE}CO SET 2 "+ this);
        this.xbee = xbee;
    }

    public void setVote_id(Integer vote_id) {
        this.vote_id = vote_id;
        get_vote_by_id();
    }
    
    
    public void get_vote_by_id() {
        Vote vote = vote_dao.get_vote_by_id(this.vote_id);
        no_votants.setText((vote.getVoters()).toString());
  
    }
    
    public Integer getVotesMade(){
        return votesMade;
    }
    
     public void setVotesMade(int votesmade){
        this.votesMade = votesmade;
    }
    
    
    
    public void setVoters(int participants) {
     this.participants = participants; 
     no_votants.setText("" + participants);
     System.out.println("los participantes después" + participants);
    }
    
    
       public void setEleve_id(List eleve_id) {
           System.out.println("ENDERECO ELEVE " + this);
        this.estudents_evaluate = eleve_id;
       }
    

    @FXML
    public void timer_set_label_error_not_visible() {
        label_error_timer.setVisible(false);
        label_number_timer_error.setVisible(false);
    }
    
    //Fonction qui récupère la liste des élèves
public void getSelectedEleve() {
        try {
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
        System.out.println("opisiame.controller.gestion_jury.Vote_Controller.initialize() INTICIALIZOU " + this);
        id.setCellValueFactory(new PropertyValueFactory<Vote, Integer>("id"));
        Nom.setCellValueFactory(new PropertyValueFactory<Vote, String>("Nom"));
        Prenom.setCellValueFactory(new PropertyValueFactory<Vote, String>("Prenom"));
        Filiere.setCellValueFactory(new PropertyValueFactory<Vote, String>("Filiere"));
        Annee.setCellValueFactory(new PropertyValueFactory<Vote, Integer>("Annee"));
        Tableau.setItems(votes);
        Attendez.setDisable(false);
        Votez.setDisable(true);
        Arretez.setDisable(true);
        try {
            Attendez();
        } catch(Exception e)
        {
            
        }
        System.out.println("opisiame.controller.gestion_jury.Vote_Controller.initialize() FDNOFNFOLFM CAGUEI");
    }

     @FXML
    public void Attendez() throws IOException {
         System.out.println("opisiame.controller.gestion_jury.Vote_Controller.Attendez()");
            }
    
        @FXML
    public void Votez() throws IOException {
        System.out.println("opisiame.controller.gestion_jury.Vote_Controller.Attendez()");
          }
    
        @FXML
    public void Arretez() throws IOException {
      System.out.println("opisiame.controller.gestion_jury.Vote_Controller.Attendez()");
    }
    
        @FXML
    public void Validate_session() throws IOException {
        try {
            show_results();
           } catch (IOException ex) {
                    Logger.getLogger(Vote_Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
      
    }
    
      @FXML
    public void Annuler_session() throws IOException {
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de Annulation");
            alert.setHeaderText("Vous êtes sur le point d'annuler la session.");
            alert.setContentText("Voulez-vous continuer?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){ 
                
                Attendez.setDisable(false);
                Votez.setDisable(true);
                Arretez.setDisable(true);
                
                motifTextField.clear();
                no_votes.setText("0");
                votesMade=0; 
                reset_null();        
                
                    
                try {
                    if(pkVotingPoll != 0){
                        Connection connection = Connection_db.getDatabase();
                        PreparedStatement ps;
                        ps = connection.prepareStatement("DELETE FROM `voting_poll` WHERE idPoll= ?");
                        ps.setInt(1, pkVotingPoll);
                        ps.executeUpdate();
                    
                        ps = connection.prepareStatement("DELETE FROM `votes` WHERE idPoll = ?");
                        ps.setInt(1, pkVotingPoll);
                        ps.executeUpdate();
                        
                        
                        ps = connection.prepareStatement("DELETE FROM `voting_poll_participant` WHERE idPoll = ?");
                        ps.setInt(1, pkVotingPoll);
                        ps.executeUpdate();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }    
    }
    
     public void switch_off_remotes(String led_id) {
        try {
            RemoteAtRequest request_led_off = new RemoteAtRequest(XBeeAddress64.BROADCAST, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_LOW.getValue()});
            System.out.println("REQUEST " + request_led_off);
            xbee.sendAsynchronous(request_led_off);
            System.out.println("opisiame.controller.gestion_jury.Vote_Controller.switch_off_remotes()YESYESYEYES");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            Logger.getLogger(Vote_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void switch_on_remotes(String led_id) {
        try {
            RemoteAtRequest request_led_off = new RemoteAtRequest(XBeeAddress64.BROADCAST, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_HIGH.getValue()});
            xbee.sendAsynchronous(request_led_off);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Link_eleve_teleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (XBeeException ex) {
            Logger.getLogger(Lancer_questionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void setNum_port(String num_port) {
        System.out.println("opisiame.controller.gestion_jury.Vote_Controller.setNum_port() " + num_port);
                try {
                    xbee.open(num_port, 9600);
                    System.out.println("xbee connected");
                } catch (XBeeException ex) {
                    ex.printStackTrace();
                } 
    }
    
    public ListenToRemoteVotingThread listen_remote(Integer idPoll) {
        //setNum_port(choix_port.getSelectionModel().getSelectedItem().toString());
       switch_off_remotes(led_yellow);
        switch_on_remotes(led_green);
        
            if (listenRemote != null) {
                listenRemote.interrupt();
            }
            listenRemote = new ListenToRemoteVotingThread(xbee, votes, idPoll, this);
            listenRemote.setRep_id_a("Oui");
            listenRemote.setRep_id_b("Non");
            listenRemote.setRep_id_c("Blanc");
            listenRemote.setRep_id_d("Non concerné");
            listenRemote.start();
        
            return listenRemote;
    }
    
    public void reset_null() {
        Vote_timer.setText("");
    }

    private boolean validate_number(String str) {
        return str.matches("[0-9]*");
    }
    
        @FXML
    public void btn_valider() {
        
        String value_timer = Vote_timer.getText();
        Boolean champ_ok = true;
       
        if (value_timer.compareTo("") == 0) {
            label_error_timer.setVisible(true);
            champ_ok = false;
        }
        if (!validate_number(value_timer)) {
            label_number_timer_error.setText("Minuteur doit être un chiffre!");
            label_number_timer_error.setVisible(true);
            champ_ok = false;
        }
        
        //System.out.println(t_liste_quiz.getSelectionModel().getSelectedItem().getId());
        if (champ_ok){
            value_timer_integer = Integer.parseInt(value_timer);
            DecimalFormat formatter = new DecimalFormat("00");
            first_digit = value_timer_integer/60;
            second_digit = value_timer_integer%60;
            label_minuteur.setText(formatter.format(first_digit) + ":" + formatter.format(second_digit));
        }
        
        
    }
    
    @FXML
    public void show_results() throws IOException{
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/jury/resultats.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            ResultatsVoteController results_controller = fxmlLoader.<ResultatsVoteController>getController();
            results_controller.setIdPoll(pkVotingPoll);
            results_controller.show_results();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Resultats");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            results_controller.setStage(stage);
    }
    
    @FXML
    public void valid_stud() throws IOException{
        motifTextField.clear();
        motifTextField.setText("Validation");
        System.out.println("Validando betch");
        
    }
    
    @FXML
    public void redouble() throws IOException{
        motifTextField.clear();
        motifTextField.setText("Redoublement");
        System.out.println("Redoublement betch");
        
    }
    
    @FXML
    public void exclusion() throws IOException{
        motifTextField.clear();
        motifTextField.setText("Exclusion");
        System.out.println("Exclusion betch");
        
    }
    
    @FXML
    public void btn_votation() throws IOException{ 
            
        if (!motifTextField.getText().trim().isEmpty()){
            Attendez.setDisable(true);
            Votez.setDisable(false);
            Arretez.setDisable(true);
            Timer minuteur_vote = new Timer();
            Timer arreter_minuteur = new Timer();
            Timer vote_minuteur = new Timer();
            Voting_poll_dao voting_poll_dao = new Voting_poll_dao();
            Calendar dateToday = Calendar.getInstance();
            dateToday.set(Calendar.HOUR_OF_DAY, 0);
            java.util.Date today = dateToday.getTime();
            java.sql.Date sqlDateToday = new java.sql.Date(today.getTime());
            String motifDuVote = motifTextField.getText().trim();
            //Integer eleve_id = null; 
        
            System.out.println("ENDERECO VOTE " + this);
            System.out.println("PARTICIPANTS " + participants);
            System.out.println("opisiame.controller.gestion_jury.Vote_Controller.btn_votation() " + xbee);
            pkVotingPoll = voting_poll_dao.insert_voting_poll(sqlDateToday, participants, motifDuVote);
            Voting_poll_Participant_dao voting_poll_Participant_dao = new Voting_poll_Participant_dao();
            for (Vote vote : votes)
                voting_poll_Participant_dao.insert_voting_poll_Participant(vote.getId(), pkVotingPoll);
            ListenToRemoteVotingThread voting_thread = listen_remote(pkVotingPoll);

        
            vote_minuteur.schedule(new TimerTask() {    
        
            @Override
            public void run() {
                Platform.runLater(() -> {
                no_votes.setText(votesMade.toString());
                    //System.out.println("votes made" + votesMade);
                });
            }
            }, 1000, 1000);
            
            minuteur_vote.schedule(new TimerTask() {    
            
            DecimalFormat formatter = new DecimalFormat("00");
        
            @Override
            public void run() {
                Platform.runLater(() -> {
                if (first_digit > 0 || second_digit > 0){
                    if (second_digit > 0){
                        //System.out.println("chegou aqui");
                        second_digit--;
                        label_minuteur.setText(formatter.format(first_digit) + ":" + formatter.format(second_digit));
                    } else if (first_digit > 0){
                        first_digit--;
                        second_digit += 59;
                        label_minuteur.setText(formatter.format(first_digit) + ":" + formatter.format(second_digit));
                    }
                }
                });
                if(participants == votesMade){
                
                    Platform.runLater(() -> {
                        Attendez.setDisable(true);
                        Votez.setDisable(true);
                        Arretez.setDisable(false);
                        minuteur_vote.cancel();
                        minuteur_vote.purge();
                        voting_thread.setRunning(false);
            });
                            } 
            }
            }, 1000, 1000);
            
            
                           
        
            arreter_minuteur.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        Attendez.setDisable(true);
                        Votez.setDisable(true);
                        Arretez.setDisable(false);
                        minuteur_vote.cancel();
                        minuteur_vote.purge();
                        voting_thread.setRunning(false);
                        
                    });
                }    
            }, value_timer_integer * 1000 + 1000);
            
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Impossible de commencer à voter.\n" +
                                 "Motif de vote obligatoire.");
            alert.showAndWait();
            alert.setHeaderText(null);
        }
    }
    
}
