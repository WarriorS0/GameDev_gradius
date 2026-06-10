package game.entity;

import engine.Entity;
import engine.shape.Bounding;
import engine.shape.Rect;
import game.Game;

public class Wall extends Entity{
	// CONSTRUCTOR

		public Wall() {
			super("Wall");
			setSize(isu.new Dimension(Game.cmPerCell, Game.cmPerCell));
			setPosition(grid.new Position(grid.width() / 2, grid.height() / 2));
			setStep(isu.new Dimension(0, 0));
		}

		// === Task COLLISION ===
		public void setBounding() {
			this.bounding = new Bounding();
			bounding.add(new Rect(isu.new Coord(center().x() + Game.cmPerCell/2, center().y() + Game.cmPerCell / 2),
					isu.new Dimension(Game.cmPerCell, Game.cmPerCell), 0));
			
			bounding_box = new Rect(center(),
					isu.new Dimension(Game.cmPerCell, Game.cmPerCell), 0);
		}
}
