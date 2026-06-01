package engine.entity;

import java.util.HashSet;
import java.util.Set;

import engine.shape.iShape;

class Bounding {

	// FIELDS

	private Set<iShape> boundings;

	// CONSTRUCTOR

	Bounding() {
		this.boundings = new HashSet<>();
	}

	// BUILDER

	void add(iShape shape) {
		if (shape == null)
			throw new IllegalArgumentException("shape cannot be null");
		this.boundings.add(shape);
	}

	// INTERSECTION

	boolean intersects(iShape shape) {
		for(iShape shape1 : this.boundings) {
			if(shape1.intersects(shape))
				return true;
		}
		return false;
	}

	boolean intersects(Bounding bounding) {
		for (iShape shape1 : this.boundings) {
			for (iShape shape2 : bounding.boundings) {
				if (shape1.intersects(shape2))
					return true;
			}
		}
		return false;
	}

}
