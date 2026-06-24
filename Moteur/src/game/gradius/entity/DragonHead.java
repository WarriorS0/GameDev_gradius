package game.gradius.entity;

import engine.entity.Entity;
import game.gradius.graphics.DragonHeadAvatar;

class DragonHead extends Entity {

	public DragonHead() {
		super("dragon_head");
		setSize(isu.new Dimension(10,10));
		new DragonHeadAvatar(this);
	}

	@Override
	protected void setBounding() {
		// TODO Auto-generated method stub
		
	}

}