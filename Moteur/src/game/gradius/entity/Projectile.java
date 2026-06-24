package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.geometry.ISU;
import engine.shape.Rect;
import game.Game;

public class Projectile extends Entity {

	public Projectile(ISU.Coord center, ISU.Vector speed) {
		super("Projectile");

		double cell = Game.game().cmPerCell;

		setSize(isu.new Dimension(0.4 * cell, 0.2 * cell));
		setStep(isu.new Dimension(cell, cell));
		category(Category.Projectile);

		place(center);
		setLinearSpeed(speed);
		
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}
}