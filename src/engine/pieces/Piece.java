package engine.pieces;

import engine.IntPoint;

//subclasses will specialize rotation. not enough time for now though (except square)
public interface Piece {

	public static final int NOTHING = 0;
	public static final int RED = 1;
	public static final int ORANGE = 2;
	public static final int YELLOW = 3;
	public static final int GREEN = 4;
	public static final int CYAN = 5;
	public static final int BLUE = 6;
	public static final int PURPLE = 7;
	public static final int PINK = 8;
	public static final int WHITE = 9;

	public int getColor(); // should be unique for every piece

	public IntPoint[] getBlocks();

	public void moveXPlus();

	public void moveXMinus();

	public void moveYPlus();

	public void moveYMinus();

	public void moveZPlus();

	public void moveZMinus();

	public void rotateXPlus();

	public void rotateXMinus();

	public void rotateYPlus();

	public void rotateYMinus();

	public void rotateZPlus();

	public void rotateZMinus();
}
