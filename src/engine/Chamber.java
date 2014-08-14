package engine;

import java.util.Random;

import engine.pieces.*;

public class Chamber { // contains all the pieces

	// so pieces don't get stuck at spawn.
	// invisible to other classes
	public static final int SAFE_HEIGHT = 4;
	
	private long score = 0;
	
	private int length;
	private int width;
	private int height;

	private int[][][] cubes; // standard r-hand coordinate system
	private Piece fallingPiece = null;
	private Piece nextPiece = null;

	private IntPoint start;
	private boolean toppedOut = false;
	
	private Random rand = new Random();
	
	public Chamber(int length, int width, int height) {
		this.length = length;
		this.width = width;
		this.height = height + SAFE_HEIGHT;
		cubes = new int[this.length][this.width][this.height];
		start = new IntPoint(length / 2, width / 2, this.height - 2);
		fallingPiece = getRandomPiece();
		nextPiece = getRandomPiece();
	}

	public int getLength() {
		return length;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height - SAFE_HEIGHT;
	}

	public boolean isToppedOut() {
		return toppedOut;
	}

	public int[][][] getCubes() {
		int[][][] copy = new int[length][width][height - SAFE_HEIGHT];
		for(int x = 0; x < length; x++) {
			for(int y = 0; y < width; y++) {
				for(int z = 0; z < height - SAFE_HEIGHT; z++) {
					copy[x][y][z] = cubes[x][y][z];
				}
			}
		}
		if(fallingPiece != null) {
			for(int i = 0; i < 4; i++) {
				IntPoint p = fallingPiece.getBlocks()[i];
				if(p.getZ() < height - SAFE_HEIGHT) {
					copy[p.getX()][p.getY()][p.getZ()] = fallingPiece
							.getColor();
				}
			}
		}
		return copy;
	}

	public boolean moveXPlus() {
		fallingPiece.moveXPlus();
		if(isObstructed())
			return true;
		fallingPiece.moveXMinus();
		return false;
	}

	public boolean moveXMinus() {
		fallingPiece.moveXMinus();
		if(isObstructed())
			return true;
		fallingPiece.moveXPlus();
		return false;
	}

	public boolean moveYPlus() {
		fallingPiece.moveYPlus();
		if(isObstructed())
			return true;
		fallingPiece.moveYMinus();
		return false;
	}

	public boolean moveYMinus() {
		fallingPiece.moveYMinus();
		if(isObstructed())
			return true;
		fallingPiece.moveYPlus();
		return false;
	}

	public boolean moveZPlus() {
		fallingPiece.moveZPlus();
		if(isObstructed())
			return true;
		fallingPiece.moveZMinus();
		return false;
	}

	public boolean moveZMinus() {
		fallingPiece.moveZMinus();
		if(isObstructed())
			return true;
		fallingPiece.moveZPlus();
		return false;
	}

	public boolean rotateXPlus() {
		fallingPiece.rotateXPlus();
		if(isObstructed())
			return true;
		fallingPiece.rotateXMinus();
		return false;
	}

	public boolean rotateXMinus() {
		fallingPiece.rotateXMinus();
		if(isObstructed())
			return true;
		fallingPiece.rotateXPlus();
		return false;
	}

	public boolean rotateYPlus() {
		fallingPiece.rotateYPlus();
		if(isObstructed())
			return true;
		fallingPiece.rotateYMinus();
		return false;
	}

	public boolean rotateYMinus() {
		fallingPiece.rotateYMinus();
		if(isObstructed())
			return true;
		fallingPiece.rotateYPlus();
		return false;
	}

	public boolean rotateZPlus() {
		fallingPiece.rotateZPlus();
		if(isObstructed())
			return true;
		fallingPiece.rotateZMinus();
		return false;
	}

	public boolean rotateZMinus() {
		fallingPiece.rotateZMinus();
		if(isObstructed())
			return true;
		fallingPiece.rotateZPlus();
		return false;
	}

	public void lockPiece() {
		IntPoint[] blocks = fallingPiece.getBlocks();
		for(int i = 0; i < 4; i++) {
			IntPoint p = blocks[i];
			if(!p.inBounds(length, width, height))
				continue;
			cubes[p.getX()][p.getY()][p.getZ()] = fallingPiece.getColor();
		}
		fallingPiece = null;
		checkAllRows();
		checkTopOut();
	}

	public void nextPiece() {
		fallingPiece = nextPiece;
		nextPiece = getRandomPiece();
	}

	public boolean pieceActive() {
		if(fallingPiece == null)
			return false;
		for(int i = 0; i < 4; i++) {
			IntPoint p = fallingPiece.getBlocks()[i];
			if(p.getZ() < height - SAFE_HEIGHT)
				return true;
		}
		return false;
	}
	
	public Piece getNextPiece() {
		return nextPiece;
	}
	
	private void checkTopOut() {
		for(int x = 0; x < length; x++) {
			for(int y = 0; y < width; y++) {
				if(cubes[x][y][height - SAFE_HEIGHT] != Piece.NOTHING) {
					toppedOut = true;
					System.out.println("Topped out");
				}
			}
		}
	}

	private boolean isObstructed() {
		IntPoint[] blocks = fallingPiece.getBlocks();
		for(int i = 0; i < 4; i++) {
			IntPoint p = blocks[i];
			if(!p.inBounds(length, width, height))
				return false;
			if(cubes[p.getX()][p.getY()][p.getZ()] != Piece.NOTHING) {
				return false;
			}
		}
		return true;
	}
		
	private Piece getRandomPiece() {
		switch(rand.nextInt(8)) {
		case 0:
			return new Line(start);
		case 1:
			return new L(start);
		case 2:
			return new Squiggly(start);
		case 3:
			return new Square(start);
		case 4:
			return new T(start);
		case 5:
			return new Hook_L(start);
		case 6:
			return new Hook_R(start);
		case 7:
			return new Corner(start);
		default:
			return null;
		}
	}

	private boolean checkAllRows() {
		boolean changed = false;
		int numberChanged = 0;
		//goes down so that rows aren't skipped
		for(int z = height - SAFE_HEIGHT; z >= 0; z--) {
			if(checkSingleRow(z)) {
				changed = true;
				numberChanged++;
				for(int i = z + 1; i < height; i++) {
					// z + 1 so that the deleted plane is overwritten first
					lowerPlane(i);
				}
			}
		}
		score += 100 * numberChanged * numberChanged;
		return changed;
	}

	private boolean checkSingleRow(int z) {
		for(int x = 0; x < length; x++) {
			for(int y = 0; y < width; y++) {
				if(cubes[x][y][z] == Piece.NOTHING)
					return false;
			}
		}
		return true;
	}

	private void lowerPlane(int z) {
		for(int x = 0; x < length; x++) {
			for(int y = 0; y < length; y++) {
				cubes[x][y][z - 1] = cubes[x][y][z];
			}
		}
	}
	
	public long getScore() {
		return score;
	}
}
