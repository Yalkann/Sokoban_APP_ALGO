package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {
	NiveauGraphiqueSwing niveau;
	CollecteurEvenements controle;

	AdaptateurSouris(NiveauGraphiqueSwing n, CollecteurEvenements c) {
		niveau = n;
		controle = c;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int c = e.getX() / niveau.largeurCase();
		int l = e.getY() / niveau.hauteurCase();
		controle.clicSouris(l, c);
	}
}
