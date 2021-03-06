package flounder.post.filters;

import flounder.devices.*;
import flounder.post.*;
import flounder.resources.*;

public class FilterPixel extends PostFilter {
	private float pixelSize;

	public FilterPixel(float pixelSize) {
		super("filterPixel", new MyFile(PostFilter.POST_LOC, "pixelFragment.glsl"));
		this.pixelSize = pixelSize;
	}

	public FilterPixel() {
		super("filterPixel", new MyFile(PostFilter.POST_LOC, "pixelFragment.glsl"));
		this.pixelSize = 2.0f;
	}

	@Override
	public void storeValues() {
		shader.getUniformFloat("pixelSize").loadFloat(pixelSize);
		shader.getUniformVec2("displaySize").loadVec2(FlounderDisplay.getWidth(), FlounderDisplay.getHeight());
	}
}
