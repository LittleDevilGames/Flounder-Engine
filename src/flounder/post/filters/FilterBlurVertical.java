package flounder.post.filters;

import flounder.devices.*;
import flounder.fbos.*;
import flounder.post.*;
import flounder.resources.*;
import flounder.shaders.*;

import static org.lwjgl.opengl.GL20.*;

public class FilterBlurVertical extends PostFilter {
	private int heightValue;
	private float scaleValue;
	private boolean fitToDisplay;
	private float sizeScalar;

	public FilterBlurVertical(float sizeScalar) {
		super(ShaderFactory.newBuilder().setName("filterBlurVertical").addType(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_LOCATION)).addType(
				new ShaderType(GL_FRAGMENT_SHADER, new MyFile(PostFilter.POST_LOC, "blurVerticalFragment.glsl"))
		).create(), FBO.newFBO(sizeScalar).create());
		this.fitToDisplay = true;
		this.sizeScalar = sizeScalar;
		init((int) (FlounderDisplay.getWidth() * sizeScalar));
	}

	public FilterBlurVertical(int widthValue, int heightValue) {
		super(ShaderFactory.newBuilder().setName("filterBlurVertical").addType(
				new ShaderType(GL_VERTEX_SHADER, VERTEX_LOCATION)).addType(
				new ShaderType(GL_FRAGMENT_SHADER, new MyFile(PostFilter.POST_LOC, "blurVerticalFragment.glsl"))
		).create(), FBO.newFBO(widthValue, heightValue).create());
		this.fitToDisplay = false;
		this.sizeScalar = 1.0f;
		init(heightValue);
	}

	private void init(int heightValue) {
		this.heightValue = heightValue;
		this.scaleValue = 2.0f;
	}

	public void setScale(float scale) {
		this.scaleValue = scale;
	}

	@Override
	public void storeValues() {
		if (fitToDisplay) {
			heightValue = (int) (FlounderDisplay.getHeight() * sizeScalar);
		}

		shader.getUniformFloat("height").loadFloat(heightValue);
		shader.getUniformFloat("scale").loadFloat(scaleValue);
	}
}
