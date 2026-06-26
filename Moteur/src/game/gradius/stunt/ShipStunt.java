package game.gradius.stunt;

import java.util.List;

import engine.entity.Entity;
import engine.gal.CompositeGALStunt;
import engine.gal.arguments.Direction;
import engine.move.Model;
import game.Game;
import game.gradius.entity.Ship;

/**
 * Basiquement un CompositeGALStunt mais qui confine son entity dans le viewport
 */
public class ShipStunt extends CompositeGALStunt {

	private static final double NORMAL_SPEED_CM_PER_S = 20.0;
	private static final double BOOSTED_SPEED_CM_PER_S = 80.0;
	
	private static final double DASH_SPEED_CM_PER_S = 80.0;
	private static final double DASH_DURATION_MS = 150.0;
	private static final double DASH_COOLDOWN_S = 0.7;
	
	private double dashCooldownRemainingS;

	public ShipStunt(Model model, Entity mainEntity, List<Entity> subEntities) {
		super(model, mainEntity, subEntities);
		setMaxLinearSpeed(NORMAL_SPEED_CM_PER_S);
	}

	@Override
	public void tick(double elapsed_s) {
		
		dashCooldownRemainingS = Math.max(0.0, dashCooldownRemainingS - elapsed_s);
		
		
		if (entity instanceof Ship ship) {
			ship.tickPower(elapsed_s);
			updateSpeedPower(ship);

			if (ship.deathAnimationPlaying()) {
				entity.setLinearSpeed(Game.game().isu.new Vector(0.0, 0.0));
				entity.setAngularSpeed(0.0);
				ship.tickDeathAnimation(elapsed_s);
				return;
			}
		}

		super.tick(elapsed_s);

		if (model.viewPort() != null) {
			model.viewPort().confine(entity);
		}
	}

	private void updateSpeedPower(Ship ship) {
		if (ship.hasSpeedPower()) {
			setMaxLinearSpeed(BOOSTED_SPEED_CM_PER_S);
		} else {
			setMaxLinearSpeed(NORMAL_SPEED_CM_PER_S);
		}
	}
	
	@Override
	public boolean startDashing(Direction direction, double intensity) {
		if (direction == null) {
			return false;
		}

		if (dashCooldownRemainingS > 0.0) {
			return false;
		}

		if (actionDuration() > 0.0) {
			return false;
		}

		setMaxLinearSpeed(DASH_SPEED_CM_PER_S);

		boolean started = super.startMoving(direction, intensity, DASH_DURATION_MS);

		if (entity instanceof Ship ship) {
			updateSpeedPower(ship);
		} else {
			setMaxLinearSpeed(NORMAL_SPEED_CM_PER_S);
		}

		if (!started) {
			return false;
		}

		dashCooldownRemainingS = DASH_COOLDOWN_S;
		return true;
	}
}
