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
    public static boolean peutCandidater(Connection con, String ine) throws SQLException {
        System.out.println("Vérification du nombre de candidatures pour l'INE : " + ine);
        if (ine == null || ine.isEmpty()) {
            throw new IllegalArgumentException("Le paramètre INE est invalide (null ou vide).");
        }
        try (PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM candidature WHERE ine = ?")) {
            System.out.println("Requête SQL : SELECT COUNT(*) FROM candidature WHERE ine = " + ine);
            pst.setString(1, ine);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1) < 5; 
            }
        }
    }

    public static boolean estCompatibleAvecClasse(String classeEtudiant, String classeOffre) {
        boolean compatible = classeOffre.equalsIgnoreCase(classeEtudiant);
        System.out.println("Vérification de compatibilité : Classe Étudiant = " + classeEtudiant +
                           ", Classe Offre = " + classeOffre + " => Compatible : " + compatible);
        return compatible;
    }

    public static void creerCandidature(Connection con, String ine, int idOffre, int idPart) throws SQLException {
        try (PreparedStatement insert = con.prepareStatement(
                "INSERT INTO candidature (ine, idOffreMobilite, date, idPartenaire) VALUES (?, ?, ?, ?)")) {
            insert.setString(1, ine);
            insert.setInt(2, idOffre);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String now = dateFormat.format(new Date());
            insert.setString(3, now);
            insert.setInt(4, idPart);

            insert.executeUpdate();
            System.out.println("Candidature créée avec succès pour ine: " + ine + ", idOffre: " + idOffre);
        }
    }


    public static boolean candidatureExiste(Connection con, String ine, int idOffre) throws SQLException {
        if (ine == null || ine.isEmpty()) {
            throw new IllegalArgumentException("Le paramètre INE est invalide (null ou vide).");
        }
        if (idOffre <= 0) { 
            throw new IllegalArgumentException("Le paramètre ID Offre est invalide.");
        }
        System.out.println("Vérification de l'existence de la candidature pour INE : " + ine + ", ID Offre : " + idOffre);
        System.out.println("Requête SQL : SELECT COUNT(*) FROM candidature WHERE ine = " + ine + " AND idOffreMobilite = " + idOffre);
        String sql = "SELECT COUNT(*) FROM candidature WHERE ine = ? AND idOffreMobilite = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, ine);
            pst.setInt(2, idOffre);
            try (ResultSet rs = pst.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}

    