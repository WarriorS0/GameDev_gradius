package engine.move;


import oop.tasks.Task;
import oop.tasks.Runtime;
import oop.tasks.Runnable;

public class Ticker implements Runnable {

	private static final int FRAME_DELAY_MS = 16;
	private static final double NANO_TO_SECONDS = 1_000_000_000.0;

	private Task task;
	private Model model;

	private long lastTime;
	private boolean running;

	public Ticker(Model model) {
		this.model = model;
		this.task = Runtime.newTask("GameLoopTask");
		this.lastTime = System.nanoTime();
		this.running = true;

		this.task.post(this);
	}

	@Override
	public void run() throws Exception {
		if (!running) {
			return;
		}

		tick();

		task.post(this, FRAME_DELAY_MS);
	}

	private void tick() {
		long current = System.nanoTime();
		double deltaT = (current - lastTime) / NANO_TO_SECONDS;
		lastTime = current;

		model.tick(deltaT);
	}

	public void stop() {
		this.running = false;
	}

	public boolean isRunning() {
		return running;
	}

	/**
	 * 
	 * À ne pas utiliser dans le jeu final. C'est du test 
	 */
	public void test() {
		while (running) {
			tick();

			try {
				Thread.sleep(FRAME_DELAY_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				stop();
			}
		}
	}
}