package Modele;

public class Deplacement {
	public int [] l, c;
	int contenu;

	Deplacement(int dL, int dC, int vL, int vC) {
		l = new int[2];
		c = new int[2];
		l[0] = dL;
		c[0] = dC;
		l[1] = vL;
		c[1] = vC;
	}
}
