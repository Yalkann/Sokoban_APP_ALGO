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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// L'interface runnable déclare une méthode run
public class Exercice_2 implements Runnable, ActionListener {
	JLabel central;
	int nombre;

	public void run() {
		// Creation d'une fenetre
		JFrame frame = new JFrame("Ma fenetre a moi");

		nombre = 0;
		central = new JLabel("Contenu initial");
		frame.add(central);

		Box boite = Box.createVerticalBox();
		JLabel label = new JLabel("Salut");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		boite.add(label);
		boite.add(Box.createGlue());
		JButton bouton = new JButton("Appuyez (gros texte pour mieux voir)");
		bouton.setAlignmentX(Component.CENTER_ALIGNMENT);
		bouton.addActionListener(this);
		boite.add(bouton);
		frame.add(boite, BorderLayout.LINE_END);

		// Un clic sur le bouton de fermeture clos l'application
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// On fixe la taille et on demarre
		frame.setSize(500, 300);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Swing s'exécute dans un thread séparé. En aucun cas il ne faut accéder directement
		// aux composants graphiques depuis le thread principal. Swing fournit la méthode
		// invokeLater pour demander au thread de Swing d'exécuter la méthode run d'un Runnable.
		SwingUtilities.invokeLater(new Exercice_2());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		nombre++;
		central.setText("Text modifié (" + nombre + " fois)");
	}
}
