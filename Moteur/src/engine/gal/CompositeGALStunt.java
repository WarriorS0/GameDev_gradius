package engine.gal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.entity.Entity;
import engine.gal.arguments.Direction;
import engine.geometry.ISU.Vector;
import engine.move.Model;

public class CompositeGALStunt extends GALStunt {

	private final List<Entity> subEntities;
	private Vector baseLinearSpeed;

	public CompositeGALStunt(Model model, Entity mainEntity, List<Entity> subEntities) {
		super(model, mainEntity);

		if (subEntities == null) {
			throw new IllegalArgumentException("subEntities cannot be null");
		}

		this.subEntities = new ArrayList<>();
		this.baseLinearSpeed = game.Game.game().isu.new Vector(0.0, 0.0);

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
	public void setLinearSpeed(Vector linearSpeed) {
	    this.targetDirection = linearSpeed;

	    Vector realSpeed = game.Game.game().isu.new Vector(
	            baseLinearSpeed.x() + linearSpeed.x(),
	            baseLinearSpeed.y() + linearSpeed.y()
	    );

	    entity.setLinearSpeed(realSpeed);

	    for (Entity subEntity : subEntities) {
	    	if (!subEntity.dead()) {
	    		subEntity.setLinearSpeed(game.Game.game().isu.new Vector(0.0, 0.0));
	    	}
	    }
	}

	@Override
	public void setAngularSpeed(double angularSpeed) {
		super.setAngularSpeed(angularSpeed);

		for (Entity subEntity : subEntities) {
			if (!subEntity.dead()) {
				subEntity.setAngularSpeed(0.0);
			}
		}
	}
	
	public void setBaseLinearSpeed(double x_cmPer_s, double y_cmPer_s) {
	    this.baseLinearSpeed = game.Game.game().isu.new Vector(x_cmPer_s, y_cmPer_s);
	    setLinearSpeed(targetDirection);
	}
	
	@Override
	public boolean startThrowing(Direction direction, double intensity) {
		if (projectileSpawner() == null) {
			return false;
		}

		Object spawner = projectileSpawner();

		try {
			var spawnMethod = spawner.getClass().getMethod(
					"spawnFrom",
					Entity.class,
					Direction.class,
					double.class
			);

			spawnMethod.invoke(spawner, entity, direction, intensity);
			return true;
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Cannot throw projectile", e);
		}
	}

}