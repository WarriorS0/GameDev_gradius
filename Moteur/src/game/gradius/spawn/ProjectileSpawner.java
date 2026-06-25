package game.gradius.spawn;

import engine.graphics.View;
import engine.geometry.ISU;
import engine.move.Model;
import game.gradius.entity.Projectile;
import game.gradius.graphics.ProjectileAvatar;
import game.gradius.stunt.ProjectileStunt;
import engine.entity.Entity;
import engine.gal.arguments.Direction;

public class ProjectileSpawner implements engine.gal.ThrowSpawner{

	private final Model model;
	private final View view;

	public ProjectileSpawner(Model model, View view) {
		if (model == null) {
			throw new IllegalArgumentException("model cannot be null");
		}

		if (view == null) {
			throw new IllegalArgumentException("view cannot be null");
		}

		this.model = model;
		this.view = view;
	}

	public Projectile spawn(ISU.Coord center, ISU.Vector speed) {
		Projectile projectile = new Projectile(center, speed);

		model.add(projectile);
		model.setStunt(projectile, new ProjectileStunt(model, projectile));
		model.cullOffScreen(projectile);

		view.add(new ProjectileAvatar(projectile));

		return projectile;
	}
	
	@Override
	public Projectile spawnFrom(Entity source, Direction direction, double intensity) {
		if (source == null || source.center() == null) {
			throw new IllegalArgumentException("source must be placed");
		}

		if (direction == null) {
			throw new IllegalArgumentException("direction cannot be null");
		}

		if (intensity < 0.0 || intensity > 1.0) {
			throw new IllegalArgumentException("intensity must be in [0, 1]");
		}

		double cell = game.Game.game().cmPerCell;

		ISU.Coord center = game.Game.game().isu.new Coord(
				source.center().x() + 1.5 * cell,
				source.center().y()
		);

		double speedValue = 80.0 * intensity;
		ISU.Vector speed;

		if (direction == Direction.N) {
			speed = game.Game.game().isu.new Vector(0.0, -speedValue);
		} else if (direction == Direction.S) {
			speed = game.Game.game().isu.new Vector(0.0, speedValue);
		} else if (direction == Direction.W) {
			speed = game.Game.game().isu.new Vector(-speedValue, 0.0);
		} else {
			speed = game.Game.game().isu.new Vector(speedValue, 0.0);
		}

		return spawn(center, speed);
	}
}