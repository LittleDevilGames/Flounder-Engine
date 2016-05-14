package flounder.lights;

/**
 * Attenuation is used in calculating the range of engine.flounder.lights.
 */
public class Attenuation {
	public float constant;
	public float linear;
	public float exponent;

	/**
	 * Creates a Attenuation object used in engine.flounder.lights. The calculation used is as follows:<br>
	 * {@code factor = constant + (linear * cameraDistance) + (exponent * (cameraDistance * cameraDistance))}
	 *
	 * @param constant The constant Attenuation value.
	 * @param linear The linear Attenuation value.
	 * @param exponent The exponent Attenuation value.
	 */
	public Attenuation(float constant, float linear, float exponent) {
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
	}

	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getLinear() {
		return linear;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public float getExponent() {
		return exponent;
	}

	public void setExponent(float exponent) {
		this.exponent = exponent;
	}
}