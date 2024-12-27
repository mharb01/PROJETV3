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
package fr.insa.toto.moveINSA.gui.vuepartenaire;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import fr.insa.toto.moveINSA.model.Partenaire;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author HP
 */
public class OffresPartGrid extends Grid <OffreMobilite> {
    private TextField ifPlaces;
    private TextField tfClasse; 
    private TextField tfAnnee;
    private Button bSave;
    
    public OffresPartGrid(List<OffreMobilite> offremobilite){
        
        this.setColumnReorderingAllowed(true);
                this.addColumn(OffreMobilite::getId).setHeader("id").setSortable(true).setResizable(true);
                this.addColumn(OffreMobilite::getNbrPlaces).setHeader("Number of places").setSortable(true).setResizable(true);
                this.addColumn(OffreMobilite::getClasse).setHeader("Targeted class").setSortable(true).setResizable(true);
                this.addColumn(OffreMobilite::getAnnee).setHeader("Year").setSortable(true).setResizable(true);
                
                // Ajouter une colonne pour le bouton "Candidater"
        this.addComponentColumn(offre -> {
            Button modifButton = new Button("Modify");

            modifButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                
                layout.add(new H3("Modification"));
                layout.add(new Text("Please only complete the field(s) to be modified "));

                
                TextField nbrPlacesField = new TextField("New number of places");
                nbrPlacesField.setValue(String.valueOf(offre.getNbrPlaces())); // Valeur existante

                TextField classeField = new TextField("New class targeted");
                classeField.setValue(offre.getClasse()); // Valeur existante
                
                TextField AnneeField = new TextField("New year");
                AnneeField.setValue(String.valueOf(offre.getAnnee()));

                layout.add(nbrPlacesField, classeField, AnneeField);

                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Confirm", e -> {
                    
                     int newNbrPlaces = nbrPlacesField.getValue().isEmpty() ? offre.getNbrPlaces() : Integer.parseInt(nbrPlacesField.getValue());
                     String newClasse = classeField.getValue().isEmpty() ? offre.getClasse() : classeField.getValue();
                     String newAnnee = AnneeField.getValue().isEmpty() ? offre.getAnnee() : AnneeField.getValue();
                     
                    try (Connection con = ConnectionPool.getConnection()) {
 
                        OffresPartGrid.modifier(con, offre.getId(), newNbrPlaces, newClasse, newAnnee);
                        
                        Notification.show("Offer : " + offre.getId() + " modified with success ! ");
                        dialog.close(); // Fermer le dialog
                        
                    } catch (SQLException ex) {
                        Notification.show("Error in the modification : " + ex.getLocalizedMessage());
                    }

                });
                
                Button cancelButton = new Button("Cancel", e -> {
                    dialog.close(); // Fermer le dialog sans faire d'action
                });

                // Ajouter les boutons au layout
                layout.add(confirmButton, cancelButton);

                // Ajouter le layout au dialog
                dialog.add(layout);

                // Afficher le dialog
                dialog.open();
            });

            return modifButton;
        });
        
        
        this.addComponentColumn(offre -> {
            Button suppButton = new Button("Delete");

            suppButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H3("Caution"));
                layout.add(new Text("Are you sure you want to delete the offer : " + offre.getId() + " ? "));
                
                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Yes", e -> {
                                       
                    try (Connection con = ConnectionPool.getConnection()) {
                    OffresPartGrid.supprimer(con, offre);
                    Notification.show("Offer : " + offre.getId() + " deleted with success ! ");
                    dialog.close(); // Fermer le dialog
                
                    } catch (SQLException ex) {
                System.out.println("Probleme : " + ex.getLocalizedMessage());
                Notification.show("Probleme : " + ex.getLocalizedMessage());
            }
                    });
                
                Button cancelButton = new Button("No", e -> {
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
        
        // Définir les éléments de la grille
        this.setItems(offremobilite);
}
   
   public static void supprimer(Connection con, OffreMobilite offre) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from offremobilite where id = ? ")){ 
                 int id = offre.getId();
                 update.setInt(1,id);
                 update.execute();       
                 }
        System.out.println("Offer deleted with success !");
    }
   
   public static void modifier(Connection con, int id, int nbrPlaces, String classe, String annee) throws SQLException {
    String sql = "update offremobilite set nbrplaces = ? , classe = ? , annee = ? where id = ?";
    try (PreparedStatement update = con.prepareStatement(sql)) {
        update.setInt(1, nbrPlaces);
        update.setString(2, classe);
        update.setString(3, annee);
        update.setInt(4, id); // Identifiant de l'offre à modifier
        update.executeUpdate();
    }
    System.out.println("Offer modified with success !");
    }
}
