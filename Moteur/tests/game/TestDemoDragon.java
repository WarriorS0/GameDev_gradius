package game;

import java.awt.Dimension;

import engine.graphics.FpsManager;
import engine.graphics.View;
import engine.move.Model;
import engine.move.Ticker;
import engine.move.ViewPort;
import game.gradius.entity.Dragon;
import game.gradius.graphics.MapView;
import game.graphics.MainPaintTest;
import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;
import oop.tasks.Runnable;
import oop.tasks.Runtime;
import oop.tasks.Task;

public class TestDemoDragon implements Runnable {
	
	public static final int WIDTH = 640;
	public static final int HEIGHT = 640;

	public static void main(String[] args) throws Exception {
		Runnable r = new TestDemoDragon();
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
		
		ViewPort vp = new ViewPort(0, 0, 120, 120);
		vp.rail(10, 0); 
		model.setViewPort(vp);
		View view = new View(vp);
		
		MapView mapView = new MapView();
		view.setBackground(mapView::paint);

		FpsManager fpsC = new FpsManager(task, FPS, FPS_LOGGING);
		
		canvas.set(new Canvas.PaintListener() {
			@Override
			public void visible(Canvas canvas) {
				fpsC.start(canvas);
			}

			@Override
			public void paint(Canvas canvas, Graphics g) {
				int windowWidth = canvas.getWidth();
				int windowHeight = canvas.getHeight();

				g.setColor(Colors.black);
				g.fillRect(0, 0, windowWidth, windowHeight);

				view.setCanvasArea(0, 0, windowWidth, windowHeight);
				view.paint(g);

				fpsC.countFrame();
			}

			@Override
			public void revoked(Canvas canvas) {
				System.exit(0);
			}
		});
		new Ticker(model);
	}

}
