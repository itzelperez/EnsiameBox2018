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
import opisiame.controller.gestion_eleve.Link_eleve_teleController;
import opisiame.model.Quiz;
import opisiame.dao.*;

/**
 * FXML Controller class
 *
 * @author zhuxiangyu
 */
public class Lancer_quizController implements Initializable {

    @FXML
    private AnchorPane content;

    @FXML
    private Label label_date;

    @FXML
    private Button btn_demarrer;

    @FXML
    private Label label_timer;

    @FXML
    private Label label_nb_quest;

    @FXML
    private Text label_titre;

    private Integer quiz_timer;
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
        int nb_quest = quiz_dao.count_nb_quest(this.quiz_id);
        label_nb_quest.setText(String.valueOf(nb_quest));
        if (nb_quest < 1) {
            btn_demarrer.setDisable(true);
        }
    }

    @FXML
    public void demarrer() throws IOException {
        Stage stage_parent = (Stage) label_nb_quest.getScene().getWindow();
        try {
            //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/lancer_question.fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_eleve/Link_eleve_tele.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Link_eleve_teleController link_eleve_tele_controller = fxmlLoader.<Link_eleve_teleController>getController();
            link_eleve_tele_controller.setQuiz_timer(Integer.valueOf(label_timer.getText()));
            link_eleve_tele_controller.setQuiz_id(quiz_id);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Animation quiz");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage_parent.close();

    }

    @FXML
    public void annuler() throws IOException {
        Stage stage = (Stage) label_nb_quest.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
