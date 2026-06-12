package game.entity;

import engine.entity.Entity;
import engine.shape.Rect;
import game.Game;

public class Wall extends Entity {

	public Wall() {
		super("Wall");

		double cmPerCell = Game.game().cmPerCell;

		setSize(isu.new Dimension(cmPerCell, cmPerCell));
		setStep(isu.new Dimension(0, 0));

		place(grid.new Position(grid.width() / 2, grid.height() / 2));
	}

	public Wall(int x_ncell, int y_ncell) {
		super("Wall");

		double cmPerCell = Game.game().cmPerCell;

		setSize(isu.new Dimension(cmPerCell, cmPerCell));
		setStep(isu.new Dimension(0, 0));

		place(grid.new Position(x_ncell, y_ncell));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(
			center(),
			size(),
			orientation()
		));
	}
}