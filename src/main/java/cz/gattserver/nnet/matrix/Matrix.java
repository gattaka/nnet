package cz.gattserver.nnet.matrix;

import java.math.BigDecimal;

public class Matrix {

	private int rows = 0;
	private int cols = 0;
	private BigDecimal data[][];

	public static interface MapFunc {
		BigDecimal map(BigDecimal value);
	}

	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		// indexově jsou nejprve řádky, pak sloupce
		data = new BigDecimal[rows][cols];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++)
				data[r][c] = BigDecimal.ZERO;
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

	public BigDecimal[] toArray() {
		BigDecimal[] result = new BigDecimal[rows * cols];
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

	public BigDecimal get(int row, int col) {
		checkDimensions(row, col);
		return data[row][col];
	}

	public void set(int row, int col, BigDecimal value) {
		checkDimensions(row, col);
		data[row][col] = value;
	}

	public void set(int row, int col, double value) {
		set(row, col, new BigDecimal(value));
	}

	public Matrix add(Matrix m) {
		if (m.getRows() != rows)
			throw new IllegalArgumentException("A and B has different number of rows");
		if (m.getCols() != cols)
			throw new IllegalArgumentException("A and B has different number of rows");
		Matrix result = new Matrix(rows, cols);
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++) {
				BigDecimal value = data[r][c].add(m.get(r, c));
				result.set(r, c, value);
			}
		return result;
	}

	public Matrix subtract(Matrix m) {
		Matrix m2 = m.multiplyByScalar(-1);
		return this.add(m2);
	}

	public Matrix addScalar(double n) {
		Matrix result = new Matrix(rows, cols);
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++) {
				BigDecimal value = data[r][c].add(new BigDecimal(n));
				result.set(r, c, value);
			}
		return result;
	}

	public Matrix multiply(Matrix m) {
		if (m.getRows() != cols)
			throw new IllegalArgumentException("A.B requires A.cols = B.rows");
		Matrix result = new Matrix(rows, m.getCols());
		for (int r = 0; r < result.getRows(); r++) {
			for (int c = 0; c < result.getCols(); c++) {
				BigDecimal sum = BigDecimal.ZERO;
				for (int i = 0; i < cols; i++)
					sum = sum.add(data[r][i].multiply(m.get(i, c)));
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
			for (int c = 0; c < result.getCols(); c++) {
				BigDecimal value = data[r][c].multiply(m.get(r, c));
				result.set(r, c, value);
			}
		return result;
	}

	public Matrix multiplyByScalar(double n) {
		Matrix result = new Matrix(rows, cols);
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++) {
				BigDecimal value = data[r][c].multiply(new BigDecimal(n));
				result.set(r, c, value);
			}
		return result;
	}

	public Matrix map(MapFunc func) {
		Matrix result = new Matrix(rows, cols);
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < cols; c++) {
				BigDecimal value = func.map(data[r][c]);
				result.set(r, c, value);
			}
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
