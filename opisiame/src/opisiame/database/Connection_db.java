/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.database;

import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author clement
 */
public class Connection_db {

    public Connection_db() {
        //chargement du fichier de ressources
    }

    private static Connection database = null;

    public static Connection getDatabase() {
        if (database == null) {
            try {
                Properties propriete = new Properties();
                InputStream prop_file = Connection_db.class.getClassLoader().getResourceAsStream("opisiame/database/propriete_db.properties");
                //chargement du fichier de propriété
                propriete.load(prop_file);
                String driver = propriete.getProperty("driver");
                String url = propriete.getProperty("url");
                String user = propriete.getProperty("user");
                String password = propriete.getProperty("password");
                Class.forName(driver);
                database = DriverManager.getConnection(url, user, password);

            } catch (FileNotFoundException ex) {
                System.err.println("erreur de l'ouverture du fichier de propriété de connection");
            } catch (IOException ex) {
                System.err.println("Le fichier de propriete ne peut pas être ouvert");
            } catch (SQLException ex) {
                System.err.println("Connection avec la base de données impossible");
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                System.err.println("classe not found");
                Logger.getLogger(Connection_db.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return database;
    }

}
