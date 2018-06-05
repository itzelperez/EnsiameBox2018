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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import opisiame.controller.gestion_eleve.Link_eleve_teleController;
import opisiame.dao.Reponse_participation_dao;
import opisiame.model.Eleve;
import opisiame.model.Vote;

/**
 *
 * @author Sandratra
 */
public class ListenToRemoteThread extends Thread {

    private XBee xbee;

    List<XBeeAddress64> remotes_responded;

    String num_port;

    List<Eleve> eleves;
    
    List<Vote> Vote;
    
    //List<Vote> votes;

    private Boolean running = true;

    private int rep_id_a;
    private int rep_id_b;
    private int rep_id_c;
    private int rep_id_d;

    String led_yellow = "D7";
    String led_green = "D5";
    String led_red = "D4";

    Reponse_participation_dao reponse_participation_dao = new Reponse_participation_dao();
   

    public ListenToRemoteThread(XBee xbee, List<Vote> vote) {
        this.xbee = xbee;
        this.Vote = vote;
   
        running = true;
        remotes_responded = new ArrayList<>();
        remotes_responded.clear();
    }
    


    public void setRep_id_a(int rep_id_a) {
        this.rep_id_a = rep_id_a;
    }

    public void setRep_id_b(int rep_id_b) {
        this.rep_id_b = rep_id_b;
    }

    public void setRep_id_c(int rep_id_c) {
        this.rep_id_c = rep_id_c;
    }

    public void setRep_id_d(int rep_id_d) {
        this.rep_id_d = rep_id_d;
    }

    public ListenToRemoteThread(XBee xBee, String num_port, List<Eleve> Eleves ){
        this.num_port = num_port;
        this.xbee = xBee;
        this.eleves = Eleves;
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

    @Override
    public void run() {
        while (running) {
            try {
                while (true) {
                    System.out.println("opisiame.controller.com.ListenToRemoteThread.run() "+ xbee);
                    xbee.clearResponseQueue();
                    XBeeResponse response = xbee.getResponse();
                    if (response.isError()) {
                        ErrorResponse errorResponse = (ErrorResponse) response;
                        System.err.println(errorResponse.getException());
                        continue;
                    }
                    ProcessResponse processResponse = new ProcessResponse(response);
                    processResponse.start();
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
           
           /* 
            for (int k = 0; k < votes.size(); k++) {
                if (votes.get(k).getAdresse_mac_tel() != null) {
                    if (votes.get(k).getAdresse_mac_tel().equals(adr_max)) {
                        return votes.get(k).getPart_id();
                    }
                }
            }*/
            return null;
        }

        /*
    DIO0 boutton vert
    DIO1 boutton gris
    DIO2 boutton rouge
    DIO3 boutton bleu
         */
        @Override
        public void run() {
            System.out.println("opisiame.controller.com.ListenToRemoteThread.ProcessResponse.run() when are you run");
            if (response.getApiId() == ApiId.RX_64_IO_RESPONSE) {
                RxResponseIoSample ioSample = (RxResponseIoSample) response;
                XBeeAddress64 address_remote = (XBeeAddress64) ioSample.getSourceAddress();
                Integer part_id = get_part_id(address_remote.toString());
                System.out.println("GOT HERE " + part_id);
                if (part_id != null) {
                    this.adress_mac = address_remote;
                    if (!test_if_is_in_list(address_remote.toString())) {
                        try {
                            for (IoSample sample : ioSample.getSamples()) {
                                if (!ioSample.containsAnalog()) {
                                    if (!sample.isD2On()) { // bouton : rouge  => b
                                        reponse_participation_dao.insert_rep_participation(rep_id_b, part_id);
                                        switch_off_led(led_green, address_remote);
                                        switch_on_led(led_yellow, address_remote);
                                        remotes_responded.add(address_remote);
                                        System.out.println("Clicked B");
                                        
                                    }
                                    if (!sample.isD1On()) { // bouton blanc  => c
                                        reponse_participation_dao.insert_rep_participation(rep_id_c, part_id);
                                        switch_off_led(led_green, address_remote);
                                        switch_on_led(led_yellow, address_remote);
                                        remotes_responded.add(address_remote);
                                        System.out.println("Clicked C");
                                    }
                                    if (!sample.isD0On()) { // bouton vert => a
                                        reponse_participation_dao.insert_rep_participation(rep_id_a, part_id);
                                        switch_off_led(led_green, address_remote);
                                        switch_on_led(led_yellow, address_remote);
                                        remotes_responded.add(address_remote);
                                        System.out.println("Clicked A");
                                    }
                                    if (!sample.isD3On()) { // bouton bleu  => d
                                        reponse_participation_dao.insert_rep_participation(rep_id_d, part_id);
                                        switch_off_led(led_green, address_remote);
                                        switch_on_led(led_yellow, address_remote);
                                        remotes_responded.add(address_remote);
                                        System.out.println("Clicked D");
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
