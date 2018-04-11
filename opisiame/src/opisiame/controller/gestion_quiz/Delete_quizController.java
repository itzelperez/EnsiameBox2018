/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_quiz;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import opisiame.dao.*;

/**
 * FXML Controller class
 *
 * @author Sandratra
 */
public class Delete_quizController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Integer quiz_id;

    @FXML
    private AnchorPane content;

    Quiz_dao quiz_dao = new Quiz_dao();

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void btn_confirm_action() {
        quiz_dao.delete_quiz(quiz_id);
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btn_cancel_action() {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }
}
