package game.graphics;

import java.awt.Dimension;
import java.util.List;
import java.util.logging.Logger;

import engine.controller.Controller;
import engine.controller.KeyManager;
import engine.entity.Entity;
import engine.gal.CompositeGALStunt;
import engine.gal.GALBot;
import engine.gal.arguments.Category;
import engine.gal.aut.AST2Aut;
import engine.gal.aut.Automaton;
import engine.graphics.FpsManager;
import engine.graphics.Hud;
import engine.graphics.View;
import engine.logs.LoggerManager;
import engine.move.Model;
import engine.move.Ticker;
import engine.move.ViewPort;
import gal.ast.AST;
import gal.parser.Parser;
import game.Game;
import game.gradius.entity.Cannon;
import game.gradius.entity.CannonSlot;
import game.gradius.entity.Ship;
import game.gradius.entity.Power;
import game.gradius.graphics.PowerAvatar;
import game.gradius.graphics.CannonAvatar;
import game.gradius.graphics.ShipAvatar;
import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;
import oop.graphics.VirtualKeyCodes;
import oop.tasks.Runnable;
import oop.tasks.Runtime;
import oop.tasks.Task;

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

		// =========================
		// Category interactions
		// =========================

		Category.setInteraction(Category.Team, Category.Team, false);

		Category.setInteraction(Category.Team, Category.Obstacle, true);
		Category.setInteraction(Category.Obstacle, Category.Team, true);

		Category.setInteraction(Category.Team, Category.Adversary, true);
		Category.setInteraction(Category.Adversary, Category.Team, true);
		
		Category.setInteraction(Category.Team, Category.Power, true);
		Category.setInteraction(Category.Power, Category.Team, true);

		// =========================
		// Ship composite entity
		// =========================

		Ship ship = new Ship();
		place(ship, 5, game.grid.height() / 2);

		Cannon topCannon = new Cannon(CannonSlot.TOP);
		Cannon bottomCannon = new Cannon(CannonSlot.BOTTOM);

		topCannon.placeRelativeTo(ship);
		bottomCannon.placeRelativeTo(ship);
		
		Power power = new Power();

		// D'abord ajouter les entities au model
		model.add(ship);
		model.add(topCannon);
		model.add(bottomCannon);
		model.add(power);

		// Ensuite seulement créer le bot / stunt GAL
		GALBot shipBot = new GALBot(ship);
		ship.bot(shipBot);

		CompositeGALStunt shipStunt = new CompositeGALStunt(
				model,
				ship,
				List.of(topCannon, bottomCannon)
		);

		shipStunt.setMaxLinearSpeed(20.0);
		shipStunt.setMaxAngularSpeed(0.0);
		shipStunt.setBaseLinearSpeed(10.0, 0.0);

		Automaton shipAutomaton = loadAutomaton("src/engine/gal/ship_fixed.gal", "Ship");
		shipBot.set(shipAutomaton);

		// =========================
		// View
		// =========================

		ViewPort vp = new ViewPort(0, 0, 120, 120);
		vp.rail(10, 0); 
		model.setViewPort(vp);
		View view = new View(vp);

		view.add(new ShipAvatar(ship));
		view.add(new CannonAvatar(topCannon));
		view.add(new CannonAvatar(bottomCannon));
		view.add(new PowerAvatar(power));

		MapView mapView = new MapView();
		view.setBackground(mapView::paint);

		FpsManager fpsC = new FpsManager(task, FPS, FPS_LOGGING);

		Hud hud = new Hud();
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

				view.setCanvasArea(0, 0, windowWidth, windowHeight);
				view.paint(g);

				fpsC.countFrame();
			}

			@Override
			public void revoked(Canvas canvas) {
				System.exit(0);
			}
		});

		// Le KeyManager capte le clavier, mais il faut aussi lui donner
		// un Controller relié au stunt du vaisseau.
		KeyManager km = new KeyManager();
		km.addDelegate(new Controller(shipStunt));
		km.bind(VirtualKeyCodes.VK_V, () -> view.toggleDebugViewPort());
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

	private Automaton loadAutomaton(String galFilePath, String automatonName) {
		try {
			AST ast = Parser.from_file(galFilePath);
			AST2Aut converter = new AST2Aut(ast);

			for (Automaton automaton : converter.getAutomata()) {
				if (automaton.name().equals(automatonName)) {
					return automaton;
				}
			}

			throw new IllegalArgumentException("Automaton not found: " + automatonName);

		} catch (Exception e) {
			throw new RuntimeException("Cannot load GAL automaton from: " + galFilePath, e);
		}
	}
}