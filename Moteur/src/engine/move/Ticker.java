package game.move;

import oop.tasks.Task;
import oop.tasks.Runtime;
import oop.tasks.Runnable;

public class Ticker implements Runnable {
	private Task task;
	private Model model;
	private long lastTime;

	public Ticker(Model model) {
		this.model = model;
		this.task = Runtime.newTask("GameLoopTask");
		this.lastTime = System.nanoTime();
		this.task.post(this);
	}

	public void test() {
		while (true) {
			long current = System.nanoTime();
			double deltaT = (current - lastTime) / 1000000000.0;
			lastTime = current;
			model.tick(deltaT);
			if (!model.entities.isEmpty()) {
				System.out.println("PacMan pos: X=" + model.entities.get(0).center().x() + " | Y="
						+ model.entities.get(0).center().y());
				System.out.println("Red pos: X=" + model.entities.get(1).center().x() + " | Y="
						+ model.entities.get(1).center().y());
			}
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() throws Exception {
		long current = System.nanoTime();
		double deltaT = (current - lastTime) / 1000000000.0;
		lastTime = current;
		model.tick(deltaT);
		task.post(this, 17);
		if (!model.entities.isEmpty()) {
//			System.out.println("PacMan pos: X=" + model.entities.get(0).center().x() + " | Y="
//					+ model.entities.get(0).center().y());
//			System.out.println("PacMan angle = " + model.entities.get(0).orientation());
//			System.out.println("Red pos: X=" + model.entities.get(1).center().x() + " | Y="
//					+ model.entities.get(1).center().y());
		}
	}
}