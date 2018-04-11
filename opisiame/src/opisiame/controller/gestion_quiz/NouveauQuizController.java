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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import opisiame.dao.Quiz_dao;
import opisiame.model.Quiz;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class NouveauQuizController implements Initializable {

    @FXML
    private TextField nom_quiz;

    @FXML
    private TextField timer;

    @FXML
    private CheckBox chkb_timer;

    @FXML
    private Label label_error_login;

    @FXML
    private Label label_error_timer;

    @FXML
    private Label label_number_timer_error;

    Quiz_dao quiz_dao = new Quiz_dao();

    private TableView<Quiz> t_liste_quiz;

    public TableView<Quiz> getT_liste_quiz() {
        return t_liste_quiz;
    }

    public void setT_liste_quiz(TableView<Quiz> t_liste_quiz) {
        this.t_liste_quiz = t_liste_quiz;
    }

    @FXML
    public void check_timer() {
        if (chkb_timer.isSelected()) {
            timer.setDisable(false);
        } else {
            timer.setDisable(true);
            label_error_timer.setVisible(false);
        }
    }

    @FXML
    public void nom_set_label_error_not_visible() {
        label_error_login.setVisible(false);
    }

    @FXML
    public void timer_set_label_error_not_visible() {
        label_error_timer.setVisible(false);
        label_number_timer_error.setVisible(false);
    }

    @FXML
    public void btn_valider() {
        //System.out.println(t_liste_quiz.getSelectionModel().getSelectedItem().getId());
        String nom = nom_quiz.getText();
        String value_timer = timer.getText();
        Boolean champ_ok = true;
        if (nom.compareTo("") == 0) {
            label_error_login.setVisible(true);
            champ_ok = false;
        }
        if ((chkb_timer.isSelected()) && (value_timer.compareTo("") == 0)) {
            label_error_timer.setVisible(true);
            champ_ok = false;
        }
        if (!validate_number(value_timer)) {
            label_number_timer_error.setVisible(true);
            champ_ok = false;
        }
        if (nom.length() > 60){
            champ_ok = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Nom du quiz trop long");
            alert.showAndWait();
        }
        if (champ_ok == true) {
            label_error_login.setVisible(false);
            label_error_timer.setVisible(false);
            label_number_timer_error.setVisible(false);
            insert_new_quiz(nom, value_timer);
        }
    }

    private boolean validate_number(String str) {
        return str.matches("[0-9]*");
    }

    public void insert_new_quiz(String value_nom, String value_timer) {
        Integer id = quiz_dao.insert_new_quiz(value_nom, value_timer);
        if (id != null) {
            Stage stage = (Stage) nom_quiz.getScene().getWindow();
            add_quiz_question(id);
            stage.close();
        }
    }

    public void add_quiz_question(Integer id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/add_question.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Add_questionController add_question_controller = fxmlLoader.<Add_questionController>getController();
            System.out.println("id dans addquiz:" + id);
            add_question_controller.setQuiz_id(id);
            System.out.println("opisiame.controller.gestion_quiz.NouveauQuizController.add_quiz_question()");

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

    @FXML
    public void btn_close() {
        Stage stage = (Stage) nom_quiz.getScene().getWindow();
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
