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
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.model.Candidature;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author HP
 */
public class CandidatureGrid extends Grid <Candidature> {
    
    public CandidatureGrid(List<Candidature> candidatures) {
        
                this.setColumnReorderingAllowed(true);
                this.addColumn(Candidature::getIdCandidature).setHeader("Id").setSortable(true).setResizable(true);
                this.addColumn(Candidature::getINE).setHeader("INE etudiant").setSortable(true).setResizable(true);
                this.addColumn(Candidature::getIdOffreMobilite).setHeader("Offre").setSortable(true).setResizable(true);
                this.addColumn(Candidature::getDate).setHeader("Date").setSortable(true).setResizable(true);
                this.addColumn(Candidature::getIdPartenaire).setHeader("IdPartenaire").setSortable(true).setResizable(true);

                this.addComponentColumn(candidature -> {
            Button suppButton = new Button("Supprimer");

            suppButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H3("Attention"));
                layout.add(new Text("Êtes-vous sûr de vouloir supprimer la candidature : " + candidature.getIdCandidature() + " ? "));
                
                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Oui", e -> {
                                       
                    try (Connection con = ConnectionPool.getConnection()) {
                    CandidatureGrid.supprimer(con, candidature);
                    Notification.show("Candidature : " + candidature.getIdCandidature() + " supprimée avec succès ! ");
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

            return suppButton;
        });
       this.setItems(candidatures);

    }
        public static void supprimer(Connection con, Candidature candidature) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from candidature where idCandidature = ? ")){ 
                 int id = candidature.getIdCandidature();
                 update.setInt(1,id);
                 update.execute();       
                 }
        System.out.println("Candidature supprimee avec succes !");
    }
    
}

