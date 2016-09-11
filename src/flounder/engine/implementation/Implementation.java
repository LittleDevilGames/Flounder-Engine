package flounder.engine.implementation;

import flounder.engine.*;
import flounder.maths.*;

/**
 * The implementation of the engine and game loop.
 */
public class Implementation implements IModule {
	private IGame game;
	private ICamera camera;
	private IRendererMaster renderer;
	private IManagerGUI managerGUI;

	private int fpsLimit;
	private boolean closedRequested;
	private Delta delta;
	private Timer timerLog;

	/**
	 * Creates a new implementation of the engine and game loop.
	 *
	 * @param game The game to be run with the engine.
	 * @param camera The main camera to use.
	 * @param renderer The master renderer to render with.
	 * @param managerGUI The manager for the implementation for GUIs.
	 * @param fpsLimit The maximum FPS the engine can render at.
	 */
	public Implementation(IGame game, ICamera camera, IRendererMaster renderer, IManagerGUI managerGUI, int fpsLimit) {
		this.game = game;
		this.camera = camera;
		this.renderer = renderer;
		this.managerGUI = managerGUI;

		this.fpsLimit = fpsLimit;
		closedRequested = false;
		delta = new Delta();
		timerLog = new Timer(1.0f);
	}

	@Override
	public void init() {
		managerGUI.init();
		renderer.init();
		camera.init();
		game.init();
	}

	@Override
	public void update() {
		delta.update();

		game.update();
		camera.update(game.getFocusPosition(), game.getFocusRotation(), game.isGamePaused());

		if (timerLog.isPassedTime()) {
			//	FlounderEngine.getLogger().log(Maths.roundToPlace(1.0f / getDelta(), 2) + "fps");
			timerLog.resetStartTime();
		}

		renderer.render();
		managerGUI.update();
	}

	@Override
	public void profile() {

	}

	public IGame getGame() {
		return game;
	}

	public ICamera getCamera() {
		return camera;
	}

	public IRendererMaster getRendererMaster() {
		return renderer;
	}

	public IManagerGUI getManagerGUI() {
		return managerGUI;
	}

	public float getDelta() {
		return delta.getDelta();
	}

	public float getDeltaTime() {
		return delta.getTime();
	}

	public int getFpsLimit() {
		return fpsLimit;
	}

	public void setFpsLimit(int fpsLimit) {
		this.fpsLimit = fpsLimit;
	}

	public boolean isRunning() {
		return !closedRequested && !FlounderEngine.getDevices().getDisplay().isClosed();
	}

	/**
	 * Requests the gameloop to stop and the game to exit.
	 */
	public void requestClose() {
		closedRequested = true;
	}

	@Override
	public void dispose() {
		game.dispose();
		renderer.dispose();
	}
}
