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
 * @author Sandratra
 */
public class Edit_quizController implements Initializable {

    private Integer quiz_id;

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

    public void setQuiz_id(Integer quiz_id) {
        this.quiz_id = quiz_id;
        get_quiz_by_id();
    }

    public void get_quiz_by_id() {
        Quiz quiz = quiz_dao.get_quiz_by_id(this.quiz_id);
        nom_quiz.setText(quiz.getNom());
        if (quiz.getTimer() != 0) {
            chkb_timer.setSelected(true);
            timer.setDisable(false);
            timer.setText((quiz.getTimer()).toString());
        }
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
        if (champ_ok == true) {
            label_error_login.setVisible(false);
            label_error_timer.setVisible(false);
            label_number_timer_error.setVisible(false);
            update_quiz(nom, value_timer);
        }
    }

    @FXML
    public void btn_modif_question() {
        btn_valider();
        Integer nb_quest = quiz_dao.count_nb_quest(this.quiz_id);
        if (nb_quest != 0) {
            affiche_modif_question();
        } else {
            open_modal_new_question();
        }
    }

    public void affiche_modif_question() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/edit_question.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Edit_questionController edit_question_controller = fxmlLoader.<Edit_questionController>getController();
            edit_question_controller.setQuiz_id(quiz_id);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier question");
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

    private boolean validate_number(String str) {
        return str.matches("[0-9]*");
    }

    public void update_quiz(String value_nom, String value_timer) {
        quiz_dao.update_quiz(this.quiz_id, value_nom, value_timer);
        Stage stage = (Stage) nom_quiz.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btn_close() {
        Stage stage = (Stage) nom_quiz.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void add_question(){
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

            Stage st = (Stage) nom_quiz.getScene().getWindow();
            st.close();

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void manage_animateur(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/manage_animateur.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Manage_animateurController manage_animateurController = fxmlLoader.<Manage_animateurController>getController();
            manage_animateurController.setQuiz_id(this.quiz_id);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestion des animateurs du quiz");
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
