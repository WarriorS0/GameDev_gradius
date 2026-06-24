package engine.graphics;

import java.util.logging.Level;

import engine.entity.Entity;
import engine.geometry.Grid;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public abstract class SpriteAvatar extends RessourceAvatar {

	private BufferedImage freeCell;
	/**
	 * sprite position in img
	 */
	int x, y, w, h;

	protected SpriteAvatar(Entity entity, String imgPath, int multX, int multY) {
		super(entity,imgPath, multX, multY);
	}
	
	protected void initImage(Graphics g, int x, int y, int w, int h) {
		if(LOGGING && INFO){
			logger.log(Level.INFO, "Init sprite avatar");
		}
		image = g.load(imagePath);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		freeCell = image.getSubimage(x, y, w, h);
	}
	
	@Override
	public void paint(Graphics g) {
		if (image == null || freeCell == null) {
			//initImage(g, x, y, w, h);
			throw new IllegalStateException("Tried to paint a sprite avatar without initializing it before !");
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