package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class InterfaceGraphique implements Runnable, Observateur, InterfaceUtilisateur {
	Jeu jeu;
	CollecteurEvenements controle;
	boolean maximized;
	JFrame frame;
	NiveauGraphiqueSwing ng;
	JLabel nbPas, nbPoussees;
	JToggleButton IA, anim;
	JButton annuler, refaire;

	InterfaceGraphique(Jeu j, CollecteurEvenements c) {
		jeu = j;
		jeu.ajouteObservateur(this);
		controle = c;
	}

	public static void demarrer(Jeu j, CollecteurEvenements c) {
		SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
	}

	private JLabel createLabel(String s) {
		JLabel lab = new JLabel(s);
		lab.setAlignmentX(Component.CENTER_ALIGNMENT);
		return lab;
	}

	private JToggleButton createToggleButton(String s, String c) {
		JToggleButton but = new JToggleButton(s);
		but.addActionListener(new AdaptateurCommande(controle, c));
		but.setAlignmentX(Component.CENTER_ALIGNMENT);
		but.setFocusable(false);
		return but;
	}

	private JButton createButton(String s, String c) {
		JButton but = new JButton(s);
		but.addActionListener(new AdaptateurCommande(controle, c));
		but.setAlignmentX(Component.CENTER_ALIGNMENT);
		but.setFocusable(false);
		return but;
	}

	public void run() {
		frame = new JFrame("Sokoban");
		ng = new NiveauGraphiqueSwing(jeu);
		frame.add(ng);

		// Décompte des pas et poussées
		Box barreLaterale = Box.createVerticalBox();
		barreLaterale.add(createLabel("Sokoban"));
		barreLaterale.add(Box.createGlue());
		nbPas = createLabel("Pas : 0");
		barreLaterale.add(nbPas);
		nbPoussees = createLabel("Poussees : 0");
		barreLaterale.add(nbPoussees);

		// Boutons de contrôle
		IA = createToggleButton("IA", "ia");
		barreLaterale.add(IA);
		anim = createToggleButton("Animations", "pause");
		barreLaterale.add(anim);
		JButton prochain = createButton("Prochain", "next");
		barreLaterale.add(prochain);

		// Annuler / Refaire
		Box annulRef = Box.createHorizontalBox();
		annuler = createButton("<", "annule");
		annuler.setEnabled(false);
		refaire = new BoutonRefaire(">", "refaire", controle, jeu);
		refaire.setEnabled(false);
		annulRef.add(annuler);
		annulRef.add(refaire);
		barreLaterale.add(annulRef);

		barreLaterale.add(Box.createGlue());
		barreLaterale.add(createLabel("Copyright GH"));
		frame.add(barreLaterale, BorderLayout.LINE_END);

		ng.addMouseListener(new AdaptateurSouris(ng, controle));
		frame.addKeyListener(new AdaptateurClavier(controle));
		Timer time = new Timer(16, new AdaptateurTemps(controle));
		time.start();
		controle.fixerInterfaceUtilisateur(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 300);
		frame.setVisible(true);
	}

	@Override
	public void basculePleinEcran() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		if (maximized) {
			device.setFullScreenWindow(null);
			maximized = false;
		} else {
			device.setFullScreenWindow(frame);
			maximized = true;
		}
	}

	@Override
	public void decale(double dL, double dC, int l, int c) {
		ng.decale(dL, dC, l, c);
	}

	@Override
	public void etapePousseur() {
		ng.etapePousseur();
	}

	@Override
	public void changeDirectionPousseur(int dL, int dC) {
		ng.changeDirectionPousseur(dL, dC);
	}

	@Override
	public void changeEtatIA(boolean e) {
		IA.setSelected(e);
	}

	@Override
	public void changeEtatAnim(boolean e) {
		anim.setSelected(e);
	}

	@Override
	public void metAJour() {
		nbPas.setText("Pas : " + jeu.niveau().nbPas());
		nbPoussees.setText("Poussees : " + jeu.niveau().nbPoussees());
		annuler.setEnabled(jeu.niveau().peutAnnuler());
		ng.repaint();
	}
}
