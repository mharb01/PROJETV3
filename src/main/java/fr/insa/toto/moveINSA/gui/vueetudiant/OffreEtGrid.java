package fr.insa.toto.moveINSA.gui.vueetudiant;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import fr.insa.toto.moveINSA.model.Etudiant;
import fr.insa.toto.moveINSA.model.Candidature;
import java.sql.Connection;
import java.sql.SQLException;
import fr.insa.toto.moveINSA.gui.vueetudiant.CandidatureManager;
import fr.insa.toto.moveINSA.gui.SimpleAuthService;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.gui.ConfirmDialog;
import fr.insa.toto.moveINSA.model.ConnectionSimpleSGBD;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author HP
 */
public class OffreEtGrid extends Grid<OffreMobilite> {

//    private final List<OffreMobilite> offres;

    public OffreEtGrid(List<OffreMobilite> offres) {
//        super(OffreMobilite.class); 
//        this.offres = offres;

        this.addColumn(OffreMobilite::getId).setHeader("ID");
        this.addColumn(OffreMobilite::getNbrPlaces).setHeader("Nombre de Places");
        this.addColumn(OffreMobilite::getProposePar).setHeader("Proposé Par");
        this.addColumn(OffreMobilite::getClasse).setHeader("Classe");
        this.addColumn(OffreMobilite::getAnnee).setHeader("Année");

        this.addComponentColumn(this::createCandidaterButton).setHeader("Actions");

        this.setItems(offres);

        System.out.println("Offres transmises à la grille : " + offres);
    }

    private Button createCandidaterButton(OffreMobilite offre) {
    Button candidaterButton = new Button("Candidater");
    System.out.println("ID de l'offre récupéré : " + offre.getId());

    candidaterButton.addClickListener(event -> {
        Etudiant etudiant = getEtudiantActuel();
        System.out.println("INE récupéré : " + etudiant.getIne());

        if (!CandidatureManager.estCompatibleAvecClasse(etudiant.getClasse(), offre.getClasse())) {
            Notification.show("Vous ne pouvez pas candidater à cette offre. Classe non compatible !");
            return;
        }

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://92.222.25.165:3306/m3_mharb01",
                "m3_mharb01",
                "0db5200c")) {

            if (CandidatureManager.candidatureExiste(con, etudiant.getIne(), offre.getId())) {
                Notification.show("Vous avez déjà candidaté à cette offre.");
                return;
            }

            if (!CandidatureManager.peutCandidater(con, etudiant.getIne())) {
                Notification.show("Vous avez atteint la limite de 5 candidatures !");
                return;
            }

        } catch (SQLException ex) {
            System.err.println("Erreur SQL lors de la vérification des candidatures : " + ex.getMessage());
            Notification.show("Erreur : " + ex.getLocalizedMessage());
            return;
        }

        ConfirmDialog dialog = new ConfirmDialog(
            "Êtes-vous sûr de vouloir candidater à cette offre ?",
            () -> {
                try (Connection con = DriverManager.getConnection(
                        "jdbc:mysql://92.222.25.165:3306/m3_mharb01",
                        "m3_mharb01",
                        "0db5200c")) {

                    CandidatureManager.creerCandidature(con, etudiant.getIne(), offre.getId(), offre.getProposePar());
                    Notification.show("Candidature enregistrée avec succès !");

                } catch (SQLException ex) {
                    System.err.println("Erreur lors de la création de la candidature : " + ex.getMessage());
                    Notification.show("Erreur : " + ex.getLocalizedMessage());
                }
            }
        );

        dialog.open();
    });

    return candidaterButton;
}



    private Etudiant getEtudiantActuel() {
        
    SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
    Integer loggedEtudiantId = sessionInfo.getLoggedEtudiantId();
    
    System.out.println("SessionInfo : " + sessionInfo);
    System.out.println("Logged Etudiant ID: " + loggedEtudiantId);

    if (sessionInfo == null) {
        throw new IllegalStateException("La session de l'étudiant est nulle.");
    }   
    
    if (loggedEtudiantId == null) {
        throw new IllegalStateException("Aucun étudiant n'est connecté.");
    }

    try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
        String query = "SELECT * FROM etudiant WHERE idEtudiant = ?";
        System.out.println("Exécution de la requête : " + query + " avec ID = " + loggedEtudiantId);
    try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, loggedEtudiantId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Étudiant trouvé : " + rs.getString("nom"));
                    int idEtudiant = rs.getInt("idEtudiant");
                    String ine = rs.getString("ine");
                    String classe = rs.getString("classe");
                    String nom = rs.getString("nom");
                    String idcoEtudiant = rs.getString("idcoEtudiant");
                    int classement = rs.getInt("classement");
                    String mdpEtudiant = rs.getString("mdpEtudiant");
                    return new Etudiant(idEtudiant, ine, nom, classe, classement, idcoEtudiant, mdpEtudiant); 
                } else {
                    throw new IllegalStateException("Étudiant non trouvé dans la base de données.");
                }
            }
        }
    } catch (SQLException ex) {
        throw new RuntimeException("Erreur lors de la récupération de l'étudiant: " + ex.getMessage(), ex);
    }
}


}