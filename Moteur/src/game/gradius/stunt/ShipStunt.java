package game.gradius.stunt;

import java.util.List;

import engine.entity.Entity;
import engine.gal.CompositeGALStunt;
import engine.move.Model;
import game.Game;
import game.gradius.entity.Ship;

/**
 * Basiquement un CompositeGALStunt mais qui confine son entity dans le viewport
 */
public class ShipStunt extends CompositeGALStunt {

	private static final double NORMAL_SPEED_CM_PER_S = 20.0;
	private static final double BOOSTED_SPEED_CM_PER_S = 80.0;

	public ShipStunt(Model model, Entity mainEntity, List<Entity> subEntities) {
		super(model, mainEntity, subEntities);
		setMaxLinearSpeed(NORMAL_SPEED_CM_PER_S);
	}

	@Override
	public void tick(double elapsed_s) {
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
}
