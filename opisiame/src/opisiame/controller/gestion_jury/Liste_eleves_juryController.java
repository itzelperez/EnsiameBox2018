/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.controller.gestion_jury;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Callback;
import opisiame.controller.gestion_eleve.Add_eleveController;
import opisiame.controller.gestion_eleve.Delete_eleveController;
import opisiame.controller.gestion_eleve.Import_excelController;
import opisiame.controller.gestion_eleve.Liste_eleves_adminController;
import opisiame.database.*;
import opisiame.model.Eleve;
import opisiame.model.Vote;

/**
 * FXML Controller class
 *
 * @author Audrey
 */

public class Liste_eleves_juryController  implements Initializable {

    private ObservableList<Vote> votes = FXCollections.observableArrayList();

    @FXML
    private AnchorPane content;
    @FXML
    private TableView<Vote> Tableau;
    @FXML
    private TableColumn<Vote, Boolean> Action_col;
    @FXML
    private TableColumn<Vote, Integer> id;
    @FXML
    private TableColumn<Vote, String> Nom;
    @FXML
    private TableColumn<Vote, String> Prenom;
    @FXML
    private TableColumn<Vote, String> Filiere;
    @FXML
    private TableColumn<Vote, Integer> Annee;
    
    @FXML
    private TextField Champ_recherche;
    @FXML
    private Button SelectAll; 
    
    private Parent root;
    
    private Stage stage;
    
    private Scene scene;
    
    public void setRoot(Parent root){
        this.root = root;
    }
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    public void setScene(Scene scene){
        this.scene = scene;
    }
    
    
    //@FXML
    //private Button commencer_vote; 

   private List<Integer> estudents_evaluate = new ArrayList<>();
    private String Cont_recherche = null;
    
    private Vote_Controller vote_controller;
    
    public void setVote_Controller(Vote_Controller vote_controller){
        System.out.println("ENDERECO SET VOTE " +this);
        this.vote_controller = vote_controller;
    }

    /*
    Fonction qui récupère la liste des élèves
     */
    public void getAllEleve(/*ObservableList<Eleve> eleves*/) {
        try {
            //ObservableList<Eleve> eleves = FXCollections.observableArrayList();
            Connection connection = Connection_db.getDatabase();
            estudents_evaluate.clear();
            PreparedStatement requette;

            if (Cont_recherche != null) {
                requette = connection.prepareStatement("SELECT participant.Part_id, participant.Part_nom, participant.Part_prenom, filiere.Filiere, filiere.Annee FROM participant \n"
                        + "LEFT JOIN filiere \n"
                        + "ON filiere.Filiere_ID = participant.Filiere_id\n"
                        + "WHERE participant.Part_id LIKE ?\n"
                        + "OR participant.Part_nom LIKE ?\n"
                        + "OR participant.Part_prenom LIKE ?\n"
                        + "OR filiere.Filiere LIKE ?\n"
                        + "OR filiere.Annee LIKE ?");
                requette.setString(1, "%" + Cont_recherche + "%");
                requette.setString(2, "%" + Cont_recherche + "%");
                requette.setString(3, "%" + Cont_recherche + "%");
                requette.setString(4, "%" + Cont_recherche + "%");
                requette.setString(5, "%" + Cont_recherche + "%");
            } else {
                requette = connection.prepareStatement("SELECT participant.Part_id, participant.Part_nom, participant.Part_prenom, filiere.Filiere, filiere.Annee FROM participant \n"
                        + "LEFT JOIN filiere \n"
                        + "ON filiere.Filiere_ID = participant.Filiere_id");
            }
            ResultSet res_requette = requette.executeQuery();
            while (res_requette.next()) {
                Vote etudiant = new Vote();
                etudiant.setId(res_requette.getInt(1));
                etudiant.setNom(res_requette.getString(2));
                etudiant.setPrenom(res_requette.getString(3));
                etudiant.setFiliere(res_requette.getString(4));
                etudiant.setAnnee(res_requette.getInt(5));
                votes.add(etudiant);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<Vote, Integer>("id"));
        Nom.setCellValueFactory(new PropertyValueFactory<Vote, String>("Nom"));
        Prenom.setCellValueFactory(new PropertyValueFactory<Vote, String>("Prenom"));
        Filiere.setCellValueFactory(new PropertyValueFactory<Vote, String>("Filiere"));
        Annee.setCellValueFactory(new PropertyValueFactory<Vote, Integer>("Annee"));
        getAllEleve(/*eleves*/);
        Tableau.setItems(votes);
        

        //Insert Button    
        Action_col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Vote, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Vote, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });

        Action_col.setCellFactory(new Callback<TableColumn<Vote, Boolean>, TableCell<Vote, Boolean>>() {
            @Override
            public TableCell<Vote, Boolean> call(TableColumn<Vote, Boolean> param) {
                return new CheckBoxCell();
            }
        });

        Tableau.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                    Tableau.getSelectionModel().select(row.getIndex());
                }
            }
        });

    }

    private class CheckBoxCell extends TableCell<Vote, Boolean> {

        final CheckBox check = new CheckBox();

        CheckBoxCell() {
            check.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (check.isSelected()) {
                        //Integer id = Tableau.getSelectionModel().getSelectedItem().getId();
                        Integer id = Tableau.getFocusModel().getFocusedItem().getId();
                        estudents_evaluate.add(id);
                        //estudents_evaluate.addAll()
                        System.out.println("Students" + estudents_evaluate.size());
                    }
                    
                    if(!check.isSelected()){
                    Integer id = Tableau.getFocusModel().getFocusedItem().getId();
                    estudents_evaluate.remove(id);
                    System.out.println("Students" + estudents_evaluate.size());
                }
                      
                }
            });
        }

        //Affichage des boutons
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                HBox lay = new HBox(1);
                lay.getChildren().add(check);
                setGraphic(lay);
            }
        }
    };


    
    public void update_tableau() {
        Tableau.getItems().clear();
        votes.clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_jury/Liste_eleves_juryController.fxml"));
        URL url = fxmlLoader.getLocation();
        ResourceBundle rb = fxmlLoader.getResources();
        this.initialize(url, rb);
        Tableau.setItems(votes);
        Tableau.refresh();
    }

    public void Rechercher() {
        Cont_recherche = Champ_recherche.getText();
        update_tableau();
    }

    @FXML
    public void ClicBoutonSuivant() throws IOException {   
        try {
            //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/jury/vote.fxml"));
            //Parent root = (Parent) fxmlLoader.load();
            //Vote_Controller vote_controller = fxmlLoader.<Vote_Controller>getController();
            System.out.println("ENDERECO LISTE " + this);
            vote_controller.setEleve_id(estudents_evaluate);
            vote_controller.getSelectedEleve();
            Stage stagep = new Stage();
            stagep.initModality(Modality.APPLICATION_MODAL);
            stagep.setTitle("Vote");
            Scene scenep = new Scene(root);
            stagep.setScene(scenep);
            stagep.centerOnScreen();
            stagep.show();


        } catch (Exception ex) {
            Logger.getLogger(Liste_eleves_adminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    //Boutton de retour
    @FXML
    public void ClicBoutonRetour() throws IOException {
        //Retour sur la fenetre menu
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/jury/session_jury.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    //boutton log out
    @FXML
    public void ClicBoutonHome() throws IOException {
        //Retour sur la fenetre d'identification
        Stage stage = (Stage) content.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/opisiame/view/utilisateur/interface_authentification.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        session.Session.Logout();
    }

    @FXML
    public void Clic_ajout_eleve() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_eleve/Add_eleve.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Add_eleveController add_controller = fxmlLoader.<Add_eleveController>getController();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Ajouter un élève");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //stage.initOwner(Tableau.getScene().getWindow());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                update_tableau();
            }
        });
    }

    @FXML
    public void Import_eleve() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/opisiame/view/gestion_eleve/import_excel.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Import_excelController add_controller = fxmlLoader.<Import_excelController>getController();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Importer des élèves");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //stage.initOwner(Tableau.getScene().getWindow());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/opisiame/image/icone.png")));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                    update_tableau();
            }

        });
    }
}


    

