 class Move  {

	Direction direction;
	double duration_ms;
	double length_cm;

	// 4 CONSTRUCTORS

	/**
	 * @apiNote asks for moving
	 *          <UL>
	 *          <LI>at celerity = intensity * maximal celerity
	 *          <LI>during duration (in ms) = n * simulationStep_ms
	 *          <LI>in the given direction
	 *          </UL>
	 * @param dir
	 * @param intensity : &in; [0,1] ≃ %
	 * @param n         : number of ticks? or number of steps? Choose your
	 *                  interpretation.
	 */
	 Move(Direction dir, double intensity, int n){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `Move`"); }

	 Move(Direction dir, double intensity){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `Move`"); }

	 Move(Direction dir, int n){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `Move`"); }

	 Move(){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `Move`"); }

	// EXEC
	 boolean exec(Entity e){ throw new UnsupportedOperationException("UNIMPLEMENTED METHOD `exec`"); }

}
