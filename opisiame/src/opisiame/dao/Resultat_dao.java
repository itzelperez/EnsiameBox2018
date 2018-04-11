/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import opisiame.database.Connection_db;
import opisiame.model.Eleve;

/**
 *
 * @author Audrey
 */
public class Resultat_dao {

    public Resultat_dao() {
    }

    public ObservableList<Eleve> get_participants_quiz(Integer id_quiz, String d) {
        ObservableList<Eleve> eleves = FXCollections.observableArrayList();
        String SQL = "SELECT P.Part_nom, P.Part_prenom, P.Part_id "
                + "FROM participant P, participant_quiz PQ "
                + "WHERE PQ.Date_participation = ? AND PQ.Quiz_id = ? AND (P.Part_id = PQ.Part_id)";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, d);
            ps.setInt(2, id_quiz);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Eleve eleve = new Eleve();
                eleve.setId(rs.getInt(3));
                eleve.setNom(rs.getString(1));
                eleve.setPrenom(rs.getString(2));
                eleves.add(eleve);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return eleves;
    }

    public String get_competence(Integer SousComp_id) {
        String s = "";
        String SQL = "SELECT C.Competence\n"
                + "FROM competences C\n"
                + "JOIN souscompetence SC ON SC.Comp_id = C.Comp_id\n"
                + "WHERE SC.SousComp_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, SousComp_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                s = rs.getString(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return s;
    }
    
    public void delete_resultat(Integer part_id){
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM reponse_participant_quiz WHERE Participation_id = ?");
            ps.setInt(1, part_id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Erreur lors de la suppression du resultat");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
