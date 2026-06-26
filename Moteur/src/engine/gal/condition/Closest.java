package engine.gal.condition;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.gal.arguments.Direction;

public class Closest extends GALCondition {
	public Closest(Direction direction, Category category) {
		super(direction, category);
	}

	@Override
	public boolean eval(Entity e) {
		// TODO Auto-generated method stub
		return false;
	}
}
