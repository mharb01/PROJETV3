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
import fr.insa.toto.moveINSA.gui.vueSRI.EtudiantGrid;
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
import fr.insa.toto.moveINSA.gui.session.SessionInfo;
import fr.insa.toto.moveINSA.model.Candidature;
import fr.insa.toto.moveINSA.model.Etudiant;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 * @author HP
 */
@PageTitle("MoveINSA")
@Route(value = "SRI/vue/candidature/liste", layout = MainLayoutSRI.class)
public class CandidaturePanel extends VerticalLayout {
    private CandidatureGrid CandidatureGrid;
    
    public CandidaturePanel() {
        
        Image liste = new Image("https://cdn-icons-png.flaticon.com/512/1472/1472457.png", "Voir tout");
             liste.setWidth("100px");
             liste.setHeight("100px");
             Button candListe = new Button (liste);
             candListe.setWidthFull();
             candListe.setHeight("100px");
             candListe.setText("Voir toutes les candidatures"); 
             candListe.addClickListener(event -> 
             {  try (Connection con = ConnectionPool.getConnection()) {                 
                 if (CandidatureGrid != null) {
                this.removeAll();  }  //Efface la liste précédente 
                this.add(new H3("Voici toutes les candidatures "));
                 CandidatureGrid = new CandidatureGrid(Candidature.toutesLesCandidatures(con));
                 this.add(CandidatureGrid);
                } catch (SQLException ex) {
                    System.out.println("Probleme : " + ex.getLocalizedMessage());
                    Notification.show("Probleme : " + ex.getLocalizedMessage());
                }
             });
             
             Image ine = new Image("https://cdn-icons-png.flaticon.com/512/2247/2247882.png", "Voir Avec INE");
             ine.setWidth("100px");
             ine.setHeight("100px");             
             Button candINE = new Button (ine);
             candINE.setWidthFull();
             candINE.setHeight("100px");
             candINE.setText("Rechercher par INE"); 
             candINE.addClickListener(event -> { choisir("ine");});
            
             
             Image partenaire = new Image("https://cdn-icons-png.flaticon.com/512/167/167707.png", "Partenaire");
             partenaire.setWidth("100px");
             partenaire.setHeight("100px"); 
             Button candPartenaire = new Button (partenaire);
             candPartenaire.setWidthFull();
             candPartenaire.setHeight("100px");
             candPartenaire.setText("Rechercher par partenaire"); 
             candPartenaire.addClickListener(event -> { choisir("partenaire");});
             
             
             Image supp = new Image("https://icons.veryicon.com/png/o/education-technology/learning-to-bully-the-king/delete-351.png", "Voir selon partenaire");
             supp.setWidth("100px");
             supp.setHeight("100px");
             Button supprimer = new Button (supp);
             supprimer.setWidthFull();
             supprimer.setHeight("100px");
             supprimer.setText("Supprimer toutes les candidatures"); 
             supprimer.addClickListener(event -> { // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H3("Attention"));
                layout.add(new Text("Êtes-vous sûr de vouloir supprimer toutes les candidatures ?"));
                
                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Oui", e -> {
                    
                    try (Connection con = ConnectionPool.getConnection()) {
                    Candidature.deleteAllConsole(con);
                    Notification.show("Toutes les candidatures ont été supprimées avec succès ! ");
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
             
             VerticalLayout buttonLayout = new VerticalLayout (candListe, candINE, candPartenaire, supprimer);
             buttonLayout.setSpacing(true);
             this.add(buttonLayout);
        }
    
     
        private void choisir(String role) {
        SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
        sessionInfo.setUserRole(role);
        
        switch (role) {
            case "ine" -> this.getUI().ifPresent(ui ->ui.navigate("SRI/vue/candidature/rechercher/ine"));
            case "partenaire" -> this.getUI().ifPresent(ui ->ui.navigate("SRI/vue/candidature/rechercher/partenaire"));
            default -> 
                this.add(new Paragraph("Erreur : Role inconnu"));
               
                }
        }
    }

