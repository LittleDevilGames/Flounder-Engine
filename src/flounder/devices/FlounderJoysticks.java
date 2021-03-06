package flounder.devices;

import flounder.framework.*;
import flounder.logger.*;
import org.lwjgl.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A module used for the creation, updating and destruction of joysticks.
 */
public class FlounderJoysticks extends Module {
	private static final FlounderJoysticks INSTANCE = new FlounderJoysticks();
	public static final String PROFILE_TAB_NAME = "Joysticks";

	private FloatBuffer joystickAxes[];
	private ByteBuffer joystickButtons[];
	private String joystickNames[];

	/**
	 * Creates a new GLFW joystick manager.
	 */
	public FlounderJoysticks() {
		super(ModuleUpdate.UPDATE_PRE, PROFILE_TAB_NAME, FlounderLogger.class, FlounderDisplay.class);
	}

	@Override
	public void init() {
		this.joystickAxes = new FloatBuffer[GLFW_JOYSTICK_LAST];
		this.joystickButtons = new ByteBuffer[GLFW_JOYSTICK_LAST];
		this.joystickNames = new String[GLFW_JOYSTICK_LAST];
	}

	@Override
	public void update() {
		// For each joystick check if connected and update.
		for (int i = 0; i < GLFW_JOYSTICK_LAST; i++) {
			if (glfwJoystickPresent(i)) {
				if (joystickAxes[i] == null || joystickButtons[i] == null || joystickNames[i] == null) {
					FlounderLogger.log("Connecting Joystick: " + i);
					joystickAxes[i] = BufferUtils.createFloatBuffer(glfwGetJoystickAxes(i).capacity());
					joystickButtons[i] = BufferUtils.createByteBuffer(glfwGetJoystickButtons(i).capacity());
					joystickNames[i] = glfwGetJoystickName(i);
				}

				joystickAxes[i].clear();
				joystickAxes[i].put(glfwGetJoystickAxes(i));

				joystickButtons[i].clear();
				joystickButtons[i].put(glfwGetJoystickButtons(i));
			} else {
				if (joystickAxes[i] != null || joystickButtons[i] != null || joystickNames[i] != null) {
					FlounderLogger.log("Disconnecting Joystick: " + i);
					joystickAxes[i].clear();
					joystickAxes[i] = null;

					joystickButtons[i].clear();
					joystickButtons[i] = null;
					joystickNames[i] = null;
				}
			}
		}
	}

	@Override
	public void profile() {
	}

	/**
	 * Determines if the GLFW joystick is connected
	 *
	 * @param joystick The joystick to check connection with.
	 *
	 * @return If the joystick is connected.
	 */
	public static boolean isConnected(int joystick) {
		return joystick >= 0 && joystick < GLFW_JOYSTICK_LAST && INSTANCE.joystickNames[joystick] != null;
	}

	/**
	 * Gets the name of the joystick.
	 *
	 * @param joystick The joystick of interest.
	 *
	 * @return The joysticks name.
	 */
	public static String getName(int joystick) {
		return INSTANCE.joystickNames[joystick];
	}

	/**
	 * Gets the value of a joystick's axis.
	 *
	 * @param joystick The joystick of interest.
	 * @param axis The axis of interest.
	 *
	 * @return The value of the joystick's axis.
	 */
	public static float getAxis(int joystick, int axis) {
		return INSTANCE.joystickAxes[joystick].get(axis);
	}

	/**
	 * Gets the whether a button on a joystick is pressed.
	 *
	 * @param joystick The joystick of interest.
	 * @param button The button of interest.
	 *
	 * @return Whether a button on a joystick is pressed.
	 */
	public static boolean getButton(int joystick, int button) {
		return INSTANCE.joystickButtons[joystick].get(button) != 0;
	}

	/**
	 * Gets the number of axes the joystick offers.
	 *
	 * @param joystick The joystick of interest.
	 *
	 * @return The number of axes the joystick offers.
	 */
	public static int getCountAxes(int joystick) {
		return INSTANCE.joystickAxes[joystick].capacity();
	}

	/**
	 * Gets the number of buttons the joystick offers.
	 *
	 * @param joystick The joystick of interest.
	 *
	 * @return The number of buttons the joystick offers.
	 */
	public static int getCountButtons(int joystick) {
		return INSTANCE.joystickButtons[joystick].capacity();
	}

	@Override
	public Module getInstance() {
		return INSTANCE;
	}

	@Override
	public void dispose() {
	}
}
