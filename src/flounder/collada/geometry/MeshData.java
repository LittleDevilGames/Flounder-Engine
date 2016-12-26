package flounder.collada.geometry;

public class MeshData {
	private float[] vertices;
	private float[] textureCoords;
	private float[] normals;
	private float[] tangents;
	private int[] indices;
	private int[] jointIds;
	private float[] vertexWeights;

	private float furthestPoint;

	public MeshData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices, int[] jointIds, float[] vertexWeights, float furthestPoint) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.tangents = tangents;
		this.indices = indices;
		this.jointIds = jointIds;
		this.vertexWeights = vertexWeights;

		this.furthestPoint = furthestPoint;
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

	public int[] getJointIds() {
		return jointIds;
	}

	public float[] getVertexWeights() {
		return vertexWeights;
	}

	public float getFurthestPoint() {
		return furthestPoint;
	}
}
