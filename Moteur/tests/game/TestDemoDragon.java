package game;

import java.awt.Dimension;

import engine.move.Model;
import game.gradius.entity.Dragon;
import game.graphics.MainPaintTest;
import oop.graphics.Canvas;
import oop.tasks.Runnable;
import oop.tasks.Runtime;
import oop.tasks.Task;

public class TestDemoDragon implements Runnable {
	
	public static final int WIDTH = 640;
	public static final int HEIGHT = 640;

	public static void main(String[] args) throws Exception {
		Runnable r = new MainPaintTest();
		Dimension windowSize = new Dimension(WIDTH, HEIGHT);
		Runtime.boot(windowSize, r);
		Runtime.shutdown();
	}

	@Override
	public void run() {
		Task task = Runtime.task();
		Canvas canvas = (Canvas) task.find("canvas");

		Game game = new Game(30, 30);
		Model model = game.model;
		
		Dragon dragon = new Dragon(5);
	}

}
