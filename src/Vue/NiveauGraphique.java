package Vue;

public interface NiveauGraphique {
	int largeur();

	int hauteur();

	void tracerImage(ImageSokoban img, int x, int y, int largeurCase, int hauteurCase);
	void tracerCroix(int valeur, int x, int y, int largeurCase, int hauteurCase);

	void decale(double dL, double dC, int l, int c);

	void etapePousseur();

	void changeDirectionPousseur(int dL, int dC);
}
