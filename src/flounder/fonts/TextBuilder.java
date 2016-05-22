package flounder.fonts;

public class TextBuilder {
	private final String text;
	private boolean centered;
	private float textSize;
	private FontType font;

	protected TextBuilder(final String text) {
		this.text = text;
		this.centered = false;
		this.textSize = 1.0f;
		this.font = FontManager.NEXA_BOLD;
	}

	public Text create() {
		return new Text(text, font, textSize, centered);
	}

	public TextBuilder center() {
		centered = true;
		return this;
	}

	public TextBuilder setFontSize(final float size) {
		textSize = size;
		return this;
	}

	public TextBuilder setFont(final FontType font) {
		this.font = font;
		return this;
	}
}
