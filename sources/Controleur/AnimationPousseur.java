package Controleur;

import Global.Configuration;
import Vue.InterfaceUtilisateur;

public class AnimationPousseur extends Animation {
	InterfaceUtilisateur vue;

	AnimationPousseur(InterfaceUtilisateur i) {
		super(Integer.parseInt(Configuration.instance().lis("LenteurPousseur")));
		vue = i;
	}
	@Override
	void miseAJour() {
		vue.etapePousseur();
	}
}
