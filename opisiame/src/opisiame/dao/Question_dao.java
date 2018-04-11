/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import opisiame.database.Connection_db;
import opisiame.model.Question;

/**
 *
 * @author Sandratra
 */
public class Question_dao {

    public Question_dao() {
    }

    Reponse_dao reponse_dao = new Reponse_dao();

    public ArrayList<Question> get_questions_by_quiz(Integer quiz_id) {
        ArrayList<Question> questions = new ArrayList<>();
        String SQL = "SELECT question.*, souscompetence.SousCompetence  FROM question"
                + " LEFT JOIN souscompetence ON souscompetence.SousComp_id = question.SousComp_id "
                + "WHERE Quiz_id = ?";
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setInt(1, quiz_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Question quest = new Question();
                quest.setId(rs.getInt(1));
                quest.setLibelle(rs.getString(2));
                if (rs.getString(3) != null) {
                    quest.setTimer(rs.getInt(3));
                }
                quest.setQuiz_id(rs.getInt(4));
                quest.setSous_comp_id(rs.getInt(5));
                quest.setImg_blob(rs.getBinaryStream(6)); //resultSet.getBlob(yourBlobColumnIndex);
                quest.setSous_comp(rs.getString(7));
                quest.setReponses(reponse_dao.get_reponses_by_quest(quest.getId()));
                questions.add(quest);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return questions;
    }

    public void update_question(Integer id, String libelle, Integer timer, Integer sous_comp_id, String url_img) {
        String SQL;
        System.out.println("url : " + url_img);
        if (url_img == null) {
            SQL = "UPDATE question SET Quest_libelle = ?, Quest_timer = ? , SousComp_id = ? WHERE Quest_id = ?";
        } else {
            SQL = "UPDATE question SET Quest_libelle = ?, Quest_timer = ? , SousComp_id = ?, Quest_img = ? WHERE Quest_id = ?";
        }

        System.out.println("sql : " + SQL);
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ps.setString(1, libelle);
            if (timer == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, timer);
            }
            ps.setInt(3, sous_comp_id);
            if (url_img != null) {
                if (url_img.compareTo("") == 0) {
                    ps.setNull(4, java.sql.Types.BLOB);
                } else {
                    File file = new File(url_img);
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        ps.setBinaryStream(4, fis, (int) file.length());
                    } catch (FileNotFoundException ex) {
                        System.err.println("Image not found");
                        ex.printStackTrace();
                    }
                }
                ps.setInt(5, id);
            } else {
                ps.setInt(4, id);
            }
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Échec de la modification de la question, aucune ligne modifiée dans la table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer insert_new_question(Integer quiz_id, String libelle, Integer timer, Integer sous_comp_id, String url_img) {
        String SQL;
        FileInputStream fis = null;
        File file = null;
        System.out.println("url image : " + url_img);
        if (url_img.compareTo("") != 0) {
            SQL = "INSERT INTO question (Quest_libelle, Quest_timer, Quiz_id, SousComp_id, Quest_img) VALUES (?,?,?,?,?)";
            file = new File(url_img);
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                System.err.println("Image not found");
                ex.printStackTrace();
            }
        } else {
            SQL = "INSERT INTO question (Quest_libelle, Quest_timer, Quiz_id, SousComp_id) VALUES (?,?,?,?)";
        }
        Integer insert_id = null;
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, libelle);
            if (timer == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, timer);
            }
            ps.setInt(3, quiz_id);
            if (sous_comp_id == null) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, sous_comp_id);
            }
            if (fis != null) {
                ps.setBinaryStream(5, fis, (int) file.length());
            }
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

    public void delete_question(Integer id) {
        try {
            Connection connection = Connection_db.getDatabase();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM question WHERE Quest_id = ?");
            ps.setInt(1, id);
            int succes = ps.executeUpdate();
            if (succes == 0) {
                System.err.println("Erreur lors de la suppression de la question");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
