package engine.graphics;

import engine.entity.Entity;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

/**
 * intermediary "private" class, not supposed to be created outside of this
 * package
 * 
 * it's only use is to put there stuff common to both SpriteAvatar and
 * AnimationAvatar
 */
abstract class RessourceAvatar extends ShapeAvatar {

	public static boolean debugCollision = false;

	protected BufferedImage image;
	protected String imagePath;

	protected RessourceAvatar(Entity entity, String imagePath, int multX, int multY) {
		super(entity, multX, multY);
		this.imagePath = imagePath;
	}

	public static void toggleDebug() {
		debugCollision = !debugCollision;
	}

	@Override
	public void paint(Graphics g) {
		if (debugCollision) {
			super.paint(g);
		}
	}

	/**
	 * dumb class to deal more easily with sprites cutting
	 */
	protected class ImageSpriteRect {
		int x, y, h, w;

		public ImageSpriteRect(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

	}

}
