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
import fr.insa.toto.moveINSA.model.Etudiant;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author HP
 */
public class EtudiantGrid extends Grid <Etudiant>{
            public EtudiantGrid(List<Etudiant> etudiants) {
                this.setColumnReorderingAllowed(true);
                this.addColumn(Etudiant::getId).setHeader("Id").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getIne).setHeader("INE").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getNom).setHeader("Nom").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getClasse).setHeader("Classe").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getClassement).setHeader("Classement").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getidco).setHeader("Identifiant").setSortable(true).setResizable(true);
                this.addColumn(Etudiant::getmdp).setHeader("Mot de passe").setSortable(true).setResizable(true);
                
                this.addComponentColumn(etudiant -> {
            Button modifButton = new Button("Modifier");

            modifButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                
                layout.add(new H3("Modification"));
                layout.add(new Text("Veuillez remplir uniquement le(s) champ(s) à modifier "));

                
                TextField ineField = new TextField("Nouvel INE");
                ineField.setValue(String.valueOf(etudiant.getIne())); // Valeur existante
                
                TextField nomField = new TextField("Nouveau nom");
                nomField.setValue(String.valueOf(etudiant.getNom()));

                TextField classeField = new TextField("Nouvelle Classe");
                classeField.setValue(etudiant.getClasse()); // Valeur existante

                TextField classementField = new TextField("Nouveau Classement");
                classementField.setValue(String.valueOf(etudiant.getClassement()));
                
                TextField idcoField = new TextField("Nouvel Identifiant");
                idcoField.setValue(etudiant.getidco());
                
                TextField mdpField = new TextField("Nouveau Mot De Passe");
                mdpField.setValue(etudiant.getmdp());
                
                layout.add(ineField, nomField, classeField, classementField, idcoField, mdpField);

                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Confirmer", e -> {
                    
                     String newINE = ineField.getValue().isEmpty() ? etudiant.getIne(): ineField.getValue();                     
                     String newNom = nomField.getValue().isEmpty() ? etudiant.getNom(): nomField.getValue();
                     String newClasse = classeField.getValue().isEmpty() ? etudiant.getClasse(): classeField.getValue();
                     int newClassement = classementField.getValue().isEmpty() ? etudiant.getClassement(): Integer.parseInt(classementField.getValue());
                     String newIdco = idcoField.getValue().isEmpty() ? etudiant.getidco(): idcoField.getValue();
                     String newMdp = mdpField.getValue().isEmpty() ? etudiant.getmdp(): mdpField.getValue();
                    
                     try (Connection con = ConnectionPool.getConnection()) {
 
                        EtudiantGrid.modifier(con, etudiant.getId(), newINE, newNom, newClasse, newClassement, newIdco, newMdp);
                        
                        Notification.show("Profil : " + etudiant.getId() + " modifié avec succès ! ");
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
                
                this.addComponentColumn(etudiant -> {
            Button suppButton = new Button("Supprimer");

            suppButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H3("Attention"));
                layout.add(new Text("Êtes-vous sûr de vouloir supprimer le profil étudiant : " + etudiant.getId() + " ? "));
                
                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Oui", e -> {
                                       
                    try (Connection con = ConnectionPool.getConnection()) {
                    EtudiantGrid.supprimer(con, etudiant);
                    Notification.show("Etudiant : " + etudiant.getId() + " supprimé avec succès ! ");
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

                
                this.setItems(etudiants);
            }
    

            public static void modifier(Connection con, int id, String ine, String nom, String classe, int classement, String idco, String mdp) throws SQLException {
    String sql = "update etudiant set ine = ? , nom = ? , classe = ? , classement = ? , idcoEtudiant = ? , mdpEtudiant = ? where idEtudiant = ?";
    try (PreparedStatement update = con.prepareStatement(sql)) {
        update.setString(1, ine);
        update.setString(2, nom);
        update.setString(3, classe);
        update.setInt(4, classement);
        update.setString(5, idco);
        update.setString(6, mdp);
        update.setInt(7, id);
        update.executeUpdate();
    }
    System.out.println("Profil étudiant modifié avec succès !");
}
            
            
            public static void supprimer(Connection con, Etudiant etudiant) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "delete from etudiant where id = ? ")){ 
                 int nbrPlaces = etudiant.getId();             
                 update.setInt(1,nbrPlaces);
                 update.execute();       
                 }
        System.out.println("Profil supprime avec succes !");
    }
            
            
}
