/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_resultat;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import opisiame.dao.*;
import opisiame.model.Eleve;
import opisiame.model.Participant;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class Delete_resultController implements Initializable {
    
    @FXML
    AnchorPane content;

    int quiz_id;
    String date_part_quiz;
    Resultat_dao resultat_dao = new Resultat_dao();
    Participation_quiz_dao participation_quiz_dao = new Participation_quiz_dao();

    private List<Integer> participants = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //participants.setAll(Resultat_dao.get_participants_quiz(quiz_id, date_part_quiz));
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }

    public void setDatePart(String date_quiz) {
        this.date_part_quiz = date_quiz;
    }

    @FXML
    public void btn_confirm_action() {
        participants = (participation_quiz_dao.get_participation_id(quiz_id, date_part_quiz));
        for (int i = 0; i < participants.size(); i++) {
            resultat_dao.delete_resultat(participants.get(i));
            participation_quiz_dao.delete_participation(participants.get(i));
        }
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
        
    }

    @FXML
    public void btn_cancel_action() {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }
}
