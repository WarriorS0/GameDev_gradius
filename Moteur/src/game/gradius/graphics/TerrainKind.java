package game.gradius.graphics;

/**
 * The kinds of terrain tile in {@code background.png}, each with its source
 * rectangle in the sprite sheet, its world width, and its own collision boxes.
 *
 * World height is derived from the source pixel height through {@link
 * #CM_PER_SRC_PX}, so sprites are never stretched.
 *
 * Collision boxes use a span convention that cannot overflow the tile: a
 * {@link Box} is given by its start and end fractions on each axis, all in
 * [0, 1].
 * <ul>
 * <li>x0..x1 span the width, left (0) to right (1);</li>
 * <li>y0..y1 span the height from the anchored edge (0, the side glued to the
 * world border) to the inner tip (1, toward the middle of the tunnel).</li>
 * </ul>
 * So {@code Box(0, 1, 0, 0.3)} is a full-width base occupying the first 30% of
 * the height, and {@code Box(0.4, 0.6, 0, 1)} is a narrow central column over
 * the full height. Silhouettes:
 * <pre>
 *   flat    ___      one full base box
 *   medium  _|_      base + narrow central peak
 *   big    _=|=_     base + central peak + two lower shoulders
 * </pre>
 */
public enum TerrainKind {

	/** Big mountain: base + central peak + two shoulders ( _=|=_ ). */
	BIG(24, 92, 1016, 316, 60.0, new Box[] {
			new Box(0.00, 1.00, 0.0, 0.30), // base, full width
			new Box(0.30, 0.45, 0.0, 0.65), // left shoulder
			new Box(0.42, 0.58, 0.0, 1.00), // central peak
			new Box(0.55, 0.70, 0.0, 0.65) // right shoulder
	}),

	/** Medium mountain variant 1: base + central peak ( _|_ ). */
	MEDIUM_1(27, 508, 294, 118, 24.0, new Box[] {
			new Box(0.00, 1.00, 0.0, 0.30),
			new Box(0.40, 0.60, 0.0, 1.00)
	}),

	/** Medium mountain variant 2 ( _|_ ). */
	MEDIUM_2(360, 508, 330, 116, 24.0, new Box[] {
			new Box(0.00, 1.00, 0.0, 0.30),
			new Box(0.40, 0.60, 0.0, 1.00)
	}),

	/** Medium mountain variant 3 ( _|_ ). */
	MEDIUM_3(717, 544, 318, 80, 24.0, new Box[] {
			new Box(0.00, 1.00, 0.0, 0.30),
			new Box(0.35, 0.65, 0.0, 1.00)
	}),

	/** Flat ground: single full box ( ___ ). */
	FLAT(24, 676, 320, 26, 24.0, new Box[] {
			new Box(0.00, 1.00, 0.0, 1.00)
	});

	/**
	 * World cm per source pixel of height. One knob for the whole terrain
	 * thickness; keeps every sprite's aspect ratio.
	 */
	public static final double CM_PER_SRC_PX = 0.12;

	/**
	 * A collision box as start/end fractions on each axis, all in [0, 1].
	 * x0..x1: left-to-right span. y0..y1: span from the anchored edge (0) to the
	 * inner tip (1).
	 */
	public static final class Box {
		public final double x0, x1, y0, y1;

		public Box(double x0, double x1, double y0, double y1) {
			this.x0 = x0;
			this.x1 = x1;
			this.y0 = y0;
			this.y1 = y1;
		}
	}

	/** Source rectangle in background.png, in image pixels. */
	public final int srcX, srcY, srcW, srcH;

	/** Tile width in the world, in cm. */
	public final double width_cm;

	/** Collision boxes in normalized span coordinates. */
	public final Box[] boxes;

	TerrainKind(int srcX, int srcY, int srcW, int srcH, double width_cm, Box[] boxes) {
		this.srcX = srcX;
		this.srcY = srcY;
		this.srcW = srcW;
		this.srcH = srcH;
		this.width_cm = width_cm;
		this.boxes = boxes;
	}

	/**
	 * @return the tile height in the world, in cm, from the source pixel height
	 */
	public double height_cm() {
		return srcH * CM_PER_SRC_PX;
	}
}