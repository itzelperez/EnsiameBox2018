/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_resultat;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import opisiame.dao.*;
import opisiame.database.Connection_db;
import opisiame.model.Eleve;
import opisiame.model.Question;
import opisiame.model.Reponse;
import opisiame.model.Reponse_eleve_quiz;
import opisiame.model.Resultat_eleve_comp;

/**
 * FXML Controller class
 *
 * @author clement
 */
public class Resultat_par_eleveController implements Initializable {

    Resultat_dao resultat_dao = new Resultat_dao();
    Question_dao question_dao = new Question_dao();
    Reponse_dao reponse_dao = new Reponse_dao();
    Sous_comp_dao sous_comp_dao = new Sous_comp_dao();

    @FXML
    private GridPane content;
    @FXML
    private Label label_note;
    @FXML
    private ComboBox CB_eleves;
    @FXML
    private TableView<Resultat_eleve_comp> tab_comp;
    @FXML
    private TableView<Reponse_eleve_quiz> tab_question;
    @FXML
    private TableColumn<Reponse_eleve_quiz, String> question;
    @FXML
    private TableColumn<Reponse_eleve_quiz, String> c_bonne_r;
    @FXML
    private TableColumn<Reponse_eleve_quiz, String> c_r_eleve;
    @FXML
    private TableColumn<Resultat_eleve_comp, String> c_comp;
    @FXML
    private TableColumn<Resultat_eleve_comp, Integer> c_pourcent;
    @FXML
    private RadioButton rb_un_eleve;
    @FXML
    private RadioButton rb_tous_eleves;

    int quiz_id;
    int participation_id;
    int nbre_questions;
    int nbre_rep_eleve;
    int nbre_bonnes_rep;
    float note = 0;
    String date_quiz;
    String nom_quiz;
    private ObservableList<Eleve> liste_eleves = FXCollections.observableArrayList();
    private ArrayList<Question> liste_questions = new ArrayList<>();
    private ArrayList<Reponse> liste_reponses = new ArrayList<>();
    private ArrayList<Reponse> liste_reponses_eleve = new ArrayList<>();
    private ObservableList<Reponse_eleve_quiz> a_afficher_1 = FXCollections.observableArrayList();
    private ObservableList<Resultat_eleve_comp> a_afficher_2 = FXCollections.observableArrayList();

    public void setId(int id) {
        quiz_id = id;
        rb_un_eleve.selectedProperty().set(true);
    }

    public void setDate(String d) {
        date_quiz = d;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        nbre_bonnes_rep = 0;

        question.setCellValueFactory(new PropertyValueFactory<Reponse_eleve_quiz, String>("lib_ques"));
        c_bonne_r.setCellValueFactory(new PropertyValueFactory<Reponse_eleve_quiz, String>("rep_quiz"));
        c_r_eleve.setCellValueFactory(new PropertyValueFactory<Reponse_eleve_quiz, String>("rep_eleve"));

        c_comp.setCellValueFactory(new PropertyValueFactory<Resultat_eleve_comp, String>("nom_comp"));
        c_pourcent.setCellValueFactory(new PropertyValueFactory<Resultat_eleve_comp, Integer>("pourcentage"));

        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps;
            ps = connection.prepareStatement("SELECT Quiz_nom "
                    + "FROM quiz \n"
                    + "WHERE Quiz_id LIKE ?\n");
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nom_quiz = rs.getString(1);
            };

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        liste_eleves = resultat_dao.get_participants_quiz(quiz_id, date_quiz);

        //remplissage de la combobox avec le nom des élèves
        int taille = liste_eleves.size();
        String NomPrenom = "";
        for (int i = 0; i < taille; i++) {
            NomPrenom = liste_eleves.get(i).getId().toString();
            //CB_eleves.getItems().add(NomPrenom);
            CB_eleves.getItems().add(liste_eleves.get(i));
        }
        CB_eleves.getSelectionModel().selectFirst();
        
        rb_tous_eleves.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rb_un_eleve.setSelected(false);
                res_par_question();
            }
        });
    }
    
    public void res_par_question() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_resultat/resultat_questions.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Rep_questionController rep_questionController = fxmlLoader.<Rep_questionController>getController();
            rep_questionController.setQuiz_selected_id(this.quiz_id);
            rep_questionController.setDate_participation_str(this.date_quiz);
            Stage stage = new Stage();
            stage.setTitle("Résultats");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
            
            Stage st = (Stage) rb_tous_eleves.getScene().getWindow();
            st.close();


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void BtnValider() throws IOException {

        //réinitialisation des différents éléments
        tab_comp.getItems().clear();
        tab_question.getItems().clear();
        a_afficher_1.clear();
        a_afficher_2.clear();
        nbre_bonnes_rep = 0;
        nbre_questions = 0;
        nbre_rep_eleve = 0;
        note = 0;
        label_note.setText("Note : ");

        Reponses();
        tab_question.setItems(a_afficher_1); //on remplit le premier tableau
        tab_comp.setItems(a_afficher_2);//on remplit le deuxieme tableau

        //affichage de la note de l'élève
        note = (float) ((nbre_bonnes_rep / (float) nbre_questions) * 20.0);
        label_note.setText("Note : " + note + " / 20");
    }

    public void Reponses() {

        //récupération du numéro éudiant (part_id), et du participation_id
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps;
            ps = connection.prepareStatement("SELECT Participation_id "
                    + "FROM participant_quiz \n"
                    + "WHERE Date_participation LIKE ? AND Part_id LIKE ?\n");
            //+ "WHERE Part_id LIKE ?\n");
            ps.setString(1, date_quiz.substring(0, date_quiz.length() - 2));
            //ps.setInt(2, Integer.parseInt(CB_eleves.getSelectionModel().getSelectedItem().toString()));
            Eleve eleve_selected = (Eleve) CB_eleves.getSelectionModel().getSelectedItem();
            ps.setInt(2, eleve_selected.getId());
            ResultSet rs = ps.executeQuery();
            rs.next();
            participation_id = rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        /*
        **
        **REMPLISSAGE DES TABLEAUX
        **
        **/
        //récupérer les questions / réponses / réponses de l'élève correspondant au quiz
        // 1 - récupérer les questions du quiz
        liste_questions.clear();
        liste_questions = question_dao.get_questions_by_quiz(quiz_id);
        nbre_questions = liste_questions.size();

        //récupération des réponses de l'élève au quiz
        liste_reponses_eleve.clear();
        liste_reponses_eleve = reponse_dao.get_reponses_eleve(participation_id);
        nbre_rep_eleve = liste_reponses_eleve.size();

        //récupération de la liste des compétences du quiz  
        for (int k = 0; k < liste_questions.size(); k++) {
            String n = resultat_dao.get_competence(liste_questions.get(k).getSous_comp_id()); //nom de la competence
            Resultat_eleve_comp r = new Resultat_eleve_comp(n, 0, 0);
            //ajouter la comp si elle n'appartient pas encore à la liste
            boolean check = list_contains(a_afficher_2, r);
            if (!check) {
                a_afficher_2.add(r);
            }
        }

        //récup de la bonne réponse et de la réponse de l'élève à la question (dans afficher)
        for (int i = 0; i < nbre_questions; i++) {

            Reponse_eleve_quiz afficher = new Reponse_eleve_quiz();
            afficher.setNum_ques(Integer.valueOf(i + 1));
            afficher.setLib_ques(liste_questions.get(i).getLibelle());

            //récupération de l'indice de la compétence correspondante dans la liste a_afficher_2
            int indice = 0;
            String comp = sous_comp_dao.get_comp(liste_questions.get(i).getSous_comp_id());
            while (indice < a_afficher_2.size() && !a_afficher_2.get(indice).getNom_comp().equals(comp)) {
                indice++;
            }
            a_afficher_2.get(indice).setNbre_questions(a_afficher_2.get(indice).getNbre_questions().intValue() + 1);

            //récupération de toutes les réponses des questions du quiz
            char nom_question = 'A';
            int ind_question = liste_questions.get(i).getId();
            liste_reponses = reponse_dao.get_reponses_by_quest(ind_question);

            //initialisation
            afficher.setRep_eleve("");

            //recherche de la bonne reponse
            for (int j = 0; j < 4; j++) {

                if (liste_reponses.get(j).getIs_bonne_reponse() == 1) {
                    afficher.setRep_quiz(Character.toString(nom_question)); //si cette réponse est la bonne, on garde son libelle (A, B, C ou D)
                }

                //recherche de la réponse de l'élève
                for (int g = 0; g < nbre_rep_eleve; g++) {
                    if (liste_reponses.get(j).getId().equals(liste_reponses_eleve.get(g).getId())) {
                        afficher.setRep_eleve(Character.toString(nom_question)); //si l'élève a choisi cette reponse, on garde son libelle (A, B, C ou D)e
                    }
                }

                nom_question++; //passage à la réponse (A, B, C, D) suivante
            }

            //si l'élève n'a pas répondu à la question, on l'indique
            if (afficher.getRep_eleve().equals("")) {
                afficher.setRep_eleve("-");
            }

            //si l'élève a bien repondu, on lui ajoute un point
            if (afficher.getRep_eleve().equals(afficher.getRep_quiz())) {
                nbre_bonnes_rep = nbre_bonnes_rep + 1;
                a_afficher_2.get(indice).setPourcentage((float) (a_afficher_2.get(indice).getPourcentage().intValue() + 1));
            }

            a_afficher_1.add(afficher);
        }

        //calcul de pourcentage de réussite à la compétence
        for (int i = 0; i < a_afficher_2.size(); i++) {
            float p = a_afficher_2.get(i).getPourcentage().floatValue() / a_afficher_2.get(i).getNbre_questions().floatValue();
            a_afficher_2.get(i).setPourcentage(p * 100);
        }

    }

    @FXML
    public void ClicBoutonRetour() throws IOException {
        Stage stage = (Stage) content.getScene().getWindow();
        stage.close();
    }

    public boolean list_contains(ObservableList<Resultat_eleve_comp> L, Resultat_eleve_comp o) {
        boolean check = false;
        String comp = o.getNom_comp();
        for (int i = 0; i < L.size(); i++) {
            if (L.get(i).getNom_comp().equals(comp)) {
                check = true;
            } else {
                check = false;
            }
        }
        return check;
    }

}
