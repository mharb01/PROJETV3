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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import fr.insa.beuvron.vaadin.utils.ConnectionPool;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import fr.insa.toto.moveINSA.model.Partenaire;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author francois
 */
public class PartenaireGrid extends Grid<Partenaire> {
    
    private TextField tfRef;
    private TextField tfPays; 
    private TextField tfidco;
    private TextField tfmdp;
    private Button bSave;
    
    public PartenaireGrid(List<Partenaire> partenaires) {
        this.setColumnReorderingAllowed(true);
        this.addColumn(Partenaire::getId).setHeader("Id").setSortable(true).setResizable(true);
        this.addColumn(Partenaire::getRefPartenaire).setHeader("Réference").setSortable(true).setResizable(true);
        this.addColumn(Partenaire::getPays).setHeader("Pays").setSortable(true).setResizable(true);
        this.addColumn(Partenaire::getidco).setHeader("Id.connection").setSortable(true).setResizable(true);
        this.addColumn(Partenaire::getmdp).setHeader("Mot de Passe").setSortable(true).setResizable(true);
        
        this.addComponentColumn(part -> {
            Button modifButton = new Button("Modifier");

            modifButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("500px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                
                layout.add(new H3("Modification"));
                layout.add(new Text("Veuillez remplir uniquement le(s) champ(s) à modifier "));

                
                TextField refField = new TextField("Nouvelle référence");
                refField.setValue(String.valueOf(part.getRefPartenaire())); // Valeur existante

                TextField paysField = new TextField("Nouveau pays");
                paysField.setValue(part.getPays()); // Valeur existante
                
                TextField idcoField = new TextField("Nouvel identifiant");
                idcoField.setValue(part.getidco()); // Valeur existante

                TextField mdpField = new TextField("Nouvel mot de passe");
                mdpField.setValue(part.getmdp()); // Valeur existante
                
                layout.add(refField, paysField, idcoField, mdpField );

                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Confirmer", e -> {
                    
                     String newRef = refField.getValue().isEmpty() ? part.getRefPartenaire(): refField.getValue();
                     String newPays = paysField.getValue().isEmpty() ? part.getPays(): paysField.getValue();
                     String newIdco = idcoField.getValue().isEmpty() ? part.getidco(): idcoField.getValue();
                     String newMdp = mdpField.getValue().isEmpty() ? part.getmdp(): mdpField.getValue();
                     
                    try (Connection con = ConnectionPool.getConnection()) {
 
                        PartenaireGrid.modifier(con, part.getId(), newRef, newPays,newIdco, newMdp);
                        
                        Notification.show("Offre : " + part.getId() + " modifiée avec succès ! ");
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
        
        this.addComponentColumn(part -> {
            Button suppButton = new Button("Supprimer");

            suppButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H3("Attention"));
                layout.add(new Text("Êtes-vous sûr de vouloir supprimer le profil : " + part.getId() + " du partenaire " + part.getRefPartenaire()+ " ? "));
                
                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Oui", e -> {
                                       
                    try (Connection con = ConnectionPool.getConnection()) {
                    PartenaireGrid.supprimer(con, part);
                    Notification.show("Partenaire : " + part.getId() + " supprimé avec succès ! ");
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
        
        this.setItems(partenaires);
    }
    
    public static void supprimer(Connection con, Partenaire part) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from partenaire where id = ? ")){ 
                 int id = part.getId();             
                 update.setInt(1,id);
                 update.execute();       
                 }
        System.out.println("Offre supprimee avec succes !");
    }
    
    public static void modifier(Connection con, int id, String refPart, String pays, String idco, String mdp) throws SQLException {
    String sql = "update partenaire set refPartenaire = ?, pays = ? , idcoPartenaire = ? , mdpPartenaire = ? where id = ?";
    try (PreparedStatement update = con.prepareStatement(sql)) {
        update.setString(1, refPart);
        update.setString(2, pays);
        update.setString(3, idco);
        update.setString(4, mdp); 
        update.setInt(5, id);
        update.executeUpdate();
    }
    System.out.println("Profil partenaire modifié avec succès !");
}
}
