import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimationPousseur implements ActionListener {
	AireDeDessin aire;
	Point depart, arrivee;
	Timer temps;
	double progres, vitesseAnim;

	AnimationPousseur(AireDeDessin a) {
		aire = a;
		temps = new Timer(16, this);
		vitesseAnim = 0.1;
	}

	void clic(int x, int y) {
		arrivee = new Point(x, y);
		depart = aire.position();
		temps.start();
		progres = 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action event dans l'animation");
		progres += vitesseAnim;
		if (progres > 1) {
			progres = 1;
			temps.stop();
		}
		int x = (int) Math.round((1-progres)*depart.x + progres*arrivee.x);
		int y = (int) Math.round((1-progres)*depart.y + progres*arrivee.y);
		aire.fixePosition(x, y);
	}
}
