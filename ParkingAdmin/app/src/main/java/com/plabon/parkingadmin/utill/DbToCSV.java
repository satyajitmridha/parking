package com.plabon.parkingadmin.utill;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbToCSV {
   // String filename ="Desktop:test.csv";
       /*
        FileWriter fw = null;
        try {
            fw = new FileWriter(filename);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String query = "select * from testtable";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        while (true) {
            try {
                if (!rs.next()) break;
*/
    /*        fw.append(rs.getString(1));
            fw.append(',');
            fw.append(rs.getString(2));
            fw.append(',');
            fw.append(rs.getString(3));
            fw.append('\n');

                fw.flush();
                fw.close();
                conn.close();
                System.out.println("CSV File is created successfully.");

            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }*/

}
