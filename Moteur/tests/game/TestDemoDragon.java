package game;

import java.awt.Dimension;
import java.util.logging.Logger;

import engine.controller.Controller;
import engine.controller.KeyManager;
import engine.gal.GALBot;
import engine.graphics.BackgroundView;
import engine.graphics.FpsManager;
import engine.graphics.View;
import engine.graphics.avatars.AnimationAvatar;
import engine.graphics.hud.FollowerLabel;
import engine.graphics.hud.Hud;
import engine.graphics.hud.Label;
import engine.graphics.hud.PixelCoordinate;
import engine.logs.LoggerManager;
import engine.move.Model;
import engine.move.Ticker;
import engine.move.ViewPort;
import game.gradius.entity.Dragon;
import game.graphics.MainPaintTest;
import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.graphics.VirtualKeyCodes;
import oop.graphics.Graphics.Colors;
import oop.tasks.Runnable;
import oop.tasks.Runtime;
import oop.tasks.Task;

public class TestDemoDragon implements Runnable {
	
	private static final Logger logger = LoggerManager.getLogger(MainPaintTest.class.getName());
	private static final int FPS = 30;
	private static final boolean FPS_LOGGING = true;
	public static final int WIDTH = 640;
	public static final int HEIGHT = 640;
	private boolean showDebugValues = false;

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

		Game game = new Game(256, 32);
		Model model = game.model;
		
		Dragon dragon = new Dragon(6);
		dragon.place(game.grid.new Position(20,20));
		View view = game.view;
		
		
		BackgroundView bgView = new BackgroundView("src/game/gradius/graphics/map_gradius.png", 317, 204, 200, 200);
		view.setBackground(bgView::paint); // on doit utiliser un method reference operator sinon ça marche pas

		FpsManager fpsC = new FpsManager(task, FPS, FPS_LOGGING);
		Hud hud = new Hud();
		
		FollowerLabel dragonDebugBehavior = new FollowerLabel(() -> dragon.getHead().debugInfoBehavior(), dragon.getHead(),
				new PixelCoordinate(0, -25), Colors.white);
		dragonDebugBehavior.setVisibility(false);
		hud.add(dragonDebugBehavior);
		FollowerLabel dragonDebugMoves = new FollowerLabel(() -> dragon.getHead().debugInfoMove(), dragon.getHead(), new PixelCoordinate(0, 20),
				Colors.white);
		dragonDebugMoves.setVisibility(false);
		hud.add(dragonDebugMoves);
		
		Label labelDebug = new Label(() -> "'TAB' to toggle debug mode. 'V' to toggle viewport debug mode.",
				new PixelCoordinate(6, 12), Colors.white, false);
		hud.add(labelDebug);
		StringBuilder sb = new StringBuilder();
		sb.append("FPS");
		Label labelFPS = new Label(
				() -> String.format(" FPS %s { min: %s; avg: %s ; max: %s (from the last %ds) } ",
						fpsC.getFormattedFps(), fpsC.getFormattedMinFps(), fpsC.getFormattedAvgFps(),
						fpsC.getFormattedMaxFps(), fpsC.NB_LAST_FPS_SAVED),
				new PixelCoordinate(12, 24), Colors.white, false);
		labelFPS.setVisibility(false);
		hud.add(labelFPS);
		Label labelPaintTime = new Label(() -> String.format(" FPS %s { min: %s; avg: %s ; max: %s (from the last %ds) } ",
						fpsC.getFormattedFps(), fpsC.getFormattedMinFps(), fpsC.getFormattedAvgFps(),
						fpsC.getFormattedMaxFps(), fpsC.NB_LAST_FPS_SAVED),
				new PixelCoordinate(12, 24), Colors.white, false);
		labelPaintTime.setVisibility(false); 
		hud.add(labelPaintTime);
		Label labelTickTime = new Label(
				() -> String.format(" TickTime:  %sms { min: %s ; avg: %s ; max: %s (from the last %d ticks) }",
						model.getFormattedTickTime(), model.getFormattedMinTickTime(), model.getFormattedAvgTickTime(),
						model.getFormattedMaxTickTime(), model.NB_LAST_TICK_TIME_SAVED),
				new PixelCoordinate(12, 48), Colors.white, false);
		labelTickTime.setVisibility(false);
		hud.add(labelTickTime);

		view.setHUD(hud);
		
		
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

				view.paint(canvas, g);
				
				fpsC.countFrame();
			}

			@Override
			public void revoked(Canvas canvas) {
				System.exit(0);
			}
		});
		
		KeyManager km = new KeyManager();
		km.bind(VirtualKeyCodes.VK_TAB, () -> {
			showDebugValues = !showDebugValues;
			labelFPS.setVisibility(showDebugValues);
			labelPaintTime.setVisibility(showDebugValues);
			labelTickTime.setVisibility(showDebugValues);
			dragonDebugBehavior.setVisibility(showDebugValues);
			dragonDebugMoves.setVisibility(showDebugValues);
			AnimationAvatar.debugCollision = showDebugValues;
		});
		km.bind(VirtualKeyCodes.VK_V, () -> view.toggleDebugViewPort());
		canvas.set(km);
		
		new Ticker(model);
	}

}
