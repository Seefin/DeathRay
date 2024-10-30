package deathray.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
		Matrix<TestDouble> t = new Matrix<>(2,2);
		assertEquals("Expected 2 rows",2, t.getRows());
		assertEquals("Expected 2 columns", 2,t.getColumns());
		try {
			new Matrix<>(-1,1);
			fail("Should throw illegal Argument Exception for rows < 1");
		} catch (Exception e) {
			assertEquals("Expected IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe error", "Cannot create matrix with <1 rows", e.getMessage());
		}
		try {
			new Matrix<>(1,-1);
			fail("Should throw illegal Argument Exception for rows < 1");
		} catch (Exception e) {
			assertEquals("Expected IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe error", "Cannot create matrix with <1 columns", e.getMessage());
		}
	}

	@Test
	public void testMatrixConstructor() {
		Matrix<TestDouble> t = new Matrix<>(firstMatrixdata);
		assertEquals("Expected 3 rows", 3, t.getRows());
		assertEquals("Expected 2 columns", 2, t.getColumns());
		for(int r = 0; r < firstMatrixdata.length; r++) {
			for(int c = 0; c < firstMatrixdata[0].length; c++) {
				assertEquals(String.format("Value ad (%d,%d) should match", r, c), firstMatrixdata[r][c], t.getValue(r, c));
			}
		}
		try {
			new Matrix<TestDouble>(new TestDouble[][] {null});
			fail("Shoul throw exception");
		} catch(Exception e) {
			assertEquals("Expected NullPointerException", NullPointerException.class,e.getClass());
		}
		try {
			TestDouble ragged[][] = new TestDouble[][] {
				new TestDouble[] {new TestDouble(1), new TestDouble(5)},
				new TestDouble[] {new TestDouble(1), new TestDouble(2), new TestDouble(3), new TestDouble(7)},
				new TestDouble[] {new TestDouble(2), new TestDouble(3)}
			};
			new Matrix<TestDouble>(ragged);
			fail("Should throw illegal argument exception for ragged matrix");
		} catch(Exception e) {
			assertEquals("Expected IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe error", "All rows of a matrix should have the same number of columns", e.getMessage());
		}
		try {
			TestDouble ragged[][] = new TestDouble[][] {
				new TestDouble[] {new TestDouble(1), new TestDouble(5)},
				new TestDouble[] {new TestDouble(1), new TestDouble(2), new TestDouble(3), new TestDouble(7)},
				new TestDouble[] {new TestDouble(2), new TestDouble(3)}
			};
			List<List<TestDouble>> r1 = new ArrayList<>();
			for(int r = 0; r < ragged.length; r++) {
				TestDouble[] row = ragged[r];
				for(int c = 0; c < row.length; c++) {
					if(r >= r1.size()) {
						r1.add(new ArrayList<>());
					}
					r1.get(r).add(ragged[r][c]);
				}
			}
			new Matrix<TestDouble>(r1);
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
			new Matrix<TestDouble>(sparse);
		} catch( Exception e) {
			assertEquals("Expected IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe error", "No rows of a matrix can be null", e.getMessage());
		}
	}

	@Test
	public void testGetRows() {
		Matrix<TestDouble> t = new Matrix<>(firstMatrixdata);
		assertEquals("Number of rows should equal number of provided array", firstMatrixdata.length, t.getRows());
	}

	@Test
	public void testGetColumns() {
		Matrix<TestDouble> t = new Matrix<>(firstMatrixdata);
		assertEquals("Number of columns should equal number of provided array", firstMatrixdata[0].length, t.getColumns());
	}

	@Test
	public void testGetValue() {
		Matrix<TestDouble> t = new Matrix<>(firstMatrixdata);
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
		Matrix<TestDouble> t = new Matrix<>(firstMatrixdata);
		Matrix<TestDouble> u = t.setValue(TestDouble.ZERO, 0, 0);
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
		Matrix<TestDouble> t = new Matrix<>(firstMatrixdata);
		try {
			t.scalarMult(null);
			fail("Expected exception for null scalar");
		} catch(Exception e) {
			assertEquals("Expected IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe problem", "Cannot multiply by null", e.getMessage());
		}
		Matrix<TestDouble> u = t.scalarMult(scalar);
		assertEquals("Scaled Matrix has same #rows", t.getRows(),u.getRows());
		assertEquals("Scaled Matrix has same #cols", t.getColumns(),u.getColumns());
		assertEquals("Scaled matrix is 2x original", new Matrix<TestDouble>(scaledMatrixdata), u);
		Matrix<TestDouble> v = t.setValue(null, 0, 0);
		Matrix<TestDouble> w = v.scalarMult(scalar);
		assertEquals("Scaled Matrix has same #rows", t.getRows(),w.getRows());
		assertEquals("Scaled Matrix has same #cols", t.getColumns(),w.getColumns());
		assertEquals("Scaled matrix is 2x original", new Matrix<TestDouble>(scaledMatrixdata).setValue(null, 0, 0), w);
	}

	@Test
	public void testAdd() {
		Matrix<TestDouble> t = new Matrix<>(firstMatrixdata);
		Matrix<TestDouble> u = t.add(new Matrix<>(firstMatrixdata));
		Matrix<TestDouble> expected = new Matrix<>(new TestDouble[][] {
			new TestDouble[] {new TestDouble(2), new TestDouble(10)},
			new TestDouble[] {new TestDouble(4), new TestDouble(6)},
			new TestDouble[] {new TestDouble(2), new TestDouble(14)}
		});
		assertEquals("Matrix retains #rows after add", t.getRows(), u.getRows());
		assertEquals("Matrix retains #cols after add", t.getColumns(), u.getColumns());
		assertEquals("Matrix addition is element-wise", expected, u);
		try {
			Matrix<TestDouble> m1 = new Matrix<>(firstMatrixdata);
			Matrix<TestDouble> m2 = new Matrix<>(secondMatrixData);
			m1.add(m2);
			fail("Should throw exception");
		} catch(Exception e) {
			assertEquals("Should be IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe issue","Cannot add two matricies of different orders together", e.getMessage());
		}
		try {
			Matrix<TestDouble> m1 = new Matrix<>(firstMatrixdata);
			Matrix<TestDouble> m2 = new Matrix<>(new TestDouble[][] {
				new TestDouble[] {new TestDouble(1), new TestDouble(2), new TestDouble(3), new TestDouble(7)},
				new TestDouble[] {new TestDouble(5), new TestDouble(2), new TestDouble(8), new TestDouble(1)},
				new TestDouble[] {new TestDouble(5), new TestDouble(2), new TestDouble(8), new TestDouble(1)}
			});
			m1.add(m2);
			fail("Should throw exception");
		} catch(Exception e) {
			assertEquals("Should be IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe issue","Cannot add two matricies of different orders together", e.getMessage());
		}
	}

	@Test
	public void testSubtract() {
		Matrix<TestDouble> t = new Matrix<>(firstMatrixdata);
		Matrix<TestDouble> u = t.subtract(new Matrix<>(firstMatrixdata));
		Matrix<TestDouble> expected = new Matrix<>(new TestDouble[][] {
			new TestDouble[] {new TestDouble(0), new TestDouble(0)},
			new TestDouble[] {new TestDouble(0), new TestDouble(0)},
			new TestDouble[] {new TestDouble(0), new TestDouble(0)}
		});
		assertEquals("Matrix retains #rows after add", t.getRows(), u.getRows());
		assertEquals("Matrix retains #cols after add", t.getColumns(), u.getColumns());
		assertEquals("Matrix addition is element-wise", expected, u);
		try {
			Matrix<TestDouble> m1 = new Matrix<>(firstMatrixdata);
			Matrix<TestDouble> m2 = new Matrix<>(secondMatrixData);
			m1.subtract(m2);
			fail("Should throw exception");
		} catch(Exception e) {
			assertEquals("Should be IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe issue","Cannot subtract two matricies of different orders", e.getMessage());
		}
		try {
			Matrix<TestDouble> m1 = new Matrix<>(firstMatrixdata);
			Matrix<TestDouble> m2 = new Matrix<>(new TestDouble[][] {
				new TestDouble[] {new TestDouble(1), new TestDouble(2), new TestDouble(3), new TestDouble(7)},
				new TestDouble[] {new TestDouble(5), new TestDouble(2), new TestDouble(8), new TestDouble(1)},
				new TestDouble[] {new TestDouble(5), new TestDouble(2), new TestDouble(8), new TestDouble(1)}
			});
			m1.subtract(m2);
			fail("Should throw exception");
		} catch(Exception e) {
			assertEquals("Should be IllegalArgumentException", IllegalArgumentException.class,e.getClass());
			assertEquals("Message should describe issue","Cannot subtract two matricies of different orders", e.getMessage());
		}
	}

	@Test
	public void testTranspose() {
		Matrix<TestDouble> t = new Matrix<>(secondMatrixData);
		Matrix<TestDouble> u = t.transpose();
		Matrix<TestDouble> expected = new Matrix<>(new TestDouble[][] {
			new TestDouble[] {new TestDouble(1), new TestDouble(5)},
			new TestDouble[] {new TestDouble(2), new TestDouble(2)},
			new TestDouble[] {new TestDouble(3), new TestDouble(8)},
			new TestDouble[] {new TestDouble(7), new TestDouble(1)}
		});
		assertEquals("Pre-transpose cols = post-transpose rows", t.getColumns(), u.getRows());
		assertEquals("Pre-transpose rows = post-transpose columns", t.getRows(), u.getColumns());
		assertEquals("Transposed Matrix has rows and columns sqapped", expected, u);
	}

	@Test
	public void testMultiply() {
		Matrix<TestDouble> m1 = new Matrix<>(new TestDouble[][] {
			new TestDouble[] {new TestDouble(1),new TestDouble(2),new TestDouble(3)},
			new TestDouble[] {new TestDouble(4),new TestDouble(5),new TestDouble(6)}
		});
		Matrix<TestDouble> m2 = new Matrix<>(new TestDouble[][] {
			new TestDouble[] {new TestDouble(7), new TestDouble(8)},
			new TestDouble[] {new TestDouble(9), new TestDouble(10)},
			new TestDouble[] {new TestDouble(11), new TestDouble(12)},
		});
		Matrix<TestDouble> t = m1.multiply(m2);
		Matrix<TestDouble> expected = new Matrix<>(new TestDouble[][] {
			new TestDouble[] {new TestDouble(58), new TestDouble(64)},
			new TestDouble[] {new TestDouble(139), new TestDouble(154)}
		});
		assertEquals("Should have similar row count", expected.getRows(), t.getRows());
		assertEquals("Should have similar columns", expected.getColumns(), t.getColumns());
		assertEquals("Result is dot product of elements", expected, t);
		try {
			Matrix<TestDouble> m3 = new Matrix<>(new TestDouble[][] {
				new TestDouble[] {new TestDouble(7), new TestDouble(8)},
				new TestDouble[] {new TestDouble(9), new TestDouble(10)}
			});
			m1.multiply(m3);
			fail("should throw exception");
		} catch(Exception e) {
			assertEquals("Should be IllegalArgumentException", IllegalArgumentException.class, e.getClass());
			assertEquals("Message should describe issue", "Cannot multiple matrix where cols != other.rows", e.getMessage());
		}
	}
	
	@Test
	public void testToString() {
		Matrix<TestDouble> m1 = new Matrix<>(firstMatrixdata);
		String s = m1.toString();
		assertNotNull("toString() should not return null", s);
		assertEquals("Should equal format", "Matrix\n"
				+ "0: [TestDouble [_value=1.0] TestDouble [_value=5.0] \n"
				+ "1: [TestDouble [_value=2.0] TestDouble [_value=3.0] \n"
				+ "2: [TestDouble [_value=1.0] TestDouble [_value=7.0] \n", s);
	}
	
	@Test 
	public void testHashCode() {
		Matrix<TestDouble> m1 = new Matrix<TestDouble>(firstMatrixdata);
		Matrix<TestDouble> m2 = new Matrix<TestDouble>(secondMatrixData);
		Matrix<TestDouble> m3 = new Matrix<TestDouble>(firstMatrixdata);
		assertNotEquals("HashCode is unique", m1.hashCode(), m2.hashCode());
		assertEquals("HashCode is constant", m1.hashCode(), m1.hashCode());
		assertEquals("HashCode is deteriministic", m1.hashCode(), m3.hashCode());
	}
	
	@Test
	public void testEquals() {
		Matrix<TestDouble> m1 = new Matrix<TestDouble>(firstMatrixdata);
		Matrix<TestDouble> m2 = new Matrix<TestDouble>(secondMatrixData);
		Matrix<TestDouble> m3 = new Matrix<TestDouble>(firstMatrixdata);
		assertTrue("Matrix equals self", m1.equals(m1));
		assertTrue("Matrix with equivalent contents are equal", m1.equals(m3));
		assertFalse("Matrix doesn't equal non-Matrix self", m1.equals(System.out));
		assertFalse("Matrix doesn't equal non-equal self", m1.equals(m2));
		
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
