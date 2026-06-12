package engine.gal.arguments;

import java.util.HashMap;
import java.util.Map;

public class Direction {

	// CONSTANTS

	public static final Direction B; // Backward
	public static final Direction F; // Forward
	public static final Direction H; // Here
	public static final Direction N; // North
	public static final Direction S; // South
	public static final Direction E; // East
	public static final Direction W; // West
	
	private static Map<String, Direction> directions = new HashMap<>();

	// STATIC INITIALIZATION

	/**
	 * @apiNote the global variables must be initialized in that section,
	 * @implNote which is executed at the class loading
	 */
	static {
		H = new Direction("Here");
		N = new Direction("North");
		S = new Direction("South");
		E = new Direction("East");
		W = new Direction("West");
		F = new Direction("Forward");
		B = new Direction("Backward");

		register(H, "H", "Here");
		register(N, "N", "North");
		register(S, "S", "South");
		register(E, "E", "East");
		register(W, "W", "West");
		register(F, "F", "Forward", "Front");
		register(B, "B", "Backward", "Back");
	}

	private static void register(Direction direction, String... names) {
		for (String name : names) {
			directions.put(name, direction);
		}
	}

	// STATIC

	/**
	 * @apiNote {@code directions} associates a Direction to a name in order to
	 *          ensure uniqueness (thus sharing) of the instance associated to that
	 *          name.
	 * @apiNote Advantages
	 *          <UL>
	 *          <LI>Sharing reduces memory consumption</LI>
	 *          <LI>Comparison can be done using {@code ==} instead of
	 *          {@code String:equals} on names.</LI>
	 *          <LI>it is easy to use: {@code Direction.canonical(name)} provides
	 *          the unique representative of the Direction associated to that
	 *          name.</LI>
	 *          </UL>
	 */
	

	// FACTORY

	/**
	 * @apiNote implements sharing : it avoids creating new direction each time the
	 *          parser encounters a direction.
	 * @return the existing direction associated to a name if it already exists
	 */
	public static Direction canonical(String name) {
		Direction direction = directions.get(name);

		if (direction == null) {
			throw new IllegalArgumentException("Unknown direction: " + name);
		}

		return direction;
	}

	// CONSTRUCTOR

	private String name;

	private Direction(String name) {
		this.name = name;
	}

	// PREDICATE

	public boolean isAbsolute() {
		return this == N || this == S || this == E || this == W;
	}

	public boolean isRelative() {
		return this == F || this == B;
	}

	public boolean isHere() {
		return this == H;
	}

	// CONVERSION

	public int toAngle() {
		if (this == E) {
			return 0;
		}
		if (this == S) {
			return 90;
		}
		if (this == W) {
			return 180;
		}
		if (this == N) {
			return 270;
		}

		throw new IllegalStateException("Direction has no absolute angle: " + name);
	}

	@Override
	public String toString() {
		return name;
	}
}