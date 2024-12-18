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

import fr.insa.toto.moveINSA.gui.vueSRI.EtudiantGrid;
import com.vaadin.flow.component.button.Button;
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
import fr.insa.toto.moveINSA.model.Etudiant;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
@PageTitle("MoveINSA")
@Route(value = "SRI/vue/etudiant/liste", layout = MainLayoutSRI.class)
public class EtudiantPanel extends VerticalLayout {
    private EtudiantGrid etudiantGrid;
    public EtudiantPanel() {

             Image liste = new Image("https://cdn-icons-png.flaticon.com/512/1472/1472457.png", "Voir tout");
             liste.setWidth("100px");
             liste.setHeight("100px");
             Button etudiantListe = new Button (liste);
             etudiantListe.setWidthFull();
             etudiantListe.setHeight("100px");
             etudiantListe.setText("Voir tous les étudiants"); 
             etudiantListe.addClickListener(event -> 
             {  try (Connection con = ConnectionPool.getConnection()) {                 
                 if (etudiantGrid != null) {
                this.removeAll();  }  //Efface la liste précédente 
                this.add(new H3("Tous les étudiants"));
                 etudiantGrid = new EtudiantGrid(Etudiant.tousLesEtudiants(con));
                 this.add(etudiantGrid);
                } catch (SQLException ex) {
                    System.out.println("Probleme : " + ex.getLocalizedMessage());
                    Notification.show("Probleme : " + ex.getLocalizedMessage());
                }
             });
             
             Image ine = new Image("https://cdn-icons-png.flaticon.com/512/2247/2247882.png", "Voir Avec INE");
             ine.setWidth("100px");
             ine.setHeight("100px");             
             Button etudiantINE = new Button (ine);
             etudiantINE.setWidthFull();
             etudiantINE.setHeight("100px");
             etudiantINE.setText("Rechercher par INE"); 
             etudiantINE.addClickListener(event -> 
             this.getUI().ifPresent(ui ->ui.navigate("SRI/vue/etudiant/rechercher/ine"))
             );
             
             Image classe = new Image("https://cdn-icons-png.flaticon.com/512/7092/7092289.png", "Voir Avec Classe");
             classe.setWidth("100px");
             classe.setHeight("100px"); 
             Button etudiantClasse = new Button (classe);
             etudiantClasse.setWidthFull();
             etudiantClasse.setHeight("100px");
             etudiantClasse.setText("Rechercher par Classe"); 
             etudiantClasse.addClickListener(event -> { choisir("classe");});
             
             VerticalLayout buttonLayout = new VerticalLayout (etudiantListe, etudiantINE, etudiantClasse);
             buttonLayout.setSpacing(true);
             this.add(buttonLayout);
        }
    
    private void choisir(String role) {
        SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
        sessionInfo.setUserRole(role);
        
        switch (role) {
            case "ine" -> this.getUI().ifPresent(ui ->ui.navigate("SRI/vue/etudiant/rechercher/ine"));
            case "classe" -> this.getUI().ifPresent(ui ->ui.navigate("SRI/vue/etudiant/rechercher/classe"));
            default -> 
                this.add(new Paragraph("Erreur : Role inconnu"));
               
                }
        }
    }
    

