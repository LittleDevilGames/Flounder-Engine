package flounder.fonts;

import flounder.devices.*;
import flounder.framework.*;
import flounder.guis.*;
import flounder.loaders.*;
import flounder.maths.*;
import flounder.maths.vectors.*;
import flounder.visual.*;

/**
 * A class that holds info about a text object.
 */
public class Text {
	private String textString;
	private FontType fontType;
	private float fontSize;
	private GuiAlign guiAlign;

	private int textMesh;
	private int vertexCount;
	private float lineMaxSize;
	private int numberOfLines;
	private float originalWidth;
	private float originalHeight;

	private String newText;

	private ValueDriver positionXDriver;
	private ValueDriver positionYDriver;
	private ValueDriver alphaDriver;
	private ValueDriver scaleDriver;
	private ValueDriver glowDriver;
	private ValueDriver borderDriver;
	private Colour colour;
	private Colour borderColour;
	private Vector2f position;
	private float rotation;

	private boolean solidBorder;
	private boolean glowBorder;

	private float currentScale;
	private float currentX;
	private float currentY;
	private float currentAlpha;
	private float glowSize;
	private float borderSize;

	private boolean loaded;

	/**
	 * Creates a new text object.
	 *
	 * @param text The inital text.
	 * @param font The font family to use.
	 * @param fontSize The font size to use.
	 * @param guiAlign How the text should be aligned.
	 */
	protected Text(String text, FontType font, float fontSize, GuiAlign guiAlign) {
		this.textString = text;
		this.fontType = font;
		this.fontSize = fontSize;
		this.guiAlign = guiAlign;

		this.textMesh = -1;
		this.vertexCount = -1;

		this.alphaDriver = new ConstantDriver(1.0f);
		this.scaleDriver = new ConstantDriver(1.0f);
		this.glowDriver = new ConstantDriver(0.0f);
		this.borderDriver = new ConstantDriver(0.0f);
		this.colour = new Colour(0.0f, 0.0f, 0.0f, 1.0f);
		this.borderColour = new Colour(1.0f, 1.0f, 1.0f, 1.0f);
		this.position = new Vector2f();
		this.rotation = 0.0f;

		this.solidBorder = false;
		this.glowBorder = false;

		this.glowSize = 0.0f;
		this.borderSize = 0.0f;

		this.loaded = false;
	}

	/**
	 * Creates a new text builder.
	 *
	 * @param text The inital text.
	 *
	 * @return The newly created text builder.
	 */
	public static TextBuilder newText(String text) {
		return new TextBuilder(text);
	}

	/**
	 * Initializes the text.
	 *
	 * @param absX The absolute x position.
	 * @param absY The absolute Y position.
	 * @param maxXLength The max length for the text.
	 */
	public void init(float absX, float absY, float maxXLength) {
		positionXDriver = new ConstantDriver(absX);
		positionYDriver = new ConstantDriver(absY);
		lineMaxSize = maxXLength;

		if (!loaded) {
			fontType.loadText(this);
			loaded = true;
		}
	}

	/**
	 * Updates the text status.
	 */
	public void update() {
		if (loaded && newText != null) {
			delete();
			textString = newText;
			fontType.loadText(this);
			newText = null;
		}

		float delta = Framework.getDelta();

		currentScale = scaleDriver.update(delta);
		currentX = positionXDriver.update(delta);
		currentY = positionYDriver.update(delta);
		currentAlpha = alphaDriver.update(delta);
		glowSize = glowDriver.update(delta);
		borderSize = borderDriver.update(delta);
	}

	public String getTextString() {
		return textString;
	}

	public void setText(String newText) {
		if (!textString.equals(newText)) {
			this.newText = newText;
		}
	}

	public float getFontSize() {
		return fontSize;
	}

	public FontType getFontType() {
		return fontType;
	}

	public GuiAlign getGuiAlign() {
		return guiAlign;
	}

	/**
	 * @return {@code true} if the mouse cursor is currently over this component.
	 */
	public boolean isMouseOver() {
		// TODO: Fix maths?
		float positionX = position.x / FlounderDisplay.getAspectRatio();
		float positionY = position.y;

		//	if (FlounderMouse.isDisplaySelected() && FlounderDisplay.isFocused()) {
		if (FlounderGuis.getSelector().getCursorX() >= positionX - (getCurrentWidth() / 2.0f) && FlounderGuis.getSelector().getCursorX() <= positionX + (getCurrentWidth() / 2.0f)) {
			if (FlounderGuis.getSelector().getCursorY() >= positionY - (getCurrentHeight() / 2.0f) && FlounderGuis.getSelector().getCursorY() <= positionY + (getCurrentHeight() / 2.0f)) {
				return true;
			}
		}
		//	}

		return false;
	}

	protected Vector2f getPosition() {
		float scaleFactor = (currentScale - 1.0f) / 2.0f;
		float xChange = scaleFactor * originalWidth;
		float yChange = scaleFactor * originalHeight;

		switch (guiAlign) {
			case LEFT:
				return position.set((currentX - xChange) + (currentScale * originalWidth * 0.5f), currentY - yChange);
			case CENTRE:
				return position.set((currentX - xChange) * (currentScale * FlounderDisplay.getAspectRatio()), currentY - yChange);
			case RIGHT:
				return position.set((currentX - xChange) * FlounderDisplay.getAspectRatio(), currentY - yChange);
			default:
				return position.set((currentX - xChange), currentY - yChange);
		}
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	protected float getTotalBorderSize() {
		if (solidBorder) {
			if (borderSize == 0.0f) {
				return 0.0f;
			} else {
				return calculateEdgeStart() + borderSize;
			}
		} else if (glowBorder) {
			return calculateEdgeStart();
		} else {
			return 0.0f;
		}
	}

	protected float calculateEdgeStart() {
		float size = fontSize * currentScale;
		return 1.0f / 300.0f * size + 137.0f / 300.0f;
	}

	public int getMesh() {
		return textMesh;
	}

	protected void setMeshInfo(int vao, int verticesCount) {
		textMesh = vao;
		vertexCount = verticesCount;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public float getMaxLineSize() {
		return lineMaxSize;
	}

	public Colour getColour() {
		return colour;
	}

	public void setColour(Colour colour) {
		this.colour.set(colour);
	}

	public void setColour(float r, float g, float b) {
		this.colour.set(r, g, b);
	}

	public Colour getBorderColour() {
		return borderColour;
	}

	public void setBorderColour(float r, float g, float b) {
		borderColour.set(r, g, b);
	}

	public void setBorderColour(Colour borderColour) {
		this.borderColour.set(borderColour);
	}

	public void setScaleDriver(ValueDriver scaleDriver) {
		this.scaleDriver = scaleDriver;
	}

	public void setBorder(ValueDriver driver) {
		borderDriver = driver;
		solidBorder = true;
		glowBorder = false;
	}

	public void removeBorder() {
		solidBorder = false;
		glowBorder = false;
	}

	protected float getGlowSize() {
		if (solidBorder) {
			return calculateAntialiasSize();
		} else if (glowBorder) {
			return glowSize;
		} else {
			return 0;
		}
	}

	protected float calculateAntialiasSize() {
		float size = fontSize * currentScale;
		size = (size - 1.0f) / (1.0f + size / 4.0f) + 1.0f;
		return 0.1f / size;
	}

	public void setGlowing(ValueDriver driver) {
		solidBorder = false;
		glowBorder = true;
		glowDriver = driver;
	}

	public void setAlphaDriver(ValueDriver driver) {
		alphaDriver = driver;
	}

	public float getScale() {
		return currentScale;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	protected void setNumberOfLines(int number) {
		numberOfLines = number;
	}

	public float getBorderSize() {
		return borderSize;
	}

	public float getOriginalWidth() {
		return originalWidth;
	}

	protected void setOriginalWidth(float width) {
		originalWidth = width;
	}

	public float getOriginalHeight() {
		return originalHeight;
	}

	protected void setOriginalHeight(float height) {
		originalHeight = height;
	}

	public float getCurrentWidth() {
		return originalWidth * currentScale;
	}

	public float getCurrentHeight() {
		return originalHeight * currentScale;
	}

	public float getCurrentX() {
		return currentX;
	}

	public float getCurrentY() {
		return currentY;
	}

	public float getCurrentAlpha() {
		return currentAlpha;
	}

	public boolean isLoaded() {
		return loaded && textMesh != -1 && vertexCount != -1;
	}

	/**
	 * Deletes the text VAO from memory.
	 */
	public void delete() {
		FlounderLoader.deleteVAOFromCache(textMesh);
	}
}
