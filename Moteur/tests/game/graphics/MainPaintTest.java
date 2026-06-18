package game.graphics;

import java.awt.Dimension;
import java.util.List;
import java.util.logging.Logger;

import engine.entity.Entity;
import engine.gal.CompositeGALStunt;
import engine.gal.GALBot;
import game.move.BasicStunt;
import engine.gal.actions.Move;
import engine.gal.arguments.Category;
import engine.gal.arguments.Direction;
import engine.gal.aut.Automaton;
import engine.gal.aut.State;
import engine.gal.aut.Transition;
import engine.gal.condition.GALCondition;
import engine.graphics.FpsManager;
import engine.graphics.Hud;
import engine.graphics.Label;
import engine.graphics.View;
import engine.logs.LoggerManager;
import engine.move.Model;
import engine.move.Ticker;
import game.Game;
import game.entity.Ship;
import game.gradius.graphics.CannonAvatar;
import game.gradius.graphics.ShipAvatar;
import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;
import oop.graphics.VirtualKeyCodes;
import oop.tasks.Runnable;
import oop.tasks.Runtime;
import oop.tasks.Task;
import engine.controller.KeyManager;
import game.entity.Cannon;
import game.entity.CannonSlot;

public class MainPaintTest implements Runnable {

	private static final Logger logger = LoggerManager.getLogger(MainPaintTest.class.getName());
	private static final int FPS = 30;
	private static final boolean FPS_LOGGING = true;

	public static final int WIDTH = 640;
	public static final int HEIGHT = 640;

	public static void main(String[] args) throws Exception {
		Runnable r = new MainPaintTest();
		Dimension windowSize = new Dimension(WIDTH, HEIGHT);
		Runtime.boot(windowSize, r);
		Runtime.shutdown();
	}

	@Override
	public void run() throws Exception {

		logger.info("STARTED SHIP TEST MAIN");
		Task task = Runtime.task();
		Canvas canvas = (Canvas) task.find("canvas");

		Game game = new Game(38, 41);
		Model model = new Model(game.grid);

		Category.setInteraction(Category.Team, Category.Team, false);

		Category.setInteraction(Category.Team, Category.Obstacle, true);
		Category.setInteraction(Category.Obstacle, Category.Team, true);

		Category.setInteraction(Category.Team, Category.Adversary, true);
		Category.setInteraction(Category.Adversary, Category.Team, true);

		// =========================
		// Ship test entity
		// =========================

		Ship ship = new Ship();
		place(ship, 5, game.grid.height() / 2);
		

		Cannon topCannon = new Cannon(CannonSlot.TOP);
		Cannon bottomCannon = new Cannon(CannonSlot.BOTTOM);

		topCannon.placeRelativeTo(ship);
		bottomCannon.placeRelativeTo(ship);
		
		System.out.println("top = " + topCannon.center().x() + ", " + topCannon.center().y());
		System.out.println("bottom = " + bottomCannon.center().x() + ", " + bottomCannon.center().y());
		
		model.add(ship);
		model.add(topCannon);
		model.add(bottomCannon);
		
		BasicStunt topCannonStunt = new BasicStunt(model, topCannon);
		topCannonStunt.set(game.isu.new Vector(0, 0));
		topCannonStunt.set_aSpeed(0);

		BasicStunt bottomCannonStunt = new BasicStunt(model, bottomCannon);
		bottomCannonStunt.set(game.isu.new Vector(0, 0));
		bottomCannonStunt.set_aSpeed(0);
		
		// =========================
		// Minimal GAL test
		// =========================
		// Pour l'instant on ne passe pas par ship.gal : on construit un automate
		// à la main pour vérifier le câblage Entity -> GALBot -> GALStunt -> Model.
		// Comportement : à chaque action GAL, le vaisseau avance vers l'Est.

		GALBot shipBot = new GALBot(ship);
		ship.bot(shipBot);

		CompositeGALStunt shipStunt = new CompositeGALStunt(model, ship, List.of(topCannon, bottomCannon));
		shipStunt.setMaxLinearSpeed(20.0);
		shipStunt.setMaxAngularSpeed(0.0); // le vaisseau Gradius ne tourne pas

		State life = new State("Life", 0);
		Automaton shipAutomaton = new Automaton("Ship", life);
		shipAutomaton.add(new Transition(life, GALCondition.TRUE, new Move(Direction.E, 1.0, 1), life));
		shipBot.set(shipAutomaton);

		// =========================
		// View
		// =========================

		View view = new View();
		view.add(new ShipAvatar(ship));
		view.add(new CannonAvatar(topCannon));
		view.add(new CannonAvatar(bottomCannon));

		MapView mapView = new MapView();

		FpsManager fpsC = new FpsManager(task, FPS, FPS_LOGGING);

		Hud hud = new Hud();
		Label labelFPS = new Label(() -> "FPS " + fpsC.getFps(), 20, 50, Colors.white);
		hud.add(labelFPS);
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

				double totalWidthPixels = game.grid.width() * game.cmPerCell * game.pixelPerCm;
				double totalHeightPixels = game.grid.height() * game.cmPerCell * game.pixelPerCm;

				double scale = Math.min(windowWidth / totalWidthPixels, windowHeight / totalHeightPixels);

				int offsetX = (int) Math.round((windowWidth - totalWidthPixels * scale) / 2.0);
				int offsetY = (int) Math.round((windowHeight - totalHeightPixels * scale) / 2.0);

				g.translate(offsetX, offsetY);
				g.scale(scale, scale);

				mapView.paint(g);
				view.paint(g);

				fpsC.countFrame();
			}

			@Override
			public void revoked(Canvas canvas) {
				System.exit(0);
			}
		});

		KeyManager km = new KeyManager();
		km.bind(VirtualKeyCodes.VK_TAB, () -> labelFPS.setVisibility(!labelFPS.isVisible()));
		canvas.set(km);

		/*
		 * Le Ticker se lance déjà dans son constructeur.
		 */
		new Ticker(model);
	}

	private void place(Entity entity, int x, int y) {
		Game game = Game.game();
		entity.place(game.grid.new Position(x, y));
	}

}
