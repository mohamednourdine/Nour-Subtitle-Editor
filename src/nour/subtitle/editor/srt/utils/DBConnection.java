/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nour.subtitle.editor.srt.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author NourSoft
 */
public class DBConnection {

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bitirmeproje?useUnicode=yes&characterEncoding=UTF-8", "root", "");
            System.out.println("Database  Successfully Connection");
            
            return conection;
            
        } catch (Exception ex) {
            System.out.println("Database.getConnection() Error -->"
                    + ex.getMessage());
            return null;
        }
    }

    public static void close(Connection con) {
        try {
            con.close();
        } catch (Exception ex) {
        }
    }
}
