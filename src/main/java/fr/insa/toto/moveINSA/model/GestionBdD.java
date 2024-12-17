/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.moveINSA.model;

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.exceptions.ExceptionsUtils;
import fr.insa.beuvron.utils.list.ListUtils;
import fr.insa.beuvron.utils.database.ResultSetUtils;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.h2.jdbc.meta.DatabaseMetaServer;

public class GestionBdD {

    /**
     * création complète du schéma de la BdD.
     * 
     * @param con
     * @throws SQLException
     */
    public static void creeSchema(Connection con)
            throws SQLException {
        con.setAutoCommit(false);
        try (Statement st = con.createStatement()) {
            // creation des tables 
           st.executeUpdate(
                    "create table partenaire ( \n"
                    + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                    + " refPartenaire varchar(50) not null unique,\n"
                    + " pays varchar(50) not null,\n"
                    + " idcoPartenaire varchar(50) not null unique,\n"
                    + " mdpPartenaire varchar(50) not null \n"
                    + ")");
            st.executeUpdate(
                    "create table SRI ( \n"
                    + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "idSRI") + ",\n"
                    + " idcoSRI varchar(50) not null unique,\n "        
                    + " refSRI varchar(50) not null unique,\n"
                    + " mdpSRI varchar(50) not null unique\n"
                    + ")");
            st.executeUpdate(
                    "create table etudiant ( \n"
                    + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "idEtudiant") + ",\n"
                    + " ine varchar(50) not null unique,\n "
                    + " nom varchar(50) not null,\n "
                    + " classe varchar(50) not null,\n "
                    + " classement int not null unique,\n "
                    + " idcoEtudiant varchar(50) not null unique,\n "        
                    + " mdpEtudiant varchar(50) not null unique\n"
                    + ")");
            st.executeUpdate(
                    "create table candidature ( \n"
                    + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "idCandidature") + ",\n"
                    + " ine varchar(50) not null,\n "
                    + " idOffreMobilite int not null,\n "
                    + " date varchar(50) not null\n "
                    + ")");
            st.executeUpdate(
                    "create table offremobilite ( \n"
                    + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                    + " nbrplaces int not null,\n"
                    + " classe varchar(50) not null,\n"    
                    + " proposepar int not null\n"
                    + ")");
            // création des liens
            st.executeUpdate(
                    """
                    alter table offremobilite
                        add constraint fk_offremobilite_proposepar
                        foreign key (proposepar) references partenaire(id)
                        on delete restrict on update restrict
                    
                   """);
            st.executeUpdate(
                    """        
                    alter table candidature
                        add constraint fk_offremobilite_id
                        foreign key (idOffreMobilite) references offremobilite(id)
                        on delete restrict on update restrict
                    """);
            st.executeUpdate(
                    """
                    alter table candidature
                        add constraint fk_etudiant_ine
                        foreign key (INE) references etudiant(ine)
                        on delete restrict on update restrict 
                    """);
            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }
//test
    /**
     * suppression complete de toute la BdD.
     *
     * @param con
     * @throws SQLException
     */
    public static void deleteSchema(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            // je supprime d'abord les liens
            try {
                st.executeUpdate(
                        "alter table offremobilite drop constraint fk_offremobilite_proposepar");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        "alter table candidature drop constraint fk_offremobilite_id");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        "alter table candidature drop constraint fk_etudiant_ine");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            // je peux maintenant supprimer les tables
            try {
                st.executeUpdate("drop table offremobilite");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate("drop table partenaire");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table etudiant");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table candidature");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table SRI");
            } catch (SQLException ex) {
            }
        }
    }

    /**
     * crée un jeu de test dans la BdD.
     *
     * @param con
     * @throws SQLException
     */
    public static void initBdDTest(Connection con) throws SQLException {
        List<Partenaire> partenaires = List.of(
                new Partenaire("MIT", "USA","MIT","provisoire"),
                new Partenaire("Oxford", "Angleterre","Oxford","provisoire")
        );
        for (var p : partenaires) {
            p.saveInDB(con);
        }
        List<Etudiant> etudiants;
        etudiants = List.of(
                new Etudiant("A0000000001", "Yassine","GT2E2",32,"yassine01","provisoire1"),
                new Etudiant("A0000000002", "Toto","GM4",52,"toto01","provisoire2")
        );
        for (var e : etudiants) {
            e.saveInDB(con);
        }
        List<OffreMobilite> offres = List.of(
                new OffreMobilite(1, partenaires.get(0).getId(), "GT2E2"),
                new OffreMobilite(2, partenaires.get(0).getId(), "MIQ2"),
                new OffreMobilite(5, partenaires.get(1).getId(), "GC2")
        );
        for (var o : offres) {
            o.saveInDB(con);
        }
        List<SRI> SRI = List.of(
                new SRI("SRI1", "SRI1", "mdp1"),
                new SRI("SRI2", "SRI2", "mdp2")
        );
        for (var s : SRI) {
            s.saveInDB(con);

    }
    }

    public static void razBDD(Connection con) throws SQLException {
        deleteSchema(con);
        creeSchema(con);
        initBdDTest(con);
    }

    public static boolean verifierDonneesEtudiant(Connection con, String identifiant, String mdp) throws SQLException{
        String rech = "SELECT 1 FROM etudiant WHERE idcoEtudiant =? AND mdpEtudiant = ? LIMIT 1";
        try (PreparedStatement pst = con.prepareStatement(rech)){
            pst.setString(1, identifiant);
            pst.setString(2, mdp);
            try (ResultSet rs = pst.executeQuery()){
                return rs.next();
            }
        }
    }
    public static boolean verifierDonneesSRI(Connection con, String identifiant, String mdp) throws SQLException{
        String rech = "SELECT 1 FROM SRI WHERE idcoSRI =? AND mdpSRI = ? LIMIT 1";
        try (PreparedStatement pst = con.prepareStatement(rech)){
            pst.setString(1, identifiant);
            pst.setString(2, mdp);
            try (ResultSet rs = pst.executeQuery()){
                return rs.next();
            }
        }
    }

    public static Etudiant getEtudiant(Connection con, String idcoEtudiant) throws SQLException{
        String rech = "SELECT * FROM etudiant WHERE idcoEtudiant = ?";
        try(PreparedStatement pst = con.prepareStatement(rech)){
            pst.setString(1, idcoEtudiant);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String ine = rs.getString("ine");
                    String nom = rs.getString("nom");
                    String classe = rs.getString("classe");
                    int classement = rs.getInt("classement");
                    String mdpEtudiant = rs.getString("mdpEtudiant");
                    
                    return new Etudiant(id, ine, nom, classe, classement, idcoEtudiant, mdpEtudiant);
                }
            }     
        }
        return null;
    }
    
    public static SRI getSRI(Connection con, String idcoSRI) throws SQLException{
        String rech = "SELECT * FROM SRI WHERE idcoSRI = ?";
        try(PreparedStatement pst = con.prepareStatement(rech)){
            pst.setString(1, idcoSRI);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("idSRI");
                    String refSRI = rs.getString("refSRI");
                    String idco = rs.getString("idcoSRI");
                    String mdpSRI = rs.getString("mdpSRI");
                    
                    return new SRI(id, idco, refSRI, mdpSRI); 
                }
            }     
        }
        return null;
    }
    
    public static void menuPartenaire(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu partenaires");
            System.out.println("==================");
            System.out.println((i++) + ") liste de tous les partenaires");
            System.out.println((i++) + ") créer un nouveau partenaire");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                i = 1 ;
                int j = 1;
                if (rep == j++) {
                    List<Partenaire> users = Partenaire.tousLesPartaires(con);
                    System.out.println(users.size() + " utilisateurs : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    Partenaire.creeConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }
    
    public static void menuOffreEtudiant (Connection con) {
        int rep = -1;
        int i = 1;
        while (rep != 0) {
            System.out.println((i++) + ") Voir toutes les offres");
            System.out.println((i++) + ") Voir tous les partenaires");
            System.out.println((i++) + ") Rerchercher les offres");
            System.out.println((i++) + ") Candidater à une offre");
            //System.out.println((i++) + ") Mes favoris");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                i = 1 ;
                int j = 1;
                if (rep == j++) {
                    List<OffreMobilite> users = OffreMobilite.toutesLesOffres(con);
                    System.out.println(users.size() + " offres : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    Partenaire.tousLesPartaires(con);
                    List<Partenaire> users = Partenaire.tousLesPartaires(con);
                    System.out.println(users.size() + " partenaires : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                    
                } else if (rep == j++) {
                    int rep1 = -1;
                    int k = 1;
                    while (rep1 != 0) { 
                    System.out.println((k++) + ") Rechercher via nom du partenaire");
                    System.out.println((k++) + ") Rechercher via pays"); 
                    System.out.println((k++) + ") Rechercher via classe");
                    System.out.println("0) Retour");
                    rep1 = ConsoleFdB.entreeEntier("Votre choix : ");
                    try {
                        int l = 1;
                        k = 1;
                        if (rep1 == l++) {
                            
                    List<OffreMobilite> users = OffreMobilite.rechercherRef(con);
                    System.out.println(users.size() + " offres : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                        
                        } else if (rep1 == l++) {
                    List<OffreMobilite> users = OffreMobilite.rechercherPays(con);
                    System.out.println(users.size() + " offres : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                        }
                        else if (rep1 == l++) {
                    System.out.println("Choisissez l'offre à laquelle vous souhaitez candidater");
                    Candidature.selectInConsole(con);
                    //aller chercher les infos sur les classes
                    //if () vérifier les classes
                    Candidature.creeConsole(con); //à voir si modifie pour automatiser avec les paramètres
                        }
                    
                } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
                }            
                }
                }
                 
            }catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
    }
    }
    
//    public static void menuCandidatureEtudiant (Connection con) {
//        //afficher toutes les candidatures
//        List<Candidature> users = Etudiant.toutesCandidaturesEtudiants(con);
//                    System.out.println(users.size() + " utilisateurs : ");
//                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
//                    
//        //pouvoir supprimer ses candidatures et voir si accepter à cet endroit
//    }
    
//    public static void menuFavori (Connection con) {
//    à faire peut être
//        System.out.println("TO DO favori");
//    }
    
    //à tester avec le menu SRI
    public static void menuEtudiantSRI (Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu SRI");
            System.out.println("==================");
            System.out.println((i++) + ") Liste de tous les étudiants");
            System.out.println((i++) + ") Créer un nouvel étudiant");
            System.out.println((i++) + ") Modifier le profil d'un étudiant");
//            System.out.println((i++) + ") Supprimer le profil d'un étudiant");
            System.out.println((i++) + ") Supprimer tous les profils des étudiants");
            System.out.println((i++) + ") Rechercher un étudiant"); //rechercher puis modifier ?
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                i = 1 ;
                int j = 1;
                if (rep == j++) {
                    List<Etudiant> users = Etudiant.tousLesEtudiants(con);
                    System.out.println(users.size() + " utilisateurs : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    Etudiant.creeConsole(con);
                }else if (rep == j++) {
                    Etudiant.modifConsoleparSRI(con);
                }else if (rep == j++) {
                    Etudiant.supprConsole(con);
                }else if (rep == j++) {
                    Etudiant.supprallConsole(con);
                }else if (rep == j++) {
                    Etudiant.rechercher(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }
    //à enlever
    public static void menuCandidatureEtudiant(Connection con) {
        try {
            int rep = -1;
            List<Candidature> candidatures = Candidature.toutesLesCandidatures(con);
            System.out.println(candidatures.size() + " offres : ");
            System.out.println(ListUtils.enumerateList(candidatures, (elem) -> elem.toString()));
            
            System.out.println("Souhaitez-vous supprimer une de vos candidatures");
            while (rep != 0) {
                int i = 1;
                System.out.println("Gerer mes candidatures");
                System.out.println("==================");
                System.out.println((i++) + ") Valider ma liste de candidature");
                System.out.println((i++) + ") Supprimer une candidature");
                System.out.println((i++) + ") Supprimer toutes mes candidatures");
                System.out.println("0) Retour");
                rep = ConsoleFdB.entreeEntier("Votre choix : ");
                try {
                    i = 1 ;
                    int j = 1;
                    if (rep == j++) {
                        System.out.println("to do");
                    } else if (rep == j++) {
                        Candidature.selectInConsole(con);
                    }else if (rep == j++) {
                        Candidature.selectInConsole(con);
                    }else if (rep == j++) {
                        Candidature.deleteAllConsole(con);
                    }
                } catch (Exception ex) {
                    System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBdD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void menuCandidatureSRI(Connection con) throws SQLException{
        int rep = -1;
        while (rep!= 0){
            int i = 1;
            System.out.println("Menu de gestion de candidature par le SRI");
            System.out.println("==================");
            System.out.println((i++) + ") Liste des candidatures déposées");
            System.out.println((i++) + ") Modifier une candidature");
            System.out.println((i++) + ") Supprimer une candidature");
            System.out.println((i++) + ") Supprimer toutes les candidatures");
            System.out.println((i++) + ") Filtrer les offres");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++){
                    List<Candidature> candidatures = Candidature.toutesLesCandidatures(con);
                    System.out.println(candidatures.size() + " utilisateurs : ");
                    System.out.println(ListUtils.enumerateList(candidatures, (elem) -> elem.toString()));
                } else if (rep == j++){
                    Candidature.updateInConsole(con);               
                } else if (rep == j++){
                    Candidature.deleteInConsole(con);
                } else if (rep == j++){
                    Candidature.deleteAllConsole(con);
                }
            }
            catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    } 
    //a faire
    public static void changermdp (Connection con) {
        System.out.println("to do");
    }
    
    public static void menuConnection (Connection con) throws SQLException{
        int i = 1;
        System.out.println("Vous connecter en tant que:");
        System.out.println((i++) + ") Etudiant");
        System.out.println((i++) + ") Partenaire");
        System.out.println((i++) + ") Membre SRI");
        int r = ConsoleFdB.entreeEntier("Votre choix : "); 
        int j = 1;
        if (r == j++) {
            String identifiant = ConsoleFdB.entreeString("Entrez votre identifiant: ");
            String mdp = ConsoleFdB.entreeString("Entrez votre mot de passe:");
            try (con){
                boolean OK = verifierDonneesEtudiant(con, identifiant, mdp);
                if (OK){
                    Etudiant etudiant = getEtudiant(con, identifiant);
                    menuPrincipalEtudiant(etudiant);
                } else {
                    System.out.println("Identifiant ou mot de passe incorrect, veuillez réessayer");
                    menuConnection(con);
                }
            }
        } else if (r == j++) {
            int k = 1;
            System.out.println((k++) + ") Connection à votre compte Partenaire");
            System.out.println((k++) +") Creation d'un compte Partenaire");
            int rep2 = ConsoleFdB.entreeEntier("Votre choix : "); 
            if (rep2 == 1){
                String identifiant = ConsoleFdB.entreeString("Entrez votre identifiant: ");
                String mdp = ConsoleFdB.entreeString("Entrez votre mot de passe:");
                menuPrincipalPartenaire(con);
            }
            else if (rep2 == 2){
                String refPartenaire = ConsoleFdB.entreeString("Veuillez indiquer votre reference Partenaire:");
                String pays = ConsoleFdB.entreeString("Veuillez indiquer votre pays :");
                String mdpP = ConsoleFdB.entreeString("Veuillez indiquer votre mot de passe:");
                Partenaire nouveauPartenaire = new Partenaire(-1, refPartenaire, pays, refPartenaire,mdpP);
                nouveauPartenaire.saveInDB(con);
                menuPrincipalPartenaire(con);
            }
        } else if (r == j++) {
            String identifiant = ConsoleFdB.entreeString("Entrez votre identifiant: ");
            String mdp = ConsoleFdB.entreeString("Entrez votre mot de passe:");
            try (con){
                boolean OK = verifierDonneesSRI(con, identifiant, mdp);
                if (OK){
                    SRI sri = getSRI(con, identifiant);
                    menuPrincipalSRI();
                } else {
                    System.out.println("Identifiant ou mot de passe incorrect, veuillez réessayer");
                    menuConnection(con);
                }
            }
            menuPrincipalSRI();
        }
    }
    
    public static void menuProfilEtudiant (Connection con, Etudiant etudiant) throws SQLException{
        int i = 1;
        System.out.println("Nom : " + etudiant.getNom());
        System.out.println("Classe : " + etudiant.getClasse());
        System.out.println("INE : " + etudiant.getIne());
        System.out.println("Classement : " + etudiant.getClassement());
        System.out.println("ID de connecxion : " + etudiant.getIdcoEtudiant());
        System.out.println("Mot de passe : " + etudiant.getMdpEtudiant());
        System.out.println("Veuillez choisir une action :");
        System.out.println( i + ") Changer de mot de passe");
        System.out.println("0) Deconnexion");
        if (i == 1){
            Etudiant.modifConsolemdpEtudiant(con, etudiant.getIdcoEtudiant());
        }
    }
    
    //à faire
    public static void menuProfil(Connection con) {
        System.out.println("TO DO profil");
        // mettre les infos type nom, idconnection, mdp et modifier mdp. pour étudiant, mettre en plus classe et ine. Photo de profil ?
    }
    
    //à modifier pour partenaire et sri, utiliser le même ?
    public static void menuOffre(Connection con) { 
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu offres mobilité");
            System.out.println("==================");
            System.out.println((i++) + ") liste de toutes les offres");
            System.out.println((i++) + ") créer une nouvelle offre");
            System.out.println((i++) + ") modifier une offre");
            System.out.println((i++) + ") supprimer une offre");
            System.out.println((i++) + ") supprimer toutes les offres");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<OffreMobilite> offres = OffreMobilite.toutesLesOffres(con);
                    System.out.println(offres.size() + " offres : ");
                    System.out.println(ListUtils.enumerateList(offres, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    OffreMobilite.creeConsole(con);
                } else if (rep == j++) {
                    OffreMobilite.modifConsole(con);                   
                } else if (rep == j++) {
                    OffreMobilite.suppConsole(con);
                } else if (rep == j++) {
                    OffreMobilite.suppALLConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void menuBdD(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu gestion base de données");
            System.out.println("============================");
            System.out.println((i++) + ") RAZ BdD = delete + create + init");
            System.out.println((i++) + ") donner un ordre SQL update quelconque");
            System.out.println((i++) + ") donner un ordre SQL query quelconque");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    razBDD(con);
                } else if (rep == j++) {
                    String ordre = ConsoleFdB.entreeString("ordre SQL : ");
                    try (PreparedStatement pst = con.prepareStatement(ordre)) {
                        pst.executeUpdate();
                    }
                } else if (rep == j++) {
                    String ordre = ConsoleFdB.entreeString("requete SQL : ");
                    try (PreparedStatement pst = con.prepareStatement(ordre)) {
                        try (ResultSet rst = pst.executeQuery()) {
                            System.out.println(ResultSetUtils.formatResultSetAsTxt(rst));
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.beuvron", 3));
            }
        }
    }

    public static void menuGestionSRI(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu SRI");
            System.out.println("==================");
            System.out.println((i++) + ") Liste de tous les membre SRI");
            System.out.println((i++) + ") Créer un nouveau membre SRI");
            System.out.println((i++) + ") Modifier un membre SRI");
            System.out.println((i++) + ") Supprimer un membre SRI");
            System.out.println((i++) + ") Supprimer tous les membres SRI");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<SRI> users = SRI.tousLesSRI(con);
                    System.out.println(users.size() + " utilisateurs : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    SRI.creeConsole(con);
                }
                else if (rep == j++) {
                    SRI.modifConsole(con);
                }
                else if (rep == j++) {
                    SRI.suppConsole(con);
                }
                else if (rep == j++) {
                    SRI.suppALLConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }
    
    public static void menuPrincipalEtudiant(Etudiant etudiant) {
        int rep = -1;
        Connection con = null;
        try {
            con = ConnectionSimpleSGBD.defaultCon();
            System.out.println("Connection OK");
        } catch (SQLException ex) {
            System.out.println("Problème de connection : " + ex.getLocalizedMessage());
            throw new Error(ex);
        }
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu principal");
            System.out.println("==================");
            System.out.println((i++) + ") Mon profil");
            System.out.println((i++) + ") Les offres");
            System.out.println((i++) + ") Mes candidatures");
            //System.out.println((i++) + ") Mes favoris");
            System.out.println("0) Me déconnecter");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    menuProfilEtudiant(con, etudiant); //faire menuProfil commun ?
                } else if (rep == j++) {
                    menuOffreEtudiant(con);
                } else if (rep == j++) {
                    menuCandidatureEtudiant(con);
                } //else if (rep == j++) {
                    //menuFavori(con);
               // }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }
    
    public static void menuPrincipalSRI() {
int rep1 = -1;
        Connection con = null;
        try {
            con = ConnectionSimpleSGBD.defaultCon();
            System.out.println("Connection OK");
        } catch (SQLException ex) {
            System.out.println("Problème de connection : " + ex.getLocalizedMessage());
            throw new Error(ex);
        }
            System.out.println("1) Se connecter à mon profil SRI");
            System.out.println("0) Déconnexion");
            rep1 = ConsoleFdB.entreeEntier("Votre choix : ");
            if (rep1 == 1){
                int i = 1;
                int r;
                System.out.println((i++) + ") Acceder au menu ETUDIANT");
                System.out.println((i++) + ") Acceder au menu PARTENAIRE");
                System.out.println((i++) + ") Acceder au menu CANDIDATURE");
                System.out.println(((i++) + ") Gestion de la base de donnees"));
                r = ConsoleFdB.entreeEntier("Votre choix : "); 
                int j = 1;
                if (j==r){
                    menuEtudiantSRI(con);
                }
                else if (r == 3){
                    try {
                        menuCandidatureSRI(con);
                    } catch (SQLException ex) {
                        Logger.getLogger(GestionBdD.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if (r == 4){
                    menuBdD(con);
                }
                else{
                    System.out.println("Page en cours de création!");
                }
            }
        System.out.println("Connection OK");
             }            
   
    public static void menuPrincipalPartenaire(Connection con) throws SQLException {
        System.out.println("Bienvenue cher Partenaire!");
        System.out.println("Veuillez selectionner une option");
        int i = 1;
        System.out.println((i++) + ") Consulter les offres publiees");
        System.out.println((i++) + ") Publier une nouvelle offre");
        System.out.println("0) Retour");
        int r = ConsoleFdB.entreeEntier("Votre choix");
        int j = 1;
        if (r == 1){
            List<OffreMobilite> offres = OffreMobilite.toutesLesOffres(con);
            System.out.println(offres.size() + " offres : ");
            System.out.println(ListUtils.enumerateList(offres, (elem) -> elem.toString()));
        }
        else if (r == 2){
            int nbrPlaces = ConsoleFdB.entreeEntier("Nombre de places offertes:");
            int proposePar = ConsoleFdB.entreeEntier("id de votre Etablissement:");
            String classe = ConsoleFdB.entreeString("classe ciblee: ");
            OffreMobilite offre = new OffreMobilite(nbrPlaces, proposePar, classe);
            offre.saveInDB(con);
        }
//        int rep = -1;
//        Connection con = null;
//        try {
//            con = ConnectionSimpleSGBD.defaultCon();
//            System.out.println("Connection OK");
//        } catch (SQLException ex) {
//            System.out.println("Problème de connection : " + ex.getLocalizedMessage());
//            throw new Error(ex);
//        }
//        while (rep != 0) {
//            int i = 1;
//            System.out.println("Menu principal");
//            System.out.println("==================");
//            System.out.println((i++) + ") test driver mysql");
//            System.out.println((i++) + ") menu gestion BdD");
//            System.out.println((i++) + ") menu partenaires");
//            System.out.println((i++) + ") menu offres");
//            System.out.println((i++) + ") menu SRI");
//            System.out.println("0) Fin");
//            rep = ConsoleFdB.entreeEntier("Votre choix : ");
//            try {
//                int j = 1;
//                if (rep == j++) {
//                    try {
//                        Class<Driver> mysqlDriver = (Class<Driver>) Class.forName("com.mysql.cj.jdbc.Driver");
//                    } catch (ClassNotFoundException ex) {
//                        System.out.println("com.mysql.cj.jdbc.Driver not found");
//                    }
//                    DatabaseMetaData meta = con.getMetaData();
//                    System.out.println("jdbc driver version : " + meta.getDriverName() + " ; " + meta.getDriverVersion());
//                } else if (rep == j++) {
//                    menuBdD(con);
//                } else if (rep == j++) {
//                    menuPartenaire(con);
//                } else if (rep == j++) {
//                    menuOffre(con);
//                }
//                else if (rep == j++) {
//                    menuSRI(con);
//                }
//            } catch (Exception ex) {
//                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
//            }
//        }
    }
    
   
    
    public static void main(String[] args) throws SQLException {
        int r = -1;
        Connection con = null;
        try {
            con = ConnectionSimpleSGBD.defaultCon();
            System.out.println("Connection OK");
        } catch (SQLException ex) {
            System.out.println("Problème de connection : " + ex.getLocalizedMessage());
            throw new Error(ex);
        }
        menuConnection(con);
    }
}