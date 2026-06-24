package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.Avatar;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class Laser1Avatar extends Avatar{
	private static final String SPRITE_PATH = "src/game/gradius/graphics/small_enemies.png";
	private static final double ANIMATION_DURATION_MS = 100.0;

	private BufferedImage spriteSheet;
	private BufferedImage[] frames;
	private double time;
	private int frameIndex;

	public Laser1Avatar(Entity entity) {
		super(entity);
	}

	@Override
	public void initImages(Graphics g) {
		spriteSheet = g.load(SPRITE_PATH);

		frames = new BufferedImage[] {
			spriteSheet.getSubimage(126, 121, 10, 4),
			spriteSheet.getSubimage(137, 121, 10, 4),
		};

		time = 0.0;
		frameIndex = 0;
	}

	@Override
	public void updateAnimation(double delta_t) {
		if (frames == null || frames.length == 0) {
			return;
		}

		time += delta_t;

		while (time >= ANIMATION_DURATION_MS) {
			time -= ANIMATION_DURATION_MS;
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
	}

}
