package game.gradius.graphics;

import engine.entity.Entity;
import engine.geometry.Grid;
import engine.graphics.Avatar;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class AvatarTerrain extends Avatar {

	private static final String SPRITE_PATH = "src/game/graphics/pacman_sprite.png";

	private BufferedImage sprite;
	private BufferedImage freeCell;

	public AvatarTerrain() {
		this(null);
	}

	public AvatarTerrain(Entity entity) {
		super(entity);
	}

	@Override
	public void initImages(Graphics g) {
		sprite = g.load(SPRITE_PATH);
		freeCell = sprite.getSubimage(250, 5, 20, 15);
	}

	@Override
	public void updateAnimation(double delta_t) {
		/*
		 * Le terrain n'a pas d'animation.
		 */
	}

	@Override
	public void paint(Graphics g) {
		if (sprite == null || freeCell == null) {
			initImages(g);
		}

		Game game = Game.game();

		if (game == null) {
			return;
		}

		Grid grid = game.grid;

		int cellSize = Math.max(1, this.cmToPixel(game.cmPerCell));

		for (int x = 0; x < grid.width(); x++) {
			for (int y = 0; y < grid.height(); y++) {
				int xPixel = x * cellSize;
				int yPixel = y * cellSize;

				g.drawImage(freeCell, xPixel, yPixel, cellSize, cellSize);
			}
		}
	}
}