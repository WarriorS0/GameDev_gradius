package game.gradius.stunt;

import java.util.List;

import engine.entity.Entity;
import engine.gal.CompositeGALStunt;
import engine.move.Model;

/**
 * Basiquement un CompositeGALStunt mais qui confine son entity dans le viewport
 */
public class ShipStunt extends CompositeGALStunt {

	public ShipStunt(Model model, Entity mainEntity, List<Entity> subEntities) {
		super(model, mainEntity, subEntities);
	}

	@Override
	public void tick(double elapsed_s) {
		super.tick(elapsed_s);
		if (model.viewPort() != null)
			model.viewPort().confine(entity);
	}

}
