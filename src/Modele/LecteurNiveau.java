package Modele;

import java.util.Scanner;
import java.io.InputStream;

public class LecteurNiveau {
	Scanner s;

	public LecteurNiveau(InputStream in) {
		s = new Scanner(in);
	}

	Niveau lisProchainNiveau() {
		Niveau n = new Niveau();
		String ligne = null;
		int i=0;

		try {
			ligne = s.nextLine();
		} catch (Exception e) {
			return null;
		}
		while (ligne.length() > 0) {
			// System.out.println("[" + ligne + "]");
			if (ligne.charAt(0) == ';') {
				int j=1;
				while (ligne.charAt(j) == ' ') {
					j++;
				}
				n.fixeNom(ligne.substring(j));
			} else {
				for (int j=0; j<ligne.length(); j++) {
					switch(ligne.charAt(j)) {
					case '#':
						n.ajouteMur(i,j);
						break;
					case '@':
						n.ajoutePousseur(i,j);
						break;
					case '$':
						n.ajouteCaisse(i,j);
						break;
					case '+':
						n.ajoutePousseur(i,j);
						n.ajouteBut(i,j);
						break;
					case '*':
						n.ajouteCaisse(i,j);
						n.ajouteBut(i,j);
						break;
					case '.':
						n.ajouteBut(i,j);
						break;
					case ' ':
						break;
					default:
						System.err.println("Caractère inconnu lors de la lecture de niveau");
						System.exit(0);
					}
				}
			}
			ligne = s.nextLine();
			i++;
		}
		return n;
	}
}
