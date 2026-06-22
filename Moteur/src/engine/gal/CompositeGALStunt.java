package engine.gal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import engine.entity.Entity;
import engine.geometry.ISU.Vector;
import engine.move.Model;

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
	public void setLinearSpeed(Vector linearSpeed) {
		super.setLinearSpeed(linearSpeed);

		for (Entity subEntity : subEntities) {
			if (!subEntity.dead()) {
				subEntity.setLinearSpeed(linearSpeed);
			}
		}
	}

	@Override
	public void setAngularSpeed(double angularSpeed) {
		super.setAngularSpeed(angularSpeed);

		for (Entity subEntity : subEntities) {
			if (!subEntity.dead()) {
				subEntity.setAngularSpeed(angularSpeed);
			}
		}
	}

}