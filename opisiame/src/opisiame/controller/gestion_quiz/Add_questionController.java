/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_quiz;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import opisiame.controller.competence.Affichage_sous_competencesController;
import opisiame.controller.competence.CompetencesController;
import opisiame.dao.Competence_dao;
import opisiame.dao.Question_dao;
import opisiame.dao.Reponse_dao;
import opisiame.dao.Sous_comp_dao;
import opisiame.model.Competence;
import opisiame.model.Reponse;
import opisiame.model.Sous_competence;

/**
 * FXML Controller class
 *
 * @author Sandratra
 */
public class Add_questionController implements Initializable {

    private Integer Quiz_id;

    @FXML
    private TextArea enonce;

    @FXML
    private TextArea rep_a;

    @FXML
    private TextArea rep_b;

    @FXML
    private TextArea rep_c;

    @FXML
    private TextArea rep_d;

    @FXML
    private TextField timer;

    @FXML
    private ComboBox combo_sous_comp;

    @FXML
    private CheckBox checkbx_a;

    @FXML
    private CheckBox checkbx_b;

    @FXML
    private CheckBox checkbx_c;

    @FXML
    private CheckBox checkbx_d;

    @FXML
    private ComboBox combo_competence;

    @FXML
    private Label label_error;

    @FXML
    private Label label_ajout_ok;

    @FXML
    private Label error_label_timer;

    @FXML
    private ImageView img_view;

    @FXML
    private Label nb_carac_restant_a;

    @FXML
    private Label nb_carac_restant_b;

    @FXML
    private Label nb_carac_restant_c;

    @FXML
    private Label nb_carac_restant_d;

    private String libelle, rep_1, rep_2, rep_3, rep_4, sous_competence, url_img;
    private Integer timer_value;

    ArrayList<CheckBox> chkb;

    Competence_dao competence_dao = new Competence_dao();
    Reponse_dao reponse_dao = new Reponse_dao();
    Sous_comp_dao sous_comp_dao = new Sous_comp_dao();

    private ObservableList<Competence> liste_Competence;//contient les champs "competence" pour le combobox
    private ObservableList<Sous_competence> list_sous_comp;
    Integer res_carac = 255;

    public Add_questionController() {
        url_img = "";
    }

    Question_dao question_dao = new Question_dao();

    public Integer getQuiz_id() {
        return Quiz_id;
    }

    public void setQuiz_id(Integer Quiz_id) {
        this.Quiz_id = Quiz_id;
        System.out.println("setquizid: " + this.Quiz_id);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        set_data_combo_competence();
        chkb = new ArrayList(Arrays.asList(checkbx_a, checkbx_b, checkbx_c, checkbx_d));
    }

    public void reset_null() {
        enonce.setText("");
        rep_a.setText("");
        rep_b.setText("");
        rep_c.setText("");
        rep_d.setText("");
        timer.setText("");
        for (CheckBox checkBox : chkb) {
            checkBox.setSelected(false);
        }
        delete_image();
    }

    public Boolean check_bonne_rep() {
        for (CheckBox checkBox : chkb) {
            if (checkBox.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public Boolean check_form() {
        boolean b = false;
        libelle = enonce.getText();
        rep_1 = rep_a.getText();
        rep_2 = rep_b.getText();
        rep_3 = rep_c.getText();
        rep_4 = rep_d.getText();

        if ((libelle.compareTo("") != 0)
                && (rep_1.compareTo("") != 0)
                && (rep_2.compareTo("") != 0)
                && (rep_3.compareTo("") != 0)
                && (rep_4.compareTo("") != 0)
                && check_comp()) {
            b = true;
        }

        return b;
    }

    private Boolean check_timer() {
        timer_value = null;
        Boolean b = true;
        if (timer.getText().compareTo("") != 0) {
            if (validate_number(timer.getText())) {
                timer_value = Integer.valueOf(timer.getText());
                if (timer_value < 5400) {
                    b = true;
                } else {
                    b = false;
                }
            } else {
                b = false;
            }
        }
        return b;
    }

    public Boolean check_comp() {
        if ((combo_competence.getSelectionModel().isEmpty()) || (combo_sous_comp.getSelectionModel().isEmpty())) {
            competence_problem();
            return false;
        }
        return true;
    }

    @FXML
    public void text_change() {
        label_error.setVisible(false);
        error_label_timer.setVisible(false);
        label_ajout_ok.setVisible(false);
    }

    public void affich_error_bonne_rep() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur sur le choix de la bonne réponse");
        alert.setHeaderText(null);
        alert.setContentText("Veuillez choisir au moins une bonne réponse");
        alert.showAndWait();
    }

    @FXML
    public void ajout_question() {
        if (!check_bonne_rep()) {
            affich_error_bonne_rep();
        } else if (check_form()) {
            if (check_timer()) {
                ajout_quest_rep();
                reset_null();
                label_ajout_ok.setVisible(true);
            } else {
                error_label_timer.setVisible(true);
            }

        } else {
            label_error.setVisible(true);
        }
    }

    public void ajout_quest_rep() {
        Integer sous_comp_id = null;
        if (combo_sous_comp.getSelectionModel().getSelectedItem() != null) {
            sous_comp_id = ((Sous_competence) combo_sous_comp.getSelectionModel().getSelectedItem()).getId();
        }
        Integer quest_id = question_dao.insert_new_question(this.Quiz_id, libelle, timer_value, sous_comp_id, url_img);
        ArrayList<String> reponses = new ArrayList(Arrays.asList(rep_1, rep_2, rep_3, rep_4));
        for (int i = 0; i < reponses.size(); i++) {
            Integer is_true = chkb.get(i).isSelected() ? 1 : 0;
            Reponse rep = new Reponse(reponses.get(i), is_true, quest_id);
            reponse_dao.insert_new_reponse(rep);
        }
    }

    @FXML
    public void terminer_quiz() {
        text_change();
        if (!check_bonne_rep()) {
            affich_error_bonne_rep();
        } else if (check_form()) {
            if (check_timer()) {
                ajout_quest_rep();
                Stage stage = (Stage) combo_sous_comp.getScene().getWindow();
                stage.close();
            } else {
                error_label_timer.setVisible(true);
            }

        } else {
            label_error.setVisible(true);
        }
    }

    private void set_data_combo_competence() {
        combo_competence.getItems().clear();
        liste_Competence = competence_dao.get_all_competence();
        combo_competence.getItems().addAll(liste_Competence);
        combo_competence.getSelectionModel().selectFirst();
        if (liste_Competence.size() > 0) {
            set_data_combo_sous_comp((Competence) combo_competence.getSelectionModel().getSelectedItem());
        }
        combo_competence.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Competence>() {
            @Override
            public void changed(ObservableValue<? extends Competence> ov, Competence t, Competence t1) {
                if (t1 != null) {
                    set_data_combo_sous_comp(t1);
                }
            }
        });
    }

    private void set_data_combo_sous_comp(Competence competence) {
        combo_sous_comp.getItems().clear();
        list_sous_comp = sous_comp_dao.get_all_sous_competence(competence.getId());
        combo_sous_comp.getItems().clear();
        combo_sous_comp.getItems().addAll(list_sous_comp);
        if (list_sous_comp.size() > 0) {
            combo_sous_comp.getSelectionModel().selectFirst();
        }
        combo_sous_comp.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Sous_competence>() {
            @Override
            public void changed(ObservableValue<? extends Sous_competence> ov, Sous_competence t, Sous_competence t1) {
                if (t1 != null) {
                    System.out.println("Selected comp : " + t1.getId() + " " + t1.getLibelle());
                }
            }
        });
    }

    @FXML
    public void ajout_image() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choisir un fichier");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image file", "*.jpg", "*.jpeg", "*.png")
        );
        File selected_file = chooser.showOpenDialog(img_view.getScene().getWindow());
        if (selected_file != null) {
            if (selected_file.length() > 500000) {
                file_problem();
            } else {
                url_img = selected_file.getAbsolutePath();
                affiche_img();
            }
        }
    }

    public void file_problem() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/file_size_problem.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vérification du formulaire");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(checkbx_a.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void competence_problem() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_quiz/competence_problem.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Vérification du formulaire");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(checkbx_a.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validate_number(String str) {
        return str.matches("[0-9]*");
    }

    public void affiche_img() {
        //System.out.println("url : "+url_img);
        img_view.setImage(new Image("file:///" + url_img));
        img_view.setSmooth(true);
        img_view.setPreserveRatio(true);
    }

    @FXML
    public void delete_image() {
        img_view.setImage(null);
        url_img = "";
    }

    public void uncheck_other(CheckBox x) {
        for (CheckBox checkBox : chkb) {
            if (x != checkBox) {
                checkBox.setSelected(false);
            }
        }
    }

    @FXML
    public void checkbx_selected_action_a() {
        uncheck_other(checkbx_a);
    }

    @FXML
    public void checkbx_selected_action_b() {
        uncheck_other(checkbx_b);
    }

    @FXML
    public void checkbx_selected_action_c() {
        uncheck_other(checkbx_c);
    }

    @FXML
    public void rep_text_change_a() {
        int nb_c = res_carac - rep_a.getText().length();
        nb_carac_restant_a.setText("(" + nb_c + " caractères restants)");
    }

    @FXML
    public void rep_text_change_b() {
        int nb_c = res_carac - rep_b.getText().length();
        nb_carac_restant_b.setText("(" + nb_c + " caractères restants)");
    }

    @FXML
    public void rep_text_change_c() {
        int nb_c = res_carac - rep_c.getText().length();
        nb_carac_restant_c.setText("(" + nb_c + " caractères restants)");
    }

    @FXML
    public void rep_text_change_d() {
        int nb_d = res_carac - rep_d.getText().length();
        nb_carac_restant_d.setText("(" + nb_d + " caractères restants)");
    }

    @FXML
    public void checkbx_selected_action_d() {
        uncheck_other(checkbx_d);
    }

    @FXML
    public void open_competence() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/competence/competences.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            CompetencesController comp_controller = fxmlLoader.<CompetencesController>getController();
            comp_controller.setAfficheRetour(2);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(checkbx_a.getScene().getWindow());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();

            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    set_data_combo_competence();
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Liste_quizController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void open_sous_comp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/competence/affichage_sous_competences.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Affichage_sous_competencesController aff_controller = fxmlLoader.<Affichage_sous_competencesController>getController();

            //stock l'id de la compétence sélectionnée
            Competence comp = (Competence) combo_competence.getSelectionModel().getSelectedItem();
            int compID = comp.getId();
            aff_controller.setComp_id(compID);
            aff_controller.update_tableau();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Compétences");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initOwner(checkbx_a.getScene().getWindow());
            stage.centerOnScreen();
            stage.show();

            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    set_data_combo_sous_comp((Competence) combo_competence.getSelectionModel().getSelectedItem());
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(CompetencesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
