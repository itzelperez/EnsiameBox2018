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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import opisiame.database.Connection_db;
import opisiame.model.Competence;
import opisiame.model.Sous_competence;

/**
 *
 * @author Sandratra
 */
public class Sous_comp_dao {

    public Integer insert_new_sous_comp(String libelle, Integer comp_id) {
        String SQL = "INSERT INTO souscompetence (SousCompetence, Comp_id) VALUES (?,?)";
        Integer insert_id = null;
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, libelle);
            ps.setInt(2, comp_id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la création de la question, aucune ligne ajoutée dans la table.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                insert_id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insert_id;
    }

    public ObservableList<Sous_competence> get_all_sous_competence(Integer comp_id) {
        ObservableList<Sous_competence> sous_competences = FXCollections.observableArrayList();
        String SQL = "SELECT *  FROM souscompetence WHERE Comp_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, comp_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Sous_competence sous_competence = new Sous_competence();
                sous_competence.setId(rs.getInt(1));
                sous_competence.setLibelle(rs.getString(2));
                sous_competence.setComp_id(rs.getInt(3));
                sous_competences.add(sous_competence);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return sous_competences;
    }

    public String get_comp(Integer SousComp_id) {
        String Comp = "";
        String SQL = "SELECT C.Competence "
                + "FROM competences C "
                + "JOIN souscompetence SC "
                + "ON C.Comp_id = SC.Comp_id "
                + "WHERE SC.SousComp_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, SousComp_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Comp = rs.getString(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Comp;
    }
}
