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
package fr.insa.toto.moveINSA.gui;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.insa.toto.moveINSA.gui.MainLayout;
import fr.insa.toto.moveINSA.model.ConnectionSimpleSGBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@PageTitle("MoveINSA")
@Route( value = "connexion", layout = MainLayout.class)
public class VueConnexion extends VerticalLayout {

    public VueConnexion() throws SQLException {
        
        this.setSizeFull();
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);      

        Image login = new Image("https://cdn-icons-png.flaticon.com/512/5087/5087579.png", "Etudiant");
        login.setWidth("150px");
        login.setHeight("150px");
        
        H3 titre = new H3("Veuillez vous identifier / Login ");
        titre.getStyle().set("font-weight", "bold"); 
        titre.getStyle().set("color", "pink");       
        
        String role = SessionInfo.getOrCreateCurSessionInfo().getUserRole();
        System.out.println("Rôle reçu dans VueConnexion : " + role);

        TextField usernameField = new TextField("Identifiant");
        PasswordField passwordField = new PasswordField("Mot de passe");
        Button loginButton = new Button("Se connecter");
        
        add(login, titre, usernameField, passwordField, loginButton);

        loginButton.addClickListener(event -> {
    String username = usernameField.getValue().trim();
    String password = passwordField.getValue().trim();
    
    Boolean verif;
    try {
        verif = SimpleAuthService.authenticate(username, password, role);
        System.out.println("Vérification réussie: " + verif); 

        if (verif) {
            switch (role) {
                case "Etudiant" -> {
                try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
                String query = "SELECT idEtudiant, nom , ine FROM etudiant WHERE idcoEtudiant = ? AND mdpEtudiant = ?";
                try (PreparedStatement pst = connection.prepareStatement(query)) {
                    pst.setString(1, username);
                    pst.setString(2, password);
                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            Integer idEtudiant = rs.getInt("idEtudiant");
                            String nom = rs.getString("nom");
                            String ine = rs.getString("ine");
                            SessionInfo.getOrCreateCurSessionInfo().setLoggedEtudiantId(idEtudiant);
                            SessionInfo.getOrCreateCurSessionInfo().setLoggedEtudiantNom(nom);
                            SessionInfo.getOrCreateCurSessionInfo().setLoggedEtudiantINE(ine);                            
                            System.out.println("Étudiant connecté avec ID : " + idEtudiant);
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(VueConnexion.class.getName()).log(Level.SEVERE, null, ex);
            }
            UI.getCurrent().navigate("etudiant/vue");
        }
                case "Partenaire" -> { 
                    try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
                String rech = "SELECT id, refPartenaire FROM partenaire WHERE idcoPartenaire =? AND mdpPartenaire = ? LIMIT 1";
            try (PreparedStatement pst = connection.prepareStatement(rech)){
                pst.setString(1, username);
                pst.setString(2, password);                   
                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            Integer id = rs.getInt("id");
                            String refPartenaire = rs.getString("refPartenaire");
                            SessionInfo.getOrCreateCurSessionInfo().setPartId(id);
                            SessionInfo.getOrCreateCurSessionInfo().setPartRef(refPartenaire);
                            System.out.println("Partenaire connecté avec ID : " + id);
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(VueConnexion.class.getName()).log(Level.SEVERE, null, ex);
            }
                    UI.getCurrent().navigate("partenaire/vue");
                }
                case "SRI" -> {
                    
                    try (Connection connection = ConnectionSimpleSGBD.defaultCon()) {
                    String rech = "SELECT idSRI, refSRI FROM SRI WHERE idcoSRI =? AND mdpSRI = ? LIMIT 1";
            try (PreparedStatement pst = connection.prepareStatement(rech)){
                pst.setString(1, username);
                pst.setString(2, password);
                try (ResultSet rs = pst.executeQuery()){
                    if (rs.next()) {
                        int idSRI = rs.getInt("idSRI");
                        String refSRI = rs.getString("refSRI");
                        SessionInfo.getOrCreateCurSessionInfo().setLoggedSRIId(idSRI);
                        SessionInfo.getOrCreateCurSessionInfo().setLoggedSRIref(refSRI);                        
                        System.out.println("Profil SRI connecté avec ID : " + idSRI);
                    }
                }
            }  
                    UI.getCurrent().navigate("SRI/vue");
                    
                }
                    
                }
                default -> Notification.show("Erreur de rôle");
            }
        } else {
            Notification.show("Identifiant ou mot de passe incorrect");
        }
    } catch (SQLException ex) {
        Logger.getLogger(VueConnexion.class.getName()).log(Level.SEVERE, null, ex);
    }
});
    }}
        
        
        
        
       