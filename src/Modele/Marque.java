package Modele;

public class Marque {
	int l, c;
	int [] valeur;

	Marque(int v, int l, int c) {
		valeur = new int[2];
		valeur[1] = v;
		this.l = l;
		this.c = c;
	}
}
