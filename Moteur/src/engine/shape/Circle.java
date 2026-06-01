// = Circle =
package engine.shape;

import engine.geometry.ISU;

public class Circle extends Shape{

	double radius;

	// CONSTRUCTOR

	Circle(ISU.Coord center, double radius) {
		super(center);
		
	}

	// INTERSECTION
	@Override
	public boolean intersects(Rect rect) {
		throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `intersects`");
	}
	
	@Override
	public boolean intersects(Circle circle) {
		if(this.center.distanceTo(circle.center) < this.radius + circle.radius)
			return true;
		return false;
	}
	
	@Override
	public boolean intersects(iShape shape) {
		throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `intersects`");
	}

}
