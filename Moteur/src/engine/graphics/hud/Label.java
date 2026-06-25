package engine.graphics.hud;

import java.util.function.Supplier;

import oop.graphics.Color;
import oop.graphics.Font;
import oop.graphics.Graphics;

public class Label implements ILabel {

	private Supplier<String> text;
	private boolean visible;

	private String fontName;
	private int fontStyle;
	private int fontSize;

	private boolean mustBeDeleted;

	public boolean centeredText;

	/**
	 * pixel coordinate
	 */
	protected PixelCoordinate position;
	protected PixelCoordinate offset;

	public Color color;

	public Label(String text, PixelCoordinate position, Color color, boolean centeredText) {
		this(text, position, true, DEFAULT_FONT_NAME, DEFAULT_FONT_SIZE, DEFAULT_FONT_STYLE, color, centeredText);
	}

	public Label(Supplier<String> text, PixelCoordinate position, Color color, boolean centeredText) {
		this(text, position, new PixelCoordinate(0, 0), true, DEFAULT_FONT_NAME, DEFAULT_FONT_SIZE, DEFAULT_FONT_STYLE,
				color, centeredText);
	}

	public Label(String text, PixelCoordinate position, boolean visible, String fontName, int fontSize, int fontStyle,
			Color color, boolean centeredText) {
		this(() -> text, position, new PixelCoordinate(0, 0), visible, fontName, fontSize, fontStyle, color,
				centeredText);
	}

	public Label(Supplier<String> text, PixelCoordinate position, PixelCoordinate offset, boolean visible,
			String fontName, int fontSize, int fontStyle, Color color, boolean centeredText) {
		this.text = text;
		this.position = position;
		this.visible = visible;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
		this.color = color;
		this.centeredText = centeredText;
		this.mustBeDeleted = false;
		this.offset = offset;
	}

	@Override
	public void draw(Graphics g) {

		g.setColor(color);
		Font f = g.getFont(fontName, fontStyle, fontSize);
		g.setFont(f);

		String text = this.text.get();
		g.drawString(text, position.x - ((centeredText) ? f.getWidth(text) / 2 : 0), position.y);
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisibility(boolean shown) {
		this.visible = shown;
	}

	@Override
	public void changeFont(String name, int style, int size) {
		this.fontName = name;
		this.fontStyle = style;
		this.fontSize = size;
	}

	@Override
	public String getFontName() {
		return fontName;
	}

	@Override
	public int getFontStyle() {
		return fontStyle;
	}

	@Override
	public int getFontSize() {
		return fontSize;
	}

	@Override
	public void update(int canvasWidth, int canvasHeigh) {
		// pas besoin de maj la position d'un label immobile
	}

	@Override
	public boolean mustBeDeleted() {
		return mustBeDeleted;
	}

	public void delete() {
		mustBeDeleted = true;
	}

	@Override
	public void set(Supplier<String> newText) {
		this.text = newText;
	}

	@Override
	public PixelCoordinate offset() {
		return offset;
	}
}
