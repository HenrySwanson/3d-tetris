package geom;

import java.util.Arrays;

/**
 * Represents a matrix containing floats.
 * 
 * @author Henry Swanson
 * @version 1.1 May 24, 2012
 */
public class Matrix {
	
	/** The number of rows in the matrix */
	private int numRows;
	/** The number of columns in the matrix */
	private int numColumns;
	/** The elements in the matrix */
	private float[][] elements;
	
	/**
	 * Creates the zero matrix of the specified size.
	 * 
	 * @param numRows The number of rows in the matrix
	 * @param numColumns The number of columns in the matrix
	 */
	public Matrix(int rows, int columns) {
		if(rows <= 0 || columns <= 0)
			throw new IllegalArgumentException("Non-positive matrix size");
		
		numRows = rows;
		numColumns = columns;
		elements = new float[rows][columns];
	}
	
	/**
	 * Creates a matrix from the specified array of elements.
	 * 
	 * @param data The elements to be put in the array
	 */
	public Matrix(float[][] data) {
		numRows = data.length;
		if(numRows == 0)
			throw new IllegalArgumentException("Zero length array");
		
		numColumns = data[0].length;
		for(int i = 0; i < numRows; i++)
			if(data[i].length != numColumns)
				throw new IllegalArgumentException("Non-rectangular array");
		
		elements = new float[numRows][numColumns];
		for(int i = 0; i < numRows; i++)
			for(int j = 0; j < numColumns; j++)
				elements[i][j] = (float) data[i][j];
	}
	
	/**
	 * Constructs a matrix of the given size, filled with the given data, in
	 * row-major order.
	 * 
	 * @param rows The number of rows in the matrix
	 * @param cols The number of columns in the matrix
	 * @param data The elements in the matrix, in row-major order
	 */
	public Matrix(int rows, int cols, float... data) {
		this(rows, cols);
		if(rows * cols != data.length)
			throw new IllegalArgumentException("float array of improper size");
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++)
				elements[i][j] = (float) data[i * cols + j];
	}
	
	/**
	 * Creates a copy of the specified matrix.
	 * 
	 * @param m The matrix to copy
	 */
	public Matrix(Matrix m) {
		numRows = m.numRows;
		numColumns = m.numColumns;
		elements = new float[numRows][numColumns];
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				elements[i][j] = m.elements[i][j];
			}
		}
	}
	
	/**
	 * Returns the identity matrix of the given rank.
	 * 
	 * @param rank The rank of the returned matrix
	 * @return The identity matrix of the given rank
	 */
	public static Matrix getIdentity(int rank) {
		Matrix m = new Matrix(rank, rank);
		for(int i = 0; i < rank; i++)
			m.elements[i][i] = 1;
		return m;
	}
	
	/**
	 * Returns the matrix that translates a point by the specified values.
	 * 
	 * @param deltas The amounts by which all points should be translated
	 * @return The matrix that translates a point by the specified values
	 */
	public static Matrix getTranslation(float... deltas) {
		Matrix m = Matrix.getIdentity(deltas.length + 1);
		for(int i = 0; i < deltas.length; i++)
			m.elements[i][deltas.length] = (float) deltas[i];
		return m;
	}
	
	/**
	 * Returns a rotation of the given angle around the x axis.
	 * 
	 * @param rad The angle by which to turn
	 * @return A rotation matrix around the x axis
	 */
	public static Matrix getXRotation(float rad) {
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		return new Matrix(4, 4, 1, 0, 0, 0, 0, cos, -sin, 0, 0, sin, cos, 0, 0,
				0, 0, 1);
	}
	
	/**
	 * Returns a rotation of the given angle around the y axis.
	 * 
	 * @param rad The angle by which to turn
	 * @return A rotation matrix around the y axis
	 */
	public static Matrix getYRotation(float rad) {
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		return new Matrix(4, 4, cos, 0, sin, 0, 0, 1, 0, 0, -sin, 0, cos, 0, 0,
				0, 0, 1);
	}
	
	/**
	 * Returns a rotation of the given angle around the z axis.
	 * 
	 * @param rad The angle by which to turn
	 * @return A rotation matrix around the z axis
	 */
	public static Matrix getZRotation(float rad) {
		float cos = (float) Math.cos(rad);
		float sin = (float) Math.sin(rad);
		return new Matrix(4, 4, cos, -sin, 0, 0, sin, cos, 0, 0, 0, 0, 1, 0, 0,
				0, 0, 1);
	}
	
	/**
	 * Returns the number of numRows.
	 * 
	 * @return The number of numRows
	 */
	public int getRows() {
		return numRows;
	}
	
	/**
	 * Returns the number of numColumns.
	 * 
	 * @return The number of numColumns
	 */
	public int getColumns() {
		return numColumns;
	}
	
	/**
	 * Returns the element at the specified position.
	 * 
	 * @param i The row index
	 * @param j The column index
	 * @return The element at i, j
	 */
	public float get(int i, int j) {
		return elements[i][j];
	}
	
	/**
	 * Sets the element at the specified position to a specified value.
	 * 
	 * @param i The row index
	 * @param j The column index
	 * @param d The new value
	 */
	public void set(int i, int j, float d) {
		elements[i][j] = d;
	}
	
	/**
	 * Returns the specified row vector of this matrix.
	 * 
	 * @param row The row index
	 * @return The specified row vector of this matrix
	 */
	public Matrix getRow(int row) {
		Matrix m = new Matrix(1, numColumns);
		for(int i = 0; i < numColumns; i++)
			m.elements[0][i] = elements[row][i];
		return m;
	}
	
	/**
	 * Returns the specified column vector of this matrix.
	 * 
	 * @param column The column index
	 * @return The specified column vector of this matrix
	 */
	public Matrix getColumn(int column) {
		Matrix m = new Matrix(numRows, 1);
		for(int i = 0; i < numRows; i++)
			m.elements[i][0] = elements[i][column];
		return m;
	}
	
	/**
	 * Returns a submatrix consisting of the given rows and columns.
	 * 
	 * @param rows The indices of the rows to include
	 * @param cols The indices of the cols to include
	 * @return A submatrix consisting of the given rows and columns
	 */
	public Matrix submatrix(int[] rows, int[] cols) {
		Matrix m = new Matrix(rows.length, cols.length);
		for(int i = 0; i < rows.length; i++)
			for(int j = 0; j < cols.length; j++)
				m.elements[i][j] = elements[rows[i]][cols[j]];
		return m;
	}
	
	// TODO rref, row and column operations
	
	/**
	 * Returns the sum of this and the specified matrix.
	 * 
	 * @param m The matrix which will be added to this
	 * @return The sum of this and the specified matrix
	 */
	public Matrix add(Matrix m) {
		if(numRows != m.numRows || numColumns != m.numColumns)
			throw new IllegalArgumentException("Incompatible dimensions");
		
		Matrix sum = new Matrix(numRows, numColumns);
		for(int i = 0; i < numRows; i++)
			for(int j = 0; j < numColumns; j++)
				sum.elements[i][j] = elements[i][j] + m.elements[i][j];
		
		return sum;
	}
	
	/**
	 * Returns the difference of this and the specified matrix.
	 * 
	 * @param m The matrix which will be subtracted from this
	 * @return The difference of this and the specified matrix
	 */
	public Matrix subtract(Matrix m) {
		if(numRows != m.numRows || numColumns != m.numColumns)
			throw new IllegalArgumentException("Incompatible dimensions");
		
		Matrix diff = new Matrix(numRows, numColumns);
		for(int i = 0; i < numRows; i++)
			for(int j = 0; j < numColumns; j++)
				diff.elements[i][j] = elements[i][j] - m.elements[i][j];
		
		return diff;
	}
	
	/**
	 * Returns the product of this and the specified matrix.
	 * 
	 * @param m The matrix which will be multiplied by this
	 * @return The product of this and the specified matrix
	 */
	public Matrix multiply(Matrix m) {
		if(numColumns != m.numRows)
			throw new IllegalArgumentException("Incompatible dimensions: " +
					numRows + " x " + numColumns + " * " + m.numRows + " x " +
					m.numColumns);
		
		Matrix prod = new Matrix(numRows, m.numColumns);
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < m.numColumns; j++) {
				float sum = 0;
				for(int k = 0; k < numColumns; k++) {
					sum += elements[i][k] * m.elements[k][j];
				}
				prod.elements[i][j] = sum;
			}
		}
		
		return prod;
	}
	
	/**
	 * Returns the scalar product of this matrix and the specified value.
	 * 
	 * @param d The number which this will be multiplied by
	 * @return The scalar product of this matrix and the specified value
	 */
	public Matrix multiply(float d) {
		Matrix m = new Matrix(numRows, numColumns);
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				m.elements[i][j] = elements[i][j] * d;
			}
		}
		return m;
	}
	
	public Point multiply(Point p) {
		if(numColumns != p.getDimension())
			throw new IllegalArgumentException(
					"Point is of incorrect dimension: " + p.getDimension());
		Point image = new Point(numRows);
		for(int i = 0; i < numRows; i++) {
			float sum = 0;
			for(int j = 0; j < numColumns; j++)
				sum += elements[i][j] * p.get(j);
			image.setComponent(sum, i);
		}
		return image;
	}
	
	/**
	 * Returns the transpose of this matrix.
	 * 
	 * @return The transpose of this matrix
	 */
	public Matrix transpose() {
		Matrix m = new Matrix(numColumns, numRows);
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				m.elements[j][i] = elements[i][j];
			}
		}
		return m;
	}
	
	/**
	 * Returns true if this matrix is square.
	 * 
	 * @return True if this matrix is square
	 */
	public boolean isSquare() {
		return numRows == numColumns;
	}
	
	/**
	 * Returns the determinant of this matrix.
	 * 
	 * @return The determinant of this matrix
	 */
	public float getDeterminant() {
		if(!isSquare())
			throw new IllegalArgumentException("Non-square matrix");
		if(numRows == 1) return elements[0][0];
		if(numRows == 2) {
			return elements[0][0] * elements[1][1] - elements[0][1] *
					elements[1][0];
		}
		
		float sum = 0;
		for(int i = 0; i < numRows; i++) {
			Matrix sub = getExcludedMatrix(i, 0);
			float term = elements[i][0] * sub.getDeterminant();
			sum += term * ((i & 1) == 0 ? 1 : -1);
		}
		
		return sum; // TODO improve this and get inverse
	}
	
	/**
	 * Returns true if this matrix is invertible.
	 * 
	 * @return True if this matrix is invertible
	 */
	public boolean isInvertable() {
		if(!isSquare()) return false;
		return getDeterminant() != 0;
	}
	
	/**
	 * Returns the inverse of this matrix.
	 * 
	 * @return The inverse of this matrix
	 */
	public Matrix inverse() {
		if(!isInvertable())
			throw new IllegalArgumentException("Cannot invert matrix");
		
		Matrix inv = new Matrix(numRows, numColumns);
		if(numRows == 1) {
			inv.set(0, 0, 1 / elements[0][0]);
			return inv;
		}
		
		float det = getDeterminant();
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				float sign = (i + j) % 2 == 0 ? 1 : -1;
				float minor = getExcludedMatrix(i, j).getDeterminant();
				inv.elements[j][i] = sign * minor / det;
			}
		}
		return inv;
	}
	
	/**
	 * Returns a matrix that has the same elements as this matrix, but after
	 * removing the specified row and column.
	 * 
	 * @param row The row index
	 * @param column The column index
	 * @return A matrix that has the same elements as this matrix, but after
	 *         removing the specified row and column
	 */
	private Matrix getExcludedMatrix(int row, int column) {
		Matrix m = new Matrix(numRows - 1, numColumns - 1);
		for(int i = 0; i < numRows - 1; i++) {
			for(int j = 0; j < numColumns - 1; j++) {
				m.elements[i][j] = elements[i < row ? i : i + 1][j < column ? j
						: j + 1];
			}
		}
		return m;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numRows;
		result = prime * result + numColumns;
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Matrix other = (Matrix) obj;
		if(numRows != other.numRows) return false;
		if(numColumns != other.numColumns) return false;
		if(!Arrays.deepEquals(elements, other.elements)) return false;
		return true;
	}
	
	@Override
	public String toString() {
		return numRows + "x" + numColumns + " Matrix: " +
				Arrays.deepToString(elements);
	}
}
