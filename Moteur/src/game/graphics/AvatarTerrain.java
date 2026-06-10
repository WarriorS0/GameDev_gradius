package engine.graphics;

import engine.Entity;
import engine.Grid;
import game.Game;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public class AvatarTerrain extends Avatar {
	private BufferedImage sprite;
	private BufferedImage case_libre;

	public AvatarTerrain(Entity e) {
		super(e);
	}

	@Override
	public void initImages(Graphics g) {
		sprite = g.load("src/engine/graphics/pacman_sprite.png");
		case_libre = sprite.getSubimage(250, 5, 20, 15);
	}

	@Override
	public void paint(Graphics g) {
		if (case_libre == null) {
			initImages(g);
		}
		Grid grid = Game.game.grid;
		int colonnes = grid.width();
		int lignes = grid.height();
		double tailleCellulePixel = Game.cmPerCell * Game.pixelPerCm;

		for (int col = 0; col < colonnes; col++) {
			for (int lig = 0; lig < lignes; lig++) {
				int xPixel = (int) (col * tailleCellulePixel);
				int yPixel = (int) (lig * tailleCellulePixel);
				Grid.Position pos = grid.new Position(col, lig);
				Grid.Cell cell = grid.cellAt(pos);

				g.drawImage(case_libre, xPixel, yPixel);
				// faire des cellules vide donc sol, remplit obstacle mur, remplit entités type
				// pacman/phantom/gum
			}
		}
	}

	@Override
	public void updateAnimation(double delta_t) {
		// TODO Auto-generated method stub

	}

}
