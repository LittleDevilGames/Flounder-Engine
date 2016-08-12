package flounder.models;

import flounder.engine.*;
import flounder.maths.vectors.*;
import flounder.physics.*;
import flounder.resources.*;

/**
 * Class that represents a loaded model.
 */
public class Model {
	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private float[] tangents;
	private int[] indices;

	private MyFile file;
	private boolean loaded;

	private AABB aabb;

	private int vaoID;
	private int vaoLength;

	/**
	 * Creates a new OpenGL model object.
	 */
	protected Model() {
		this.loaded = false;
	}

	/**
	 * Manually loads values into a model.
	 *
	 * @param vertices The vertices to load.
	 * @param textureCoords The texture coords to load.
	 * @param normals The normals to load.
	 * @param tangents The tangents to load.
	 * @param indices The indices to load.
	 */
	public Model(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		loadData(vertices, textureCoords, normals, tangents, indices);
		FlounderEngine.getModels().loadModelToOpenGL(this, null);
		this.loaded = true;
	}

	/**
	 * Creates a new Model Builder.
	 *
	 * @param file The model file to be loaded.
	 *
	 * @return A new Model Builder.
	 */
	public static ModelBuilder newModel(MyFile file) {
		return new ModelBuilder(file);
	}

	/**
	 * Creates a new empty Model.
	 *
	 * @return A new empty Model.
	 */
	public static Model getEmptyModel() {
		return new Model();
	}

	protected void loadData(ModelData data) {
		data.createRaw(this);
		data.destroy();
	}

	protected void loadData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.tangents = tangents;
		this.indices = indices;

		this.loaded = true;

		this.aabb = createAABB();
	}

	private AABB createAABB() {
		float minX = 0, minY = 0, minZ = 0;
		float maxX = 0, maxY = 0, maxZ = 0;
		int tripleCount = 0;

		for (float position : vertices) {
			if (tripleCount == 0 && position < minX) {
				minX = position;
			} else if (tripleCount == 0 && position > maxX) {
				maxX = position;
			}

			if (tripleCount == 1 && position < minY) {
				minY = position;
			} else if (tripleCount == 1 && position > maxY) {
				maxY = position;
			}

			if (tripleCount == 2 && position < minZ) {
				minZ = position;
			} else if (tripleCount == 2 && position > maxZ) {
				maxZ = position;
			}

			if (tripleCount >= 2) {
				tripleCount = 0;
			} else {
				tripleCount++;
			}
		}

		return new AABB(new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ));
	}

	public AABB getAABB() {
		return aabb;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextures() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public float[] getTangents() {
		return tangents;
	}

	public int[] getIndices() {
		return indices;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVaoLength() {
		return vaoLength;
	}

	public void setVaoID(int vaoID) {
		this.vaoID = vaoID;
	}

	public void setVaoLength(int vaoLength) {
		this.vaoLength = vaoLength;
	}

	/**
	 * Gets texture file this was stored in.
	 *
	 * @return The texture file.
	 */
	public MyFile getFile() {
		return file;
	}

	/**
	 * Sets the file this texture was loaded from.
	 *
	 * @param file The file this texture was loaded from.
	 */
	public void setFile(MyFile file) {
		this.file = file;
	}

	/**
	 * Gets if the texture is loaded.
	 *
	 * @return If the texture is loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Deletes the model from OpenGL memory.
	 */
	public void delete() {
		loaded = false;
		FlounderEngine.getProcessors().sendGLRequest(new ModelDeleteRequest(vaoID));
	}
}
