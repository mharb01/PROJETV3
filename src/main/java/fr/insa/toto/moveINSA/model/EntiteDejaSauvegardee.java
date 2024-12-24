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
package fr.insa.toto.moveINSA.model;

import fr.insa.beuvron.utils.list.ListUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author francois
 */
public class EntiteDejaSauvegardee extends SQLException {

    public EntiteDejaSauvegardee() {
        super("L'entité à déjà été sauvegardée (id != -1");
    }
    public static List ListePays() {
        List<String> pays = new ArrayList<>();
        pays.add("France");
        pays.add("Allemagne");
        pays.add("Italie");
        pays.add("Espagne");
        pays.add("États-Unis");
        pays.add("Canada");
        pays.add("Japon");
        pays.add("Brésil");
        return pays;
        //chercher comment ajouter un pays
    }
    /*public static String selectInConsolePays() throws SQLException {
        return ListUtils.selectOne("choisissez un pays :",
                ListePays(),(elem) );
    }*/
    public static List ListeClasse() {
        List<String> classe = new ArrayList<>();
        classe.add("GT2E2");
        classe.add("GT2E3");
        classe.add("GE2");
        classe.add("GE3");
        classe.add("GE4");
        classe.add("G2");
        classe.add("G3");
        classe.add("G4");
        classe.add("GM2");
        classe.add("GM3");
        classe.add("GM4");
        classe.add("Pl2");
        classe.add("Pl3");
        classe.add("Pl4");
        classe.add("MIQ2");
        classe.add("MIQ3");
        classe.add("MIQ4");
        return classe;
    }
//    public static String selectInConsoleClasse() throws SQLException {
//        return ListUtils.selectOne("choisissez un pays :",
//                ListeClasse(),(elem) );
//    }
}
