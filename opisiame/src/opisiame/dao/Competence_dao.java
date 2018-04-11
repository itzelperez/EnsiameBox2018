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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import opisiame.database.Connection_db;
import opisiame.model.Competence;

/**
 *
 * @author Sandratra
 */
public class Competence_dao {

    public Competence_dao() {
    }

    public ArrayList<Competence> get_competence_by_id(Integer Comp_id) {
        ArrayList<Competence> competences = new ArrayList<>();
        String SQL = "SELECT *  FROM competences WHERE Comp_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, Comp_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Competence competence = new Competence();
                competence.setId(rs.getInt(1));
                competence.setNom(rs.getString(2));
                competences.add(competence);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return competences;
    }

    public ObservableList<Competence> get_all_competence() {
        ObservableList<Competence> competences = FXCollections.observableArrayList();
        String SQL = "SELECT *  FROM competences";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Competence competence = new Competence();
                competence.setId(rs.getInt(1));
                competence.setNom(rs.getString(2));
                competences.add(competence);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return competences;
    }
}
