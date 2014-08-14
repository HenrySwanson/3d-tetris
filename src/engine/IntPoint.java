package engine;

import geom.Point;

public class IntPoint { // point, but can be used as vector

	public static final IntPoint LEFT = new IntPoint(-1, 0, 0);
	public static final IntPoint RIGHT = new IntPoint(1, 0, 0);
	public static final IntPoint FRONT = new IntPoint(0, 1, 0);
	public static final IntPoint BACK = new IntPoint(0, -1, 0);
	public static final IntPoint UP = new IntPoint(0, 0, 1);
	public static final IntPoint DOWN = new IntPoint(0, 0, -1);
	public static final IntPoint ZERO = new IntPoint(0, 0, 0);

	private int x = 0;
	private int y = 0;
	private int z = 0;

	public IntPoint(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public IntPoint(IntPoint p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public IntPoint add(IntPoint p) {
		return new IntPoint(x + p.x, y + p.y, z + p.z);
	}

	public IntPoint subtract(IntPoint p) {
		return new IntPoint(x - p.x, y - p.y, z - p.z);
	}

	public Point midpoint(IntPoint p) {
		return new Point((x + p.x) / 2.0f, (y + p.y) / 2.0f,
				(z + p.z) / 2.0f, 1);
	}

	public boolean inBounds(int length, int width, int height) {
		return 0 <= x && x < length && 0 <= y && y < width && 0 <= z
				&& z < height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		IntPoint other = (IntPoint) obj;
		if(x != other.x)
			return false;
		if(y != other.y)
			return false;
		if(z != other.z)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IntPoint [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
