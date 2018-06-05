/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.com;

import com.rapplogic.xbee.XBeePin;
import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.ErrorResponse;
import com.rapplogic.xbee.api.RemoteAtRequest;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeAddress64;
import com.rapplogic.xbee.api.XBeeException;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.wpan.IoSample;
import com.rapplogic.xbee.api.wpan.RxResponseIoSample;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import opisiame.controller.gestion_eleve.Link_eleve_teleController;
import opisiame.controller.gestion_jury.Vote_Controller;
import opisiame.dao.Reponse_jury_dao;
import opisiame.model.Eleve;
import opisiame.model.Vote;

/**
 *
 * @author Sandratra
 */
public class ListenToRemoteVotingThread extends Thread {

    private XBee xbee;
    
    private Integer idPoll;

    List<XBeeAddress64> remotes_responded;

    String num_port;

    List<Eleve> eleves;
    
    List<Vote> Vote;
    
    private boolean stop_thread;
    
    Integer votesmade = 0; 

    private Boolean running = true;

    private String rep_id_a;
    private String rep_id_b;
    private String rep_id_c;
    private String rep_id_d;
    
    
    @FXML
    private Label no_votants;
    
    private Vote_Controller vote_controller;

    String led_yellow = "D7";
    String led_green = "D5";
    String led_red = "D4";

    Reponse_jury_dao reponse_jury_dao = new Reponse_jury_dao();
    
    ArrayList<XBeeAddress64> voted = new ArrayList<>();
   

    public ListenToRemoteVotingThread(XBee xbee, List<Vote> vote, Integer idPoll, Vote_Controller vote_controller) {
        this.xbee = xbee;
        this.Vote = vote;
        this.idPoll = idPoll;
        this.vote_controller = vote_controller;
   
        running = true;
        remotes_responded = new ArrayList<>();
        remotes_responded.clear();
    }
    


    public void setRep_id_a(String rep_id_a) {
        this.rep_id_a = rep_id_a;
    }

    public void setRep_id_b(String rep_id_b) {
        this.rep_id_b = rep_id_b;
    }

    public void setRep_id_c(String rep_id_c) {
        this.rep_id_c = rep_id_c;
    }

    public void setRep_id_d(String rep_id_d) {
        this.rep_id_d = rep_id_d;
    }

    public ListenToRemoteVotingThread(XBee xBee, String num_port, List<Eleve> Eleves ){
        this.num_port = num_port;
        this.xbee = xBee;
        this.eleves = Eleves;
        System.out.println("opisiame.controller.com.ListenToRemoteVotingThread.<init>() are you even being created");
        //this.votes = Votes;
        running = true;
        remotes_responded = new ArrayList<>();
        remotes_responded.clear();
    }

    public Boolean test_if_is_in_list(String adr_mac) {
        for (int i = 0; i < remotes_responded.size(); i++) {
            if (remotes_responded.get(i).toString().equals(adr_mac)) {
                return true;
            }
        }
        return false;
    }
    
    public Integer getVotesMade(){
        return votesmade;
    }
    
    public void setVotesMade(int votesmade){
        this.votesmade = votesmade;
    }
    
    

    @Override
    public void run() {
        System.out.println("opisiame.controller.com.ListenToRemoteVotingThread.run() deus sidj");
        while (running) {
            try {
                while (true) {
                    xbee.clearResponseQueue();
                    XBeeResponse response = xbee.getResponse();
                    if (response.isError()) {
                        ErrorResponse errorResponse = (ErrorResponse) response;
                        System.err.println(errorResponse.getException());
                        continue;
                    }
                    System.out.println("opisiame.controller.com.ListenToRemoteVotingThread.run() maria");
                    ProcessResponse processResponse = new ProcessResponse(response);
                    System.out.println("opisiame.controller.com.ListenToRemoteVotingThread.run() jesus");
                    processResponse.start();
                    System.out.println("opisiame.controller.com.ListenToRemoteVotingThread.run() espirito santo");
                }
            } catch (Exception e) {
                //e.printStackTrace();
            } finally {
//                    xbee.close();
            }
        }
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    public void switch_on_led(String led_id, XBeeAddress64 address_remote) throws XBeeException {

        RemoteAtRequest request_led_on = new RemoteAtRequest(address_remote, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_HIGH.getValue()});
        xbee.sendAsynchronous(request_led_on);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Link_eleve_teleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void switch_off_led(String led_id, XBeeAddress64 address_remote) throws XBeeException {
        RemoteAtRequest request_led_off = new RemoteAtRequest(address_remote, led_id, new int[]{XBeePin.Capability.DIGITAL_OUTPUT_LOW.getValue()});
        xbee.sendAsynchronous(request_led_off);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Link_eleve_teleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class ProcessResponse extends Thread {

        XBeeResponse response;

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

        public Integer get_part_id(String adr_max) {
            for (int i = 0; i < eleves.size(); i++) {
                if (eleves.get(i).getAdresse_mac_tel() != null) {
                    if (eleves.get(i).getAdresse_mac_tel().equals(adr_max)) {
                        return eleves.get(i).getPart_id();
                    }
                }
            }
           
            return null;
        }
        

        @Override
        public void run() {
            System.out.println("opisiame.controller.com.ListenToRemoteVotingThread.ProcessResponse.run() IT SHOULD GET HERE");
            if (response.getApiId() == ApiId.RX_64_IO_RESPONSE) {
                RxResponseIoSample ioSample = (RxResponseIoSample) response;
                XBeeAddress64 address_remote = (XBeeAddress64) ioSample.getSourceAddress();
                System.out.println("MUITA GRACA " + address_remote);
                if (true) {
                    this.adress_mac = address_remote;
                    System.out.println("opisiame.controller.com.ListenToRemoteVotingThread.ProcessResponse.run() DEUS EH TOP");
                    if (!test_if_is_in_list(address_remote.toString())) {
                        try {
                            if (!voted.contains(adress_mac)){
                            
                            for (IoSample sample : ioSample.getSamples()) {
                                if (!ioSample.containsAnalog()) {
                                    if (!sample.isD2On()) { // bouton : rouge  => b
                                        reponse_jury_dao.insert_rep_jury(rep_id_b, idPoll, address_remote.toString());
                                        switch_off_led(led_green, address_remote);
                                        switch_on_led(led_yellow, address_remote);
                                        remotes_responded.add(address_remote);
                                        votesmade++; 
                                        System.out.println("Clicked B");
                                        vote_controller.setVotesMade(votesmade);
                                        System.out.println("votes" +votesmade);
                                        
                                    }
                                    if (!sample.isD1On()) { // bouton blanc  => c
                                        reponse_jury_dao.insert_rep_jury(rep_id_c, idPoll, address_remote.toString());
                                        switch_off_led(led_green, address_remote);
                                        switch_on_led(led_yellow, address_remote);
                                        remotes_responded.add(address_remote);
                                        votesmade++; 
                                        System.out.println("Clicked C");
                                        vote_controller.setVotesMade(votesmade);
                                        System.out.println("votes" +votesmade);
                                    }
                                    if (!sample.isD0On()) { // bouton vert => a
                                        reponse_jury_dao.insert_rep_jury(rep_id_a, idPoll, address_remote.toString());
                                        switch_off_led(led_green, address_remote);
                                        switch_on_led(led_yellow, address_remote);
                                        remotes_responded.add(address_remote);
                                        votesmade++; 
                                        System.out.println("Clicked A");
                                        vote_controller.setVotesMade(votesmade);
                                        System.out.println("votes" +votesmade);
                                    }
                                    if (!sample.isD3On()) { // bouton bleu  => d
                                        reponse_jury_dao.insert_rep_jury(rep_id_d, idPoll, address_remote.toString());
                                        switch_off_led(led_green, address_remote);
                                        switch_on_led(led_yellow, address_remote);
                                        remotes_responded.add(address_remote);
                                        votesmade++; 
                                        System.out.println("Clicked D");
                                        vote_controller.setVotesMade(votesmade);
                                        System.out.println("votes" +votesmade);
                                    }
                                }
                            }
                            }
                        } catch (XBeeException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }

}
