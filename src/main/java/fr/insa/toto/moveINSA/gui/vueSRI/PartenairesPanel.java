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
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.gui.MainLayoutSRI;
import fr.insa.toto.moveINSA.gui.MainLayoutSRI;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.model.Partenaire;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
@PageTitle("MoveINSA")
@Route(value = "SRI/vue/partenaires/liste", layout = MainLayoutSRI.class)
public class PartenairesPanel extends VerticalLayout {
    private PartenaireGrid partGrid;
    public PartenairesPanel() {
        
        Image liste = new Image("https://cdn-icons-png.flaticon.com/512/1472/1472457.png", "Voir tout");
             liste.setWidth("100px");
             liste.setHeight("100px");
             Button partListe = new Button (liste);
             partListe.setWidthFull();
             partListe.setHeight("100px");
             partListe.setText("Voir tous les partenaires"); 
             partListe.addClickListener(event ->
             {  try (Connection con = ConnectionPool.getConnection()) {                 
                 if (partGrid != null) {
                this.removeAll();  }  //Efface la liste précédente 
                this.add(new H3("Tous les partenaires"));
                 partGrid = new PartenaireGrid(Partenaire.tousLesPartaires(con));
                 this.add(partGrid);
                } catch (SQLException ex) {
                    System.out.println("Probleme : " + ex.getLocalizedMessage());
                    Notification.show("Probleme : " + ex.getLocalizedMessage());
                }
             });
        
        Image pays = new Image("https://i.pinimg.com/originals/e8/97/63/e8976376980363ea178ca2a5894ad68a.png", "Voir selon pays");
             pays.setWidth("110px");
             pays.setHeight("100px");
             Button partPays = new Button (pays);
             partPays.setWidthFull();
             partPays.setHeight("100px");
             partPays.setText("Voir par pays");
             partPays.addClickListener(event -> { choisir("pays");});  
             
             
             
             Image ref = new Image("https://cdn-icons-png.flaticon.com/512/2247/2247882.png", "Voir Avec INE");
             ref.setWidth("100px");
             ref.setHeight("100px");             
             Button partRef = new Button (ref);
             partRef.setWidthFull();
             partRef.setHeight("100px");
             partRef.setText("Voir par Référence"); 
             partRef.addClickListener(event -> { choisir("ref");});
               
             
             Image supp = new Image("https://icons.veryicon.com/png/o/education-technology/learning-to-bully-the-king/delete-351.png", "Voir selon partenaire");
             supp.setWidth("100px");
             supp.setHeight("100px");
             Button supprimer = new Button (supp);
             supprimer.setWidthFull();
             supprimer.setHeight("100px");
             supprimer.setText("Supprimer tous les partenaires"); 
             supprimer.addClickListener(event -> { // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H3("Attention"));
                layout.add(new Text("Êtes-vous sûr de vouloir supprimer tous les profils partenaires ?"));
                
                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Oui", e -> {
                    
                    try (Connection con = ConnectionPool.getConnection()) {
                    Partenaire.suppALLConsole(con);
                    Notification.show("Toutes les profils partenaires ont été supprimés avec succès ! ");
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
             
             VerticalLayout buttonLayout = new VerticalLayout (partListe, partPays, partRef, supprimer);
             buttonLayout.setSpacing(true);
             this.add(buttonLayout);
        
    }
    
    private void choisir(String role) {
        SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
        sessionInfo.setUserRole(role);
        
        switch (role) {
            case "pays" -> this.getUI().ifPresent(ui ->ui.navigate("SRI/vue/partenaire/rechercher/pays"));
            case "ref" -> this.getUI().ifPresent(ui ->ui.navigate("SRI/vue/partenaire/rechercher/reference"));
            default -> 
                this.add(new Paragraph("Erreur : Role inconnu"));
               
                }
        }
    
}
