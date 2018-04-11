/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.utilisateur;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import opisiame.controller.competence.CompetencesController;
import opisiame.controller.gestion_jury.Menu_Mode_Jury;

/**
 *
 * @author Audrey
 */
public class menu_adminController implements Initializable {

    @FXML
    private AnchorPane content;

    @FXML
    public void ClicImageOnOff() throws IOException {
        //remise à zéro des variables d'identification (login + mdp)
        session.Session.Logout();
        //ouvre la fenêtre Interface_authentification
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/interface_authentification.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void ClicBoutonParam() throws IOException {
        //ouvre la fenêtre ModifParam
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/ModifParam.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void ClicBoutonEleves() throws IOException {
        //ouvre la fenêtre liste_eleves_admin
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/gestion_eleve/liste_eleves_admin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void ClicBoutonAnimateurs() throws IOException {
        //ouvre la fenêtre liste_profs_admin
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/animateur/liste_anim.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void ClicBoutonEnseignants() throws IOException {
        //ouvre la fenêtre liste_profs_admin
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/prof/liste_profs_admin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void ClicBoutonQuiz() throws IOException {
        //ouvre la fenêtre liste_quiz (admin)
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/gestion_quiz/liste_quiz.fxml")); //à modifier
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void ClicBoutonCompetences() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/competence/competences.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        CompetencesController comp_controller = fxmlLoader.<CompetencesController>getController();
        comp_controller.setAfficheRetour(1);
        //ouvre la fenêtre liste_quiz (admin)
        Stage stage = (Stage) content.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }
    
    
        @FXML
    public void ClicBoutonJury() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/jury/menu_mode_jury.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Menu_Mode_Jury jury_controller = fxmlLoader.<Menu_Mode_Jury>getController();
        jury_controller.setAfficheRetour(1);
        //ouvre la fenêtre liste_quiz (admin)
        Stage stage = (Stage) content.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
