/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_quiz;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;

/**
 *
 * @author Sandratra
 */
public class Timer_update_label extends Thread {

    private volatile boolean running = true;

    public void arreter() {
        this.running = false;
    }
    private Button btn_next_question;

    private Integer duree;
    
    private Boolean etat;

    public Timer_update_label(Button btn_next_question, Integer duree) {
        this.btn_next_question = btn_next_question;
        this.duree = duree;
        etat = false;
    }

    public Boolean getEtat() {
        return etat;
    }
    
    @Override
    public void run() {
        while (duree > 0) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    duree--;
                    btn_next_question.setText(duree.toString());
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Timer_update_label.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
