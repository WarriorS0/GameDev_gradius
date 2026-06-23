package game.graphics;

import java.awt.Dimension;
import java.util.logging.Logger;

import engine.controller.Controller;
import engine.controller.KeyManager;
import engine.controller.MouseManager;
import engine.entity.Entity;
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
import game.Game;
import game.entity.Ghost;
import game.entity.PacMan;
import game.entity.Wall;
import game.gradius.entity.Ship;
import game.gradius.graphics.ShipAvatar;
import game.move.BasicStunt;
import game.move.GhostStunt;
import game.move.PacManStunt;
import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;
import oop.graphics.VirtualKeyCodes;
import oop.tasks.Runnable;
import oop.tasks.Runtime;
import oop.tasks.Task;

public class MainPaint implements Runnable {

	private static final Logger logger = LoggerManager.getLogger(MainPaint.class.getName());
	private static final int FPS = 30;
	private static final boolean FPS_LOGGING = true;

	public static final int WIDTH = 640;
	public static final int HEIGHT = 640;

	public static void main(String[] args) throws Exception {
		Runnable r = new MainPaint();
		Dimension windowSize = new Dimension(WIDTH, HEIGHT);
		Runtime.boot(windowSize, r);
		Runtime.shutdown();
	}

	@Override
	public void run() throws Exception {

		logger.info("STARTED MAIN");
		Task task = Runtime.task();
		Canvas canvas = (Canvas) task.find("canvas");

		Game game = new Game(38, 41);
		Model model = new Model(game.grid);

		Ship ship = new Ship();
		place( ship , 3,3);
		
		PacMan pacman = new PacMan();
		place(pacman, 2, 2);

		Ghost blinky = new Ghost();
		place(blinky, 7, 7);

		Ghost pinky = new Ghost();
		place(pinky, 5, 11);

		Ghost inky = new Ghost();
		place(inky, 11, 11);

		Ghost clyde = new Ghost();
		place(clyde, 15, 15);

		Ghost weak = new Ghost();
		place(weak, 19, 19);

		Ghost weakOver = new Ghost();
		place(weakOver, 10, 19);

		Ghost deadGhost = new Ghost();
		place(deadGhost, 23, 23);

		//addWalls(model, game);

		model.add(ship);
		
		model.add(pacman);
		PacManStunt pacmanStunt = new PacManStunt(model, pacman);
		pacmanStunt.setLinearSpeed(game.isu.new Vector(20, 0));
		pacmanStunt.setAngularSpeed(0);

		model.add(blinky);
		GhostStunt blinkyStunt = new GhostStunt(model, blinky);
		blinkyStunt.setLinearSpeed(game.isu.new Vector(10, 0));
		blinkyStunt.setAngularSpeed(0);

		model.add(pinky);
		GhostStunt pinkyStunt = new GhostStunt(model, pinky);
		pinkyStunt.setLinearSpeed(game.isu.new Vector(10, 0));
		pinkyStunt.setAngularSpeed(0);

		model.add(inky);
		GhostStunt inkyStunt = new GhostStunt(model, inky);
		inkyStunt.setLinearSpeed(game.isu.new Vector(10, 0));
		inkyStunt.setAngularSpeed(0);

		model.add(clyde);
		GhostStunt clydeStunt = new GhostStunt(model, clyde);
		clydeStunt.setLinearSpeed(game.isu.new Vector(10, 0));
		clydeStunt.setAngularSpeed(50);

		model.add(weak);
		GhostStunt weakStunt = new GhostStunt(model, weak);
		weakStunt.setLinearSpeed(game.isu.new Vector(10, 0));
		weakStunt.setAngularSpeed(0);

		model.add(weakOver);
		GhostStunt weakOverStunt = new GhostStunt(model, weakOver);
		weakOverStunt.setLinearSpeed(game.isu.new Vector(10, 0));
		weakOverStunt.setAngularSpeed(0);

		model.add(deadGhost);
		GhostStunt deadGhostStunt = new GhostStunt(model, deadGhost);
		deadGhostStunt.setLinearSpeed(game.isu.new Vector(10, 0));
		deadGhostStunt.setAngularSpeed(0);

		ViewPort vp = new ViewPort(0, 0, 60, 60);
		vp.follow(pacman);
		model.setViewPort(vp);
		View view = new View(vp);

		ShipAvatar shipAvatar = new ShipAvatar(ship);
		
		PacmanAvatar pacmanAvatar = new PacmanAvatar(pacman);

		BlinkyAvatar blinkyAvatar = new BlinkyAvatar(blinky);
		PinkyAvatar pinkyAvatar = new PinkyAvatar(pinky);
		InkyAvatar inkyAvatar = new InkyAvatar(inky);
		ClydeAvatar clydeAvatar = new ClydeAvatar(clyde);

		ClydeAvatar weakAvatar = new ClydeAvatar(weak);
		weakAvatar.weak = true;

		ClydeAvatar weakOverAvatar = new ClydeAvatar(weakOver);
		weakOverAvatar.weak_over = true;

		ClydeAvatar deadGhostAvatar = new ClydeAvatar(deadGhost);
		deadGhostAvatar.kill();

		view.add(shipAvatar);
		view.add(pacmanAvatar);
		view.add(blinkyAvatar);
		view.add(pinkyAvatar);
		view.add(inkyAvatar);
		view.add(clydeAvatar);
		view.add(weakAvatar);
		view.add(weakOverAvatar);
		view.add(deadGhostAvatar);

		MapView mapView = new MapView();
		// pour que l'image de fond se dessine correctement quand il faut, on utilise le
		// method reference operator
		view.setBackground(mapView::paint);

		FpsManager fpsC = new FpsManager(task, FPS, FPS_LOGGING);

		Hud hud = new Hud();
		// Exemple de label avec le fps
		Label labelDebug = new Label(() -> "'TAB' to toggle debug mode.", new PixelCoordinate(10, 10), Colors.white,
				false);
		hud.add(labelDebug);
		Label labelFPS = new Label(() -> "FPS " + fpsC.getFps(), new PixelCoordinate(10, 20), Colors.white, false);
		hud.add(labelFPS);
		FollowerLabel fbPacman = new FollowerLabel(() -> pacman.debugInfo(), Colors.white, pacman, 0, 10);
		fbPacman.setView(view);
		hud.add(fbPacman);
		FollowerLabel fbBlinky = new FollowerLabel(() -> blinky.debugInfo(), Colors.white, blinky, 0, 10);
		fbBlinky.setView(view);
		hud.add(fbBlinky);

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

				// La vue gère la transformation et le clipping du viewport cad de la caméra
				// elle ajuste la zone d'affichage (viewport) à cet espace de canvas et dessine
				// l'arrière-plan de la carte dans l'espace monde, sous les avatars.
				view.setCanvasArea(0, 0, windowWidth, windowHeight);
				view.paint(g);

				fpsC.countFrame();
			}

			@Override
			public void revoked(Canvas canvas) {
				System.exit(0);
			}
		});

		// NEW KeyManager and MouseManager ! J'ai mis deux exemples d'utilisation :

		KeyManager km = new KeyManager();
		km.addDelegate(new Controller(pacmanStunt));
		km.bind(VirtualKeyCodes.VK_TAB, () -> {
			labelFPS.setVisibility(!labelFPS.isVisible());
			fbPacman.setVisibility(!fbPacman.isVisible());
			fbBlinky.setVisibility(!fbBlinky.isVisible());
		});
		// Appuyer sur tab fait appraître/disparaître le label de fps.

		// 'V' bascule la vue d'ensemble : on dézoome sur toute la carte et le
		// viewport réel (qui continue de suivre l'entité) est tracé en vert.
		km.bind(VirtualKeyCodes.VK_V, () -> view.toggleDebugViewPort());

		MouseManager mm = new MouseManager();
		// Cliquer sur le bouton du millieu de la souris print un msg dans la console.
		mm.bind(MouseManager.BNO_MIDDLE_BUTTON_MOUSE, () -> System.out.print("MIDDLE BUTTON MOUSE CLICKED TEST!"));

		canvas.set(km);
		canvas.set(mm);

		/*
		 * Le Ticker se lance déjà dans son constructeur.
		 */
		new Ticker(model);
	}

	private void place(Entity entity, int x, int y) {
		Game game = Game.game();
		entity.place(game.grid.new Position(x, y));
	}

	private void addWalls(Model model, Game game) {
		for (int x = 0; x < game.grid.width(); x++) {
			addWall(model, game, x, 1);
		}

		for (int y = 0; y < game.grid.height(); y++) {
			if (y != 18 && y != 19) {
				addWall(model, game, 1, y);
			}
		}

		for (int y = 0; y < game.grid.height(); y++) {
			if (y != 18 && y != 19) {
				addWall(model, game, game.grid.width() - 2, y);
			}
		}

		for (int x = 0; x < game.grid.width(); x++) {
			addWall(model, game, x, game.grid.height() - 2);
		}
	}

	private void addWall(Model model, Game game, int x, int y) {
		Wall wall = new Wall();
		wall.place(game.grid.new Position(x, y));

		model.add(wall);

		BasicStunt wallStunt = new BasicStunt(model, wall);
		wallStunt.setLinearSpeed(game.isu.new Vector(0, 0));
		wallStunt.setAngularSpeed(0);
	}
}