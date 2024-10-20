package deathray.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatrixTest {
	
	private static final TestDouble[][] firstMatrixdata = new TestDouble[][] {
		new TestDouble[] {new TestDouble(1), new TestDouble(5)},
		new TestDouble[] {new TestDouble(2), new TestDouble(3)},
		new TestDouble[] {new TestDouble(1), new TestDouble(7)}
	};

	private static final TestDouble[][] secondMatrixData = new TestDouble[][] {
		new TestDouble[] {new TestDouble(1), new TestDouble(2), new TestDouble(3), new TestDouble(7)},
		new TestDouble[] {new TestDouble(5), new TestDouble(2), new TestDouble(8), new TestDouble(1)}
	};
	
	private static final TestDouble[][] scaledMatrixdata = new TestDouble[][] {
		new TestDouble[] {new TestDouble(2), new TestDouble(10)},
		new TestDouble[] {new TestDouble(4), new TestDouble(6)},
		new TestDouble[] {new TestDouble(2), new TestDouble(14)}
	};
	
	private static final TestDouble scalar = new TestDouble(2d);
	
	@Test
	public void testMatrixEmpty() {
		// [ 1 0 ]
		// [ 0 1 ]
		Matrix t = new Matrix(2,2);
		assertEquals("Expected 2 rows",2, t.getRows());
		assertEquals("Expected 2 columns", 2,t.getColumns());
		try {
			Matrix u = new Matrix(-1,1);
			fail("Should throw illegal Argument Exception for rows < 1");
		} catch (Exception e) {
			assertEquals("Expected IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe error", "Cannot create matrix with <1 rows", e.getMessage());
		}
		try {
			Matrix u = new Matrix(1,-1);
			fail("Should throw illegal Argument Exception for rows < 1");
		} catch (Exception e) {
			assertEquals("Expected IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe error", "Cannot create matrix with <1 columns", e.getMessage());
		}
	}

	@Test
	public void testMatrixConstructor() {
		Matrix t = new Matrix(firstMatrixdata);
		assertEquals("Expected 3 rows", 3, t.getRows());
		assertEquals("Expected 2 columns", 2, t.getColumns());
		for(int r = 0; r < firstMatrixdata.length; r++) {
			for(int c = 0; c < firstMatrixdata[0].length; c++) {
				assertEquals(String.format("Value ad (%d,%d) should match", r, c), firstMatrixdata[r][c], t.getValue(r, c));
			}
		}
		try {
			TestDouble ragged[][] = new TestDouble[][] {
				new TestDouble[] {new TestDouble(1), new TestDouble(5)},
				new TestDouble[] {new TestDouble(1), new TestDouble(2), new TestDouble(3), new TestDouble(7)},
				new TestDouble[] {new TestDouble(2), new TestDouble(3)}
			};
			t = new Matrix(ragged);
			fail("Should throw illegal argument exception for ragged matrix");
		} catch(Exception e) {
			assertEquals("Expected IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe error", "All rows of a matrix should have the same number of columns", e.getMessage());
		}
		try {
			TestDouble sparse[][] = new TestDouble[][] {
				new TestDouble[] {new TestDouble(1), new TestDouble(5)},
				null,
				new TestDouble[] {new TestDouble(2), new TestDouble(3)}
			};
			t = new Matrix(sparse);
		} catch( Exception e) {
			assertEquals("Expected IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe error", "No rows of a matrix can be null", e.getMessage());
		}
	}

	@Test
	public void testGetRows() {
		Matrix t = new Matrix(firstMatrixdata);
		assertEquals("Number of rows should equal number of provided array", firstMatrixdata.length, t.getRows());
	}

	@Test
	public void testGetColumns() {
		Matrix t = new Matrix(firstMatrixdata);
		assertEquals("Number of columns should equal number of provided array", firstMatrixdata[0].length, t.getColumns());
	}

	@Test
	public void testGetValue() {
		Matrix t = new Matrix(firstMatrixdata);
		for(int r = 0; r < firstMatrixdata.length; r++) {
			for(int c = 0; c < firstMatrixdata[0].length; c++) {
				assertEquals(String.format("Value ad (%d,%d) should match", r, c), firstMatrixdata[r][c], t.getValue(r, c));
			}
		}
		try {
			t.getValue(firstMatrixdata.length + 1, 0);
			fail("Illegal row should throw exception");
		} catch(Exception e) {
			assertEquals("Expected IndexOutOfBoundsException", IndexOutOfBoundsException.class,e.getClass());
			assertEquals("Message should be OOB index", ""+(firstMatrixdata.length + 1), e.getMessage());
		}
		try {
			t.getValue(-1, 0);
			fail("Illegal row should throw exception");
		} catch(Exception e) {
			assertEquals("Expected IndexOutOfBoundsException", IndexOutOfBoundsException.class,e.getClass());
			assertEquals("Message should be OOB index", ""+-1, e.getMessage());
		}
		try {
			t.getValue(0, firstMatrixdata[0].length + 1);
			fail("Illegal row should throw exception");
		} catch(Exception e) {
			assertEquals("Expected IndexOutOfBoundsException", IndexOutOfBoundsException.class,e.getClass());
			assertEquals("Message should be OOB index", ""+(firstMatrixdata[0].length + 1), e.getMessage());
		}
		try {
			t.getValue(0, -1);
			fail("Illegal row should throw exception");
		} catch(Exception e) {
			assertEquals("Expected IndexOutOfBoundsException", IndexOutOfBoundsException.class,e.getClass());
			assertEquals("Message should be OOB index", ""+-1, e.getMessage());
		}
	}

	@Test
	public void testSetValue() {
		Matrix t = new Matrix(firstMatrixdata);
		Matrix u = t.setValue(TestDouble.ZERO, 0, 0);
		assertEquals("Setting value should return new matrix and not modify existing one", TestDouble.ONE, t.getValue(0, 0));
		assertEquals("New matrix should have specified element set to specified value", TestDouble.ZERO, u.getValue(0, 0));
		try {
			t.setValue(TestDouble.ZERO,firstMatrixdata.length + 1, 0);
			fail("Illegal row should throw exception");
		} catch(Exception e) {
			assertEquals("Expected IndexOutOfBoundsException", IndexOutOfBoundsException.class,e.getClass());
			assertEquals("Message should be OOB index", ""+(firstMatrixdata.length + 1), e.getMessage());
		}
		try {
			t.setValue(TestDouble.ZERO,-1, 0);
			fail("Illegal row should throw exception");
		} catch(Exception e) {
			assertEquals("Expected IndexOutOfBoundsException", IndexOutOfBoundsException.class,e.getClass());
			assertEquals("Message should be OOB index", ""+-1, e.getMessage());
		}
		try {
			t.setValue(TestDouble.ZERO,0, firstMatrixdata[0].length + 1);
			fail("Illegal row should throw exception");
		} catch(Exception e) {
			assertEquals("Expected IndexOutOfBoundsException", IndexOutOfBoundsException.class,e.getClass());
			assertEquals("Message should be OOB index", ""+(firstMatrixdata[0].length + 1), e.getMessage());
		}
		try {
			t.setValue(TestDouble.ZERO,0, -1);
			fail("Illegal row should throw exception");
		} catch(Exception e) {
			assertEquals("Expected IndexOutOfBoundsException", IndexOutOfBoundsException.class,e.getClass());
			assertEquals("Message should be OOB index", ""+-1, e.getMessage());
		}
	}

	@Test
	public void testScalarMult() {
		Matrix t = new Matrix(firstMatrixdata);
		Matrix u = t.scalarMult(scalar);
		assertEquals("Scaled Matrix has same #rows", t.getRows(),u.getRows());
		assertEquals("Scaled Matrix has same #cols", t.getColumns(),u.getColumns());
		assertEquals("Scaled matrix is 2x original", new Matrix(scaledMatrixdata), u);
	}

	@Test
	public void testAdd() {
		fail("Not yet implemented");
	}

	@Test
	public void testSubtract() {
		fail("Not yet implemented");
	}

	@Test
	public void testTranspose() {
		fail("Not yet implemented");
	}

	@Test
	public void testMultiply() {
		fail("Not yet implemented");
	}

	private static final class TestDouble extends ArithmeticPrimitive {
		public static final TestDouble ZERO = new TestDouble(0);
		public static final TestDouble ONE = new TestDouble(1);
		private static final long serialVersionUID = 6221159515567806724L;
		private final double _value;
		
		public TestDouble(double d) {
			this._value = d;
		}
		
		public TestDouble(int i) {
			this._value = Double.parseDouble(Integer.toString(i));
		}
		
		public double getDoubleValue() {
			return Double.valueOf(_value);
		}

		@Override
		public ArithmeticPrimitive add(ArithmeticPrimitive n) {
			TestDouble other = getTestDouble(n);
			return new TestDouble(getDoubleValue() + other.getDoubleValue());
		}

		@Override
		public ArithmeticPrimitive subtract(ArithmeticPrimitive n) {
			TestDouble other = getTestDouble(n);
			return new TestDouble(getDoubleValue() - other.getDoubleValue());
		}

		@Override
		public ArithmeticPrimitive multiplyBy(ArithmeticPrimitive n) {
			TestDouble other = getTestDouble(n);
			return new TestDouble(getDoubleValue() * other.getDoubleValue());
		}

		@Override
		public ArithmeticPrimitive divideBy(ArithmeticPrimitive n) {
			TestDouble other = getTestDouble(n);
			return new TestDouble(getDoubleValue()/other.getDoubleValue());
		}

		@Override
		public int compareTo(ArithmeticPrimitive o) {
			TestDouble other = getTestDouble(o);
			return Double.compare(getDoubleValue(), other.getDoubleValue());
		}
		
		@Override
		public boolean equals(Object obj) {
			if( obj == null ) {
				return false;
			}
			if( obj == this ) {
				return true;
			}
			if( !TestDouble.class.isInstance(obj) ) {
				return false;
			}
			return TestDouble.class.cast(obj).getDoubleValue() == getDoubleValue();
		}

		private TestDouble getTestDouble(ArithmeticPrimitive n) {
			Class<TestDouble> clazz = TestDouble.class;
			if (!clazz.isInstance(n)) {
				throw new IllegalArgumentException("Cannot operate on TestDouble and " + n.getClass().getName());
			}
			TestDouble other = clazz.cast(n);
			return other;
		}

		@Override
		public String toString() {
			return "TestDouble [_value=" + this._value + "]";
		}
	}
}
