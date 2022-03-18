package Modele;

import Modele.Niveau;

import java.io.OutputStream;
import java.io.PrintStream;

public class RedacteurNiveau {
	PrintStream p;

	public RedacteurNiveau(OutputStream out) {
		p = new PrintStream(out);
	}

	public void ecrisNiveau(Niveau n) {
		for (int i=0; i<n.lignes(); i++) {
			int dernier = 0;
			for (int j=0; j<n.colonnes(); j++)
				if (!n.estVide(i,j))
					dernier = j;
			for (int j=0; j<=dernier; j++) {
				if (n.aMur(i, j)) {
					p.print('#');
				} else if (n.aPousseur(i, j)) {
					if (n.aBut(i, j))
						p.print('+');
					else
						p.print('@');
				} else if (n.aCaisse(i,j)) {
					if (n.aBut(i, j))
						p.print('*');
					else
						p.print('$');
				} else if (n.aBut(i, j)) {
					p.print('.');
				} else {
					p.print(' ');
				}
			}
			p.println();
		}
		p.println("; " + n.nom());
		p.println();
	}
}
