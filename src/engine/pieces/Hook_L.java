package engine.pieces;

import engine.IntPoint;

public class Hook_L implements Piece {

	private IntPoint center = null;
	private IntPoint[] others = new IntPoint[3]; //offsets from center

	public Hook_L(IntPoint start) {
		center = start;
		others[0] = new IntPoint(-1, 1, 0);
		others[1] = new IntPoint(-1, 0, 0);
		others[2] = new IntPoint(0, 0, 1);
	}

	@Override
	public int getColor() {
		return Piece.PINK;
	}

	@Override
	public IntPoint[] getBlocks() {
		IntPoint[] point3ds = new IntPoint[4];
		point3ds[0] = center;
		for(int i = 0; i < 3; i++)
			point3ds[i+1] = center.add(others[i]);
		return point3ds;
	}

	@Override
	public void moveXPlus() {
		center = center.add(IntPoint.RIGHT);
	}

	@Override
	public void moveXMinus() {
		center = center.add(IntPoint.LEFT);
	}

	@Override
	public void moveYPlus() {
		center = center.add(IntPoint.FRONT);
	}

	@Override
	public void moveYMinus() {
		center = center.add(IntPoint.BACK);
	}

	@Override
	public void moveZPlus() {
		center = center.add(IntPoint.UP);
	}

	@Override
	public void moveZMinus() {
		center = center.add(IntPoint.DOWN);
	}

	@Override
	public void rotateXPlus() {
		for(int i = 0; i < others.length; i++) {
			IntPoint p = others[i];
			others[i] = new IntPoint(p.getX(), -p.getZ(), p.getY());
		}
	}

	@Override
	public void rotateXMinus() {
		for(int i = 0; i < others.length; i++) {
			IntPoint p = others[i];
			others[i] = new IntPoint(p.getX(), p.getZ(), -p.getY());
		}
	}

	@Override
	public void rotateYPlus() {
		for(int i = 0; i < others.length; i++) {
			IntPoint p = others[i];
			others[i] = new IntPoint(p.getZ(), p.getY(), -p.getX());
		}
	}

	@Override
	public void rotateYMinus() {
		for(int i = 0; i < others.length; i++) {
			IntPoint p = others[i];
			others[i] = new IntPoint(-p.getZ(), p.getY(), p.getX());
		}
	}

	@Override
	public void rotateZPlus() {
		for(int i = 0; i < others.length; i++) {
			IntPoint p = others[i];
			others[i] = new IntPoint(-p.getY(), p.getX(), p.getZ());
		}
	}

	@Override
	public void rotateZMinus() {
		for(int i = 0; i < others.length; i++) {
			IntPoint p = others[i];
			others[i] = new IntPoint(p.getY(), -p.getX(), p.getZ());
		}
	}

}
