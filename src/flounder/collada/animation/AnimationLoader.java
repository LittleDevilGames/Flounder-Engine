package flounder.collada.animation;

import flounder.maths.matrices.*;
import flounder.parsing.xml.*;
import org.lwjgl.*;

import java.nio.*;
import java.util.*;

public class AnimationLoader {
	private XmlNode animationData;

	public AnimationLoader(XmlNode animationData) {
		this.animationData = animationData;
	}

	public AnimationData extractAnimation() {
		float[] times = getKeyTimes();
		float duration = times[times.length - 1];
		AnimationKeyFrameData[] keyFrames = initKeyFrames(times);
		List<XmlNode> animationNodes = animationData.getChildren("animation");

		for (XmlNode jointNode : animationNodes) {
			loadJointTransforms(keyFrames, jointNode);
		}

		return new AnimationData(duration, keyFrames);
	}

	private float[] getKeyTimes() {
		XmlNode timeData = animationData.getChild("animation").getChild("source").getChild("float_array");
		String[] rawTimes = timeData.getData().split(" ");
		float[] times = new float[rawTimes.length];

		for (int i = 0; i < times.length; i++) {
			times[i] = Float.parseFloat(rawTimes[i]);
		}

		return times;
	}

	private AnimationKeyFrameData[] initKeyFrames(float[] times) {
		AnimationKeyFrameData[] frames = new AnimationKeyFrameData[times.length];

		for (int i = 0; i < frames.length; i++) {
			frames[i] = new AnimationKeyFrameData(times[i]);
		}

		return frames;
	}

	private void loadJointTransforms(AnimationKeyFrameData[] frames, XmlNode jointData) {
		String jointNameId = getJointName(jointData);
		String dataId = getDataId(jointData);
		XmlNode transformData = jointData.getChildWithAttribute("source", "id", dataId);
		String[] rawData = transformData.getChild("float_array").getData().split(" ");
		processTransforms(jointNameId, rawData, frames);
	}

	private String getDataId(XmlNode jointData) {
		XmlNode node = jointData.getChild("sampler").getChildWithAttribute("input", "semantic", "OUTPUT");
		return node.getAttribute("source").substring(1);
	}

	private String getJointName(XmlNode jointData) {
		XmlNode channelNode = jointData.getChild("channel");
		String data = channelNode.getAttribute("target");
		return data.split("/")[0];
	}

	private void processTransforms(String jointName, String[] rawData, AnimationKeyFrameData[] keyFrames) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		float[] matrixData = new float[16];

		for (int i = 0; i < keyFrames.length; i++) {
			for (int j = 0; j < 16; j++) {
				matrixData[j] = Float.parseFloat(rawData[i * 16 + j]);
			}

			buffer.clear();
			buffer.put(matrixData);
			buffer.flip();
			Matrix4f transform = new Matrix4f();
			transform.load(buffer);
			transform.transpose();
			keyFrames[i].addJointTransform(new JointTransformData(jointName, transform));
		}
	}
}
