package Vue;

import java.io.InputStream;

public abstract class ImageSokoban {
	static ImageSokoban getImageSokoban(InputStream in) {
		return new ImageSokobanSwing(in);
	}
	abstract <E> E image();
}
