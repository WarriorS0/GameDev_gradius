package game.move;

import java.util.List;
import java.util.logging.Logger;

import engine.entity.Entity;
import engine.geometry.ISU.Vector;
import engine.logs.LoggerManager;
import engine.move.Model;
import engine.move.Stunt;
import game.Game;
import game.entity.Ghost;

public class PacManStunt extends Stunt {
	
	private static Logger logger = LoggerManager.getLogger(PacManStunt.class.getName());

	private static final double DEFAULT_SPEED = 20.0;

	public PacManStunt(Model model, Entity entity) {
		super(model, entity);
		model.setStunt(entity, this);
	}

	@Override
	public void set(int orientation) {
		if (entity.dead()) {
			return;
		}

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

	public void kill() {
		entity.kill();
		super.set(Game.game().isu.new Vector(0, 0));
		super.set_aSpeed(0);
	}

	public void revive() {
		entity.revive();
	}

	@Override
	protected void collision(Entity other) {
		if (other instanceof Ghost) {
			kill();
			logger.fine("PACMAN EST MORT");
			return;
		}

		set(Game.game().isu.new Vector(0, 0));
		set_aSpeed(0);

		logger.finer("PACMAN COLLISION avec " + other.name());
	}

	@Override
	protected void collision(List<Entity> entities) {
		for (Entity other : entities) {
			if (other instanceof Ghost) {
				kill();
				logger.fine("PACMAN EST MORT");
				return;
			}
		}

		set(Game.game().isu.new Vector(0, 0));
		set_aSpeed(0);
		logger.finer("PACMAN COLLISION avec plusieurs entités");
	}

	@Override
	public void set(Vector linearSpeed) {
		if (entity.dead()) {
			super.set(Game.game().isu.new Vector(0, 0));
			return;
		}

		super.set(linearSpeed);
	}

	@Override
	public void set_aSpeed(int angularSpeed) {
		if (entity.dead()) {
			super.set_aSpeed(0);
			return;
		}

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