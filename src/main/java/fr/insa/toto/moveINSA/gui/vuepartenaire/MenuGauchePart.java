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

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import fr.insa.toto.moveINSA.gui.VuePrincipale;
import fr.insa.toto.moveINSA.gui.jeu.BoiteACoucou;
import fr.insa.toto.moveINSA.gui.jeu.TrouveEntier;
import fr.insa.toto.moveINSA.gui.testDataGrid.TestDataGrid;
import fr.insa.toto.moveINSA.gui.testDataGrid.TestGridDirect;
import fr.insa.toto.moveINSA.gui.testDataGrid.TestResultSetGrid;
import fr.insa.toto.moveINSA.gui.vueSRI.NouveauPartenairePanel;
import fr.insa.toto.moveINSA.gui.vueSRI.NouvelleOffrePanel;
import fr.insa.toto.moveINSA.gui.vueSRI.OffresPanel;
import fr.insa.toto.moveINSA.gui.vueSRI.PartenairesPanel;
import fr.insa.toto.moveINSA.gui.RAZBdDPanel;
import fr.insa.toto.moveINSA.gui.TestDriverPanel;

/**
 *
 * @author HP
 */
public class MenuGauchePart extends SideNav {
    
    public MenuGauchePart() {
        SideNavItem main = new SideNavItem("Main",VuePrincipale.class);
        SideNavItem offres = new SideNavItem("Offers");
        offres.addItem(new SideNavItem("my offers", OffresPartPanel.class));
        offres.addItem(new SideNavItem("new", NouvelleOffrePartPanel.class));
        SideNavItem candidatures = new SideNavItem("Applications");
        this.addItem(main, offres, candidatures);
    }
}
