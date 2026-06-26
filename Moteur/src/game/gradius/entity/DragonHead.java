package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.shape.Rect;

class DragonHead extends Entity {

	public DragonHead() {
		super("dragon_head");
		setSize(isu.new Dimension(25,25));
		category(Category.Adversary);
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}

}