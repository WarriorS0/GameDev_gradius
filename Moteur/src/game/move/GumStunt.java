package game.move;

import java.util.List;

import engine.entity.Entity;
import engine.geometry.ISU.Vector;
import game.Game;

public class GumStunt extends engine.move.Stunt {

	public GumStunt(engine.move.Model model, Entity entity) {
		super(model, entity);
		model.setStunt(entity, this);

		set(Game.game().isu.new Vector(0, 0));
		set_aSpeed(0);
	}

	@Override
	public void set(int orientation) {
		super.set(orientation);
		set(Game.game().isu.new Vector(0, 0));
	}



	@Override
	protected void collision(Entity other) {
		//System.out.println("GUM COLLISION avec " + other.name());

	}

	@Override
	protected void collision(List<Entity> entities) {
		//System.out.println("GUM COLLISION avec plusieurs entités");
	}

	@Override
	public void set(Vector linearSpeed) {
		super.set(linearSpeed);
	}

	@Override
	public void set_aSpeed(int angularSpeed) {
		super.set_aSpeed(angularSpeed);
	}
}