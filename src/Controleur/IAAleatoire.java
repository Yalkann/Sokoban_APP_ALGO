package Controleur;

import Global.Configuration;
import Modele.Coup;
import Modele.Niveau;
import Structures.Sequence;

import java.util.Random;

class IAAleatoire extends IA {
	Random r;

	IAAleatoire() {
		r = new Random();
	}

	Sequence<Coup> joue(Niveau n) {
		int dL = 0;
		int dC = 0;
		int destL = 0;
		int destC = 0;
		Coup cp = null;

		while ((dL*dL+dC*dC != 1) || (cp == null)) {
			dL = r.nextInt(3) - 1;
			dC = r.nextInt(3) - 1;
			destL = n.pousseurL()+dL;
			destC = n.pousseurC()+dC;
			cp = n.determinerCoup(dL, dC);
		}

		cp.marque(0xFF0000, destL, destC);
		Sequence<Coup> res = Configuration.instance().nouvelleSequence();
		res.insereQueue(cp);
		return res;
	}
}
