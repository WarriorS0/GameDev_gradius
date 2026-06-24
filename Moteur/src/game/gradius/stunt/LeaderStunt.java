package game.gradius.stunt;

import java.util.LinkedList;
import java.util.List;

import engine.entity.Entity;
import engine.move.Model;
import engine.move.Stunt;
import game.gradius.stunt.FollowerStunt.MovementState;

public class LeaderStunt extends Stunt {
	
	private final LinkedList<MovementState> history = new LinkedList<>();

	public LeaderStunt(Model model, Entity entity) {
		super(model, entity);
		// TODO Auto-generated constructor stub
	}
	
	public LinkedList<MovementState> getHistory() {
		return history;
	}

	@Override
	protected void collision(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void collision(List<Entity> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void tick(double d) {
		history.addLast(new MovementState(entity.center(), entity.orientation()));
	}
}