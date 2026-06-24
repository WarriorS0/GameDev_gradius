package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.Avatar;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class ShipAvatar extends Avatar {

	private static final String SPRITE_PATH = "src/game/gradius/graphics/vic_viper.png";
	private static final double FRAME_DURATION_S = 0.1; // attention c 'est des secondes

	private BufferedImage spriteSheet;
	private BufferedImage[] frames;
	private double time;
	private int frameIndex;

	public ShipAvatar(Entity entity) {
		super(entity);
	}

	@Override
	public void initImages(Graphics g) {
		spriteSheet = g.load(SPRITE_PATH);

		frames = new BufferedImage[] { spriteSheet.getSubimage(18, 13, 34, 18), spriteSheet.getSubimage(53, 13, 34, 18),
				spriteSheet.getSubimage(88, 13, 34, 18), spriteSheet.getSubimage(123, 13, 34, 18),
				spriteSheet.getSubimage(158, 13, 34, 18) };

		time = 0.0;
		frameIndex = 0;
	}

	@Override
	public void updateAnimation(double delta_t) {
		if (frames == null || frames.length == 0) {
			return;
		}

		time += delta_t;

		while (time >= FRAME_DURATION_S) {
			time -= FRAME_DURATION_S;
			frameIndex = (frameIndex + 1) % frames.length;
		}
	}

	@Override
	public void paint(Graphics g) {
		if (spriteSheet == null || frames == null) {
			initImages(g);
		}

		if (dead() || entity().center() == null) {
			return;
		}

		BufferedImage img = frames[frameIndex];

		int width = Math.max(1, this.cmToPixel(entity().size().x()));
		int height = Math.max(1, this.cmToPixel(entity().size().y()));

		int xCenter = this.cmToPixel(entity().center().x());
		int yCenter = this.cmToPixel(entity().center().y());

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.drawImage(img, xTopLeft, yTopLeft, width, height);
		
		super.paint(g);
	}
}