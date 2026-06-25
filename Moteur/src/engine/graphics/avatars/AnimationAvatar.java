package engine.graphics.avatars;

import java.util.logging.Level;

import engine.entity.Entity;
import engine.geometry.ISU;
import oop.graphics.BufferedImage;
import oop.graphics.Graphics;

public abstract class AnimationAvatar extends RessourceAvatar {

	private static double DEFAULT_FRAME_DURATION_S; // attention c'est des secondes
	private double frameDurationS; // attention c'est des secondes

	static {
		DEFAULT_FRAME_DURATION_S = 0.1;
	}

	private BufferedImage[] frames;
	private double time;
	private int frameIndex;

	protected AnimationAvatar(Entity entity, String imagePath, int multX, int multY) {
		super(entity, imagePath, multX, multY);
		this.frameDurationS = DEFAULT_FRAME_DURATION_S;
	}

	protected void initImage(Graphics g, ImageSpriteRect[] framesArray) {
		if (LOGGING && INFO) {
			logger.log(Level.INFO, "Init animation avatar");
		}
		image = g.load(this.imagePath);

		frames = new BufferedImage[framesArray.length];

		for (int i = 0; i < framesArray.length; i++) {
			ImageSpriteRect isr = framesArray[i];
			frames[i] = image.getSubimage(isr.x, isr.y, isr.w, isr.h);
		}

		time = 0.0;
		frameIndex = 0;
	}

	@Override
	public void updateAnimation(double delta_t) {
		if (frames == null || frames.length == 0) {
			return;
		}

		time += delta_t;

		while (time >= frameDurationS) {
			// System.out.println(String.format("updating animation time:%f,
			// frameDurationS:%f", time, frameDurationS));
			time -= frameDurationS;
			frameIndex = (frameIndex + 1) % frames.length;
		}
	}

	@Override
	public void paint(Graphics g) {
		if (image == null || frames == null) {
			throw new IllegalStateException("Tried to paint an animation avatar without initializing it before !");
		}

		if (dead() || entity().center() == null) {
			return;
		}

		super.paint(g); // check if need to show debug collision boxes

		BufferedImage frame = frames[frameIndex];

		ISU.Coord coord = entity().center();
		ISU.Dimension size = entity().size();

		int width = Math.max(1, this.cmToPixel(size.x())) * multX;
		int height = Math.max(1, this.cmToPixel(size.y())) * multY;

		int xCenter = this.cmToPixel(coord.x());
		int yCenter = this.cmToPixel(coord.y());

		int xTopLeft = xCenter - width / 2;
		int yTopLeft = yCenter - height / 2;

		g.drawImage(frame, xTopLeft, yTopLeft, width, height);
	}
}
