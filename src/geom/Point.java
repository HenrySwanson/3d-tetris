package geom;


import java.util.Arrays;

/**
 * Represents a point in an arbitrary number of dimensions.
 * 
 * @author Henry Swanson
 * @version 1.0 May 17, 2012
 */
public class Point {
	
	/** The number of dimensions the point exists in */
	private int dimension;
	/** The components of the point */
	private float[] components;
	
	/**
	 * Constructs a point at the origin in the given number of dimensions.
	 * 
	 * @param dimension The dimensionality of the point
	 */
	public Point(int dimension) {
		this.dimension = dimension;
		components = new float[dimension];
	}
	
	/**
	 * Constructs a point at the given location.
	 * 
	 * @param components The components of the point
	 */
	public Point(float... components) {
		this(components.length);
		for(int i = 0; i < components.length; i++)
			this.components[i] = components[i];
		if(dimension!=4) {
			Thread.dumpStack();
			System.exit(1);
		}
	}
	
	/**
	 * Constructs a copy of the given point.
	 * 
	 * @param p The point to copy
	 */
	public Point(Point p) {
		this(p.components);
		dimension = p.dimension;
	}
	
	/**
	 * Returns the dimension of the point.
	 * 
	 * @return The dimension of the point
	 */
	public int getDimension() {
		return dimension;
	}
	
	/**
	 * Returns the component of the point along the given axis.
	 * 
	 * @return The component of the point along the given axis
	 */
	public float get(int axis) {
		return components[axis];
	}
	
	/**
	 * Sets the component along a given axis to the given value.
	 * 
	 * @param value The value of the component
	 * @param axis The axis to modify
	 */
	public void setComponent(float value, int axis) {
		components[axis] = value;
	}
	
	/**
	 * Computes the square of the Euclidean distance between this and the
	 * specified point.
	 * 
	 * @param p The point to which distance is measured
	 * @return The square of the distance between this and the specified point
	 */
	public double getDistanceSq(Point p) {
		if(dimension != p.dimension)
			throw new IllegalArgumentException("Points do not have same size: " + dimension + ", " + p.dimension);
		float sum = 0;
		for(int i = 0; i < dimension; i++) {
			float diff = components[i] - p.components[i];
			sum += diff * diff;
		}
		return sum;
	}
	
	/**
	 * Computes the Euclidean distance between this and the specified point.
	 * 
	 * @param p The point to which distance is measured
	 * @return The distance between this and the specified point
	 */
	public float getDistance(Point p) {
		return (float) Math.sqrt(getDistanceSq(p));
	}
	
	/**
	 * Computes the non-homogeneous form of the point. The result will be one
	 * dimension fewer than this.
	 * 
	 * @return The non-homogeneous form of the point
	 */
	public Point unhomogenize() {
		if(dimension == 0)
			throw new IllegalStateException("Cannot reduce dimension below 0");
		Point image = new Point(dimension - 1);
		for(int i = 0; i < image.dimension; i++)
			image.setComponent(components[i] / components[dimension - 1], i);
		return image;
	}
	
	/**
	 * Sums this and the given point as if they were vectors.
	 * @param p The point to be added
	 * @return The sum of the two points
	 */
	public Point add(Point p) {
		if(dimension != p.dimension)
			throw new IllegalArgumentException("Points do not have same size: " + dimension + ", " + p.dimension);
		Point sum = new Point(dimension);
		float thisLast = components[dimension - 1];
		float pLast = p.components[p.dimension - 1];
		for(int i = 0; i < dimension - 1; i++)
			sum.components[i] = components[i] + p.components[i] * thisLast / pLast; //corrects for differences in last component
		sum.components[dimension - 1] = thisLast; //sets last component to correct value
		return sum;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(components);
		result = prime * result + dimension;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		Point other = (Point) obj;
		if(!Arrays.equals(components, other.components)) return false;
		if(dimension != other.dimension) return false;
		return true;
	}
	
	@Override
	public String toString() {
		String arrayStr = Arrays.toString(components);
		return "(" + arrayStr.substring(1, arrayStr.length() - 1) + ")";
	}
}