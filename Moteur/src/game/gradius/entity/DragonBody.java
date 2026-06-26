package game.gradius.entity;

import engine.entity.Entity;
import engine.gal.arguments.Category;
import engine.shape.Rect;

class DragonBody extends Entity {

	public DragonBody() {
		super("dragon_body");
		
		setSize(isu.new Dimension(20,20));
		category(Category.Adversary);
	}

	@Override
	protected void setBounding() {
		clearBounding();

		addBounding(new Rect(center(), size(), orientation()));
	}

}