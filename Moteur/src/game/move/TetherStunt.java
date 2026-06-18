package game.move;

import java.util.List;

import engine.entity.Entity;
import engine.move.Model;
import engine.move.Stunt;

public class TetherStunt extends Stunt {

	private Entity leader;
	private double targetDistance;
	
	public TetherStunt(Model model, Entity entity, Entity leader, double targetDistance) {
		super(model, entity);
		this.leader = leader;
		this.targetDistance = targetDistance;
	}

	@Override
	protected void collision(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void collision(List<Entity> entities) {
		// TODO Auto-generated method stub
		
	}
}
