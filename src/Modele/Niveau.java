package Modele;

import Global.Configuration;
import Structures.Iterateur;

public class Niveau extends Historique<Coup> implements Cloneable {
	/* Une case contient un entier pour coder MUR, POUSSEUR, BUT, POUSSEUR_SUR_BUT, ...
       On choisit de prendre un bit différent de notre entier pour coder la présence
       de chaque objet
	*/
    final int MUR = 1;
	final int POUSSEUR = 2;
	final int BUT = 4;
	final int CAISSE = 8;

	int [][] contenu;
	int l, c;
	String nom;
	int pousseurL, pousseurC;
	int nbBut;
	int nbCaisseSurBut;
	int nbPas, nbPoussees;

	Niveau() {
		contenu = new int[1][1];
		l = 0;
		c = 0;
		int nbPas = 0;
		int nbPoussees = 0;
	}

	@Override
	public Niveau clone() {
		Niveau clone = null;
		try {
			clone = (Niveau) super.clone();
			clone.contenu = contenu.clone();
		} catch (CloneNotSupportedException e) {
			Configuration.instance().logger().severe("Bug interne serieux avec le clone");
			System.exit(1);
		}
		return clone;
	}

	int ajuste(int c, int i) {
		while (c <= i) {
			c *= 2;
		}
		return c;
	}

	void redimensionne(int l, int c) {
		int oldL = contenu.length;
		int oldC = contenu[0].length;
		if ((oldL <= l) || (oldC <= c)) {
			int newL = ajuste(oldL, l);
			int newC = ajuste(oldC, c);
			int [][] newTab = new int[newL][newC];
			for (int i=0; i<oldL; i++)
				for (int j=0; j<oldC; j++)
					newTab[i][j] = contenu[i][j];
			contenu = newTab;
		}
	}

	void fixeNom(String s) {
		nom = s;
	}

	void marquer(int valeur, int l, int c) {
		contenu[l][c] = (contenu[l][c] & 0xFF) | (valeur << 8);
	}

	public int marque(int l, int c) {
		return contenu[l][c] >> 8;
	}

	void videCase(int i, int j) {
		contenu[i][j] = 0;
	}

	void ajoute(int element, int i, int j) {
		redimensionne(i, j);
		contenu[i][j] |= element;
		if (l <= i)
			l = i+1;
		if (c <= j)
			c = j+1;
		if (element == CAISSE)
			if (aBut(i, j))
				nbCaisseSurBut++;
		if (element == POUSSEUR) {
			pousseurL = i;
			pousseurC = j;
		}

	}

	void supprime(int element, int i, int j) {
		contenu[i][j] &= ~element;
		if (element == CAISSE)
			if (aBut(i, j))
				nbCaisseSurBut--;
	}

	public Coup determinerCoup(int dL, int dC) {
		Coup resultat = new Coup();
		int destL = pousseurL+dL;
		int destC = pousseurC+dC;
		boolean caisseEnlevee = false;
		if (aCaisse(destL, destC)) {
			int caisseL = destL+dL;
			int caisseC = destC+dC;
			if (estLibre(caisseL, caisseC)) {
				resultat.deplace(destL, destC, caisseL, caisseC);
				caisseEnlevee = true;
			}
		}
		if (caisseEnlevee || estLibre(destL, destC)) {
			resultat.deplace(pousseurL, pousseurC, destL, destC);
			return resultat;
		} else
			return null;
	}

	public void jouerCoup(Coup cp) {
		cp.fixerNiveau(this);
		nouveau(cp);
	}

	int contenu(int l, int c) {
		return contenu[l][c] & (POUSSEUR | CAISSE);
	}

	void ajouteMur(int i, int j) {
		// System.out.println("Ajout d'un mur en (" + i + ", " + j + ")");
		ajoute(MUR, i, j);
	}

	void ajoutePousseur(int i, int j) {
		// System.out.println("Ajout d'un pousseur en (" + i + ", " + j + ")");
		ajoute(POUSSEUR, i, j);
		pousseurL = i;
		pousseurC = j;
	}

	void ajouteCaisse(int i, int j) {
		// System.out.println("Ajout d'une caisse en (" + i + ", " + j + ")");
		ajoute(CAISSE, i, j);
		if (aBut(i, j))
			nbCaisseSurBut++;
	}

	void ajouteBut(int i, int j) {
		// System.out.println("Ajout d'un but en (" + i + ", " + j + ")");
		ajoute(BUT, i, j);
		nbBut++;
		if (aCaisse(i, j))
			nbCaisseSurBut++;
	}

	void modifDecompte(int contenu, int modif) {
		switch (contenu) {
			case POUSSEUR:
				nbPas += modif;
				break;
			case CAISSE:
				nbPoussees += modif;
		}
	}

	public int nbPas() {
		return nbPas;
	}

	public int nbPoussees() {
		return nbPoussees;
	}

	public int lignes() {
		return l;
	}

	public int colonnes() {
		return c;
	}

	String nom() {
		return nom;
	}

	boolean estVide(int l, int c) {
		return contenu[l][c] == 0;
	}

	public boolean aMur(int l, int c) {
		return (contenu[l][c] & MUR) != 0;
	}

	public boolean aBut(int l, int c) {
		return (contenu[l][c] & BUT) != 0;
	}

	public boolean aPousseur(int l, int c) {
		return (contenu[l][c] & POUSSEUR) != 0;
	}

	public boolean aCaisse(int l, int c) {
		return (contenu[l][c] & CAISSE) != 0;
	}

	boolean estLibre(int l, int c) {
		return !aMur(l,c) && !aCaisse(l,c);
	}

	public boolean estTermine() {
		return nbCaisseSurBut == nbBut;
	}

	public int pousseurL() {
		return pousseurL;
	}

	public int pousseurC() {
		return pousseurC;
	}
}
