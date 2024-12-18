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
package fr.insa.toto.moveINSA.gui.vueSRI;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.beuvron.vaadin.utils.dataGrid.ColumnDescription;
import fr.insa.beuvron.vaadin.utils.dataGrid.GridDescription;
import fr.insa.beuvron.vaadin.utils.dataGrid.ResultSetGrid;
import fr.insa.toto.moveINSA.gui.MainLayoutSRI;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.gui.vueetudiant.OffreEtGrid;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author francois
 */
@PageTitle("MoveINSA")
@Route(value = "SRI/vue/offres/liste", layout = MainLayoutSRI.class)
public class OffresPanel extends VerticalLayout {

    /**
     * Un petit composant qui affiche un entier sous forme d'un ensemble d'icones.
     */
    public static class IntAsIcon extends HorizontalLayout {

        public IntAsIcon(int nbr) {
            for (int i = 0; i < nbr; i++) {
                this.add(new Icon(VaadinIcon.EXIT));
            }
        }
    }
    private OffreGrid offresGrid;
    private ResultSetGrid gOffres;
    private Button bPostule;

    public OffresPanel(){
        
        Image liste = new Image("https://cdn-icons-png.flaticon.com/512/1472/1472457.png", "Voir tout");
             liste.setWidth("100px");
             liste.setHeight("100px");
             Button offreListe = new Button (liste);
             offreListe.setWidthFull();
             offreListe.setHeight("100px");
             offreListe.setText("Voir toutes les offres");
             offreListe.addClickListener(event -> 
             {  try (Connection con = ConnectionPool.getConnection()) {                 
                 if (offresGrid != null) {
                this.removeAll();  }  //Efface la liste précédente 
                this.add(new H3("Voici toutes les offres"));
                 offresGrid = new OffreGrid(OffreMobilite.toutesLesOffres(con));
                 this.add(offresGrid);
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
             
             
             
        Image supp = new Image("https://icons.veryicon.com/png/o/education-technology/learning-to-bully-the-king/delete-351.png", "Voir selon partenaire");
             supp.setWidth("100px");
             supp.setHeight("100px");
             Button supprimer = new Button (supp);
             supprimer.setWidthFull();
             supprimer.setHeight("100px");
             supprimer.setText("Supprimer toutes les offres"); 
             supprimer.addClickListener(event -> { // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H3("Attention"));
                layout.add(new Text("Êtes-vous sûr de vouloir supprimer toutes les offres ?"));
                
                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Oui", e -> {
                    
                    try (Connection con = ConnectionPool.getConnection()) {
                    OffreMobilite.suppALLConsole(con);
                    Notification.show("Toutes les offres ont été supprimées avec succès ! ");
                    dialog.close(); // Fermer le dialog
                
                    } catch (SQLException ex) {
                System.out.println("Probleme : " + ex.getLocalizedMessage());
                Notification.show("Probleme : " + ex.getLocalizedMessage());
            }
                    });
                
                Button cancelButton = new Button("Non", e -> {
                    dialog.close(); // Fermer le dialog sans faire d'action
                });

                // Ajouter les boutons au layout
                layout.add(confirmButton, cancelButton);

                // Ajouter le layout au dialog
                dialog.add(layout);

                // Afficher le dialog
                dialog.open();
            });
             
             
             VerticalLayout buttonLayout = new VerticalLayout (offreListe, offrePart, offrePays, supprimer);
             buttonLayout.setSpacing(true);
             this.add(buttonLayout);     
    }

    private void choisir(String role) {
        SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
        sessionInfo.setUserRole(role);
        
        switch (role) {
            case "pays" -> this.getUI().ifPresent(ui ->ui.navigate("SRI/vue/offre/rechercher/pays"));
            case "part" -> this.getUI().ifPresent(ui ->ui.navigate("SRI/vue/offre/rechercher/partenaire"));
            default -> 
                this.add(new Paragraph("Erreur : Role inconnu"));
               
                }
        }
    
}
