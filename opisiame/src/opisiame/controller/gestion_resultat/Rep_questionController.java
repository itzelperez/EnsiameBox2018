/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_resultat;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import opisiame.controller.gestion_quiz.Liste_quizController;
import opisiame.dao.Participation_quiz_dao;
import opisiame.dao.Question_dao;
import opisiame.dao.Quiz_dao;
import opisiame.dao.Reponse_dao;
import opisiame.dao.Reponse_question_dao;
import opisiame.dao.Sous_comp_dao;
import opisiame.model.Participant;
import opisiame.model.Participation_quiz;
import opisiame.model.Question;
import opisiame.model.Quiz;
import opisiame.model.Rep_eleves_quiz;
import opisiame.model.Reponse;
import opisiame.model.Reponse_eleve_quiz;
import opisiame.model.Reponse_question;

/**
 * FXML Controller class
 *
 * @author Sandratra
 */
public class Rep_questionController implements Initializable {

    @FXML
    private TabPane onglets = new TabPane();

    @FXML
    private Tab tab_res_eleves_num = new Tab();
    @FXML
    private TableColumn<Rep_eleves_quiz, String> c_nom1;
    @FXML
    private TableColumn<Rep_eleves_quiz, String> c_prenom1;
    @FXML
    private TableColumn<Rep_eleves_quiz, Integer> c_num1;
    @FXML
    private TableColumn<Rep_eleves_quiz, Double> c_note1;
    @FXML
    private TableColumn<Rep_eleves_quiz, Double> c_pourcent1;

    @FXML
    private Tab tab_res_eleves_pas_num = new Tab();
    @FXML
    private TableColumn<Rep_eleves_quiz, String> c_nom2;
    @FXML
    private TableColumn<Rep_eleves_quiz, String> c_prenom2;
    @FXML
    private TableColumn<Rep_eleves_quiz, Double> c_note2;
    @FXML
    private TableColumn<Rep_eleves_quiz, Double> c_pourcent2;

    @FXML
    private Tab tab_liste_questions = new Tab();
    @FXML
    private TableColumn<Reponse_question, Integer> pourcentage;
    @FXML
    private TableColumn<Reponse_question, String> rep_a;
    @FXML
    private TableColumn<Reponse_question, String> rep_b;
    @FXML
    private TableColumn<Reponse_question, String> rep_c;
    @FXML
    private TableColumn<Reponse_question, String> rep_d;
    @FXML
    private TableColumn<Reponse_question, String> question;

    @FXML
    private TableView<Rep_eleves_quiz> t_liste_res_eleves_num;

    @FXML
    private TableView<Rep_eleves_quiz> t_liste_res_eleves_pas_num;

    @FXML
    private TableView<Reponse_question> t_liste_rep;
        
    @FXML
    private RadioButton rb_un_eleve;
    @FXML
    private RadioButton rb_tous_eleves;

//    @FXML
//    private ComboBox select_quiz;
    @FXML
    private Button btn_export;

//    @FXML
//    private ComboBox date_select_quiz;
    private ObservableList<Quiz> liste_quiz;
    private ObservableList<Participation_quiz> participation_quizs;
    private ObservableList<Reponse_question> liste_reponses_question;

    private ObservableList<Participant> liste_participants = FXCollections.observableArrayList();
    private ObservableList<Rep_eleves_quiz> liste_resultats_eleves = FXCollections.observableArrayList();
    private ArrayList<Question> liste_questions = new ArrayList<>();
    private ArrayList<Reponse> liste_reponses = new ArrayList<>();
    private ArrayList<Reponse> liste_reponses_eleve = new ArrayList<>();
    private ObservableList<Rep_eleves_quiz> a_afficher_1 = FXCollections.observableArrayList();

    Integer quiz_selected_id;

    public Integer getQuiz_selected_id() {
        return quiz_selected_id;
    }

    public void setQuiz_selected_id(Integer quiz_selected_id) {
        this.quiz_selected_id = quiz_selected_id;
        rb_tous_eleves.setSelected(true);
    }

    Participation_quiz participation_quiz;
    String date_participation;

    public String getDate_participation() {
        return date_participation;
    }

    public void setDate_participation_str(String date_participation) {
        this.date_participation = date_participation;
        Participation_quiz pq = participation_quiz_dao.get_part_quiz(date_participation, quiz_selected_id, participation_quizs);
        setParticipation_quiz(pq);
        btn_export.setDisable(false);
    }

    //dao utilisés
    Quiz_dao quiz_dao = new Quiz_dao();
    Participation_quiz_dao participation_quiz_dao = new Participation_quiz_dao();
    Question_dao question_dao = new Question_dao();
    Reponse_question_dao reponse_question_dao = new Reponse_question_dao();
    Reponse_dao reponse_dao = new Reponse_dao();
    Sous_comp_dao sous_comp_dao = new Sous_comp_dao();

    public void setParticipation_quiz(Participation_quiz participation_quiz) {
        this.participation_quiz = participation_quiz;
        show_result();
    }

    public void show_result() {
        liste_reponses_question = FXCollections.observableArrayList();
        liste_reponses_question.clear();
        List<Question> questions = question_dao.get_questions_by_quiz(quiz_selected_id);
        for (Question quest : questions) {
            Reponse_question rq = reponse_question_dao.get_res_by_quest(quest.getId(), participation_quiz);
            rq.setQuestion(quest.getLibelle());
            liste_reponses_question.add(rq);
        }
        t_liste_rep.setItems(liste_reponses_question);

        liste_resultats_eleves = FXCollections.observableArrayList();
        liste_resultats_eleves.clear();
        liste_resultats_eleves = remplissage_2e_tab();
        t_liste_res_eleves_num.setItems(liste_resultats_eleves);
        t_liste_res_eleves_pas_num.setItems(liste_resultats_eleves);
    }

    @FXML
    public void export_result() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_resultat/choix_export.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Choix_exportController choix_exportController = fxmlLoader.<Choix_exportController>getController();

            if (tab_liste_questions.isSelected()) {
                choix_exportController.setOngletActif("questions");
            } else if (tab_res_eleves_num.isSelected()) {
                choix_exportController.setOngletActif("eleves");
            }else if (tab_res_eleves_pas_num.isSelected()) {
                choix_exportController.setOngletActif("eleves_pas_num");
            }

            choix_exportController.setReponse_questions(liste_reponses_question);
            choix_exportController.setResultatsEleves(liste_resultats_eleves);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Selection du format du fichier d'export des résultats");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //    stage.initOwner(select_quiz.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        participation_quizs = participation_quiz_dao.get_participation_quizs(session.Session.getUser_id());
        liste_quiz = participation_quiz_dao.get_quizs(participation_quizs);

        question.setCellValueFactory(new PropertyValueFactory<Reponse_question, String>("question"));
        rep_a.setCellValueFactory(new PropertyValueFactory<Reponse_question, String>("str_pourcentage_rep_a"));
        rep_b.setCellValueFactory(new PropertyValueFactory<Reponse_question, String>("str_pourcentage_rep_b"));
        rep_c.setCellValueFactory(new PropertyValueFactory<Reponse_question, String>("str_pourcentage_rep_c"));
        rep_d.setCellValueFactory(new PropertyValueFactory<Reponse_question, String>("str_pourcentage_rep_d"));
        pourcentage.setCellValueFactory(new PropertyValueFactory<Reponse_question, Integer>("str_pourcentage"));

        c_num1.setCellValueFactory(new PropertyValueFactory<Rep_eleves_quiz, Integer>("num_eleve"));
        c_note1.setCellValueFactory(new PropertyValueFactory<Rep_eleves_quiz, Double>("note_eleve"));
        c_pourcent1.setCellValueFactory(new PropertyValueFactory<Rep_eleves_quiz, Double>("Pourcent_eleve"));
        c_prenom1.setCellValueFactory(new PropertyValueFactory<Rep_eleves_quiz, String>("prenom_eleve"));
        c_nom1.setCellValueFactory(new PropertyValueFactory<Rep_eleves_quiz, String>("nom_eleve"));

        c_note2.setCellValueFactory(new PropertyValueFactory<Rep_eleves_quiz, Double>("note_eleve"));
        c_pourcent2.setCellValueFactory(new PropertyValueFactory<Rep_eleves_quiz, Double>("Pourcent_eleve"));
        c_prenom2.setCellValueFactory(new PropertyValueFactory<Rep_eleves_quiz, String>("prenom_eleve"));
        c_nom2.setCellValueFactory(new PropertyValueFactory<Rep_eleves_quiz, String>("nom_eleve"));

        rb_un_eleve.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rb_tous_eleves.setSelected(false);
                res_par_eleve();
            }
        });
    }
    
    public void res_par_eleve() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_resultat/Resultat_par_eleve.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Resultat_par_eleveController resultat_par_eleveController = fxmlLoader.<Resultat_par_eleveController>getController();
            resultat_par_eleveController.setId(this.quiz_selected_id);
            resultat_par_eleveController.setDate(this.date_participation);
            
            URL url = fxmlLoader.getLocation();
            ResourceBundle rb = fxmlLoader.getResources();
            resultat_par_eleveController.initialize(url, rb);
            
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
    public void ClicBoutonRetour() throws IOException {
        Stage stage = (Stage) btn_export.getScene().getWindow();
        stage.close();
    }

    //remplissage 2eme onglet
    @FXML
    public ObservableList<Rep_eleves_quiz> remplissage_2e_tab() {
        liste_participants.clear();
        liste_participants.setAll(participation_quiz_dao.get_participants(quiz_selected_id, participation_quiz.getDate_participation()));
        a_afficher_1.clear();

        //infos sur le quiz
        int nbre_questions = 0;
        liste_questions = question_dao.get_questions_by_quiz(quiz_selected_id);
        nbre_questions = liste_questions.size();

        //pour chaque etudiant ayant participe au quiz
        for (int i = 0; i < liste_participants.size(); i++) {

            //initialisation
            liste_reponses.clear();
            liste_reponses_eleve.clear();
            int nbre_rep_eleve = 0;
            int nbre_bonnes_rep = 0;
            Double note = 0.;
            Double pourcenta = 0.;

            //creation de la ligne a afficher correspondant a un etudiant
            Rep_eleves_quiz a_afficher = new Rep_eleves_quiz();
            a_afficher.setNum_eleve(liste_participants.get(i).getPart_id()); //récup du numero d'etudiant
            a_afficher.setNom_eleve(liste_participants.get(i).getPart_nom());
            a_afficher.setPrenom_eleve(liste_participants.get(i).getPart_prenom());

            //recup des réponses de l'etudiant au quiz
            liste_reponses_eleve = reponse_dao.get_reponses_eleve(participation_quiz_dao.get_part_id(liste_participants.get(i).getPart_id(), quiz_selected_id, participation_quiz.getDate_participation()));
            nbre_rep_eleve = liste_reponses_eleve.size();

            //récup de la bonne réponse et de la réponse de l'élève à la question (dans afficher)
            for (int j = 0; j < nbre_questions; j++) {

                Reponse_eleve_quiz rep_eleve = new Reponse_eleve_quiz();
                rep_eleve.setNum_ques(Integer.valueOf(j + 1));

                //récupération de toutes les réponses à la question j du quiz
                char nom_question = 'A';
                int ind_question = liste_questions.get(j).getId();
                liste_reponses = reponse_dao.get_reponses_by_quest(ind_question);

                //initialisation
                rep_eleve.setRep_eleve("");

                //recherche de la bonne reponse
                for (int k = 0; k < 4; k++) {

                    //recherche de la réponse à la question
                    if (liste_reponses.get(k).getIs_bonne_reponse() == 1) {
                        rep_eleve.setRep_quiz(Character.toString(nom_question)); //si cette réponse est la bonne, on garde son libelle (A, B, C ou D)
                    }

                    //recherche de la réponse de l'élève
                    for (int g = 0; g < nbre_rep_eleve; g++) {
                        if (liste_reponses.get(k).getId().equals(liste_reponses_eleve.get(g).getId())) {
                            rep_eleve.setRep_eleve(Character.toString(nom_question)); //si l'élève a choisi cette reponse, on garde son libelle (A, B, C ou D)e
                        }
                    }
                    nom_question++; //passage à la réponse (A, B, C, D) suivante
                }

                //si l'élève n'a pas répondu à la question, on l'indique
                if (rep_eleve.getRep_eleve().equals("")) {
                    rep_eleve.setRep_eleve("-");
                }

                //si l'élève a bien repondu, on lui ajoute un point
                if (rep_eleve.getRep_eleve().equals(rep_eleve.getRep_quiz())) {
                    nbre_bonnes_rep = nbre_bonnes_rep + 1;
                }
            }

            note = (((float) nbre_bonnes_rep / nbre_questions) * 20.);
            pourcenta = (((float) nbre_bonnes_rep / nbre_questions) * 100.);
            a_afficher.setNote_eleve(note);
            a_afficher.setPourcent_eleve(pourcenta);

            a_afficher_1.add(a_afficher);
        }

        return a_afficher_1;

    }

}
