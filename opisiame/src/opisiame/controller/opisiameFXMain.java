/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller;

import java.io.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Sandratra programme principal
 */
public class opisiameFXMain extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/interface_authentification.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/competence/competences.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/gestion_quiz/liste_quiz.fxml"));

        stage.setTitle("OPI'SIAME");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
