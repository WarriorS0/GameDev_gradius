package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.Avatar;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public abstract class EnemyAvatar extends Avatar {

	private static final String SPRITE_PATH = "src/game/gradius/graphics/small_enemies.png";
	private static final double ANIMATION_DURATION_MS = 120.0;

	private BufferedImage spriteSheet;
	private BufferedImage[] frames;
	private double time;
	private int frameIndex;

	public EnemyAvatar(Entity entity) {
		super(entity);
	}

	@Override
	public void initImages(Graphics g) {
		spriteSheet = g.load(SPRITE_PATH);

		frames = new BufferedImage[] {
			spriteSheet.getSubimage(8, 21, 15, 15),
			spriteSheet.getSubimage(24, 21, 15, 15),
			spriteSheet.getSubimage(41, 21, 15, 15)
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

		Game game = Game.game();
		double pixelPerCm = game.pixelPerCm;

		int width = Math.max(1, (int) Math.round(entity().size().x() * pixelPerCm));
		int height = Math.max(1, (int) Math.round(entity().size().y() * pixelPerCm));

		int xCenter = (int) Math.round(entity().center().x() * pixelPerCm);
		int yCenter = (int) Math.round(entity().center().y() * pixelPerCm);

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.drawImage(img, xTopLeft, yTopLeft, width, height);
	}


}
