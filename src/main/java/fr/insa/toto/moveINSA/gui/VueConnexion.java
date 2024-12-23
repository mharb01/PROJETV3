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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Route("connexion")
public class VueConnexion extends VerticalLayout {

    public VueConnexion() throws SQLException {
        
        this.setWidthFull();
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);      

        String role = SessionInfo.getOrCreateCurSessionInfo().getUserRole();
        System.out.println("Rôle reçu dans VueConnexion : " + role);

        TextField usernameField = new TextField("Identifiant");
        PasswordField passwordField = new PasswordField("Mot de passe");
        Button loginButton = new Button("Se connecter");
        
        add(usernameField, passwordField, loginButton);

        loginButton.addClickListener(event -> {
    String username = usernameField.getValue().trim();
    String password = passwordField.getValue().trim();
    
    Boolean verif;
    try {
        verif = SimpleAuthService.authenticate(username, password, role);
        System.out.println("Vérification réussie: " + verif); 

        if (verif) {
            switch (role) {
                case "Etudiant" -> UI.getCurrent().navigate("etudiant/vue");
                case "Partenaire" -> UI.getCurrent().navigate("partenaire/vue");
                case "SRI" -> UI.getCurrent().navigate("SRI/vue");
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
        
        
        
        
       