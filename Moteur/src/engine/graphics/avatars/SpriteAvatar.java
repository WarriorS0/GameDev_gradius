package engine.graphics.avatars;

import java.util.logging.Level;

import engine.entity.Entity;
import engine.geometry.ISU;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public abstract class SpriteAvatar extends RessourceAvatar {

	private BufferedImage sprite;
	/**
	 * sprite position in img
	 */
	int x, y, w, h;

	protected SpriteAvatar(Entity entity, String imgPath, int multX, int multY) {
		super(entity, imgPath, multX, multY);
	}

	protected void initImage(Graphics g, int x, int y, int w, int h) {

		if (hasBeenInitialized)
			return;
		hasBeenInitialized = true;

		if (LOGGING && INFO) {
			logger.log(Level.INFO, "Init sprite avatar");
		}
		image = g.load(imagePath);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		sprite = image.getSubimage(x, y, w, h);
	}

	@Override
	public void paint(Graphics g) {
		if (image == null || sprite == null) {
			// initImage(g, x, y, w, h);
			throw new IllegalStateException("Tried to paint a sprite avatar without initializing it before !");
		}

		if (dead() || entity().center() == null) {
			return;
		}

		super.paint(g); // check if need to show debug collision boxes

		ISU.Coord coord = entity().center();
		ISU.Dimension size = entity().size();

		int width = Math.max(1, this.cmToPixel(size.x())) * multX;
		int height = Math.max(1, this.cmToPixel(size.y())) * multY;

		int xCenter = this.cmToPixel(coord.x());
		int yCenter = this.cmToPixel(coord.y());

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.drawImage(sprite, xTopLeft, yTopLeft, width, height);
	}
}