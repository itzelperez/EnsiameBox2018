/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_quiz;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sandratra
 */
public class Popup_no_questionController implements Initializable {

    Integer quiz_id;

    @FXML
    AnchorPane content;

    public Integer getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
    }

    public Popup_no_questionController() {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void add_question() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/add_question.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Add_questionController add_question_controller = fxmlLoader.<Add_questionController>getController();
            add_question_controller.setQuiz_id(this.quiz_id);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajout question");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();

            Stage st = (Stage) content.getScene().getWindow();
            st.close();

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void Annuler() {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }
}
