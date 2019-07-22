package cz.gattserver.nnet.matrix;

public class MatrixUtils {

	private MatrixUtils() {
	}

	public static Matrix createIdentity(int dimension) {
		Matrix matrix = new Matrix(dimension, dimension);
		for (int i = 0; i < dimension; i++)
			matrix.set(i, i, 1);
		return matrix;
	}

	public static Matrix fromFlatArray(int rows, int cols, float[] array) {
		Matrix matrix = new Matrix(rows, cols);
		int i = 0;
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				matrix.set(r, c, array[i++]);
			}
		}
		return matrix;
	}

}
