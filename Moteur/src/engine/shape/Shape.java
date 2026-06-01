package engine.shape;

import engine.geometry.ISU;
import game.Game;

abstract class Shape implements iShape {

	// FIELD

	ISU isu;
	ISU.Coord center;

	// CONSTRUCTOR

	Shape(ISU.Coord center) {
		this.isu = Game.game().isu;
		this.center = center;
	}

	@Override
	public boolean intersects(iShape shape) {
		throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `intersects`");

	}

	@Override
	abstract public boolean intersects(Circle circle);
	@Override
	abstract public boolean intersects(Rect rect);
}
