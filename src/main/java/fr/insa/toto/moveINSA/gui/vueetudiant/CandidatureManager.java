package fr.insa.toto.moveINSA.gui.vueetudiant;

/**
 *
 * @author moham
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CandidatureManager {

    
    
    public static void creerCandidature(Connection con, String ine, int idOffre) throws SQLException {
        try (PreparedStatement insert = con.prepareStatement(
                "INSERT INTO candidature (ine, idOffreMobilite, date) VALUES (?, ?, ?)")) {
            insert.setString(1, ine);
            insert.setInt(2, idOffre);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = dateFormat.format(new Date());            
            insert.setString(3, now);
            insert.executeUpdate();
            System.out.println("Requête SQL : SELECT COUNT(*) FROM candidature WHERE ine = ? AND idOffreMobilite = ?");
            System.out.println("Paramètres : ine = " + ine + ", idOffre = " + idOffre);
        }
    }

    public static boolean candidatureExiste(Connection con, String ine, int idOffre) throws SQLException {
        try (PreparedStatement check = con.prepareStatement(
                "SELECT COUNT(*) FROM candidature WHERE ine = ? AND idOffreMobilite = ?")) {
            check.setString(1, ine);
            check.setInt(2, idOffre);
            try (ResultSet rs = check.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public static void supprimerCandidature(Connection con, String ine, int idOffre) throws SQLException {
        try (PreparedStatement delete = con.prepareStatement(
                "DELETE FROM candidature WHERE ine = ? AND idOffre = ?")) {
            delete.setString(1, ine);
            delete.setInt(2, idOffre);
            delete.executeUpdate();
        }
    }

    public static String afficherCandidaturesEtudiant(Connection con, String ine) throws SQLException {
        StringBuilder result = new StringBuilder();
        try (PreparedStatement query = con.prepareStatement(
                "SELECT idOffre, dateCandidature FROM candidature WHERE ine = ?")) {
            query.setString(1, ine);
            try (ResultSet rs = query.executeQuery()) {
                while (rs.next()) {
                    result.append("Offre ID: ").append(rs.getInt("idOffre"))
                          .append(", Date: ").append(rs.getTimestamp("dateCandidature"))
                          .append("\n");
                }
            }
        }
        return result.toString();
    }
    
    public static boolean peutCandidater(Connection con, String ine) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM candidature WHERE ine = ?")) {
            pst.setString(1, ine);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
        return rs.getInt(1) < 5; 
            }
        }
    }

}

    