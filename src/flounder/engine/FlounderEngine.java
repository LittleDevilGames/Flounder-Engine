package flounder.engine;

import flounder.devices.*;
import flounder.engine.implementation.*;
import flounder.events.*;
import flounder.fonts.*;
import flounder.guis.*;
import flounder.guis.cursor.*;
import flounder.loaders.*;
import flounder.logger.*;
import flounder.materials.*;
import flounder.maths.matrices.*;
import flounder.models.*;
import flounder.networking.*;
import flounder.particles.*;
import flounder.physics.renderer.*;
import flounder.processing.*;
import flounder.profiling.*;
import flounder.shaders.*;
import flounder.textures.*;

/**
 * Deals with much of the initializing, updating, and cleaning up of the engine.
 */
public class FlounderEngine extends Thread implements IModule {
	private static FlounderEngine instance;

	private Version version;

	private FlounderDevices devices;
	private FlounderProcessors processors;
	private FlounderEvents events;
	private FlounderLoader loader;
	private FlounderMaterials materials;
	private FlounderModels models;
	private FlounderTextures textures;
	private FlounderFonts fonts;
	private FlounderGuis guis;
	private FlounderCursor cursor;
	private FlounderParticles particles;
	private FlounderAABBs aabbs;
	private FlounderNetwork network;
	private FlounderLogger logger;
	private FlounderShaders shaders;
	private FlounderProfiler profiler;

	private Implementation implementation;

	private boolean initialized;

	/**
	 * Carries out the setup for basic engine components and the engine. Call {@link #startEngine(FontType)} immediately after this.
	 *
	 * @param implementation The game implementation of the engine.
	 * @param width The window width in pixels.
	 * @param height The window height in pixels.
	 * @param title The window title.
	 * @param vsync If the window will use vSync..
	 * @param antialiasing If OpenGL will use antialiasing.
	 * @param samples How many MFAA samples should be done before swapping buffers. Zero disables multisampling. GLFW_DONT_CARE means no preference.
	 * @param fullscreen If the window will start fullscreen.
	 */
	public FlounderEngine(Implementation implementation, int width, int height, String title, boolean vsync, boolean antialiasing, int samples, boolean fullscreen) {
		instance = this;

		// Increment revision every fix for the minor version release. Minor version represents the build month. Major incremented every two years OR after major core engine rewrites.
		version = new Version("1.09.11");

		this.devices = new FlounderDevices(width, height, title, vsync, antialiasing, samples, fullscreen);
		this.processors = new FlounderProcessors();
		this.events = new FlounderEvents();
		this.loader = new FlounderLoader();
		this.materials = new FlounderMaterials();
		this.models = new FlounderModels();
		this.textures = new FlounderTextures();
		this.fonts = new FlounderFonts();
		this.guis = new FlounderGuis();
		this.cursor = new FlounderCursor();
		this.particles = new FlounderParticles();
		this.aabbs = new FlounderAABBs();
		this.network = new FlounderNetwork(1331);
		this.logger = new FlounderLogger();
		this.shaders = new FlounderShaders();
		this.profiler = new FlounderProfiler(title + " Profiler");

		this.implementation = implementation;
	}

	/**
	 * Starts the engine!
	 *
	 * @param defaultType Sets the default font family (nullable).
	 */
	public void startEngine(FontType defaultType) {
		if (defaultType != null) {
			TextBuilder.DEFAULT_TYPE = defaultType;
		}

		run();
	}

	@Override
	public void init() {
		if (!initialized) {
			logger.init();
			profiler.init();
			devices.init();
			shaders.init();
			processors.init();
			loader.init();
			materials.init();
			models.init();
			textures.init();
			fonts.init();
			guis.init();
			cursor.init();
			particles.init();
			aabbs.init();
			network.init();
			events.init();
			implementation.init();

			initialized = true;
		}
	}

	@Override
	public void run() {
		try {
			init();

			while (isRunning()) {
				update();
				profile();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.exception(e);
		} finally {
			dispose();
		}
	}

	@Override
	public void update() {
		if (initialized) {
			devices.update();

			loader.update();
			materials.update();
			models.update();
			textures.update();
			shaders.update();
			processors.update();
			fonts.update();
			guis.update();
			cursor.update();
			events.update();

			implementation.update();

			particles.update();
			aabbs.update();
			logger.update();
			profiler.update();
			network.update();

			devices.swapBuffers();
		}
	}

	@Override
	public void profile() {
		if (FlounderEngine.getProfiler().isOpen()) {
			devices.profile();
			processors.profile();
			shaders.profile();
			events.profile();
			implementation.profile();
			particles.profile();
			aabbs.profile();
			network.profile();
			loader.profile();
			materials.profile();
			models.profile();
			textures.profile();
			guis.profile();
			cursor.profile();
			fonts.profile();
			logger.profile();
			profiler.profile();
		}
	}

	/**
	 * Gets the engines current version.
	 *
	 * @return The engines current version.
	 */
	public static Version getVersion() {
		return instance.version;
	}

	/**
	 * Gets the engines current device manager.
	 *
	 * @return The engines current device manager.
	 */
	public static FlounderDevices getDevices() {
		return instance.devices;
	}

	/**
	 * Gets the engines current profiler.
	 *
	 * @return The engines current profiler.
	 */
	public static FlounderProfiler getProfiler() {
		return instance.profiler;
	}

	/**
	 * Gets the engines current event manager.
	 *
	 * @return The engines current event manager.
	 */
	public static FlounderEvents getEvents() {
		return instance.events;
	}

	/**
	 * Gets the engines current logger.
	 *
	 * @return The engines current logger.
	 */
	public static FlounderLogger getLogger() {
		return instance.logger;
	}

	/**
	 * Gets the engines current OpenGL loader.
	 *
	 * @return The engines current OpenGL loader.
	 */
	public static FlounderLoader getLoader() {
		return instance.loader;
	}

	/**
	 * Gets the engines current OpenGL model loader.
	 *
	 * @return The engines current OpenGL model loader.
	 */
	public static FlounderModels getModels() {
		return instance.models;
	}

	/**
	 * Gets the engines current MTL materials loader.
	 *
	 * @return The engines current MTL materials loader.
	 */
	public static FlounderMaterials getMaterials() {
		return instance.materials;
	}

	/**
	 * Gets the engines current shader load processor.
	 *
	 * @return The engines current shader load processor.
	 */
	public static FlounderShaders getShaders() {
		return instance.shaders;
	}

	/**
	 * Gets the engines current request processor.
	 *
	 * @return The engines current request processor.
	 */
	public static FlounderProcessors getProcessors() {
		return instance.processors;
	}

	/**
	 * Gets the engines current texture manager.
	 *
	 * @return The engines current texture manager.
	 */
	public static FlounderTextures getTextures() {
		return instance.textures;
	}

	/**
	 * Gets the engines current gui manager.
	 *
	 * @return The engines current gui manager.
	 */
	public static FlounderGuis getGuis() {
		return instance.guis;
	}

	/**
	 * Gets the engines current cursor manager.
	 *
	 * @return The engines current cursor manager.
	 */
	public static FlounderCursor getCursor() {
		return instance.cursor;
	}

	/**
	 * Gets the engines current font manager.
	 *
	 * @return The engines current font manager.
	 */
	public static FlounderFonts getFonts() {
		return instance.fonts;
	}

	/**
	 * Gets the engines current particles renderer manager.
	 *
	 * @return The engines current particles renderer manager.
	 */
	public static FlounderParticles getParticles() {
		return instance.particles;
	}

	/**
	 * Gets the engines current AABB renderer manager.
	 *
	 * @return The engines current AABB renderer manager.
	 */
	public static FlounderAABBs getAABBs() {
		return instance.aabbs;
	}

	/**
	 * Gets the engines current network manager.
	 *
	 * @return The engines current network manager.
	 */

	public static FlounderNetwork getNetwork() {
		return instance.network;
	}

	/**
	 * Gets the modules current game manager.
	 *
	 * @return The modules current game manager.
	 */
	public static IGame getGame() {
		return instance.implementation.getGame();
	}

	/**
	 * Gets the engines camera implementation.
	 *
	 * @return The engines camera implementation.
	 */
	public static ICamera getCamera() {
		return instance.implementation.getCamera();
	}

	/**
	 * Gets the modules current master renderer.
	 *
	 * @return The modules current master renderer.
	 */
	public static IRendererMaster getMasterRenderer() {
		return instance.implementation.getRendererMaster();
	}

	/**
	 * Gets the modules current GUI manager.
	 *
	 * @return The modules current GUI manager.
	 */
	public static IManagerGUI getManagerGUI() {
		return instance.implementation.getManagerGUI();
	}

	/**
	 * Gets the projection matrix used in the current scene renderObjects.
	 *
	 * @return The projection matrix used in the current scene renderObjects.
	 */
	public static Matrix4f getProjectionMatrix() {
		return instance.implementation.getRendererMaster().getProjectionMatrix();
	}

	public static void setTargetFPS(int fpsLimit) {
		instance.implementation.setFpsLimit(fpsLimit);
	}

	public static int getTargetFPS() {
		return instance.implementation.getFpsLimit();
	}

	/**
	 * Gets the delta (seconds) between updates.
	 *
	 * @return The delta between updates.
	 */
	public static float getDelta() {
		return instance.implementation.getDelta();
	}

	/**
	 * Gets the current engine time (all delta added up).
	 *
	 * @return The current engine time.
	 */
	public static float getDeltaTime() {
		return instance.implementation.getDeltaTime();
	}

	/**
	 * Gets if the engine still running?
	 *
	 * @return Is the engine still running?
	 */
	public static boolean isRunning() {
		return instance.implementation.isRunning();
	}

	/**
	 * Gets the current screen blur factor.
	 *
	 * @return The current screen blur factor.
	 */
	public static float getScreenBlur() {
		return instance.implementation.getGame().getScreenBlur();
	}

	/**
	 * Gets if the game currently paused.
	 *
	 * @return Is the game currently paused?
	 */
	public static boolean isGamePaused() {
		return instance.implementation.getGame().isGamePaused();
	}

	/**
	 * Requests the gameloop to stop and the game to exit.
	 */
	public static void requestClose() {
		instance.implementation.requestClose();
	}

	/**
	 * Gets if the engine is currently initialized.
	 *
	 * @return Is the engine is currently initialized?
	 */
	public static boolean isInitialized() {
		return instance.initialized;
	}

	@Override
	public void dispose() {
		if (initialized) {
			processors.dispose();
			shaders.dispose();
			events.dispose();
			models.dispose();
			materials.dispose();
			loader.dispose();
			network.dispose();
			particles.dispose();
			aabbs.dispose();
			textures.dispose();
			cursor.dispose();
			guis.dispose();
			fonts.dispose();
			implementation.dispose();
			devices.dispose();
			profiler.dispose();
			logger.dispose();

			initialized = false;
		}
	}
}
