package game.gradius.entity;

import engine.entity.Entity;
import game.gradius.graphics.DragonBodyAvatar;

class DragonBody extends Entity {

	public DragonBody() {
		super("dragon_body");
		
		setSize(isu.new Dimension(5,3));
		new DragonBodyAvatar(this);
	}

	@Override
	protected void setBounding() {
		// TODO Auto-generated method stub
		
	}

}