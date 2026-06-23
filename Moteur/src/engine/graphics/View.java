package engine.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import engine.geometry.ISU;
import engine.move.ViewPort;
import game.Game;
import oop.graphics.Color;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;

public class View {

	private List<Avatar> avatars;
	private long lastTime;

	private final ViewPort vp;
	private int canvasX, canvasY, canvasW, canvasH;

	private Hud hud; // optionnal

	/**
	 * When true, the view zooms out to show the whole map and draws a rectangle
	 * where the real view port currently is. The real view port keeps following its
	 * target; only the rendering changes.
	 */
	private boolean debugViewPort = false;

	/**
	 * Enables or disables the view-port debug mode (whole-map overview with the
	 * real view port drawn as a rectangle).
	 *
	 * @param on true to show the overview
	 */
	public void setDebugViewPort(boolean on) {
		this.debugViewPort = on;
	}

	/**
	 * Flips the view-port debug mode. Convenient for a key binding.
	 */
	public void toggleDebugViewPort() {
		this.debugViewPort = !this.debugViewPort;
	}

	/**
	 * @return whether the view-port debug overview is active
	 */
	public boolean isDebugViewPort() {
		return debugViewPort;
	}

	/**
	 * Colour of the view-port rectangle drawn in debug mode.
	 */
	private Color viewPortDebugColor = Colors.green;

	/**
	 * Optional background drawn in world space (cm), before the avatars, so it goes
	 * through the same camera transform. May be null.
	 */
	private WorldBackground background;

	/**
	 * A drawing callback expressed in world coordinates (the same space avatars
	 * paint in: cm times pixelPerCm). It is invoked with the camera transform
	 * already installed.
	 */
	public interface WorldBackground {
		void paint(Graphics g);
	}

	/**
	 * Sets a background painted in world space under the avatars, or null.
	 *
	 * @param background the background callback, or null for none
	 */
	public void setBackground(WorldBackground background) {
		this.background = background;
	}

	/**
	 * Attache un HUD à la vue ; il sera dessiné par-dessus la scène.
	 *
	 * @param hud le HUD à dessiner, ou null pour aucun
	 */
	public void setHUD(Hud hud) {
		this.hud = hud;
	}

	public View(ViewPort vp) {
		this.vp = Objects.requireNonNull(vp, "view port cannot be null");
		this.avatars = new ArrayList<>();
		this.lastTime = System.currentTimeMillis();
	}

	/**
	 * @return the view port driving this view
	 */
	public ViewPort viewPort() {
		return vp;
	}

	public List<Avatar> avatars() {
		return avatars;
	}

	public void add(Avatar avatar) {
		avatars.add(avatar);
	}

	public void remove(Avatar avatar) {
		avatars.remove(avatar);
	}

	/**
	 * Sets the canvas rectangle this view draws into, in pixels. Must be called
	 * before {@link #paint(Graphics)}; otherwise the drawing area is empty and
	 * nothing is rendered.
	 *
	 * @param x      left edge of the drawing area, in pixels
	 * @param y      top edge of the drawing area, in pixels
	 * @param width  width of the drawing area, in pixels
	 * @param height height of the drawing area, in pixels
	 */
	public void setCanvasArea(int x, int y, int width, int height) {
		this.canvasX = x;
		this.canvasY = y;
		this.canvasW = width;
		this.canvasH = height;
	}

	/**
	 * @return left edge of the extent rendered this frame, in cm: the real view
	 *         port origin normally, or 0 in the whole-map debug overview
	 */
	private double renderOriginX() {
		return debugViewPort ? 0 : vp.originX();
	}

	/**
	 * @return top edge of the extent rendered this frame, in cm
	 */
	private double renderOriginY() {
		return debugViewPort ? 0 : vp.originY();
	}

	/**
	 * @return width of the extent rendered this frame, in cm
	 */
	private double renderWidth() {
		return debugViewPort ? Game.game().width_cm : vp.width_cm();
	}

	/**
	 * @return height of the extent rendered this frame, in cm
	 */
	private double renderHeight() {
		return debugViewPort ? Game.game().height_cm : vp.height_cm();
	}

	/**
	 * @return the cm -> device pixel factor on the x axis for the current frame
	 */
	private double scaleX() {
		return canvasW / renderWidth();
	}

	/**
	 * @return the cm -> device pixel factor on the y axis for the current frame
	 */
	private double scaleY() {
		return canvasH / renderHeight();
	}

	/**
	 * Projects a world coordinate (cm) to screen pixels, using the same extent as
	 * the scene currently rendered (the real view port, or the whole map in debug
	 * overview). On a toric axis the point is unfolded around the rendered origin
	 * only when the extent is a sub-window of the world; when the whole axis is
	 * shown, the raw coordinate is already in range and must not be unfolded.
	 *
	 * @param worldPoint a coordinate in the world, in cm (must not be null)
	 * @return the screen position in pixels, or null if the point lies outside the
	 *         rendered extent (caller should then skip drawing)
	 */
	public PixelCoordinate worldToScreen(ISU.Coord worldPoint) {
		Objects.requireNonNull(worldPoint, "world point cannot be null");

		if (canvasW <= 0 || canvasH <= 0) {
			return null;
		}

		Game game = Game.game();

		double originX = renderOriginX();
		double originY = renderOriginY();
		double rw = renderWidth();
		double rh = renderHeight();

		boolean subWindowX = rw < game.width_cm;
		boolean subWindowY = rh < game.height_cm;

		double wx = subWindowX ? game.isu.euclideanX(originX, worldPoint.x()) : worldPoint.x();
		double wy = subWindowY ? game.isu.euclideanY(originY, worldPoint.y()) : worldPoint.y();

		// Tout ce qui est en dehors du viewport ne doit pas être affiché
		if (wx < originX || wx > originX + rw || wy < originY || wy > originY + rh) {
			return null;
		}

		int px = (int) Math.round(canvasX + (wx - originX) * scaleX());
		int py = (int) Math.round(canvasY + (wy - originY) * scaleY());

		return new PixelCoordinate(px, py);
	}

	public void paint(Graphics g) {
		Objects.requireNonNull(g, "graphics cannot be null");

		if (canvasW <= 0 || canvasH <= 0) {
			return;
		}

		long currentTime = System.currentTimeMillis();
		double delta_t = (currentTime - lastTime) / 1000.0;
		lastTime = currentTime;

		Game game = Game.game();

		// Source unique de vérité pour l'étendue rendue partagée avec

		// worldToScreen afin que les Labels se projettent exactement sur ce qui est
		// affiché.
		double renderOriginX = renderOriginX();
		double renderOriginY = renderOriginY();
		double renderWidth = renderWidth();
		double renderHeight = renderHeight();

		// Total cm -> device pixel factor on each axis for the chosen extent.
		double sx = scaleX();
		double sy = scaleY();

		// Clip pour avoir l'effet de caméra, seule la partie du canvas dans le viewport
		// est affichée.
		Object savedTransform = g.getTransform();
		g.setClip(canvasX, canvasY, canvasW, canvasH);

		// Met à jour l'animation des Avatars à chaque frames
		for (Avatar avatar : avatars) {
			avatar.updateAnimation(delta_t);
		}

		// Base pass.
		paintScene(g, savedTransform, renderOriginX, renderOriginY, sx, sy, 0, 0);

		// Toric wrap-around
		double worldW = game.width_cm;
		double worldH = game.height_cm;

		boolean wrapX = game.torusOnXaxis && renderOriginX + renderWidth > worldW;
		boolean wrapY = game.torusOnYaxis && renderOriginY + renderHeight > worldH;

		if (wrapX) {
			paintScene(g, savedTransform, renderOriginX, renderOriginY, sx, sy, worldW, 0);
		}
		if (wrapY) {
			paintScene(g, savedTransform, renderOriginX, renderOriginY, sx, sy, 0, worldH);
		}
		if (wrapX && wrapY) {
			paintScene(g, savedTransform, renderOriginX, renderOriginY, sx, sy, worldW, worldH);
		}

		// En mode debug, dessine une rectangle là ou est le viewport
		if (debugViewPort) {
			paintViewPortRect(g, savedTransform, renderOriginX, renderOriginY, sx, sy);
		}

		g.setTransform(savedTransform);

		// hud is optionnal, on dessine uniquement si présent (càd non null)
		if (hud != null) {
			hud.draw(g);
		}
	}

	/**
	 * Paint the scene
	 * 
	 * @param g
	 * @param baseTransform
	 * @param renderOriginX
	 * @param renderOriginY
	 * @param sx
	 * @param sy
	 * @param worldShiftX_cm
	 * @param worldShiftY_cm
	 */
	private void paintScene(Graphics g, Object baseTransform, double renderOriginX, double renderOriginY, double sx,
			double sy, double worldShiftX_cm, double worldShiftY_cm) {
		g.setTransform(baseTransform);

		double txPix = canvasX - (renderOriginX - worldShiftX_cm) * sx;
		double tyPix = canvasY - (renderOriginY - worldShiftY_cm) * sy;

		g.translate((int) Math.round(txPix), (int) Math.round(tyPix));
		g.scale(sx / Game.game().pixelPerCm, sy / Game.game().pixelPerCm);

		if (background != null) {
			background.paint(g);
		}

		for (Avatar avatar : avatars) {
			avatar.paint(g);
		}
	}

	/**
	 * Draws the real view port as a rectangle, in the current (whole-map) render
	 * frame. Handles the toric seam by drawing a second copy shifted by one world
	 * period when the view port overflows the map edge.
	 *
	 * @param g             the graphics
	 * @param baseTransform the canvas transform to start from
	 * @param renderOriginX left edge of the rendered extent, in cm
	 * @param renderOriginY top edge of the rendered extent, in cm
	 * @param sx            cm -> pixel factor on x
	 * @param sy            cm -> pixel factor on y
	 */
	private void paintViewPortRect(Graphics g, Object baseTransform, double renderOriginX, double renderOriginY,
			double sx, double sy) {
		g.setTransform(baseTransform);

		Game game = Game.game();
		Color previous = g.getColor();
		g.setColor(viewPortDebugColor);

		double vw = vp.width_cm();
		double vh = vp.height_cm();

		drawRectCm(g, renderOriginX, renderOriginY, vp.originX(), vp.originY(), vw, vh, sx, sy);

		if (game.torusOnXaxis && vp.originX() + vw > game.width_cm) {
			drawRectCm(g, renderOriginX, renderOriginY, vp.originX() - game.width_cm, vp.originY(), vw, vh, sx, sy);
		}
		if (game.torusOnYaxis && vp.originY() + vh > game.height_cm) {
			drawRectCm(g, renderOriginX, renderOriginY, vp.originX(), vp.originY() - game.height_cm, vw, vh, sx, sy);
		}

		g.setColor(previous);
	}

	/**
	 * Draws a rectangle given in world cm into the current render frame, in device
	 * pixels (the canvas transform is assumed reset to baseTransform).
	 * 
	 * 
	 * @param g             graphism object
	 * @param renderOriginX origin x
	 * @param renderOriginY origin y
	 * @param rectX_cm      x position of ract
	 * @param rectY_cm      y position of ract
	 * @param w_cm          width
	 * @param h_cm          height
	 * @param sx            screen x
	 * @param sy            screen y
	 */
	private void drawRectCm(Graphics g, double renderOriginX, double renderOriginY, double rectX_cm, double rectY_cm,
			double w_cm, double h_cm, double sx, double sy) {
		int x = (int) Math.round(canvasX + (rectX_cm - renderOriginX) * sx);
		int y = (int) Math.round(canvasY + (rectY_cm - renderOriginY) * sy);
		int w = (int) Math.round(w_cm * sx);
		int h = (int) Math.round(h_cm * sy);

		g.drawRect(x, y, w, h);
	}
}