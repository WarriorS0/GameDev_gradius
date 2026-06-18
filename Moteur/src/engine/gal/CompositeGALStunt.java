package engine.gal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.entity.Entity;
import engine.geometry.Grid.Cell;
import engine.geometry.ISU.Coord;
import engine.geometry.ISU.Vector;
import engine.move.Model;
import game.Game;

public class CompositeGALStunt extends GALStunt {

	private final List<Entity> subEntities;

	public CompositeGALStunt(Model model, Entity mainEntity, List<Entity> subEntities) {
		super(model, mainEntity);

		if (subEntities == null) {
			throw new IllegalArgumentException("subEntities cannot be null");
		}

		this.subEntities = new ArrayList<>();

		for (Entity subEntity : subEntities) {
			addSubEntity(subEntity);
		}
	}

	public void addSubEntity(Entity subEntity) {
		if (subEntity == null) {
			throw new IllegalArgumentException("subEntity cannot be null");
		}

		if (subEntity == entity) {
			throw new IllegalArgumentException("mainEntity cannot be a subEntity of itself");
		}

		if (!subEntities.contains(subEntity)) {
			subEntities.add(subEntity);
		}
	}

	public void removeSubEntity(Entity subEntity) {
		subEntities.remove(subEntity);
	}

	public List<Entity> subEntities() {
		return Collections.unmodifiableList(subEntities);
	}

	@Override
	public void set(Vector linearSpeed) {
		super.set(linearSpeed);

		for (Entity subEntity : subEntities) {
			if (!subEntity.dead()) {
				model.setLinearSpeed(subEntity, linearSpeed);
			}
		}
	}
	
	@Override
	public void set_aSpeed(int angularSpeed) {
		super.set_aSpeed(angularSpeed);

		for (Entity subEntity : subEntities) {
			if (!subEntity.dead()) {
				model.setAngularSpeed(subEntity, angularSpeed);
			}
		}
	}

	@Override
	protected void set(Cell cell) {
		Coord target = cell.position().toISUCoord();
		moveTo(target);
	}

	@Override
	protected void set(double x, double y) {
		Coord target = Game.game().isu.new Coord(x, y);
		moveTo(target);
	}

	public void moveTo(Coord target) {
		if (target == null) {
			throw new IllegalArgumentException("target cannot be null");
		}

		Coord oldCenter = entity.center();

		entity.place(target);

		double dx = target.x() - oldCenter.x();
		double dy = target.y() - oldCenter.y();

		Vector delta = Game.game().isu.new Vector(dx, dy);

		for (Entity subEntity : subEntities) {
			if (!subEntity.dead()) {
				subEntity.translate(delta);
			}
		}
	}
}