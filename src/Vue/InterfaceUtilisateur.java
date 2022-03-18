package Vue;

public interface InterfaceUtilisateur {
	void basculePleinEcran();
	void decale(double dL, double dC, int l, int c);
	void etapePousseur();
	void changeDirectionPousseur(int dL, int dC);
	void changeEtatIA(boolean e);
	void changeEtatAnim(boolean e);

}
