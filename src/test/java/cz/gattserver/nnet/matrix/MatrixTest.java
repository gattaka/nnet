package cz.gattserver.nnet.matrix;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTest {

	@Test
	public void testInit1() {
		Matrix matrix = new Matrix(2, 3);
		assertEquals(2, matrix.getRows());
		assertEquals(3, matrix.getCols());
	}

	@Test
	public void testInit2() {
		Matrix matrix = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		assertEquals(2, (int) matrix.get(0, 0));
		assertEquals(1, (int) matrix.get(0, 1));
		assertEquals(5, (int) matrix.get(0, 2));
		assertEquals(6, (int) matrix.get(1, 0));
		assertEquals(7, (int) matrix.get(1, 1));
		assertEquals(11, (int) matrix.get(1, 2));
	}
	

	@Test
	public void testTranspose() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = a.transpose();
		assertEquals(3, b.getRows());
		assertEquals(2, b.getCols());
		assertEquals(2, (int) b.get(0, 0));
		assertEquals(6, (int) b.get(0, 1));
		assertEquals(1, (int) b.get(1, 0));
		assertEquals(7, (int) b.get(1, 1));
		assertEquals(5, (int) b.get(2, 0));
		assertEquals(11, (int) b.get(2, 1));
	}

	@Test
	public void testAdd() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 5, 7, 3, 1, 1 });
		Matrix c = a.add(b);
		assertEquals(4, (int) c.get(0, 0));
		assertEquals(6, (int) c.get(0, 1));
		assertEquals(12, (int) c.get(0, 2));
		assertEquals(9, (int) c.get(1, 0));
		assertEquals(8, (int) c.get(1, 1));
		assertEquals(12, (int) c.get(1, 2));
	}

	@Test
	public void testSubtract() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 5, 7, 3, 1, 1 });
		Matrix c = a.subtract(b);
		assertEquals(0, (int) c.get(0, 0));
		assertEquals(-4, (int) c.get(0, 1));
		assertEquals(-2, (int) c.get(0, 2));
		assertEquals(3, (int) c.get(1, 0));
		assertEquals(6, (int) c.get(1, 1));
		assertEquals(10, (int) c.get(1, 2));
	}

	@Test
	public void testAddScalar() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix c = a.addScalar(5);
		assertEquals(7, (int) c.get(0, 0));
		assertEquals(6, (int) c.get(0, 1));
		assertEquals(10, (int) c.get(0, 2));
		assertEquals(11, (int) c.get(1, 0));
		assertEquals(12, (int) c.get(1, 1));
		assertEquals(16, (int) c.get(1, 2));
	}

	@Test
	public void testMultiplyByScalar() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix c = a.multiplyByScalar(5);
		assertEquals(10, (int) c.get(0, 0));
		assertEquals(5, (int) c.get(0, 1));
		assertEquals(25, (int) c.get(0, 2));
		assertEquals(30, (int) c.get(1, 0));
		assertEquals(35, (int) c.get(1, 1));
		assertEquals(55, (int) c.get(1, 2));
	}

	@Test
	public void testMap() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix c = a.map(x -> x * 2 + 1);
		assertEquals(5, (int) c.get(0, 0));
		assertEquals(3, (int) c.get(0, 1));
		assertEquals(11, (int) c.get(0, 2));
		assertEquals(13, (int) c.get(1, 0));
		assertEquals(15, (int) c.get(1, 1));
		assertEquals(23, (int) c.get(1, 2));
	}

	@Test
	public void testMultiplyHadamard() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 5, 7, 3, 1, 1 });
		Matrix c = a.multiplyHadamard(b);
		assertEquals(4, (int) c.get(0, 0));
		assertEquals(5, (int) c.get(0, 1));
		assertEquals(35, (int) c.get(0, 2));
		assertEquals(18, (int) c.get(1, 0));
		assertEquals(7, (int) c.get(1, 1));
		assertEquals(11, (int) c.get(1, 2));
	}

	@Test
	public void testMultiply() {
		Matrix a = MatrixUtils.fromFlatArray(3, 2, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = MatrixUtils.fromFlatArray(2, 1, new double[] { 5, 4 });
		Matrix c = a.multiply(b);
		assertEquals(3, c.getRows());
		assertEquals(1, c.getCols());
		assertEquals(14, (int) c.get(0, 0));
		assertEquals(49, (int) c.get(1, 0));
		assertEquals(79, (int) c.get(2, 0));
	}
}
