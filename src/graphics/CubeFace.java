package graphics;

import java.awt.Color;

import engine.pieces.Piece;
import geom.Point;

public class CubeFace {
	
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public static final int Z_AXIS = 2;
		
	private static final float INNER_SIZE = 0.35f;
	
	private Point center = null;
	private int orientation = 0;
	private Color color;
	private Color lightColor;
	
	public CubeFace(Point p, int orientation, int c) {
		center = p;
		this.orientation = orientation;
		this.color = mapColor(c);
		float[] f = Color.RGBtoHSB(color.getRed(), color.getGreen(),
				color.getBlue(), null);
		lightColor = Color.getHSBColor(f[0], f[1] * 0.6f, f[2]);
	}
	
	private Color mapColor(int c) {
		switch(c) {
			case Piece.BLUE:
				return Color.BLUE;
			case Piece.CYAN:
				return Color.CYAN;
			case Piece.GREEN:
				return Color.GREEN;
			case Piece.ORANGE:
				return Color.ORANGE;
			case Piece.PINK:
				return Color.PINK;
			case Piece.PURPLE:
				return Color.MAGENTA;
			case Piece.RED:
				return Color.RED;
			case Piece.WHITE:
				return Color.WHITE;
			case Piece.YELLOW:
				return Color.YELLOW;
			case Piece.NOTHING:
				return new Color(0, 0, 0, 0);
		}
		return null;
	}
	
	public Point getSquareCenter() {
		return center;
	}
	
	public Point[] getCorners() {
		Point[] corners = new Point[4];
		switch(orientation) {
			case X_AXIS:
				corners[0] = center.add(new Point(0, 0.5f, 0.5f, 1));
				corners[1] = center.add(new Point(0, 0.5f, -0.5f, 1));
				corners[2] = center.add(new Point(0, -0.5f, -0.5f, 1));
				corners[3] = center.add(new Point(0, -0.5f, 0.5f, 1));
				return corners;
			case Y_AXIS:
				corners[0] = center.add(new Point(0.5f, 0, 0.5f, 1));
				corners[1] = center.add(new Point(0.5f, 0, -0.5f, 1));
				corners[2] = center.add(new Point(-0.5f, 0, -0.5f, 1));
				corners[3] = center.add(new Point(-0.5f, 0, 0.5f, 1));
				return corners;
			case Z_AXIS:
				corners[0] = center.add(new Point(0.5f, 0.5f, 0, 1));
				corners[1] = center.add(new Point(0.5f, -0.5f, 0, 1));
				corners[2] = center.add(new Point(-0.5f, -0.5f, 0, 1));
				corners[3] = center.add(new Point(-0.5f, 0.5f, 0, 1));
				return corners;
		}
		return null;
	}
	
	public Point[] getInnerCorners() {
		Point[] corners = new Point[4];
		switch(orientation) {
			case X_AXIS:
				corners[0] = center
						.add(new Point(0, INNER_SIZE, INNER_SIZE, 1));
				corners[1] = center.add(new Point(0, INNER_SIZE, -INNER_SIZE, 1));
				corners[2] = center.add(new Point(0, -INNER_SIZE, -INNER_SIZE, 1));
				corners[3] = center.add(new Point(0, -INNER_SIZE, INNER_SIZE, 1));
				return corners;
			case Y_AXIS:
				corners[0] = center.add(new Point(INNER_SIZE, 0, INNER_SIZE, 1));
				corners[1] = center.add(new Point(INNER_SIZE, 0, -INNER_SIZE, 1));
				corners[2] = center.add(new Point(-INNER_SIZE, 0, -INNER_SIZE, 1));
				corners[3] = center.add(new Point(-INNER_SIZE, 0, INNER_SIZE, 1));
				return corners;
			case Z_AXIS:
				corners[0] = center.add(new Point(INNER_SIZE, INNER_SIZE, 0, 1));
				corners[1] = center.add(new Point(INNER_SIZE, -INNER_SIZE, 0, 1));
				corners[2] = center.add(new Point(-INNER_SIZE, -INNER_SIZE, 0, 1));
				corners[3] = center.add(new Point(-INNER_SIZE, INNER_SIZE, 0, 1));
				return corners;
		}
		return null;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Color getLighterColor() {
		return lightColor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result +
				((lightColor == null) ? 0 : lightColor.hashCode());
		result = prime * result + orientation;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		CubeFace other = (CubeFace) obj;
		if(center == null) {
			if(other.center != null) return false;
		} else if(!center.equals(other.center)) return false;
		if(color == null) {
			if(other.color != null) return false;
		} else if(!color.equals(other.color)) return false;
		if(lightColor == null) {
			if(other.lightColor != null) return false;
		} else if(!lightColor.equals(other.lightColor)) return false;
		if(orientation != other.orientation) return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CubeFace [center=" + center + ", orientation=" + orientation +
				", color=" + color + ", lightColor=" + lightColor + "]";
	}
	
}
