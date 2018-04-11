/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_jury;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;
import opisiame.controller.competence.CompetencesController;
import session.Session;

/**
 *
 * @author itzel
 */
public class Menu_Mode_Jury implements Initializable {
    
    
    @FXML
    private AnchorPane content;
    
     private Integer affiche_retour; //pour savoir vers quelle page on retourne

     
    public void initialize(URL location, ResourceBundle resources) {

    } 
    
    public void setAfficheRetour(Integer c) {
        affiche_retour = c;
    }

    //Clic bouton de retour
    @FXML
    public void ClicBoutonRetour() throws IOException {

        Stage stage = (Stage) content.getScene().getWindow();
        if (affiche_retour != null) {

            if (affiche_retour == 1) {
                String type = Session.getType();
                if (type.equals("anim")) {
                    Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_ens.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.centerOnScreen();
                    stage.show();
                } else {
                    Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/menu_admin.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.centerOnScreen();
                    stage.show();
                }

            } else if (affiche_retour == 2) {
                stage.close();
            } else if (affiche_retour == 3) {
                stage.close();
            }

        }
    }
    
      @FXML
    public void btn_exit() throws IOException {
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
    public void btn_vote() throws IOException {
            
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/jury/session_jury.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            session_vote telecommande_controller = fxmlLoader.<session_vote>getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Conection Telecommande");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(CompetencesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

        
    }
    

    

