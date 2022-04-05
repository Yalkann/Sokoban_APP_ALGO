package Modele;

import Global.Configuration;
import Patterns.Observable;

public class Jeu extends Observable {
	Niveau courant;
	LecteurNiveau lecteur;

	public Jeu(LecteurNiveau l) {
		lecteur = l;
		prochainNiveau();
	}

	public Niveau niveau() {
		return courant;
	}

	public Coup determinerCoup(int dL, int dC) {
		return courant.determinerCoup(dL, dC);
	}

	public void jouerCoup(Coup cp) {
		if (cp == null) {
			Configuration.instance().logger().info("Déplacement impossible");
		} else {
			courant.jouerCoup(cp);
			miseAJour();
		}
	}

	public Coup annule() {
		Coup cp = courant.annuler();
		miseAJour();
		return cp;
	}

	public Coup refaire() {
		Coup cp = courant.refaire();
		miseAJour();
		return cp;
	}

	public boolean prochainNiveau() {
		courant = lecteur.lisProchainNiveau();
		return courant != null;
	}

	public int pousseurL() {
		return courant.pousseurL();
	}

	public int pousseurC() {
		return courant.pousseurC();
	}
}
