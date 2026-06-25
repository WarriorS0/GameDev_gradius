package engine.move;

import engine.entity.Entity;
import engine.geometry.ISU;
import game.Game;

/**
 * Une "caméra" sur le monde.
 *
 * La fenêtre de vue est un rectangle sur la carte : elle est définie par une
 * origine (coin supérieur gauche, en cm) et des dimensions (largeur et hauteur,
 * en cm). La classe View l'utilise pour construire sa transformation
 * (translation puis mise à l'échelle) et pour délimiter la zone d'affichage
 * (clipping) de sorte que seul le contenu situé à l'intérieur de la fenêtre de
 * vue soit visible.
 *
 * Il y a trois modes:
 * <ul>
 * <li>FREE : l'origine est définie explicitement et ne se déplace pas
 * d'elle-même ;</li>
 * <li>FOLLOW : l'origine est recalculée à chaque tick à jour afin qu'une entité
 * cible reste au centre de la fenêtre de vue;</li>
 * <li>RAIL : l'origine défile à une vitesse constante (en cm par seconde).</li>
 * </ul>
 *
 * Les contraintes toriques dépendent de la géométrie du monde :
 * <ul>
 * <li>sur un axe torique, l'origine est libre (elle se « reboucle » avec le
 * monde), mais la fenêtre de vue doit rester strictement plus petite que le
 * monde (pas de mosaïque, "tiles") ;</li>
 * <li>sur un axe non torique, la fenêtre de vue est maintenue entièrement à
 * l'intérieur du monde.</li>
 * </ul>
 */
public class ViewPort {

	/**
	 * Comportement du viewmport, cad de la caméra
	 */
	public enum Mode {
		FREE, FOLLOW, RAIL
	}

	private final ISU isu;

	private double originX_cm;
	private double originY_cm;

	private double width_cm;
	private double height_cm;

	private Mode mode;

	/**
	 * The targeted entity in FOLLOW mode, null otherwise.
	 */
	private Entity followed;

	/**
	 * Scroll speed in cm per second used in RAIL mode.
	 */
	private double railSpeedX_cm;
	private double railSpeedY_cm;

	/**
	 * Builds a free view port covering the whole world.
	 *
	 * @throws IllegalStateException if no current Game instance exists
	 */
	public ViewPort() {
		this(0, 0, worldWidth(), worldHeight());
	}

	/**
	 * Builds a free view port at the given origin and size, in cm.
	 *
	 * @param originX_cm the left edge of the view port, in cm
	 * @param originY_cm the top edge of the view port, in cm
	 * @param width_cm   the view port width, in cm (must be positive)
	 * @param height_cm  the view port height, in cm (must be positive)
	 * @throws IllegalStateException    if no current Game instance exists
	 * @throws IllegalArgumentException if width or height is not positive
	 */
	public ViewPort(double originX_cm, double originY_cm, double width_cm, double height_cm) {
		Game game = Game.game();

		if (game == null) {
			throw new IllegalStateException("No current Game instance");
		}

		this.isu = game.isu;
		this.mode = Mode.FREE;
		this.followed = null;
		this.railSpeedX_cm = 0;
		this.railSpeedY_cm = 0;

		setSize(width_cm, height_cm);
		setOrigin(originX_cm, originY_cm);
	}

	// =========================
	// Mode selection
	// =========================

	/**
	 * Switches to FREE mode and places the origin explicitly.
	 *
	 * @param originX_cm the left edge of the view port, in cm
	 * @param originY_cm the top edge of the view port, in cm
	 */
	public void moveTo(double originX_cm, double originY_cm) {
		this.mode = Mode.FREE;
		this.followed = null;
		setOrigin(originX_cm, originY_cm);
	}

	/**
	 * Switches to FOLLOW mode so the view port keeps the given entity centered.
	 *
	 * @param e the entity to follow (must not be null)
	 * @throws IllegalArgumentException if e is null
	 */
	public void follow(Entity e) {
		if (e == null) {
			throw new IllegalArgumentException("followed entity cannot be null");
		}

		this.mode = Mode.FOLLOW;
		this.followed = e;
		recenterOnFollowed();
	}

	/**
	 * Switches to RAIL mode so the view port scrolls at a constant speed.
	 *
	 * @param speedX_cm horizontal scroll speed, in cm per second
	 * @param speedY_cm vertical scroll speed, in cm per second
	 */
	public void rail(double speedX_cm, double speedY_cm) {
		this.mode = Mode.RAIL;
		this.followed = null;
		this.railSpeedX_cm = speedX_cm;
		this.railSpeedY_cm = speedY_cm;
	}

	/**
	 * @return the current camera mode
	 */
	public Mode mode() {
		return mode;
	}

	/**
	 * @return the followed entity, or null when not in FOLLOW mode
	 */
	public Entity followed() {
		return followed;
	}

	// =========================
	// Tick
	// =========================

	/**
	 * Advances the camera for one model step. Called by the model after the
	 * entities have moved.
	 *
	 * @param delta_t the elapsed time since the previous tick, in seconds
	 */
	public void tick(double delta_t) {
		switch (mode) {
		case RAIL:
			setOrigin(originX_cm + railSpeedX_cm * delta_t, originY_cm + railSpeedY_cm * delta_t);
			break;
		case FOLLOW:
			recenterOnFollowed();
			break;
		case FREE:
		default:
			break;
		}
	}

	private void recenterOnFollowed() {
		if (followed == null || followed.dead()) {
			return;
		}

		ISU.Coord c = followed.center();
		setOrigin(c.x() - width_cm / 2.0, c.y() - height_cm / 2.0);
	}

	// =========================
	// Geometry / setters
	// =========================

	/**
	 * Sets the view port size, clamped so it never exceeds the world on a toric
	 * axis (no tiling) and stays at most the world size otherwise.
	 *
	 * @param w_cm the new width, in cm (must be positive)
	 * @param h_cm the new height, in cm (must be positive)
	 * @throws IllegalArgumentException if width or height is not positive
	 */
	public void resize(double w_cm, double h_cm) {
		setSize(w_cm, h_cm);
		setOrigin(originX_cm, originY_cm);
	}

	private void setSize(double w_cm, double h_cm) {
		if (w_cm <= 0 || h_cm <= 0) {
			throw new IllegalArgumentException("view port size must be positive");
		}

		this.width_cm = Math.min(w_cm, worldWidth());
		this.height_cm = Math.min(h_cm, worldHeight());
	}

	/**
	 * Sets the origin, clamping each axis according to the world geometry.
	 *
	 * @param x_cm the left edge, in cm
	 * @param y_cm the top edge, in cm
	 */
	private void setOrigin(double x_cm, double y_cm) {
		this.originX_cm = clampOriginX(x_cm);
		this.originY_cm = clampOriginY(y_cm);
	}

	/**
	 * On a toric axis the origin wraps into the world range so it stays a valid,
	 * canonical coordinate. On a non-toric axis the view port is pushed back so it
	 * remains entirely inside the world.
	 */
	private double clampOriginX(double x_cm) {
		if (Game.game().torusOnXaxis) {
			return wrap(x_cm, worldWidth());
		}
		return clampInside(x_cm, worldWidth(), width_cm);
	}

	private double clampOriginY(double y_cm) {
		if (Game.game().torusOnYaxis) {
			return wrap(y_cm, worldHeight());
		}
		return clampInside(y_cm, worldHeight(), height_cm);
	}

	private static double wrap(double value, double period) {
		double r = value % period;

		if (r < 0) {
			r += period;
		}

		return r;
	}

	private static double clampInside(double origin, double world, double size) {
		double max = Math.max(0, world - size);
		return Math.max(0, Math.min(origin, max));
	}

	// =========================
	// Visibility / culling
	// =========================

	/**
	 * Tells whether an entity center lies within the view port, taking the toric
	 * geometry into account. Intended for culling, for example to destroy
	 * projectiles that scroll off screen.
	 *
	 * @param e the entity to test (must not be null)
	 * @return true if the entity center is inside the view port rectangle
	 * @throws IllegalArgumentException if e is null
	 */
	public boolean isVisible(Entity e) {
		if (e == null) {
			throw new IllegalArgumentException("entity cannot be null");
		}
		return this.isVisible(e.center());
	}

	public boolean isVisible(ISU.Coord coord) {

		if (coord == null) {
			return false;
		}

		return this.isVisible(coord.x(), coord.y());
	}

	public boolean isVisible(double x, double y) {

		double ex = isu.euclideanX(originX_cm, x);
		double ey = isu.euclideanY(originY_cm, y);

		return ex >= originX_cm && ex <= originX_cm + width_cm && ey >= originY_cm && ey <= originY_cm + height_cm;
	}

	// =========================
	// Getters
	// =========================

	/**
	 * @return the left edge of the view port, in cm
	 */
	public double originX() {
		return originX_cm;
	}

	/**
	 * @return the top edge of the view port, in cm
	 */
	public double originY() {
		return originY_cm;
	}

	/**
	 * @return the view port width, in cm
	 */
	public double width_cm() {
		return width_cm;
	}

	/**
	 * @return the view port height, in cm
	 */
	public double height_cm() {
		return height_cm;
	}

	// =========================
	// World helpers
	// =========================

	/**
	 * 
	 * @return Game.game().width_cm
	 */
	private static double worldWidth() {
		return Game.game().width_cm;
	}

	/**
	 * 
	 * @return Game.game().height_cm
	 */
	private static double worldHeight() {
		return Game.game().height_cm;
	}
}