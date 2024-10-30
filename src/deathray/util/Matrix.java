package deathray.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement the Matrix algebraic structure.
 * <p>
 * A Matrix is an ordered collection of rows and columns that has specific
 * operations and properties. A vector is a special case of a matrix, being
 * a one-dimensional matrix.
 * <p>
 * This implementation is backed by a 2D ArrayList, and implements some of the
 * basic operations: add, subtract, scalar multiplication, matrix multiplication,
 * and transposition.
 * <p>
 * This class is immutable; calling any method returns a copy of this matrix,
 * transformed. It does not modify the existing matrix.
 * 
 * @param <T> A type that extends {@link ArithmeticPrimitive} to support the low-level
 * operators we require
 * 
 * @author Connor F
 */
@SuppressWarnings("unchecked")
public final class Matrix<T extends ArithmeticPrimitive> {
	/**
	 * The internal matrix data. We store the values as a list of rows. A row is 
	 * itself a list, containing elements of type T.
	 */
	private final List<List<T>> matrix;
	/**
	 * The number of rows in this matrix
	 */
	private final int rows;
	/**
	 * The number of columns in this matrix
	 */
	private final int cols;
	
	/**
	 * Create an empty Matrix, with specified dimensions.
	 * <p>
	 * Creates an empty matrix, with the specified dimensions. As this class is immutable,
	 * this is not actually that useful a constructor to callers outside of the class itself,
	 * but I have left it public for the chance that it may become useful.
	 * 
	 * @param rows    Number of rows in this matrix
	 * @param columns Number of columns in this matrix
	 * @throws IllegalArgumentException If rows or columns is less than 1
	 */
	public Matrix(int rows, int columns) {
		if (rows < 1) {
			throw new IllegalArgumentException("Cannot create matrix with <1 rows");
		}
		if (columns < 1) {
			throw new IllegalArgumentException("Cannot create matrix with <1 columns");
		}
		this.matrix = new ArrayList<List<T>>();
		this.rows = rows;
		this.cols = columns;
	}
	
	/**
	 * Create a matrix with specified data.
	 * <p>
	 * Creates a new Matrix object containing the specified data. The provided data
	 * is turned into a list and fed into the {@link Matrix#Matrix(List)} constructor,
	 * so retrictions there apply here too.
	 * 
	 * @param data  Data to initalise the list with
	 * 
	 * @throws IllegalArgumentException  If the provided data array argument is null
	 */
	public Matrix(T[][] data) {
		this(data.length,data[0].length);
		this.matrix.clear();
		if( data == null || data[0] == null ) {
			throw new IllegalArgumentException("Cannot create Matrix of null data");
		}
		final int len = data[0].length;
		final List<List<T>> dataList = new ArrayList<List<T>>(data.length);
		for(int r = 0; r < data.length; r++) {
			for(int c = 0; c < data[0].length; c++) {
				if( data[r] == null ) {
					throw new IllegalArgumentException("No rows of a matrix can be null");
				}
				if( data[r].length != len ) {
					throw new IllegalArgumentException("All rows of a matrix should have the same number of columns");
				}
				if( dataList.size() <= r ) {
					dataList.add(r, new ArrayList<T>(data[r].length));
				}
				dataList.get(r).add(data[r][c]);
			}
		}
		this.matrix.addAll(dataList);
	}
	/**
	 * Create a matrix with specified data
	 * <p>
	 * Creates a new matrix with specified data. The data cannot be null (however it can 
	 * contain null), and all inner lists must be of the same size.
	 * 
	 * @param data  Data to populate the new matrix
	 * @throws NullPointerException  If the data parameter is null
	 * @throws IllegalArgumentException  If the columns have differing lengths
	 */
	public Matrix(List<List<T>> data) {
		this(data.size(), data.get(0).size());
		this.matrix.clear();
		final int colSize = getColumns();
		for(List<T> row: data) {
			if(row.size() != colSize) {
				throw new IllegalArgumentException("Can only handle Matricies with equal column lengths");
			}
		}
		this.matrix.addAll(data);
	}
	
	/**
	 * Get the number of rows in this matrix
	 * <p>
	 * The number of rows is the number of vertical elements that this matrix
	 * consists of. The value is one-based.
	 * 
	 * @return  Number of rows in this matrix
	 */
	public int getRows() {
		return this.rows;
	}
	
	/**
	 * Get the number of columns in this matrix
	 * <p>
	 * The number of columns is equal to the number of elements in each row.
	 * 
	 * @return  Number of columns in this matrix
	 */
	public int getColumns() {
		return this.cols;
	}
	
	/**
	 * Get the total number of elements in this matrix
	 * <p>
	 * The number of elements is equal to the number of columns multiplied
	 * by the number of rows. If this matrix contains an null element, it is
	 * considered an elementfor the purposes of this calculation.
	 * 
	 * @return  Number of elements in this matrix
	 */
	public int size() {
		return getColumns() * getRows();
	}
	
	/**
	 * Get the specified element.
	 * <p>
	 * Returns the element at the specified row and column in the matrix,
	 * if it exists.
	 * 
	 * @param row     The row of the element to retrieve
	 * @param column  The column of the element to retrieve
	 * 
	 * @return The specified element
	 * 
	 * @throws IndexOutOfBoundsException  If the specified row or column is not within the matrix
	 */
	public T getValue(int row, int column) {
		if( row > getRows() || row < 0 ) {
			throw new IndexOutOfBoundsException(row+"");
		}
		if( column > getColumns() || column < 0 ) {
			throw new IndexOutOfBoundsException(""+column);
		}
		return this.matrix.get(row).get(column);
	}
	
	/**
	 * Set the specified element to a new value
	 * <p>
	 * Returns a copy of this matrix, with the specified element set to the
	 * specified value.
	 * 
	 * @param value   Value of the specified element
	 * @param row     The row of the element to set
	 * @param column  The column of the element to set
	 * 
	 * @return A new Matrix, with identical data except for the specified element,
	 *         which is updated to the specified value
	 * 
	 * @throws IndexOutOfBoundsException  If the specifed row or column is
	 *         outside the bounds of this matrix
	 */
	public Matrix<T> setValue(T value, int row, int column) {
		if( row > getRows() || row < 0 ) {
			throw new IndexOutOfBoundsException(row+"");
		}
		if( column > getColumns() || column < 0 ) {
			throw new IndexOutOfBoundsException(""+column);
		}
		List<List<T>> copy = getMatrixData();
		copy.get(row).remove(column);
		copy.get(row).add(column,value);
		return new Matrix<T>(copy);
	}
	
	/**
	 * Perform scalar multiplication
	 * <p>
	 * Return a new matrix, where every element is equal to the multiplication
	 * of the corresponding element in this matrix by the specified value. This
	 * new matrix will have the same number of rows and columns as this matrix,
	 * and therefore the same number of elements
	 * 
	 * @param scalar  Value to multiply each element by
	 * 
	 * @return A new matrix, with all elements multiplied by the specified value
	 * 
	 * @throws IllegalArgumentException  If the specified scalar value is null
	 */
	public Matrix<T> scalarMult(T scalar){
		if (scalar == null) {
			throw new IllegalArgumentException("Cannot multiply by null");
		}
		List<List<T>> copy = getMatrixData();
		for(int r = 0; r < getRows(); r++) {
			for(int c = 0; c < getColumns(); c++) {
				T elem = copy.get(r).remove(c);
				if( elem != null ) {
					copy.get(r).add(c, (T) elem.multiplyBy(scalar));
				} else {
					copy.get(r).add(c, elem);
				}
			}
		}
		return new Matrix<T>(copy);
	}

	/**
	 * Add the specified matrix to this matrix
	 * <p>
	 * Return a new matrix, where the corresponding element of this matrix has
	 * been added to the corresponding element of the specified matrix.
	 * 
	 * @param other  The matrix to add to this one
	 * 
	 * @return A new matrix, where every element is the sum of the element in this
	 * matrix and the corresponding element in the specified matrix
	 * 
	 * @throws IllegalArgumentException If the specified matrix has different dimensions
	 * than this matrix
	 */
	public Matrix<T> add(Matrix<T> other){
		if ( other.getRows() != getRows() || other.getColumns() != getColumns() ) {
			throw new IllegalArgumentException("Cannot add two matricies of different orders together");
		}
		List<List<T>> copy = getMatrixData();
		for(int r = 0; r < getRows(); r++) {
			for(int c = 0; c < getColumns(); c++) {
				T elem = copy.get(r).remove(c);
				copy.get(r).add(c, (T) elem.add(other.getValue(r, c)));
			}
		}
		return new Matrix<T>(copy);
	}
	
	/**
	 * Subtract the specified matrix from this matrix
	 * <p>
	 * Return a new matrix, where the corresponding element of the specified 
	 * matrix has been subtracted from the corresponding element of this matrix.
	 * 
	 * @param other  The matrix to subtract from to this one
	 * 
	 * @return A new matrix, where every element is the difference of the element in this
	 * matrix and the corresponding element in the specified matrix
	 * 
	 * @throws IllegalArgumentException If the specified matrix has different dimensions
	 * than this matrix
	 */
	public Matrix<T> subtract(Matrix<T> other){
		if ( other.getRows() != getRows() || other.getColumns() != getColumns() ) {
			throw new IllegalArgumentException("Cannot subtract two matricies of different orders");
		}
		final List<List<T>> copy = getMatrixData();
		for(int r = 0; r < getRows(); r++) {
			for(int c = 0; c < getColumns(); c++) {
				T elem = copy.get(r).remove(c);
				copy.get(r).add(c, (T) elem.subtract(other.getValue(r, c)));
			}
		}
		return new Matrix<T>(copy);
	}
	
	/**
	 * Transpose this matrix
	 * <p>
	 * Return a new matrix which is the transposition of this matrix. The transposition of
	 * a matrix is the image of the matrix about an imaginary diagonal - i.e. the rows and 
	 * columns are swapped.
	 * 
	 * @return A new matrix that is the transposition of this matrix
	 */
	public Matrix<T> transpose(){
		List<List<T>> copy = new ArrayList<>();
		for(int i = 0; i < getColumns(); i++) {
			List<T> column = new ArrayList<T>();
			for(List<T> row : getMatrixData() ) {
				column.add(row.get(i));
			}
			copy.add(column);
		}
		return new Matrix<T>(copy);
	}
	
	/**
	 * Multiply this matrix by the specified matrix
	 * <p>
	 * Return a new matrix, which is the result of performing matrix multiplication
	 * on this matrix and the specified matrix. Matrix multiplication consolodates
	 * to matricies, into a new matrix where every element is the dot product of
	 * the corresponding row and column in the original matricies.
	 * 
	 * @param other  matrix to multiply this matrix by
	 * 
	 * @return A new matrix, where every element is the dot product of
	 * the corresponding row and column in the original matricies.
	 * 
	 * @throws IllegalArgumentException If the other matrix has a different
	 * number of rows than this matrix has columns
	 */
	public Matrix<T> multiply(Matrix<T> other){
		if (getColumns() != other.getRows()) {
			throw new IllegalArgumentException("Cannot multiple matrix where cols != other.rows");
		}
		List<List<T>> copy = new ArrayList<>();
		for(int r = 0; r < getRows(); r++) {
			for(int c = 0; c < other.getColumns(); c++) {
				if(r >= copy.size()) {
					copy.add(new ArrayList<T>());
				}
				copy.get(r).add(dotProduct(getRowVector(r), other.getColumnVector(c)));
			}
		}
		return new Matrix<T>(copy);
	}

	/**
	 * Required for Hash*
	 * <p>
	 * Required to ensure that we get a unique hash code for every matrix, for use
	 * in Hash based collections
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.matrix.hashCode();
		return result;
	}

	/**
	 * Returns true if the specified matrix is equal to this one.
	 * <p>
	 * A matrix is equal to this one if and only if it has the same number of rows,
	 * columns, and elements, and each element is in the same position.
	 * 
	 * @return true if the specified matrix is equal to this one
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Matrix)) {
			return false;
		}
		Matrix<?> other = (Matrix<?>) obj;
		if( other.getRows() != getRows() || other.getColumns() != getColumns() || size() != other.size() ) {
			return false;
		}
		return this.matrix.equals(other.matrix);
	}

	/**
	 * Get a row vector for the specified row
	 * <p>
	 * A row vector is a list of T that is each element in the specified
	 * row.
	 * 
	 * @param row  Row to get the row vector for
	 * 
	 * @return List of elements in the specified row
	 * 
	 * @throws IllegalArgumentException If the specifed row lies outside the
	 * bounds of this matrix
	 */
	private List<T> getRowVector(int row){
		if(row < 0 || row > getRows()) {
			throw new IllegalArgumentException("Cannot make row vector for row " + row);
		}
		return new ArrayList<T>(this.matrix.get(row));
	}
	
	/**
	 * Get a column vector for the specified column
	 * <p>
	 * A column vector is a list of each element in the specified column
	 * 
	 * @param column  Column to get the column vector for
	 * 
	 * @return  List of elements in the specified column
	 * 
	 * @throws IllegalArgumentException If thespecified column lies outside the 
	 * bounds of this matrix
	 */
	private List<T> getColumnVector(int column){
		if(column < 0 || column > getColumns()) {
			throw new IllegalArgumentException("Cannot make column vector for column " + column);
		}
		List<T> copy = new ArrayList<>(getRows());
		for(List<T> row : getMatrixData()) {
			copy.add(row.get(column));
		}
		return copy;
	}
	
	/**
	 * Get dot product of two vectors
	 * <p>
	 * Helper method for the {@link #multiply(Matrix)} method, mostly. Takes two
	 * one-dimensional lists and produces thedot product of these lists. It is assumed
	 * that these are row and column vectors.
	 * 
	 * @param u1  The vector to get the dot product of
	 * @param u2  The vector to get the dot product of
	 * 
	 * @return The dot product of the specified vectors
	 * 
	 * @throws IllegalArgumentException If the supplied vectors differ in length
	 */
	private T dotProduct(List<T> u1, List<T> u2) {
		if( u1.size() != u2.size() ) {
			throw new IllegalArgumentException("Cannot produce dot product of row/column vectors of differing sizes");
		}
		T sum = null;
		for(int i = 0; i < u1.size(); i++) {
			if( sum == null ) {
				sum = (T) u1.get(i).multiplyBy(u2.get(i));
			} else {
				sum = (T) sum.add((T) u1.get(i).multiplyBy(u2.get(i)));
			}
		}
		return sum;
	}

	/**
	 * Get a copy of the elements
	 * <p>
	 * Returns a copy of the elements that are in this matrix.
	 * 
	 * @return A copy of the data in this matrix
	 */
	private List<List<T>> getMatrixData() {
		List<List<T>> copy = new ArrayList<>(this.matrix.size());
		for(List<T> row : this.matrix) {
			copy.add(new ArrayList<>(row));
		}
		return copy;
	}
}
