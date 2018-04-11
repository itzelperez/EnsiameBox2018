/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opisiame.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import opisiame.database.Connection_db;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author zhuxiangyu
 */


public class Import_animateur_excel {
    //private List<Integer> Liste_id = new ArrayList<>();
    private List<String> Liste_nom = new ArrayList<>();
    private List<String> Liste_prenom = new ArrayList<>();
    private List<String> Liste_login = new ArrayList<>();
    private List<String> Liste_mdp = new ArrayList<>();
    private List<Integer> Liste_erreur = new ArrayList<>();
    private int nb_element; //nombre d'élément dans le fichier excel
    private XSSFWorkbook classeur;
      
    public Import_animateur_excel(String adresse) throws IOException {
        //openning the document
        //System.out.print("appelle constructeur import animateur excel \n");
        nb_element = 0;
        ouverture_fichier(adresse);
        //System.out.print("je suppose que ça doit marcher vu qu'il n'y a pas d'erreur \n");

        //reading the document
        Sheet sheet = classeur.getSheetAt(0);
        //creer un itérateur sur les colonnes
        Iterator<Row> iterator = sheet.iterator();

       while (iterator.hasNext()) {
           ++nb_element;
           Row row = iterator.next();
           Iterator<Cell> cell_iterator = row.cellIterator();
            
           Cell cell =cell_iterator.next();
           //Liste_id.add((int) cell.getNumericCellValue());
           //cell = cell_iterator.next();
           Liste_nom.add(cell.getStringCellValue());
           cell = cell_iterator.next();
           Liste_prenom.add(cell.getStringCellValue());
           cell = cell_iterator.next();
           Liste_login.add(cell.getStringCellValue());
           cell = cell_iterator.next();
           Liste_mdp.add(cell.getStringCellValue());
       }
           
           /*for (int i = 0; i < Liste_nom.size(); ++i){
               //System.out.print(Liste_id.get(i) + " ");
               System.out.print(Liste_nom.get(i) + " ");
               System.out.print(Liste_prenom.get(i) + " ");
               System.out.print(Liste_login.get(i) + " ");
               System.out.print(Liste_mdp.get(i) + " \n");
           }*/

           /* while (cell_iterator.hasNext()) {
                Cell cell = cell_iterator.next();
                
                

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        System.out.print(cell.getStringCellValue() + " 1");
                        System.out.print("\n");
                        
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue() + " 2");
                        System.out.print("\n");
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        System.out.print(cell.getBooleanCellValue() + " 3");
                        System.out.print("\n");
                        break;
                }*/
        update_database();
        
        }

    public Import_animateur_excel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void ouverture_fichier(String adresse) throws IOException {
        InputStream is = new FileInputStream(adresse);
        OPCPackage opc;
        try {
            opc = OPCPackage.open(is);
            classeur = new XSSFWorkbook(opc);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(Import_animateur_excel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void update_database() {
            for (int i = 0; i < nb_element; ++i) {
                 try {Connection connection = Connection_db.getDatabase();
                PreparedStatement ps = connection.prepareStatement("INSERT INTO animateur (Anim_nom, Anim_prenom, Anim_login, Anim_mdp) "
                        + "VALUES (?, ?, ?, ? )");
                //ps.setInt(1, Liste_id.get(i));
                ps.setString(1, Liste_nom.get(i));
                ps.setString(2, Liste_prenom.get(i));
                ps.setString(3, Liste_login.get(i));
                ps.setString(4, Liste_mdp.get(i));
                ps.executeUpdate();
                 } catch (SQLException ex) {
            //Logger.getLogger(Import_animateur_excel.class.getName()).log(Level.SEVERE, null, ex);
            Liste_erreur.add(i+1);
            System.out.printf("une erreur est apparu à la ligne suivante : \n");
            System.out.printf(Liste_erreur.get(0) + "\n");
        }
            }
    }
    
    public List<Integer> getErreur(){
        return Liste_erreur;
    }
}
