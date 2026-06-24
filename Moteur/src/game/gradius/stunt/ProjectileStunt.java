package game.gradius.stunt;

import java.util.List;

import engine.entity.Entity;
import engine.move.Model;
import engine.move.Stunt;

public class ProjectileStunt extends Stunt {

	public ProjectileStunt(Model model, Entity entity) {
		super(model, entity);
	}

	@Override
	protected void collision(Entity entity) {
		this.entity.kill();
	}

	@Override
	protected void collision(List<Entity> entities) {
		this.entity.kill();
	}

	@Override
	protected void tick(double d) {
		// Le projectile garde sa vitesse actuelle.
	}
}