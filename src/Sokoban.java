import Controleur.ControleurMediateur;
import Global.Configuration;
import Modele.Jeu;
import Modele.LecteurNiveau;
import Vue.InterfaceGraphique;
import Vue.InterfaceTextuelle;

import java.io.File;
import java.io.InputStream;

class Sokoban {

	public static void main(String [] args) {
		try {
			String fichierNiveaux = Configuration.instance().lis("Niveaux");
			fichierNiveaux = fichierNiveaux.replace('/', File.separatorChar);
			InputStream in = Configuration.charge(fichierNiveaux);
			LecteurNiveau l = new LecteurNiveau(in);
			Jeu j = new Jeu(l);
			ControleurMediateur c = new ControleurMediateur(j);
			if (args.length > 0) {
				int nb = Integer.parseInt(args[0]);
				for (int i=1; i<nb; i++)
					j.prochainNiveau();
			}
			switch (Configuration.instance().lis("Interface")) {
				case "Textuelle":
					InterfaceTextuelle.demarrer(j, c);
					break;
				case "Graphique":
					InterfaceGraphique.demarrer(j, c);
					break;
				default:
					Configuration.instance().logger().severe("Interface invalide");
					System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
