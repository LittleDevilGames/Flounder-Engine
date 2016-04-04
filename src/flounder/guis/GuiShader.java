package flounder.guis;

import flounder.resources.*;
import flounder.shaders.*;

public class GuiShader extends ShaderProgram {
	private static final MyFile VERTEX_SHADER = new MyFile("flounder/guis", "guiVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("flounder/guis", "guiFragment.glsl");

	protected final UniformVec4 transform = new UniformVec4("transform");
	protected final UniformFloat alpha = new UniformFloat("alpha");
	protected final UniformBoolean flipTexture = new UniformBoolean("flipTexture");

	protected GuiShader() {
		super("gui", VERTEX_SHADER, FRAGMENT_SHADER);
		super.storeAllUniformLocations(transform, alpha, flipTexture);
	}
}