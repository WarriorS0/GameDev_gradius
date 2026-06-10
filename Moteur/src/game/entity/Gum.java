package game.entity;
// == GUM ==

import engine.entity.Entity;
import engine.shape.Circle;
import game.Game;

public class Gum extends Entity {

	// CONSTRUCTOR

	public Gum() {
		super("Gum");
		super.setPosition(super.grid.new Position(5,5));
		this.setBounding();
	}

	// TASK COLLISION

	@Override
	protected void setBounding() {
		super.setBounding();
		
		super.addBounding(new Circle(super.center(), Game.game().cmPerCell / 6));

		
	}

}