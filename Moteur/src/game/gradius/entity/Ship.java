package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.shape.Rect;
import game.Game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import engine.geometry.Grid;
import engine.geometry.ISU;

public class Ship extends Entity {
	
	private final List<Cannon> cannons = new ArrayList<>();

	public Ship() {
		super("Ship");

		double cell = Game.game().cmPerCell;

		setSize(isu.new Dimension(4.0 * cell, 2.0 * cell));
		setStep(isu.new Dimension(cell, cell));
		category(Category.Team);

		place(grid.new Position(5, grid.height() / 2));
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}
	
	public void attachCannon(Cannon cannon) {
		if (cannon == null) {
			throw new IllegalArgumentException("cannon cannot be null");
		}

		if (!cannons.contains(cannon)) {
			cannons.add(cannon);
			cannon.placeRelativeTo(this);
		}
	}

	public List<Cannon> cannons() {
		return Collections.unmodifiableList(cannons);
	}

	private void syncCannons() {
		for (Cannon cannon : cannons) {
			if (!cannon.dead()) {
				cannon.placeRelativeTo(this);
			}
		}
	}
	
	//MOVEMENT
	
	@Override
	public void place(Grid.Position position) {
		super.place(position);
		syncCannons();
	}

	@Override
	public void place(ISU.Coord center) {
		super.place(center);
		syncCannons();
	}

	@Override
	public void translate(ISU.Vector v) {
		super.translate(v);
		syncCannons();
	}

	@Override
	public void translate(Grid.Vector v) {
		super.translate(v);
		syncCannons();
	}
}