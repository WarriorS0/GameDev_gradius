package engine.graphics;

import java.util.logging.Level;
import java.util.logging.Logger;

import engine.logs.LoggerManager;
import oop.graphics.Canvas;
import oop.tasks.Task;

/**
 * Anciennement Paint, c'est un Paint plus avancée Manager de FPS : pilote le
 * repaint. La boucle d'affichage est séparée de la boucle de simulation (le
 * Ticker) : ici on demande au Canvas de se redessiner à un rythme borné par
 * fpsLimit, et on tient un compteur de frames réellement dessinées (countFrame
 * appelé par la View à chaque paint).
 */
public class FpsManager {

	private static final int A_SECOND = 1000;
	private static final int MAX_FPS = 120;
	private static final int MIN_RECOMMENDED_FPS = 30;
	private static final int MAX_RECOMMENDED_FPS = 60;
	private static final int MIN_FPS = 1;

	private static final boolean LOGGING;
	private static final Logger logger;
	static {
		logger = LoggerManager.getLogger(FpsManager.class.getName());
		LOGGING = (logger.getLevel() != Level.OFF);
	}

	private final int UPDATE_FPS_COUNTER;
	private int fpsLimit, tempFps, fps, time;
	public boolean shouldLogFps;
	private final Task fpsTask;

	public FpsManager(Task task, int limit, int update_timer, boolean shouldLogFps) {
		if (this.shouldLogFps && LOGGING) logger.info("Starting fps controller");
		if (limit > MAX_FPS)
			throw new IllegalArgumentException(String.format("Maximum FPS is %d. Recommended FPS is between %d and %d.",
					MAX_FPS, MIN_RECOMMENDED_FPS, MAX_RECOMMENDED_FPS));
		if (limit < MIN_FPS)
			throw new IllegalArgumentException(String.format("Minimum fps is %d.", MIN_FPS));
		this.fpsLimit = limit;
		this.UPDATE_FPS_COUNTER = update_timer;
		this.time = 0;
		this.shouldLogFps = shouldLogFps;
		this.fpsTask = task;
	}

	public FpsManager(Task task, int limit, boolean shouldPrintFps) {
		this(task, limit, A_SECOND, shouldPrintFps);
	}

	/**
	 * Démarre les deux boucles : compteur de fps + demande de repaint.
	 * 
	 * @param canvas given Canvas
	 */
	public void start(Canvas canvas) {
		if (this.shouldLogFps && LOGGING)
			logger.info("Starting fps controller");
		this.fpsTask.post(() -> this.checkFPS());
		this.fpsTask.post(() -> this.frame(canvas));
	}

	/**
	 * 
	 * @return fpsLimit
	 */
	public int getDesiredFps() {
		return this.fpsLimit;
	}

	/**
	 * 
	 * @return fps
	 */
	public int getFps() {
		return this.fps;
	}

	/**
	 * boucle 1 : une fois par seconde, fige le compteur et le remet à zéro.
	 */
	public void checkFPS() {
		this.fps = tempFps;
		if (this.shouldLogFps && LOGGING)
			logger.info(String.format("Execution time s %d | fps: %d", this.time, this.fps));
		this.tempFps = 0;
		this.time++;
		this.fpsTask.post(() -> checkFPS(), this.UPDATE_FPS_COUNTER);
	}

	/**
	 * boucle 2 : demande un repaint tant qu'on n'a pas atteint la limite.
	 * 
	 * @param canvas given Canvas
	 */
	public void frame(Canvas canvas) {
		if (this.tempFps < this.fpsLimit)
			this.fpsTask.post(() -> canvas.repaint());
		this.fpsTask.post(() -> frame(canvas), A_SECOND / this.fpsLimit);
	}

	/**
	 * appelé à chaque frame réellement dessinée, permet d'avoir un compteur de
	 * frame accurate
	 */
	public void countFrame() {
		this.tempFps++;
	}
}
