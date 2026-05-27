// = AXIS =

/**
 * @apiNote Axis of a Torus with origin at 0
 * @implNote Coordinate ranges in [ -perimeter/2 ; perimeter/2 [
 * @implNote Negative coordinate are allowed
 */

 class Axis {

	// FIELDS

	 boolean onTorus;
	 double perimeter;
	 double halfPerimeter;

	// CONSTRUCTOR

	 Axis(boolean onTorus, double perimeter) { throw new UnsupportedOperationException("Unimplemented method"); }

	// NORMALIZE INTEGER LENGTH

	/**
	 * @apiNote normalize _integer length_ according to the geometry
	 * @implNote returns positive values
	 * @return
	 *         <UL>
	 *         <LI>length % perimeter __&in; [0, perimeter-1]__ if onTorus</LI>
	 *         <LI>length if !onTorus</LI>
	 *         </UL>
	 */
	 int normalize(int length) { return 0; }

	/**
	 * @apiNote compute length modulo perimeter
	 * @return length % perimeter __&in; [0, perimeter-1]__
	 */
	 int modp(int length, int perimeter) { return 0; }

	// NORMALIZE REAL LENGTH

	/**
	 * @apiNote normalize _real length_ according to the geometry
	 * @implNote can return negative values
	 * @return
	 *         <UL>
	 *         <LI>length module perimeter <I>&in; [-perimeter/2 , perimeter/2[</I>
	 *         if onTorus</LI>
	 *         <LI>length if !onTorus</LI>
	 *         </UL>
	 */
	 double normalize(double length) { return 0.0;   }

	/**
	 * @apiNote compute length modulo perimeter
	 * @return length % perimeter __&in; [0, perimeter[__
	 */
	 double modp(double length, double perimeter) { return 0.0;   }

	// DISTANCE

	/**
	 * @apiNote The distance on a Torus is that of the shortest path, sometimes
	 *          going in the opposite direction and across the border is shorter.
	 * @implNote Look for the detail on internet.
	 */
	 double distance(double position1, double position2) { return 0.0;   }
}
