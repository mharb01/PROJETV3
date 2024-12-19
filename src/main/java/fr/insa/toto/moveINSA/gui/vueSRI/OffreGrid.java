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
public class OffreGrid extends Grid <OffreMobilite> {
    private TextField ifPlaces;
    private TextField tfClasse; 
    private Button bSave;
   public OffreGrid(List<OffreMobilite> offremobilite){
        this.setColumnReorderingAllowed(true);
                this.addColumn(OffreMobilite::getId).setHeader("id").setSortable(true).setResizable(true);
                this.addColumn(OffreMobilite::getNbrPlaces).setHeader("Nombre de places").setSortable(true).setResizable(true);
                this.addColumn(OffreMobilite::getPartenaire).setHeader("Proposé par").setSortable(true).setResizable(true);
                this.addColumn(OffreMobilite::getClasse).setHeader("Classe cible").setSortable(true).setResizable(true);
                // Ajouter une colonne pour le bouton "Candidater"
        this.addComponentColumn(offre -> {
            Button modifButton = new Button("Modifier");

            modifButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                
                layout.add(new H3("Modification"));
                layout.add(new Text("Veuillez remplir uniquement le(s) champ(s) à modifier "));

                
                TextField nbrPlacesField = new TextField("Nombre de places");
                nbrPlacesField.setValue(String.valueOf(offre.getNbrPlaces())); // Valeur existante

                TextField classeField = new TextField("Classe cible");
                classeField.setValue(offre.getClasse()); // Valeur existante

                layout.add(nbrPlacesField, classeField);

                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Confirmer", e -> {
                    
                     int newNbrPlaces = nbrPlacesField.getValue().isEmpty() ? offre.getNbrPlaces() : Integer.parseInt(nbrPlacesField.getValue());
                     String newClasse = classeField.getValue().isEmpty() ? offre.getClasse() : classeField.getValue();
                     
                    try (Connection con = ConnectionPool.getConnection()) {
 
                        OffreGrid.modifier(con, offre.getId(), newNbrPlaces, newClasse);
                        
                        Notification.show("Offre : " + offre.getId() + " modifiée avec succès ! ");
                        dialog.close(); // Fermer le dialog
                        
                    } catch (SQLException ex) {
                        Notification.show("Erreur lors de la modification : " + ex.getLocalizedMessage());
                    }

                });
                
                Button cancelButton = new Button("Annuler", e -> {
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
            Button suppButton = new Button("Supprimer");

            suppButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H3("Attention"));
                layout.add(new Text("Êtes-vous sûr de vouloir supprimer l'offre : " + offre.getId() + " proposée par " + offre.getPartenaire() + " ? "));
                
                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Oui", e -> {
                                       
                    try (Connection con = ConnectionPool.getConnection()) {
                    OffreGrid.supprimer(con, offre);
                    Notification.show("Offre : " + offre.getId() + " supprimée avec succès ! ");
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
        
        // Définir les éléments de la grille
        this.setItems(offremobilite);
}
   
   public static void supprimer(Connection con, OffreMobilite offre) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from offremobilite where nbrplaces = ? and proposepar = ?  and classe = ? ")){ 
                 int nbrPlaces = offre.getNbrPlaces();             
                 String classe = offre.getClasse();
                 int proposepar = offre.getProposePar();
                 update.setInt(1,nbrPlaces);
                 update.setInt(2, proposepar);
                 update.setString(3, classe);
                 update.execute();       
                 }
        System.out.println("Offre supprimee avec succes !");
    }
   
   public static void modifier(Connection con, int id, int nbrPlaces, String classe) throws SQLException {
    String sql = "update offremobilite set nbrplaces = ?, classe = ? where id = ?";
    try (PreparedStatement update = con.prepareStatement(sql)) {
        update.setInt(1, nbrPlaces);
        update.setString(2, classe);
        update.setInt(3, id); // Identifiant de l'offre à modifier
        update.executeUpdate();
    }
    System.out.println("Offre modifiée avec succès !");
}
}
