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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import opisiame.model.Quiz;
import opisiame.dao.*;

/**
 * FXML Controller class
 *
 * @author zhuxiangyu
 */
public class Affichage_quizController implements Initializable {

    @FXML
    private AnchorPane content;

    @FXML
    private Label label_date;

    @FXML
    private Label label_timer;

    @FXML
    private Label label_nb_quest;

    @FXML
    private Text label_titre;

    @FXML
    private Button btn_modif;

    @FXML
    private Button btn_delete;

    public Button getBtn_delete() {
        return btn_delete;
    }

    public Button getBtn_modif() {
        return btn_modif;
    }

    private Integer quiz_id;

    Quiz_dao quiz_dao = new Quiz_dao();

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
        get_quiz_by_id();
    }

    public void get_quiz_by_id() {
        Quiz quiz = quiz_dao.get_quiz_by_id(this.quiz_id);
        label_titre.setText(quiz.getNom());
        label_date.setText(quiz.getDate_creation());
        label_timer.setText((quiz.getTimer()).toString());
        label_nb_quest.setText(String.valueOf(quiz_dao.count_nb_quest(this.quiz_id)));
    }

    @FXML
    public void voir_questions() throws IOException {
        Stage stage = (Stage) label_nb_quest.getScene().getWindow();
        if (Integer.valueOf(label_nb_quest.getText()) != 0) {
            open_question_detail(stage);
        } else {
            open_modal_new_question();
        }
        stage.close();

    }

    public void open_question_detail(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/affiche_question.fxml"));
        Parent root;
        try {
            root = (Parent) fxmlLoader.load();
            Affiche_questionController question_controller = fxmlLoader.<Affiche_questionController>getController();
            question_controller.setQuiz_id(this.quiz_id);

            Stage stage_affiche = new Stage();
            stage_affiche.initModality(Modality.APPLICATION_MODAL);
            stage_affiche.setTitle("Détail quiz");
            stage_affiche.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage_affiche.setScene(scene);
            stage_affiche.initOwner(stage.getOwner());
            stage_affiche.show();
        } catch (IOException ex) {
            Logger.getLogger(Affichage_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void open_modal_new_question() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/popup_no_question.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Popup_no_questionController popup_controller = fxmlLoader.<Popup_no_questionController>getController();
            popup_controller.setQuiz_id(this.quiz_id);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajout question");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
