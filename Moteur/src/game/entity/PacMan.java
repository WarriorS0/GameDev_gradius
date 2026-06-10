package game.entity;

import engine.entity.Entity;
import engine.shape.Circle;
import game.Game;

public class PacMan extends Entity {

	// CONSTRUCTOR

	public PacMan() {
		super("PacMan");
		
		super.setPosition(super.grid.new Position(10,10));
		this.setBounding();
	}

	// TASK COLLISION

	@Override
	protected void setBounding() {
		super.setBounding();

		this.addBounding(new Circle(super.center(), Game.game().cmPerCell/2));
	}

}