package Controleur;

public abstract class Animation {
	int lenteur;
	int decompte;

	Animation(int l) {
		lenteur = l;
	}

	abstract void miseAJour();

	void tictac() {
		decompte++;
		if (decompte >= lenteur) {
			miseAJour();
			decompte = 0;
		}
	}

	boolean estTerminee() {
		return false;
	}
}
