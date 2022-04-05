package Controleur;

import Global.Configuration;
import Modele.Coup;
import Modele.Jeu;
import Modele.Niveau;
import Structures.Sequence;
import Vue.InterfaceUtilisateur;

public class AnimationIA extends Animation {
	IA ia;
	Jeu jeu;
	Sequence<Coup> coups;
	ControleurMediateur control;

	AnimationIA(ControleurMediateur c, Jeu j) {
		super(Integer.parseInt(Configuration.instance().lis("LenteurIA")));
		ia = new IAAleatoire();
		jeu = j;
		coups = Configuration.instance().nouvelleSequence();
		control = c;
	}
	@Override
	void miseAJour() {
		Niveau n = jeu.niveau().clone();
		if (coups.estVide()) {
			coups = ia.joue(n);
		}
		if (coups.estVide()) {
			Configuration.instance().logger().warning("L'IA n'a pas joué de coup");
		} else {
			Coup cp = coups.extraitTete();
			control.jouerCoup(cp);
		}
 	}
}
