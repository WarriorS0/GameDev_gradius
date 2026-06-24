package engine.graphics;

import java.util.logging.Level;

import engine.entity.Entity;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public abstract class RessourceAvatar extends ShapeAvatar {

	protected BufferedImage image;
	protected String imagePath;

	protected RessourceAvatar(Entity entity, String imagePath, int multX, int multY) {
		super(entity, multX, multY);
		this.imagePath = imagePath;
	}

	protected void initImage(Graphics g) {
		if (LOGGING && INFO) {
			logger.log(Level.INFO, "Init ressource avatar");
		}
		image = g.load(imagePath);
	}

	protected class ImageSpriteRect {
		int x, y, h, w;

		public ImageSpriteRect(int x, int y, int h, int w) {
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
		}

	}

}
