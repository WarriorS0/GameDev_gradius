package game.graphics;

import engine.entity.Entity;
import engine.graphics.Avatar;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class GumAvatar extends Avatar {

	private static final String SPRITE_PATH = "src/engine/graphics/pacman_sprite.png";

	private BufferedImage sprite;
	private BufferedImage image;

	public GumAvatar(Entity entity) {
		super(entity);
	}

	@Override
	public void initImages(Graphics g) {
		sprite = g.load(SPRITE_PATH);
		image = sprite.getSubimage(11, 11, 1, 1);
	}

	@Override
	public void updateAnimation(double delta_t) {
		/*
		 * Une gomme n'a pas d'animation.
		 */
	}

	@Override
	public void paint(Graphics g) {
		if (sprite == null || image == null) {
			initImages(g);
		}

		if (dead() || entity().center() == null) {
			return;
		}

		Game game = Game.game();

		double cell = game.cmPerCell;
		double pixelPerCm = game.pixelPerCm;

		int gumSize = Math.max(2, (int) Math.round(cell * pixelPerCm / 4.0));

		int xCenter = (int) Math.round((entity().center().x() + cell / 2.0) * pixelPerCm);
		int yCenter = (int) Math.round((entity().center().y() + cell / 2.0) * pixelPerCm);

		int xTopLeft = xCenter - gumSize / 2;
		int yTopLeft = yCenter - gumSize / 2;

		g.drawImage(image, xTopLeft, yTopLeft, gumSize, gumSize);
	}
}