package game.move;

import java.util.List;

import engine.entity.Entity;
import engine.geometry.Grid.Cell;
import engine.geometry.ISU.Vector;
import game.Game;

public class BasicStunt extends engine.move.Stunt {

	private static final double DEFAULT_SPEED = 20.0;

	public BasicStunt(engine.move.Model model, Entity entity) {
		super(model, entity);
		model.setStunt(entity, this);
	}

	@Override
	public void set(int orientation) {
		super.set(orientation);

		double angle = normalizeAngle(orientation);

		if (angle >= 45.0 && angle < 135.0) {
			set(Game.game().isu.new Vector(0, DEFAULT_SPEED));
		} else if (angle >= 135.0 && angle < 225.0) {
			set(Game.game().isu.new Vector(-DEFAULT_SPEED, 0));
		} else if (angle >= 225.0 && angle < 315.0) {
			set(Game.game().isu.new Vector(0, -DEFAULT_SPEED));
		} else {
			set(Game.game().isu.new Vector(DEFAULT_SPEED, 0));
		}
	}


	@Override
	protected void collision(Entity other) {
		set(Game.game().isu.new Vector(0, 0));
		set_aSpeed(0);

		//System.out.println("ATTENTION COLLISION avec " + other.name());
	}

	@Override
	protected void collision(List<Entity> entities) {
		set(Game.game().isu.new Vector(0, 0));
		set_aSpeed(0);

		//System.out.println("ATTENTION COLLISION avec plusieurs entités");
	}

	@Override
	public void set(Vector linearSpeed) {
		super.set(linearSpeed);
	}

	@Override
	public void set_aSpeed(int angularSpeed) {
		super.set_aSpeed(angularSpeed);
	}

	private double normalizeAngle(double angle) {
		double normalized = angle % 360.0;

		if (normalized < 0.0) {
			normalized += 360.0;
		}

		return normalized;
	}
}