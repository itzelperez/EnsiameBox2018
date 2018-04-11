/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_jury;


import com.rapplogic.xbee.XBeePin;
import com.rapplogic.xbee.api.ApiId;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ButtonBase;

import com.rapplogic.xbee.api.ErrorResponse;
import com.rapplogic.xbee.api.RemoteAtRequest;

import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.wpan.IoSample;
import com.rapplogic.xbee.api.wpan.RxResponseIoSample;
import gnu.io.CommPortIdentifier;
import java.io.IOException;
import static java.lang.Math.log;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import opisiame.controller.gestion_eleve.Link_eleve_teleController;
import opisiame.dao.*;
import opisiame.model.Eleve;
import opisiame.model.Vote;
import org.apache.log4j.Logger;
 

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class session_vote  implements Initializable{
    
    private Integer vote_id;
    
    private ObservableList<Vote> votes ;
    
    @FXML
    private AnchorPane content;
    
    @FXML
    private Button btn_suivant;
    
    @FXML
    private Button btn_connect;
            
    @FXML
    private Button btn_disconnect;        
    
    @FXML
    private Button reopen_session;     
    
    @FXML
    private Button close_session;
    
    @FXML
    private TextField tf_mac_telec;
    
    @FXML
    private ComboBox choix_port;

    XBeeResponse response;
    
    List<XBeeAddress64> remotes_responded;
    
    List<Vote> Vote_data;
    
    int participants = 0; 

    private String num_port;
    private String disconnect; 
    
    ArrayList<XBeeAddress64> adresses = new ArrayList<>();
    
    Vote_dao vote_dao = new Vote_dao();
    
    Timestamp date_participation;
    
    Participation_vote_dao participation_vote_dao = new Participation_vote_dao();

    private final static Logger log = Logger.getLogger(session_vote.class);

    private XBee xbee = new XBee();

    String led_yellow = "D7";
    String led_green = "D5";
    String led_red = "D4";
    
    Thread_wait_for_cmd thread_wait_for_cmd;
    
public void initialize(URL url, ResourceBundle rb) {
        date_participation = new Timestamp(System.currentTimeMillis());
        init_liste_port();
        btn_connect.setDisable(true);
        btn_disconnect.setDisable(true);
        reopen_session.setDisable(true);
        close_session.setDisable(true);
        btn_suivant.setDisable(true);
        
    }


	public void init_liste_port() {
        Enumeration pList = CommPortIdentifier.getPortIdentifiers();
        System.out.println("taille liste : " + pList.toString());
        choix_port.getItems().clear();
        boolean found = false;
        // Process the list.
        while (pList.hasMoreElements()) {
            CommPortIdentifier cpi = (CommPortIdentifier) pList.nextElement();
            System.out.print("Port " + cpi.getName() + " ");
            if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println("Found Serial Port: " + cpi);
                choix_port.getItems().add(cpi.getName());
                found = true;
            }
        }
    }
    
    @FXML
    public void select_port() {
        num_port = choix_port.getSelectionModel().getSelectedItem().toString();
        System.out.println("choix port : " + num_port);
        try {
            btn_connect.setDisable(false);
            btn_disconnect.setDisable(false);
            reopen_session.setDisable(false);
            close_session.setDisable(false);
            xbee.open(num_port, 9600);
        } catch (XBeeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error lors de l'ouverture du port");
            alert.setHeaderText(null);
            alert.setContentText("Assurez-vous que le port n'est pas utilisé par une autre application "
                    + "ou que le module soit bien configuré");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    @FXML
    public void actualiser_port() {
        init_liste_port();
    }
       	
    @FXML
    public void disconnect() {
        //close the serial port
        System.out.println("disconnecting port : " + num_port);
        try {
            xbee.close();
            num_port = null; 
            choix_port.getItems().clear();
            tf_mac_telec.clear(); 
            btn_connect.setDisable(true);
            btn_disconnect.setDisable(true);
            reopen_session.setDisable(true);
            close_session.setDisable(true);
            btn_suivant.setDisable(true);
            init_liste_port();
            
        }
        catch (Exception b) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Échec de la fermeture ");
            alert.setHeaderText(null);
            alert.setContentText("Échec de la fermeture du port : " + num_port);
            alert.showAndWait();
            b.printStackTrace();
        }
    }
	
    @FXML
    public void btn_suivant() throws IOException {
        //ouvre la fenêtre liste d'eleves 
        if(participants == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Impossible de continuer sans telecommandes connectées");
            alert.showAndWait();
            alert.setHeaderText(null);
        }
        
        else{
            btn_suivant.setDisable(false);
            Stage stage = (Stage) content.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/jury/liste_eleves_jury.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();
        }
        
    }
        
    @FXML
    public void ClicBoutonRetour() throws IOException {
                Stage stage = (Stage) content.getScene().getWindow();
                stage.close();

    }
    
          @FXML
    public void btn_exit() throws IOException {
        //remise à zéro des variables d'identification (login + mdp)
        session.Session.Logout();
        //ouvre la fenêtre Interface_authentification
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/interface_authentification.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }
    
   
    @FXML
    public void btn_connect() {  
 
        if ((thread_wait_for_cmd != null) && (thread_wait_for_cmd.isAlive())) {
            thread_wait_for_cmd.interrupt();
        }    
            if (response == null) {
            tf_mac_telec.setText("Attente appui télécommande");
            System.out.println("wait");
            System.out.println("Connect command");
        }
        
        thread_wait_for_cmd = new session_vote.Thread_wait_for_cmd();
        thread_wait_for_cmd.start();
        btn_connect.arm();
    }
   
    
    @FXML
    public void btn_disconnect() throws InterruptedException { 
            try{
                tf_mac_telec.setText("Attente appui télécommande");
                System.out.println("wait");
                System.out.println("Disconnect command");
                thread_wait_for_cmd = new session_vote.Thread_wait_for_cmd();
                thread_wait_for_cmd.start();
                btn_disconnect.arm();
            }
            catch (Exception e) {
            e.printStackTrace();
        }
    }   
    
    //}
    
        @FXML
    public void reopen_session() {    
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de Réouverture");
            alert.setHeaderText("Vous êtes sur le point de déconnecter tous les télécommandes.");
            alert.setContentText("Voulez-vous continuer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){       
                tf_mac_telec.clear();
                System.out.println("wait");
                System.out.println("Disconnecting all command");
                participants = 0; 
                btn_connect.setDisable(false);
                btn_disconnect.setDisable(false);}
                try {
                    System.out.println("LENGTH" + adresses.size());
                    for (XBeeAddress64 adress : adresses){
                        switch_off_led(led_yellow, adress);
                    }
                } catch (Exception e){
                    System.out.println("UHU");
                    e.printStackTrace();
                }
                adresses.clear();
                thread_wait_for_cmd = new session_vote.Thread_wait_for_cmd();
                thread_wait_for_cmd.start();
                reopen_session.arm();
                
    }
    
    @FXML
    public void close_session() {  
            if(participants == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Impossible de valider sans telecommandes connectées");
            alert.showAndWait();
            alert.setHeaderText(null);
        }
            else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setHeaderText("Aucune télécommande ne pourrait être connectée ou déconnectée.");
            alert.setContentText("Voulez-vous continuer?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){ 
                    if ((thread_wait_for_cmd != null) && (thread_wait_for_cmd.isAlive())) {
                    close_session.arm();    
                    System.out.println("Validating sesion"); 
                    //close_session.arm();
                    thread_wait_for_cmd.interrupt();
                    btn_suivant.setDisable(false);
                    btn_connect.setDisable(true);
                    btn_disconnect.setDisable(true);
                    
                    }   
            }
    }
    }
    
    
    public Boolean test_if_tel_exists(String adress, ArrayList adresses) {
        if (adresses.size() > 0){
            adresses.add(adress);
            return true;
        } else {
                return false;
        }
    }
    
    
    public void getVoters() throws IOException{
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/jury/vote.fxml"));
    Parent root = (Parent) fxmlLoader.load();    
    Vote_Controller vote_controller = fxmlLoader.<Vote_Controller>getController();
    vote_controller.setVoters(participants);
    }
    
    /*public Integer insert_new_participation() {
    Integer part_id = participation_vote_dao.insert_participation( vote_id, date_participation);
    return part_id;
    }*/
    
        class Thread_wait_for_cmd extends Thread {

        @Override
        public void run() {
            try {
                btn_suivant.setDisable(true);
                while (true) {
                    xbee.clearResponseQueue();
                    XBeeResponse response = xbee.getResponse();
                    if (response.isError()) {
                        log.info("response contains errors", ((ErrorResponse) response).getException());
                        //continue;
                    }

                    if (response.getApiId() == ApiId.RX_64_IO_RESPONSE) {
                        RxResponseIoSample ioSample = (RxResponseIoSample) response;
                        if (ioSample.getRssi() > -50) {
                            session_vote.ProcessResponse processResponse = new session_vote.ProcessResponse(response);
                            processResponse.start();
                            //break;
                        }
                    }

                }
            } catch (Exception e) {
               // log.error(e);
            }
        }
    }
    
    
        public void switch_on_led(String led_id, XBeeAddress64 address_remote) throws XBeeException {

        RemoteAtRequest request_led_on = new RemoteAtRequest(address_remote, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_HIGH.getValue()});
        xbee.sendAsynchronous(request_led_on);
        try {
            Thread.sleep(500);

        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(session_vote.class.getName()).log(Level.SEVERE, null, ex);
        }
        // xbee.clearResponseQueue();
    }

    public void switch_off_led(String led_id, XBeeAddress64 address_remote) throws XBeeException {
        RemoteAtRequest request_led_off = new RemoteAtRequest(address_remote, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_LOW.getValue()});
        xbee.sendAsynchronous(request_led_off);
        try {
            Thread.sleep(250);

        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(session_vote.class.getName()).log(Level.SEVERE, null, ex);

        }
        // xbee.clearResponseQueue();
    }

        class ProcessResponse extends Thread {

        XBeeResponse response;
        private Integer vote_id;

        public ProcessResponse(XBeeResponse response) {
            this.response = response;
        }

        XBeeAddress64 adress_mac;

        public XBeeAddress64 getAdress_mac() {
            return adress_mac;
        }

        public void setAdress_mac(XBeeAddress64 adress_mac) {
            this.adress_mac = adress_mac;
        }
        
        
        @Override
        public void run() {
            
            RxResponseIoSample ioSample = (RxResponseIoSample) response;
            System.err.println("iosample ::: " + ioSample.toString());

            XBeeAddress64 address_remote = (XBeeAddress64) ioSample.getSourceAddress();

            System.out.println(address_remote.toString());

            this.adress_mac = address_remote;

            System.err.println("address64 : " + address_remote);
              
            if(btn_connect.isArmed()){    
                System.out.println("Connecting command");
                try {     
                    if (!adresses.contains(adress_mac)){
                        adresses.add(adress_mac);
                        
                    for (IoSample sample : ioSample.getSamples()) {
                        if (!ioSample.containsAnalog()) {
                            switch_on_led(led_yellow, address_remote);
                            participants++;
                            tf_mac_telec.setText("" + participants);
                            System.out.println(adresses);
                            getVoters();
                        }
                    }
                }
                    }
                    catch (XBeeException e) {
                    e.printStackTrace();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(session_vote.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            
            if(btn_disconnect.isArmed()){  
                try {     
                    if (adresses.contains(adress_mac)){
                        adresses.remove(adress_mac);
                    for (IoSample sample : ioSample.getSamples()) {
                        if (!ioSample.containsAnalog()) {
                            switch_off_led(led_yellow, address_remote);
                            System.out.println("Disconnecting command");
                            participants--;
                            tf_mac_telec.setText("" + participants);
                            Integer nb_quest = vote_dao.count_nb_quest(this.vote_id);
                            getVoters();
                            System.out.println(adresses);
                            //participation_vote_dao.delete_participation();

                        }
                    }
                }
                }
                    catch (XBeeException e) {
                    e.printStackTrace();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(session_vote.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  
        }    
    }
}

        
