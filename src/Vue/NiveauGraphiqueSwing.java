package Vue;

/*
 * Sokoban - Encore une nouvelle version (à but pédagogique) du célèbre jeu
 * Copyright (C) 2018 Guillaume Huard
 *
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).
 *
 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.
 *
 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.
 *
 * Contact:
 *          Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import Modele.Jeu;
import javax.swing.*;
import java.awt.*;

class NiveauGraphiqueSwing extends JComponent implements NiveauGraphique {
	int largeur, hauteur;
	Graphics2D drawable;
	VueNiveau vue;

	public NiveauGraphiqueSwing(Jeu j) {
		vue = new VueNiveau(j, this);
	}

	@Override
	public void paintComponent(Graphics g) {
		// Graphics 2D est le vrai type de l'objet passé en paramètre
		// Le cast permet d'avoir acces a un peu plus de primitives de dessin
		drawable = (Graphics2D) g;

		// On reccupere quelques infos provenant de la partie JComponent
		largeur = getSize().width;
		hauteur = getSize().height;

		// On efface tout
		drawable.clearRect(0, 0, largeur, hauteur);
		vue.tracerNiveau();
	}

	@Override
	public int largeur() {
		return largeur;
	}

	@Override
	public int hauteur() {
		return hauteur;
	}

	@Override
	public void tracerImage(ImageSokoban img, int x, int y, int largeurCase, int hauteurCase) {
		drawable.drawImage(img.image(), x, y, largeurCase, hauteurCase, null);
	}

	public void tracerCroix(int valeur, int x, int y, int largeurCase, int hauteurCase) {
		int margeX = largeurCase/5;
		int margeY = hauteurCase/5;
		drawable.setColor(new Color(valeur));
		drawable.setStroke(new BasicStroke(margeX));
		drawable.drawLine(x+margeX,y+margeY, x+largeurCase-margeX,y+hauteurCase-margeY);
		drawable.drawLine(x+margeX,y+hauteurCase-margeY, x+largeurCase-margeX,y+margeY);
	}

	int largeurCase() {
		return vue.largeurCase();
	}

	int hauteurCase() {
		return vue.hauteurCase();
	}

	@Override
	public void decale(double dL, double dC, int l, int c) {
		vue.fixerDecalage(dL, dC, l, c);
		repaint();
	}

	@Override
	public void etapePousseur() {
		vue.etapePousseur();
		repaint();
	}

	@Override
	public void changeDirectionPousseur(int dL, int dC) {
		vue.changeDirectionPousseur(dL, dC);
		repaint();
	}
}