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
import com.vaadin.flow.data.renderer.ComponentRenderer;
import fr.insa.toto.moveINSA.model.OffreMobilite;
import fr.insa.toto.moveINSA.model.Partenaire;
import java.util.List;
/**
 *
 * @author HP
 */
public class OffreEtGrid extends Grid <OffreMobilite> {
    public OffreEtGrid(List<OffreMobilite> offremobilite){
        this.setColumnReorderingAllowed(true);
                this.addColumn(OffreMobilite::getId).setHeader("id").setSortable(true).setResizable(true);
                this.addColumn(OffreMobilite::getNbrPlaces).setHeader("Nombre de places").setSortable(true).setResizable(true);
                this.addColumn(OffreMobilite::getPartenaire).setHeader("Proposé par").setSortable(true).setResizable(true);
                this.addColumn(OffreMobilite::getClasse).setHeader("Classe cible").setSortable(true).setResizable(true);
                // Ajouter une colonne pour le bouton "Candidater"
        this.addComponentColumn(offre -> {
            Button candidaterButton = new Button("Candidater");

            candidaterButton.addClickListener(event -> {
                // Créer le dialog
                Dialog dialog = new Dialog();
                dialog.setWidth("400px");  // Définir la largeur du dialog

                // Ajouter un message de confirmation
                VerticalLayout layout = new VerticalLayout();
                layout.add(new H3("Attention"));
                layout.add(new Text("Êtes-vous sûr de candidater pour l'offre : " + offre.getId() + " proposée par " + offre.getPartenaire() + " ? "));
                
                // Boutons pour confirmer ou annuler
                Button confirmButton = new Button("Oui", e -> {
                    
                    Notification.show("Candidature envoyée pour l'offre : " + offre.getId() + " ! ");
                    dialog.close(); // Fermer le dialog
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

            return candidaterButton;
        }).setHeader("Action");

        // Définir les éléments de la grille
        this.setItems(offremobilite);
}
}
