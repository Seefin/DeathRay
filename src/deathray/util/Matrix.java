package deathray.util;

import java.util.Arrays;

public final class Matrix {
	private final ArithmeticPrimitive[][] matrix;
	
	
	public Matrix(int rows, int columns) {
		if (rows < 1) {
			throw new IllegalArgumentException("Cannot create matrix with <1 rows");
		}
		if (columns < 1) {
			throw new IllegalArgumentException("Cannot create matrix with <1 columns");
		}
		this.matrix = new ArithmeticPrimitive[rows][columns];
	}
	
	public Matrix(ArithmeticPrimitive[][] data) {
		this.matrix = deepCopy(data);
	}
	
	public int getRows() {
		return this.matrix.length;
	}
	
	public int getColumns() {
		return this.matrix[0].length;
	}
	
	public ArithmeticPrimitive getValue(int row, int column) {
		if( row > this.matrix.length || row < 0 ) {
			throw new IndexOutOfBoundsException(row+"");
		}
		if( column > this.matrix[0].length || column < 0 ) {
			throw new IndexOutOfBoundsException(""+column);
		}
		return this.matrix[row][column];
	}
	
	public Matrix setValue(ArithmeticPrimitive value, int row, int column) {
		if( row > this.matrix.length || row < 0 ) {
			throw new IndexOutOfBoundsException(row+"");
		}
		if( column > this.matrix[0].length || column < 0 ) {
			throw new IndexOutOfBoundsException(""+column);
		}
		ArithmeticPrimitive[][] m = deepCopy(this.matrix);
		m[row][column] = value;
		return new Matrix(m);
	}
	
	public Matrix scalarMult(ArithmeticPrimitive scalar){
		if (scalar == null) {
			throw new IllegalArgumentException("Cannot multiply by null");
		}
		ArithmeticPrimitive[][] m = deepCopy(this.matrix);
		for (int i = 0; i < m.length; i++) {
			ArithmeticPrimitive[] row = m[i];
			for (int j = 0; j < row.length; j++) {
				ArithmeticPrimitive element = row[j];
				m[i][j] = element.multiplyBy(scalar);
			}
		}
		return new Matrix(m);
	}
	
	public Matrix add(Matrix other){
		if ( other.getRows() != getRows() || other.getColumns() != getColumns() ) {
			throw new IllegalArgumentException("Cannot add two matricies of different orders together");
		}
		final ArithmeticPrimitive m[][] = new ArithmeticPrimitive[getRows()][getColumns()];
		for(int r = 0; r < getRows(); r++) {
			for(int c = 0; c < getColumns(); c++) {
				m[r][c] = getValue(r, c).add(other.getValue(r, c));
			}
		}
		return new Matrix(m);
	}
	
	public Matrix subtract(Matrix other){
		if ( other.getRows() != getRows() || other.getColumns() != getColumns() ) {
			throw new IllegalArgumentException("Cannot subtract two matricies of different orders");
		}
		final ArithmeticPrimitive[][] m = new ArithmeticPrimitive[getRows()][getColumns()];
		for(int r = 0; r < getRows(); r ++)
			for(int c = 0; c < getColumns(); c++)
				m[r][c] = getValue(r,c).subtract(other.getValue(r, c));
		return new Matrix(m);
	}
	
	public Matrix transpose(){
		ArithmeticPrimitive[][] m = new ArithmeticPrimitive[getColumns()][getRows()];
		for(int r = 0; r < this.matrix.length; r++)
			for(int c = 0; c < this.matrix[0].length; c++)
				m[c][r] = this.matrix[r][c];
		return new Matrix(m);
	}
	
	public Matrix multiply(Matrix other){
		if (getColumns() != other.getRows()) {
			throw new IllegalArgumentException("Cannot multiple matrix where cols != other.rows");
		}
		final ArithmeticPrimitive[][] m = new ArithmeticPrimitive[getRows()][other.getColumns()];
		
		for(int r = 0; r < m.length; r++) {
			for(int c = 0; c < m[0].length; c++) {
				ArithmeticPrimitive accumulator = null;
				for(int e = 0; e < other.getRows(); e++) {
					if( accumulator != null ) {
						accumulator = accumulator.add(getValue(r,e).multiplyBy(other.getValue(e, c)));
					} else {
						accumulator = getValue(r, e).multiplyBy(other.getValue(e, c));
					}
				}
				m[r][c] = accumulator;
			}
		}
		
		return new Matrix(m);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(this.matrix);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Matrix)) {
			return false;
		}
		Matrix other = (Matrix) obj;
		if( other.getRows() != getRows() || other.getColumns() != getColumns() ) {
			return false;
		}
		for(int r =0; r < getRows(); r++) {
			for(int c = 0; c < getColumns(); c++) {
				if( !getValue(r, c).equals(other.getValue(r, c))) {
					return false;
				}
			}
		}
		return true;
	}

	private static ArithmeticPrimitive[][] deepCopy(ArithmeticPrimitive[][] data){
		if( data == null ) {
			throw new IllegalArgumentException("Matrix#deepCopy: Argument cannot be null");
		}
		final int len = data[0].length;
		final ArithmeticPrimitive[][] copy = new ArithmeticPrimitive[data.length][];
		for(int i = 0; i < data.length; i++) {
			if( data[i] == null ) {
				throw new IllegalArgumentException("No rows of a matrix can be null");
			}
			if( data[i].length != len ) {
				throw new IllegalArgumentException("All rows of a matrix should have the same number of columns");
			}
			copy[i] = Arrays.copyOf(data[i], data[i].length);
		}
		return copy;
	}
}
