/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_quiz;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import opisiame.controller.gestion_eleve.Add_eleveController;
import opisiame.database.Connection_db;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import opisiame.dao.Competence_dao;
import opisiame.model.Competence;

/**
 *
 * @author Audrey
 */
public class ajout_questionController implements Initializable {

    @FXML
    private AnchorPane content;
    @FXML
    private TextArea enonce;
    @FXML
    private TextField sous_comp;
    @FXML
    private ComboBox combo_competence;
    @FXML
    private TextField timer;
    @FXML
    private TextField rep_a;
    @FXML
    private TextField rep_b;
    @FXML
    private TextField rep_c;
    @FXML
    private TextField rep_d;
//    @FXML
//    private Label label_nb_carac;
    
    Competence_dao competence_dao = new Competence_dao();

    private ObservableList<Competence> liste_Competence;//contient les champs "competence" pour le combobox

       
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        set_data_combo_competence();
    }
    
    
    private void set_data_combo_competence() {
        liste_Competence = competence_dao.get_all_competence();
        combo_competence.getItems().addAll(liste_Competence);
    }

    @FXML
    public void ajout_image() {

    }
    
    @FXML
    public void delete_image(){
        
    }

    @FXML
    public void ajout_question(){
        
    }
    
    @FXML
    public void terminer_quiz(){
        
    }

    
    //affiche le nombre de caractères disponibles pour l'énoncé de la question
//    @FXML
//    public void nb_carac_restant() throws IOException{
//        
//        int nb_carac_dispo = 255-(enonce.getLength());
//        
//        label_nb_carac.setText("( " + nb_carac_dispo + " caractère(s) restant(s) )");
//        
//        if (nb_carac_dispo<=0){
//            label_nb_carac.setTextFill(Color.web("#FF0000"));
//        }
//        else {
//            label_nb_carac.setTextFill(Color.web("#000000"));
//        }
//        
//    }


}
