package engine.shape;

import engine.geometry.ISU;
import game.Game;

abstract class Shape implements iShape {

	// FIELD

	ISU isu;
	ISU.Coord center;

	// CONSTRUCTOR

	Shape(ISU.Coord center) {
		this.center = center;
		isu = this.center.isu();
		
	}
	
	

}
