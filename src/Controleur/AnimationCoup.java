package Controleur;

import Global.Configuration;
import Modele.Coup;
import Modele.Deplacement;
import Structures.Iterateur;
import Vue.InterfaceUtilisateur;

public class AnimationCoup extends Animation {
	Coup cp;
	double progres, vitesseAnim;
	InterfaceUtilisateur vue;
	int depuis, vers;

	AnimationCoup(Coup c, InterfaceUtilisateur v, int direction) {
		super(1);
		cp = c;
		vitesseAnim = Double.parseDouble(Configuration.instance().lis("VitesseAnimation"));
		vue = v;
		vers = (direction+1)/2;
		depuis = 1-vers;
		miseAJour();
	}

	@Override
	void miseAJour() {
		progres += vitesseAnim;
		if (progres > 1) {
			progres = 1;
		}
		Iterateur<Deplacement> it = cp.deplacements().iterateur();
		while (it.aProchain()) {
			Deplacement d = it.prochain();
			double dC = (d.c[depuis] - d.c[vers])*(1 - progres);
			double dL = (d.l[depuis] - d.l[vers])*(1 - progres);
			vue.decale(dL, dC, d.l[vers], d.c[vers]);
		}
	}

	@Override
	boolean estTerminee() {
		return progres >= 1;
	}
}
