package engine.graphics;

import java.util.logging.Logger;

import engine.logs.LoggerManager;
import oop.graphics.Canvas;
import oop.tasks.Task;

/**
 * Anciennement Paint, c'est un Paint plus avancée
 * Manager de FPS : pilote le repaint. La boucle d'affichage est séparée de
 * la boucle de simulation (le Ticker) : ici on demande au Canvas de se
 * redessiner à un rythme borné par fpsLimit, et on tient un compteur de frames
 * réellement dessinées (countFrame appelé par la View à chaque paint).
 */
public class FpsManager {

	private static final int A_SECOND = 1000;
	private static final int MAX_FPS = 120;
	private static final int MIN_RECOMMENDED_FPS = 30;
	private static final int MAX_RECOMMENDED_FPS = 60;
	private static final int MIN_FPS = 1;

	private final Logger logger = LoggerManager.getLogger(FpsManager.class.getName());

	private final int UPDATE_FPS_COUNTER;
	private int fpsLimit, tempFps, fps, time;
	public boolean shouldLogFps;
	private final Task fpsTask;

	public FpsManager(Task task, int limit, int update_timer, boolean shouldPrintFps) {
		if (limit > MAX_FPS)
			throw new IllegalArgumentException("Maximum FPS is " + MAX_FPS + ". Recommended FPS is between "
					+ MIN_RECOMMENDED_FPS + "-" + MAX_RECOMMENDED_FPS + ".");
		if (limit < MIN_FPS)
			throw new IllegalArgumentException("Minimum FPS is " + MIN_FPS);
		this.fpsLimit = limit;
		this.UPDATE_FPS_COUNTER = update_timer;
		this.time = 0;
		this.shouldLogFps = shouldPrintFps;
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
		if (this.shouldLogFps)
			logger.info("Execution time " + this.time + "s | fps:" + this.fps);
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
