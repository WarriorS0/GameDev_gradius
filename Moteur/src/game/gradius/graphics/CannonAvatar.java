package game.gradius.graphics;

import engine.entity.Entity;
import engine.graphics.Avatar;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;

public class CannonAvatar extends Avatar {

	private static final String SPRITE_PATH = "src/game/gradius/graphics/cannon_sprites.png";
	private static final double FRAME_DURATION_S = 0.2; //attention c 'est des secondes
	
	
	private BufferedImage spriteSheet;
	private BufferedImage[] frames;
	private double time;
	private int frameIndex;
	
	public CannonAvatar(Entity entity) {
		super(entity);
	}

	@Override
	public void initImages(Graphics g) {
		spriteSheet = g.load(SPRITE_PATH);

		frames = new BufferedImage[] {
			spriteSheet.getSubimage(24, 19, 14, 5),
			spriteSheet.getSubimage(42, 19, 14, 5),
			spriteSheet.getSubimage(60, 19, 14, 5),
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

		Game game = Game.game();
		double pixelPerCm = game.pixelPerCm;

		int width = Math.max(1, (int) Math.round(entity().size().x() * pixelPerCm))*2;
		int height = Math.max(1, (int) Math.round(entity().size().y() * pixelPerCm))*2;

		int xCenter = (int) Math.round(entity().center().x() * pixelPerCm);
		int yCenter = (int) Math.round(entity().center().y() * pixelPerCm);

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.drawImage(img, xTopLeft, yTopLeft, width, height);
	}
}