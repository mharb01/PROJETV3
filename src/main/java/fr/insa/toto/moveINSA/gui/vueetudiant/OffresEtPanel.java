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
package fr.insa.toto.moveINSA.gui.vueetudiant;

import fr.insa.toto.moveINSA.gui.vueetudiant.OffreEtGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayoutEt;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author HP
 */
@PageTitle("MoveINSA")
@Route(value = "etudiant/vue/offres/liste", layout = MainLayoutEt.class)
public class OffresEtPanel extends VerticalLayout {
    private OffreEtGrid offresetGrid;
    
    public OffresEtPanel(){
        
        Image liste = new Image("https://cdn-icons-png.flaticon.com/512/1472/1472457.png", "Voir tout");
             liste.setWidth("100px");
             liste.setHeight("100px");
             Button offreListe = new Button (liste);
             offreListe.setWidthFull();
             offreListe.setHeight("100px");
             offreListe.setText("Voir toutes les offres");
             offreListe.addClickListener(event -> 
             {  try (Connection con = ConnectionPool.getConnection()) {                 
                 if (offresetGrid != null) {
                this.removeAll();  }  //Efface la liste précédente 
                this.add(new H3("Voici toutes les offres"));
                 offresetGrid = new OffreEtGrid(OffreMobilite.toutesLesOffres(con));
                 this.add(offresetGrid);
                } catch (SQLException ex) {
                    System.out.println("Probleme : " + ex.getLocalizedMessage());
                    Notification.show("Probleme : " + ex.getLocalizedMessage());
                }
             });
             
             
             
        Image pays = new Image("https://i.pinimg.com/originals/e8/97/63/e8976376980363ea178ca2a5894ad68a.png", "Voir selon pays");
             pays.setWidth("110px");
             pays.setHeight("100px");
             Button offrePays = new Button (pays);
             offrePays.setWidthFull();
             offrePays.setHeight("100px");
             offrePays.setText("Voir les offres selon les pays");
             offrePays.addClickListener(event -> { choisir("pays");});
             
             
        Image part = new Image("https://cdn-icons-png.flaticon.com/512/167/167707.png", "Voir selon partenaire");
             part.setWidth("100px");
             part.setHeight("100px");
             Button offrePart = new Button (part);
             offrePart.setWidthFull();
             offrePart.setHeight("100px");
             offrePart.setText("Voir les offres selon le partenaire"); 
             offrePart.addClickListener(event -> { choisir("part");});
             
             
             VerticalLayout buttonLayout = new VerticalLayout (offreListe, offrePart, offrePays);
             buttonLayout.setSpacing(true);
             this.add(buttonLayout);
    }

    private void choisir(String role) {
        SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
        sessionInfo.setUserRole(role);
        
        switch (role) {
            case "pays" -> this.getUI().ifPresent(ui ->ui.navigate("etudiant/vue/offre/rechercher/pays"));
            case "part" -> this.getUI().ifPresent(ui ->ui.navigate("etudiant/vue/offre/rechercher/partenaire"));
            default -> 
                this.add(new Paragraph("Erreur : Role inconnu"));
               
                }
        }
}
