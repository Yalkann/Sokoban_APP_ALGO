package Vue;

import Global.Configuration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;

public class ImageSokobanSwing extends ImageSokoban {
	Image img;

	ImageSokobanSwing(InputStream in) {
		try {
			// Chargement d'une image utilisable dans Swing
			img = ImageIO.read(in);
		} catch (Exception e) {
			Configuration.instance().logger().severe("Impossible de charger l'image");
			System.exit(1);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	<E> E image() {
		return (E) img;
	}
}
