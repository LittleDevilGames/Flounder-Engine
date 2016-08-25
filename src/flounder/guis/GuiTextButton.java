package flounder.guis;

import flounder.engine.*;
import flounder.fonts.*;
import flounder.sounds.*;
import flounder.visual.*;

import java.util.*;

public class GuiTextButton extends GuiComponent {
	private static final float CHANGE_TIME = 0.15f;
	private static final float MAX_SCALE = 1.1f;

	private Text text;
	private boolean mouseOver;
	private ListenerBasic guiListenerLeft;
	private ListenerBasic guiListenerRight;

	private Sound mouseHoverOverSound;
	private Sound mouseLeftClickSound;
	private Sound mouseRightClickSound;

	public GuiTextButton(Text text) {
		this.text = text;
		this.mouseOver = false;
		super.addText(text, 0.0f, 0.0f, 1.0f);
	}

	public void setSounds(Sound mouseHoverOverSound, Sound mouseLeftClickSound, Sound mouseRightClickSound) {
		this.mouseHoverOverSound = mouseHoverOverSound;
		this.mouseLeftClickSound = mouseLeftClickSound;
		this.mouseRightClickSound = mouseRightClickSound;
	}

	public Text getText() {
		return text;
	}

	public void addLeftListener(ListenerBasic guiListener) {
		guiListenerLeft = guiListener;
	}

	public void addRightListener(ListenerBasic guiListener) {
		guiListenerRight = guiListener;
	}

	@Override
	protected void updateSelf() {
		if (isMouseOver() && !mouseOver) {
			text.setScaleDriver(new SlideDriver(text.getScale(), MAX_SCALE, CHANGE_TIME));
			mouseOver = true;
			FlounderEngine.getDevices().getSound().playSystemSound(mouseHoverOverSound);
		} else if (!isMouseOver() && mouseOver) {
			text.setScaleDriver(new SlideDriver(text.getScale(), 1.0f, CHANGE_TIME));
			mouseOver = false;
		}

		if (isMouseOver() && FlounderEngine.getGuis().getSelector().wasLeftClick() && guiListenerLeft != null) {
			FlounderEngine.getDevices().getSound().playSystemSound(mouseLeftClickSound);
			guiListenerLeft.eventOccurred();
			FlounderEngine.getGuis().getSelector().cancelWasEvent();
		}

		if (isMouseOver() && FlounderEngine.getGuis().getSelector().wasRightClick() && guiListenerRight != null) {
			FlounderEngine.getDevices().getSound().playSystemSound(mouseRightClickSound);
			guiListenerRight.eventOccurred();
			FlounderEngine.getGuis().getSelector().cancelWasEvent();
		}
	}

	@Override
	protected void getGuiTextures(List<GuiTexture> guiTextures) {
	}
}
