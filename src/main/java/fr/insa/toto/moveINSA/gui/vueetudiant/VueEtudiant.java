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


import fr.insa.toto.moveINSA.gui.MainLayoutEt;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import com.vaadin.flow.component.button.Button; 
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import fr.insa.toto.moveINSA.gui.MainLayout;

/**
 *
 * @author HP
 */

@Route(value = "etudiant/vue",layout= MainLayoutEt.class)
public class VueEtudiant extends VerticalLayout {
    
    public VueEtudiant (){
        
    this.setWidthFull();
    this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

    Image etudiant = new Image("https://cdn.icon-icons.com/icons2/1861/PNG/512/student3_118124.png", "Etudiant");
    etudiant.setWidth("100px");
    etudiant.setHeight("100px");
    add(etudiant);
    
    
    this.add(new H3("Mov'INSA: l'application de l'avenir"));
    this.add(new H3("Bienvenue cher étudiant !"));
    this.add(new H3("Que voulez vous faire ?"));
    
    
    
}
    
}
