package engine.graphics.hud;

import java.util.function.Supplier;

import oop.graphics.Color;

public class AnchoredLabel extends Label implements IAnchored {

	private final Anchor anchor;

	public AnchoredLabel(String text, Anchor anchor, PixelCoordinate offset, Color color, boolean centeredText) {
		this(() -> text, anchor, offset, color, centeredText);
	}

	public AnchoredLabel(Supplier<String> text, Anchor anchor, PixelCoordinate offset, Color color,
			boolean centeredText) {
		this(text, anchor, offset, true, Label.DEFAULT_FONT_NAME, Label.DEFAULT_FONT_SIZE, Label.DEFAULT_FONT_STYLE,
				color, centeredText);
	}

	public AnchoredLabel(Supplier<String> text, Anchor anchor, PixelCoordinate offset, boolean visible, String fontName,
			int fontSize, int fontStyle, Color color, boolean centeredText) {
		super(text, new PixelCoordinate(0, 0), new PixelCoordinate(0, 0), visible, fontName, fontSize, fontStyle, color,
				centeredText);
		this.anchor = anchor;
	}

	@Override
	public void update(int canvasWidth, int canvasHeigh) {
		this.position.x = anchor.calculateX(canvasWidth) + offset.x;
		this.position.y = anchor.calculateY(canvasHeigh) + offset.x;
	}

	@Override
	public Anchor anchor() {
		return this.anchor;
	}
}
