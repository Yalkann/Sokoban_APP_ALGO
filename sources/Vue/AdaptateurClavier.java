package Vue;

import Modele.Jeu;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdaptateurClavier extends KeyAdapter {
	CollecteurEvenements controle;

	AdaptateurClavier(CollecteurEvenements c) {
		controle = c;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				controle.commande("up");
				break;
			case KeyEvent.VK_DOWN:
				controle.commande("down");
				break;
			case KeyEvent.VK_LEFT:
				controle.commande("left");
				break;
			case KeyEvent.VK_RIGHT:
				controle.commande("right");
				break;
			case KeyEvent.VK_A:
			case KeyEvent.VK_Q:
				controle.commande("quit");
				break;
			case KeyEvent.VK_P:
				controle.commande("pause");
				break;
			case KeyEvent.VK_U:
				controle.commande("annule");
				break;
			case KeyEvent.VK_R:
				controle.commande("refaire");
				break;
			case KeyEvent.VK_I:
				controle.commande("ia");
				break;
			case KeyEvent.VK_ESCAPE:
				controle.commande("fullscreen");
				break;
		}
	}
}
