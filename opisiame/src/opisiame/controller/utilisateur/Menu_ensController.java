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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import opisiame.controller.competence.CompetencesController;

/**
 * FXML Controller class
 *
 * @author Audrey
 */
public class Menu_ensController implements Initializable {

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
    public void ClicBoutonNouveauQuiz() throws IOException {
        //ouvre la fenêtre liste_eleves_admin
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/gestion_quiz/nouveau_quiz.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Ajout quiz");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initOwner(content.getScene().getWindow());
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void ClicBoutonRésultatsQuiz() throws IOException {
        //ouvre la fenêtre liste_profs_admin
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/gestion_resultat/ResultatsQuiz.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    public void ClicBoutonListeQuiz() throws IOException {
        //ouvre la fenêtre liste_quiz (admin)
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/gestion_quiz/liste_quiz.fxml"));
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
