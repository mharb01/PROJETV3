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
package fr.insa.toto.moveINSA.gui;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import com.vaadin.flow.component.button.Button; 
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;


import fr.insa.toto.moveINSA.gui.session.SessionInfo;
@PageTitle("MoveINSA")
@Route(value = "", layout = MainLayout.class)
public class VuePrincipale extends VerticalLayout {

    public VuePrincipale () {
        this.setWidthFull();
        this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        this.add(new H3("Mov'INSA: l'application de l'avenir"));
        List<Paragraph> attention = List.of(
                new Paragraph("Veuillez vous identifier")
        );
        attention.get(0).getStyle().set("color", "grey");
        attention.forEach((p) -> this.add(p));

        Image etudiant = new Image("https://cdn.icon-icons.com/icons2/1861/PNG/512/student3_118124.png", "Etudiant");
        etudiant.setWidth("100px");
        etudiant.setHeight("100px");
        
        Button etudiantButton = new Button (etudiant);
        etudiantButton.setWidthFull();
        etudiantButton.setHeight("100px");
        etudiantButton.setText("Je suis Etudiant"); 
        etudiantButton.addClickListener(event -> { choisirRole("Etudiant");});
        
        Image partenaire = new Image("https://cdn-icons-png.flaticon.com/512/167/167707.png", "Partenaire");
        partenaire.setWidth("100px");
        partenaire.setHeight("100px");
        Button partenaireButton = new Button (partenaire); 
        partenaireButton.setWidthFull();
        partenaireButton.setHeight("100px");
        partenaireButton.setText("Je suis Partenaire");
        partenaireButton.addClickListener(event -> { choisirRole("Partenaire");});
        
       Image SRI = new Image("https://cdn-icons-png.flaticon.com/512/1794/1794733.png", "SRI");
       SRI.setWidth("100px");
       SRI.setHeight("100px");
       Button SRIButton = new Button (SRI);
       SRIButton.setWidthFull();
       SRIButton.setHeight("100px");
       SRIButton.setText("Système des Relations Internationales");        
       SRIButton.addClickListener(event -> { choisirRole("SRI");});
        
        VerticalLayout buttonLayout = new VerticalLayout (etudiantButton, partenaireButton, SRIButton);
        buttonLayout.setSpacing(true);
        this.add(buttonLayout);
    }
    
    
    
    
    private void choisirRole(String role) {
        SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
        sessionInfo.setUserRole(role);
        
        switch (role) {
            case "Etudiant" -> this.getUI().ifPresent(ui ->ui.navigate("etudiant/vue"));
            case "Partenaire" -> this.getUI().ifPresent(ui ->ui.navigate("partenaire/vue"));
            case "SRI" -> this.getUI().ifPresent(ui ->ui.navigate("SRI/vue"));
            default -> 
                this.add(new Paragraph("Erreur : Role inconnu"));
               
                }
        }
        
    }
    

