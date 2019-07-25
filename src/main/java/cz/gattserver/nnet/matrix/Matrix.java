package cz.gattserver.nnet.matrix;

public class Matrix {

	private int rows = 0;
	private int cols = 0;
	private double data[][];

	public static interface MapFunc {
		double map(double value);
	}

	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		// indexově jsou nejprve řádky, pak sloupce
		data = new double[rows][cols];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				data[r][c] = 0;
	}

	private void checkDimensions(int row, int col) {
		if (row > rows - 1 || row < 0)
			throw new IllegalArgumentException("Invalid row number (0-" + (rows - 1) + ")");
		if (col > cols - 1 || col < 0)
			throw new IllegalArgumentException("Invalid col number (0-" + (cols - 1) + ")");
	}

	public void print() {
		String line = "";
		for (int r = 0; r < rows; r++) {
			line += r == 0 ? "┌\t" : (r == rows - 1 ? "└\t" : "│\t");
			for (int c = 0; c < cols; c++) {
				line += data[r][c] + "\t";
				if (c == cols - 1) {
					line += r == 0 ? "┐" : (r == rows - 1 ? "┘" : "│");
					System.out.println(line);
					line = "";
				}
			}
		}
	}

	public double[] toArray() {
		double result[] = new double[rows * cols];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				result[r * cols + c] = data[r][c];
		return result;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public double get(int row, int col) {
		checkDimensions(row, col);
		return data[row][col];
	}

	public void set(int row, int col, double value) {
		checkDimensions(row, col);
		data[row][col] = value;
	}

	public Matrix add(Matrix m) {
		if (m.getRows() != rows)
			throw new IllegalArgumentException("A and B has different number of rows");
		if (m.getCols() != cols)
			throw new IllegalArgumentException("A and B has different number of rows");
		Matrix result = new Matrix(rows, cols);
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				result.set(r, c, data[r][c] + m.get(r, c));
		return result;
	}

	public Matrix subtract(Matrix m) {
		Matrix m2 = m.multiplyByScalar(-1);
		return this.add(m2);
	}

	public Matrix addScalar(double n) {
		Matrix result = new Matrix(rows, cols);
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				result.set(r, c, data[r][c] + n);
		return result;
	}

	public Matrix multiply(Matrix m) {
		if (m.getRows() != cols)
			throw new IllegalArgumentException("A.B requires A.cols = B.rows");
		Matrix result = new Matrix(rows, m.getCols());
		for (int r = 0; r < result.getRows(); r++) {
			for (int c = 0; c < result.getCols(); c++) {
				double sum = 0;
				for (int i = 0; i < cols; i++)
					sum += data[r][i] * m.get(i, c);
				result.set(r, c, sum);
			}
		}
		return result;
	}

	public Matrix multiplyHadamard(Matrix m) {
		if (m.getRows() != rows)
			throw new IllegalArgumentException("A.B requires A.rows = B.rows");
		if (m.getCols() != cols)
			throw new IllegalArgumentException("A.B requires A.cols = B.cols");
		Matrix result = new Matrix(rows, cols);
		for (int r = 0; r < result.getRows(); r++)
			for (int c = 0; c < result.getCols(); c++)
				result.set(r, c, data[r][c] * m.get(r, c));
		return result;
	}

	public Matrix multiplyByScalar(double n) {
		Matrix result = new Matrix(rows, cols);
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				result.set(r, c, data[r][c] * n);
		return result;
	}

	public Matrix map(MapFunc func) {
		Matrix result = new Matrix(rows, cols);
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				result.set(r, c, func.map(data[r][c]));
		return result;
	}

	public Matrix transpose() {
		Matrix result = new Matrix(cols, rows);
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				result.set(c, r, data[r][c]);
		return result;
	}

}
