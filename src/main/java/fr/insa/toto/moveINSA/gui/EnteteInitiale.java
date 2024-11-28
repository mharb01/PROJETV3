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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.model.Partenaire;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author francois
 */
public class EnteteInitiale extends HorizontalLayout {

    private TextField tfNom;
    private TextField tfPays;
    private Button bLogin;
    private Button bLogout;

    public EnteteInitiale() {
        this.setWidthFull();
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        this.tfNom = new TextField("ref partenaire");
        this.tfPays = new TextField("pays");
        this.bLogin = new Button("login");
        this.bLogin.addClickListener((t) -> {
            try (Connection con = ConnectionPool.getConnection()) {
                String ref = this.tfNom.getValue();
                String pays = this.tfPays.getValue();
                Optional<Partenaire> p = Partenaire.trouvePartaire(con, ref, pays);
                if (p.isEmpty()) {
                    Notification.show(ref + " n'est pas un partenaire");
                } else {
                    SessionInfo.doLogin(p.get());
                }
            } catch (SQLException ex) {
                Notification.show("Problem : " + ex.getLocalizedMessage());
            } finally {
                this.refresh();                
            }
        });
        this.bLogout = new Button("logout");
        this.bLogout.addClickListener((t) -> {
            SessionInfo.doLogout();
            this.refresh();
        });
        this.refresh();
        
        
    }

    private void refresh() {
        this.removeAll();
        
        Image insa = new Image("http://www.alsacetech.org/wp-content/uploads/2017/08/Logo_INSAStrasbourgDeveloppe-quadri_marge.jpg", "Meilleure ecole");
         add(insa);
        insa.setWidth("300px");
        insa.setHeight("75px");
        add(insa);
        
        Image logo = new Image("icons/movinsa.jpg", "Meilleur projet");
        logo.setWidth("175px");
        logo.setHeight("75px");
        add(logo); 
        
        if (SessionInfo.connected()) {
            this.add(new H3("Bonjour " + SessionInfo.getLoggedPartRef() + " !")); 
            this.add(this.bLogout);
        } else {
            this.add(this.tfNom, this.tfPays, this.bLogin);
        }
    }

}
