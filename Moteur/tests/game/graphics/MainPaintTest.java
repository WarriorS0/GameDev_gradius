package game.graphics;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import engine.controller.Controller;
import engine.controller.KeyManager;
import engine.controller.MouseManager;
import engine.entity.Entity;
import engine.gal.CompositeGALStunt;
import engine.gal.GALBot;
import engine.gal.arguments.Category;
import engine.gal.aut.AST2Aut;
import engine.gal.aut.Automaton;
import engine.graphics.FollowerLabel;
import engine.graphics.FpsManager;
import engine.graphics.Hud;
import engine.graphics.Label;
import engine.graphics.PixelCoordinate;
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
import game.gradius.graphics.CannonAvatar;
import game.gradius.graphics.MapView;
import game.gradius.graphics.ShipAvatar;
import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;
import oop.graphics.VirtualKeyCodes;
import oop.tasks.Runnable;
import oop.tasks.Runtime;
import oop.tasks.Task;

public class MainPaintTest implements Runnable {

	private static final Logger logger;
	private static final boolean LOGGING;
	private static final boolean FINER;
	private static final int FPS;
	private static final boolean FPS_LOGGING;
	
	private static final DecimalFormat dfIndex;
	private static final DecimalFormat dfTime;

	private static final boolean SHOULD_DO_PAINT_PROFILING;
	private static final boolean SHOULD_DO_PAINT_PROFILING_LOGGING;
	public static final int WIDTH;
	public static final int HEIGHT;

	static {
		// CONSTANTS
		logger = LoggerManager.getLogger(MainPaintTest.class.getName());
		LOGGING = logger.getLevel() != Level.OFF;
		FINER = logger.isLoggable(Level.FINER);
		dfIndex = new DecimalFormat("00");
		dfTime = new DecimalFormat("####00");

		FPS = 30;
		FPS_LOGGING = true;

		SHOULD_DO_PAINT_PROFILING = true;
		// to log, you need to activate logging to at least finer in jul logging
		// property file on top of this
		SHOULD_DO_PAINT_PROFILING_LOGGING = false;

		WIDTH = 640;
		HEIGHT = 640;

	}

	public static void main(String[] args) throws Exception {
		// RUNNING
		Runnable r = new MainPaintTest();
		Dimension windowSize = new Dimension(WIDTH, HEIGHT);
		Runtime.boot(windowSize, r);
		Runtime.shutdown();
	}

	public final int NB_LAST_TIME_PAINT_SAVED = 96;
	private final int[] ARRAY_LAST_TIME_PAINT_SAVED = new int[NB_LAST_TIME_PAINT_SAVED];
	private int indexArrayTimePaint = 0;
	private int paintingTime = -1;
	private int sumTime = -1;
	private int minTime = 214748367;// big numbuh
	private int avgTime = -1;
	private int maxTime = -1;

	@Override
	public void run() throws Exception {

		logger.info("STARTED SHIP TEST MAIN");

		Task task = Runtime.task();
		Canvas canvas = (Canvas) task.find("canvas");

		Game game = new Game(30, 30);
		Model model = game.model;

		// =========================
		// Category interactions
		// =========================

		Category.setInteraction(Category.Team, Category.Team, false);

		Category.setInteraction(Category.Team, Category.Obstacle, true);
		Category.setInteraction(Category.Obstacle, Category.Team, true);

		Category.setInteraction(Category.Team, Category.Adversary, true);
		Category.setInteraction(Category.Adversary, Category.Team, true);

		// =========================
		// Ship composite entity
		// =========================

		Ship ship = new Ship();
		place(ship, 5, game.grid.height() / 2);

		Cannon topCannon = new Cannon(CannonSlot.TOP);
		Cannon bottomCannon = new Cannon(CannonSlot.BOTTOM);

		topCannon.placeRelativeTo(ship);
		bottomCannon.placeRelativeTo(ship);

		// D'abord ajouter les entities au model
		model.add(ship);
		model.add(topCannon);
		model.add(bottomCannon);

		// Ensuite seulement créer le bot / stunt GAL
		GALBot shipBot = new GALBot(ship);
		ship.bot(shipBot);

		CompositeGALStunt shipStunt = new CompositeGALStunt(model, ship, List.of(topCannon, bottomCannon));

		shipStunt.setMaxLinearSpeed(70.0);
		shipStunt.setMaxAngularSpeed(0.0);

		Automaton shipAutomaton = loadAutomaton("src/engine/gal/ship_fixed.gal", "Ship");
		shipBot.set(shipAutomaton);

		// =========================
		// View
		// =========================

		ViewPort vp = new ViewPort(0, 0, 60, 60);
		vp.follow(ship);
		model.setViewPort(vp);
		View view = new View(vp);

		view.add(new ShipAvatar(ship));
		view.add(new CannonAvatar(topCannon));
		view.add(new CannonAvatar(bottomCannon));

		MapView mapView = new MapView();
		view.setBackground(mapView::paint);

		FpsManager fpsC = new FpsManager(task, FPS, FPS_LOGGING);

		Hud hud = new Hud();

		Label labelDebug = new Label(() -> "'TAB' to toggle debug mode. 'V' to toggle viewport debug mode.",
				new PixelCoordinate(6, 12), Colors.white, false);
		hud.add(labelDebug);
		StringBuilder sb = new StringBuilder();
		sb.append("FPS");
		Label labelFPS = new Label(
				() -> String.format("FPS %s { min: %s; avg: %s ; max: %s (from the last %ds) } ", fpsC.getFormattedFps(),
						fpsC.getFormattedMinFps(), fpsC.getFormattedAvgFps(), fpsC.getFormattedMaxFps(), fpsC.NB_LAST_FPS_SAVED),
				new PixelCoordinate(12, 24), Colors.white, false);
		hud.add(labelFPS);
		Label labelPaintTime = new Label(
				() -> String.format("PaintTime: %dms { min: %d ; avg: %d ; max: %d (from the last %d paints) }", paintingTime,
						minTime, avgTime, maxTime, NB_LAST_TIME_PAINT_SAVED),
				new PixelCoordinate(12, 36), Colors.white, false);
		hud.add(labelPaintTime);
		FollowerLabel flShip = new FollowerLabel(() -> ship.debugInfo(), Colors.white, ship, 0, 10);
		flShip.setView(view);
		hud.add(flShip);
//		FollowerLabel fbTopCannon = new FollowerLabel(() -> topCannon.debugInfo(), Colors.white, topCannon, 0, 10);
//		fbTopCannon.setView(view);
//		hud.add(fbTopCannon);
//		FollowerLabel fbBottomCannon = new FollowerLabel(() -> bottomCannon.debugInfo(), Colors.white, bottomCannon, 0,
//				10);
//		fbBottomCannon.setView(view);
//		hud.add(fbBottomCannon);

		view.setHUD(hud);

		canvas.set(new Canvas.PaintListener() {
			@Override
			public void visible(Canvas canvas) {
				fpsC.start(canvas);
			}

			@Override
			public void paint(Canvas canvas, Graphics g) {
				long startTimePaiting = System.currentTimeMillis();

				int windowWidth = canvas.getWidth();
				int windowHeight = canvas.getHeight();

				g.setColor(Colors.black);
				g.fillRect(0, 0, windowWidth, windowHeight);

				view.setCanvasArea(0, 0, windowWidth, windowHeight);
				view.paint(g);

				fpsC.countFrame();

				long endTimePainting = System.currentTimeMillis();

				if (SHOULD_DO_PAINT_PROFILING)
					paintProfiling(startTimePaiting, endTimePainting);
			}

			@Override
			public void revoked(Canvas canvas) {
				System.exit(0);
			}
		});

		// Le KeyManager capte le clavier, mais il faut aussi lui donner
		// un Controller relié au stunt du vaisseau.
		KeyManager km = new KeyManager();

		// Appuyer sur tab fait appraître/disparaître le label de fps.
		km.bind(VirtualKeyCodes.VK_TAB, () -> {
			labelFPS.setVisibility(!labelFPS.isVisible());
			labelPaintTime.setVisibility(!labelPaintTime.isVisible());
			flShip.setVisibility(!flShip.isVisible());
		});

		MouseManager mm = new MouseManager();
		// Cliquer sur le bouton du millieu de la souris print un msg dans la console.
		mm.bind(MouseManager.BNO_MIDDLE_BUTTON_MOUSE, () -> System.out.print("MIDDLE BUTTON MOUSE CLICKED TEST!"));

		canvas.set(km);
		canvas.set(mm);

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

	private void paintProfiling(long startTimePaiting, long endTimePainting) {
		long elapsedTimePainting = endTimePainting - startTimePaiting;
		paintingTime = (int) elapsedTimePainting;
		sumTime = -1;
		minTime = 214748367;// big numbuh
		avgTime = -1;
		maxTime = -1;

		this.ARRAY_LAST_TIME_PAINT_SAVED[this.indexArrayTimePaint++] = paintingTime;
		this.indexArrayTimePaint %= this.NB_LAST_TIME_PAINT_SAVED;
		for (int i = 0; i < this.NB_LAST_TIME_PAINT_SAVED; i++) {
			int localTime = this.ARRAY_LAST_TIME_PAINT_SAVED[i];
			sumTime += localTime;
			avgTime = sumTime / NB_LAST_TIME_PAINT_SAVED;
			if (localTime < minTime)
				minTime = localTime;
			if (localTime > maxTime)
				maxTime = localTime;
		}
		if (LOGGING && FINER && SHOULD_DO_PAINT_PROFILING_LOGGING) {
			StringBuilder sb = new StringBuilder();
			sb.append("Paiting profiling \n");
			sb.append("   START: ");
			sb.append(startTimePaiting);
			sb.append(" ; END: ");
			sb.append(endTimePainting);
			sb.append(" ; ELAPSED: ");
			sb.append(elapsedTimePainting);
			sb.append('\n');
			sb.append("   time: ");
			sb.append(paintingTime);
			sb.append(" ; min: ");
			sb.append(minTime);
			sb.append(" ; avg: ");
			sb.append(avgTime);
			sb.append(" ; max: ");
			sb.append(maxTime);
			sb.append(" ; sum: ");
			sb.append(sumTime);
			for (int i = 0; i < this.NB_LAST_TIME_PAINT_SAVED; i++) {
				if (i % 8 == 0) {
					sb.append("\n   ");
				}
				sb.append("[");
				sb.append(dfIndex.format(i));
				sb.append(":");
				sb.append(dfTime.format(this.ARRAY_LAST_TIME_PAINT_SAVED[i]));
				sb.append("]");
			}
			logger.log(Level.FINER, sb.toString());
		}
	}
}