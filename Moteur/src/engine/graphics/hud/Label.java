package engine.graphics.hud;

import java.util.function.Supplier;

import oop.graphics.Color;
import oop.graphics.Font;
import oop.graphics.Graphics;

public class Label implements HudElement {

	public final static String DEFAULT_FONT_NAME;
	public final static int DEFAULT_FONT_SIZE;
	public final static int DEFAULT_FONT_STYLE;

	static {
		DEFAULT_FONT_NAME = "Monospaced";
		DEFAULT_FONT_SIZE = 12;
		DEFAULT_FONT_STYLE = Font.PLAIN;
	}

	protected Supplier<String> text;
	private boolean visible;

	private String fontName;
	private int fontStyle;
	private int fontSize;

	public boolean centeredText;

	/**
	 * pixel coordinate
	 */
	protected PixelCoordinate pc;

	public Color color;

	public Label(String text, PixelCoordinate pc, Color color, boolean centeredText) {
		this(text, pc, true, DEFAULT_FONT_NAME, DEFAULT_FONT_SIZE, DEFAULT_FONT_STYLE, color, centeredText);
	}

	public Label(Supplier<String> text, PixelCoordinate pc, Color color, boolean centeredText) {
		this(text, pc, true, DEFAULT_FONT_NAME, DEFAULT_FONT_SIZE, DEFAULT_FONT_STYLE, color, centeredText);
	}

	public Label(String text, PixelCoordinate pc, boolean visible, String fontName, int fontSize, int fontStyle,
			Color color, boolean centeredText) {
		this(() -> text, pc, visible, fontName, fontSize, fontStyle, color, centeredText);
	}

	public Label(Supplier<String> text, PixelCoordinate pc, boolean visible, String fontName, int fontSize,
			int fontStyle, Color color, boolean centeredText) {
		this.text = text;
		this.pc = pc;
		this.visible = visible;
		this.fontName = fontName;
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
		this.color = color;
		this.centeredText = centeredText;
	}

	@Override
	public void draw(Graphics g) {

		g.setColor(color);
		Font f = g.getFont(fontName, fontStyle, fontSize);
		g.setFont(f);

		String text = this.text.get();
		g.drawString(text, pc.x - ((centeredText) ? f.getWidth(text) / 2 : 0), pc.y);
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisibility(boolean shown) {
		this.visible = shown;
	}

	public void changeFont(String name, int style, int size) {
		this.fontName = name;
		this.fontStyle = style;
		this.fontSize = size;
	}

	public String getFontName() {
		return fontName;
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public int getFontSize() {
		return fontSize;
	}

}
