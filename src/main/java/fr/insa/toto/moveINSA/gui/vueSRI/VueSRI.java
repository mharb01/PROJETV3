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
import fr.insa.toto.moveINSA.gui.MainLayoutSRI;
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

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import fr.insa.toto.moveINSA.gui.MainLayoutEt;
import fr.insa.toto.moveINSA.gui.session.SessionInfo;

/**
 *
 * @author HP
 */
@Route(value = "SRI/vue",layout= MainLayoutSRI.class)
public class VueSRI extends VerticalLayout {
    
    public VueSRI () {
    
    SessionInfo sessionInfo = SessionInfo.getOrCreateCurSessionInfo();
    String loggedSRIref = sessionInfo.getLoggedSRIref();
        
    this.setWidthFull();
    this.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    this.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
     
    
    Image SRI = new Image("https://cdn-icons-png.flaticon.com/512/1794/1794733.png", "SRI");
       SRI.setWidth("100px");
       SRI.setHeight("100px");
       add(SRI);
       
    this.add(new H3("Mov'INSA: l'application de l'avenir"));
    this.add(new H3("Bienvenue " + loggedSRIref + " !"));
    this.add(new H3("Que voulez vous faire ?"));
    }
}
