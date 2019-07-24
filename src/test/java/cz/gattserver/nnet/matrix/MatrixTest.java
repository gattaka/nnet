package cz.gattserver.nnet.matrix;

import static org.junit.Assert.*;

import java.math.BigDecimal;

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
		assertEquals(2, matrix.get(0, 0).intValue());
		assertEquals(1, matrix.get(0, 1).intValue());
		assertEquals(5, matrix.get(0, 2).intValue());
		assertEquals(6, matrix.get(1, 0).intValue());
		assertEquals(7, matrix.get(1, 1).intValue());
		assertEquals(11, matrix.get(1, 2).intValue());
	}

	@Test
	public void testTranspose() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = a.transpose();
		assertEquals(3, b.getRows());
		assertEquals(2, b.getCols());
		assertEquals(2, b.get(0, 0).intValue());
		assertEquals(6, b.get(0, 1).intValue());
		assertEquals(1, b.get(1, 0).intValue());
		assertEquals(7, b.get(1, 1).intValue());
		assertEquals(5, b.get(2, 0).intValue());
		assertEquals(11, b.get(2, 1).intValue());
	}

	@Test
	public void testAdd() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 5, 7, 3, 1, 1 });
		Matrix c = a.add(b);
		assertEquals(4, c.get(0, 0).intValue());
		assertEquals(6, c.get(0, 1).intValue());
		assertEquals(12, c.get(0, 2).intValue());
		assertEquals(9, c.get(1, 0).intValue());
		assertEquals(8, c.get(1, 1).intValue());
		assertEquals(12, c.get(1, 2).intValue());
	}

	@Test
	public void testSubtract() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 5, 7, 3, 1, 1 });
		Matrix c = a.subtract(b);
		assertEquals(0, c.get(0, 0).intValue());
		assertEquals(-4, c.get(0, 1).intValue());
		assertEquals(-2, c.get(0, 2).intValue());
		assertEquals(3, c.get(1, 0).intValue());
		assertEquals(6, c.get(1, 1).intValue());
		assertEquals(10, c.get(1, 2).intValue());
	}

	@Test
	public void testAddScalar() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix c = a.addScalar(5);
		assertEquals(7, c.get(0, 0).intValue());
		assertEquals(6, c.get(0, 1).intValue());
		assertEquals(10, c.get(0, 2).intValue());
		assertEquals(11, c.get(1, 0).intValue());
		assertEquals(12, c.get(1, 1).intValue());
		assertEquals(16, c.get(1, 2).intValue());
	}

	@Test
	public void testMultiplyByScalar() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix c = a.multiplyByScalar(5);
		assertEquals(10, c.get(0, 0).intValue());
		assertEquals(5, c.get(0, 1).intValue());
		assertEquals(25, c.get(0, 2).intValue());
		assertEquals(30, c.get(1, 0).intValue());
		assertEquals(35, c.get(1, 1).intValue());
		assertEquals(55, c.get(1, 2).intValue());
	}

	@Test
	public void testMap() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix c = a.map(x -> x.multiply(new BigDecimal(2)).add(BigDecimal.ONE));
		assertEquals(5, c.get(0, 0).intValue());
		assertEquals(3, c.get(0, 1).intValue());
		assertEquals(11, c.get(0, 2).intValue());
		assertEquals(13, c.get(1, 0).intValue());
		assertEquals(15, c.get(1, 1).intValue());
		assertEquals(23, c.get(1, 2).intValue());
	}

	@Test
	public void testMultiplyHadamard() {
		Matrix a = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = MatrixUtils.fromFlatArray(2, 3, new double[] { 2, 5, 7, 3, 1, 1 });
		Matrix c = a.multiplyHadamard(b);
		assertEquals(4, c.get(0, 0).intValue());
		assertEquals(5, c.get(0, 1).intValue());
		assertEquals(35, c.get(0, 2).intValue());
		assertEquals(18, c.get(1, 0).intValue());
		assertEquals(7, c.get(1, 1).intValue());
		assertEquals(11, c.get(1, 2).intValue());
	}

	@Test
	public void testMultiply() {
		Matrix a = MatrixUtils.fromFlatArray(3, 2, new double[] { 2, 1, 5, 6, 7, 11 });
		Matrix b = MatrixUtils.fromFlatArray(2, 1, new double[] { 5, 4 });
		Matrix c = a.multiply(b);
		assertEquals(3, c.getRows());
		assertEquals(1, c.getCols());
		assertEquals(14, c.get(0, 0).intValue());
		assertEquals(49, c.get(1, 0).intValue());
		assertEquals(79, c.get(2, 0).intValue());
	}
}
