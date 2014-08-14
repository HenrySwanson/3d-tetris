package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import engine.Chamber;
import engine.CheatCodes;
import engine.IntPoint;
import engine.pieces.Piece;
import geom.Point;

@SuppressWarnings("serial")
public class ViewingCanvas extends JPanel {
	
	private OptionPanel optionPanel;
	private Music theme = new Music();
	
	private static final Color gridColor = new Color(128, 128, 128, 64);
	private static final Color pauseColor = new Color(192, 192, 192, 192);
	
	private boolean paused = false;
	
	private Chamber chamber = null;
	private int length, width, height;
	
	private ViewMath vm;
	
	private List<CubeFace> faces = new ArrayList<CubeFace>();
	
	public ViewingCanvas() {
		setPreferredSize(new Dimension(500, 500));
		newGame();
	}
	
	private void generateCubeFaces() {
		faces.clear();
		for(int x = 0; x < length; x++) {
			for(int y = 0; y < width; y++) {
				for(int z = 0; z < height; z++) {
					addCubeFace(new IntPoint(x, y, z), IntPoint.UP);
					addCubeFace(new IntPoint(x, y, z), IntPoint.FRONT);
					addCubeFace(new IntPoint(x, y, z), IntPoint.RIGHT);
				}
			}
		}
		for(int x = 0; x < length; x++) {
			for(int y = 0; y < width; y++) {
				addCubeFace(new IntPoint(x, y, 0), IntPoint.DOWN);
			}
		}
		for(int y = 0; y < width; y++) {
			for(int z = 0; z < height; z++) {
				addCubeFace(new IntPoint(0, y, z), IntPoint.LEFT);
			}
		}
		for(int z = 0; z < height; z++) {
			for(int x = 0; x < length; x++) {
				addCubeFace(new IntPoint(x, 0, z), IntPoint.BACK);
			}
		}
		Collections.sort(faces, new CubeCompare(vm.getViewPoint()));
	}
	
	private void addCubeFace(IntPoint p1, IntPoint shift) {
		IntPoint p2 = p1.add(shift);
		int value1, value2, orientation;
		
		if(shift.equals(IntPoint.UP) || shift.equals(IntPoint.DOWN)) {
			orientation = CubeFace.Z_AXIS;
		} else if(shift.equals(IntPoint.LEFT) || shift.equals(IntPoint.RIGHT)) {
			orientation = CubeFace.X_AXIS;
		} else {
			orientation = CubeFace.Y_AXIS;
		}
		
		if(p1.inBounds(length, width, height)) {
			value1 = chamber.getCubes()[p1.getX()][p1.getY()][p1.getZ()];
		} else {
			value1 = Piece.NOTHING;
		}
		if(p2.inBounds(length, width, height)) {
			value2 = chamber.getCubes()[p2.getX()][p2.getY()][p2.getZ()];
		} else {
			value2 = Piece.NOTHING;
		}
		
		if(value1 == Piece.NOTHING) {
			faces.add(new CubeFace(p1.midpoint(p2), orientation, value2));
		} else if(value2 == Piece.NOTHING) {
			faces.add(new CubeFace(p1.midpoint(p2), orientation, value1));
		}
	}
	
	private Path2D getOuterShape(CubeFace c) {
		Point[] corners3D = c.getCorners();
		Point2D[] corners2D = new Point2D[4];
		for(int i = 0; i < corners3D.length; i++) {
			corners2D[i] = vm.getProjectedPoint(corners3D[i]);
		}
		Path2D.Float path = new Path2D.Float();
		path.moveTo(corners2D[0].getX(), corners2D[0].getY());
		path.lineTo(corners2D[1].getX(), corners2D[1].getY());
		path.lineTo(corners2D[2].getX(), corners2D[2].getY());
		path.lineTo(corners2D[3].getX(), corners2D[3].getY());
		path.closePath();
		return path;
	}
	
	private Path2D getInnerShape(CubeFace c) {
		Point[] corners3D = c.getInnerCorners();
		Point2D[] corners2D = new Point2D[4];
		for(int i = 0; i < corners3D.length; i++) {
			corners2D[i] = vm.getProjectedPoint(corners3D[i]);
		}
		Path2D.Float path = new Path2D.Float();
		path.moveTo(corners2D[0].getX(), corners2D[0].getY());
		path.lineTo(corners2D[1].getX(), corners2D[1].getY());
		path.lineTo(corners2D[2].getX(), corners2D[2].getY());
		path.lineTo(corners2D[3].getX(), corners2D[3].getY());
		path.closePath();
		return path;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform savedTransform = g2d.getTransform();
		
		float scale = Math.min(getWidth(), getHeight());
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		g2d.translate(getWidth() / 2, getHeight() / 2);
		g2d.scale(scale, scale);
		
		paintCubes(g2d, scale);
		g2d.setTransform(savedTransform);
		
		if(paused) {
			g2d.setColor(pauseColor);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	private void paintCubes(Graphics2D g2d, float scale) {
		for(CubeFace c : faces) {
			if(c.getColor().getAlpha() == 0) {
				drawEmptyFace(g2d, scale, c);
			} else {
				drawFilledFace(g2d, scale, c);
			}
		}
	}
	
	private void drawEmptyFace(Graphics2D g2d, float scale, CubeFace c) {
		g2d.setStroke(new BasicStroke(1 / scale)); // so lines are 1 thick
		Shape s = getOuterShape(c);
		g2d.setColor(gridColor);
		g2d.draw(s);
	}
	
	private void drawFilledFace(Graphics2D g2d, float scale, CubeFace c) {
		g2d.setStroke(new BasicStroke(3 / scale)); // so lines are 2 thick
		Shape s = getOuterShape(c);
		g2d.setColor(Color.BLACK);
		g2d.draw(s);
		g2d.setColor(c.getColor());
		g2d.fill(s);
		g2d.setColor(c.getLighterColor());
		g2d.fill(getInnerShape(c));
	}
	
	public void update() {
		generateCubeFaces();
		repaint();
	}
	
	public void keyPressed(KeyEvent e) {
		if(chamber.isToppedOut()) return;
		boolean updated = false;
		boolean shift = e.isShiftDown();
		switch(e.getKeyCode()) {
			case KeyEvent.VK_UP:
				if(paused && !CheatCodes.GRAYVIEW) return;
				updated = vm.moveUp();
				break;
			case KeyEvent.VK_DOWN:
				if(paused && !CheatCodes.GRAYVIEW) return;
				updated = vm.moveDown();
				break;
			case KeyEvent.VK_LEFT:
				if(paused && !CheatCodes.GRAYVIEW) return;
				updated = true;
				vm.moveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				if(paused && !CheatCodes.GRAYVIEW) return;
				updated = true;
				vm.moveRight();
				break;
			case KeyEvent.VK_SPACE:
				if(paused) return;
				if(!chamber.pieceActive()) break;
				updated = true;
				softDrop();
				break;
			case KeyEvent.VK_ENTER:
				if(paused) return;
				if(!chamber.pieceActive()) break;
				updated = true;
				hardDrop();
				break;
			default:
				if(paused) return;
				updated = shift ? rotateEvent(e.getKeyCode()) : moveEvent(e
						.getKeyCode());
				break;
		}
		if(updated) update();
	}
	
	public boolean moveEvent(int keycode) {
		if(CheatCodes.QEUPDOWN && keycode == KeyEvent.VK_Q)
			return chamber.moveZPlus();
		if(CheatCodes.QEUPDOWN && keycode == KeyEvent.VK_E)
			return chamber.moveZMinus();
		
		double theta = vm.getTheta(); // to orient motion
		double fourthPi = Math.PI / 4; // for precomputing
		switch(keycode) {
			case KeyEvent.VK_W:
				if(theta > 3 * fourthPi) return chamber.moveXPlus();
				if(theta > fourthPi) return chamber.moveYMinus();
				if(theta > -fourthPi) return chamber.moveXMinus();
				if(theta > -3 * fourthPi) return chamber.moveYPlus();
				return chamber.moveXPlus(); // to cover last 8th
			case KeyEvent.VK_A:
				if(theta > 3 * fourthPi) return chamber.moveYPlus();
				if(theta > fourthPi) return chamber.moveXPlus();
				if(theta > -fourthPi) return chamber.moveYMinus();
				if(theta > -3 * fourthPi) return chamber.moveXMinus();
				return chamber.moveYPlus(); // to cover last 8th
			case KeyEvent.VK_S:
				if(theta > 3 * fourthPi) return chamber.moveXMinus();
				if(theta > fourthPi) return chamber.moveYPlus();
				if(theta > -fourthPi) return chamber.moveXPlus();
				if(theta > -3 * fourthPi) return chamber.moveYMinus();
				return chamber.moveXMinus(); // to cover last 8th
			case KeyEvent.VK_D:
				if(theta > 3 * fourthPi) return chamber.moveYMinus();
				if(theta > fourthPi) return chamber.moveXMinus();
				if(theta > -fourthPi) return chamber.moveYPlus();
				if(theta > -3 * fourthPi) return chamber.moveXPlus();
				return chamber.moveYMinus(); // to cover last 8th
		}
		return false;
	}
	
	public boolean rotateEvent(int keycode) {
		if(keycode == KeyEvent.VK_A) return chamber.rotateZMinus();
		if(keycode == KeyEvent.VK_D) return chamber.rotateZPlus();
		
		double theta = vm.getTheta(); // to orient motion
		double fourthPi = Math.PI / 4; // for precomputing
		switch(keycode) {
			case KeyEvent.VK_W:
				if(theta > 3 * fourthPi) return chamber.rotateYPlus();
				if(theta > fourthPi) return chamber.rotateXPlus();
				if(theta > -fourthPi) return chamber.rotateYMinus();
				if(theta > -3 * fourthPi) return chamber.rotateXMinus();
				return chamber.rotateYPlus(); // to cover last 8th
			case KeyEvent.VK_Q:
				if(theta > 3 * fourthPi) return chamber.rotateXMinus();
				if(theta > fourthPi) return chamber.rotateYPlus();
				if(theta > -fourthPi) return chamber.rotateXPlus();
				if(theta > -3 * fourthPi) return chamber.rotateYMinus();
				return chamber.rotateXMinus(); // to cover last 8th
			case KeyEvent.VK_S:
				if(theta > 3 * fourthPi) return chamber.rotateYMinus();
				if(theta > fourthPi) return chamber.rotateXMinus();
				if(theta > -fourthPi) return chamber.rotateYPlus();
				if(theta > -3 * fourthPi) return chamber.rotateXPlus();
				return chamber.rotateYMinus(); // to cover last 8th
			case KeyEvent.VK_E:
				if(theta > 3 * fourthPi) return chamber.rotateXPlus();
				if(theta > fourthPi) return chamber.rotateYMinus();
				if(theta > -fourthPi) return chamber.rotateXMinus();
				if(theta > -3 * fourthPi) return chamber.rotateYPlus();
				return chamber.rotateXPlus(); // to cover last 8th
		}
		return false;
	}
	
	public void softDrop() {
		if(paused) return;
		boolean dropped = chamber.moveZMinus();
		if(!dropped && !CheatCodes.LOCKENTER) {
			chamber.lockPiece();
			chamber.nextPiece();
			optionPanel.update();
		}
		update();
	}
	
	public void hardDrop() {
		if(paused) return;
		while(chamber.moveZMinus()) {
			;
		} // empty on purpose
		chamber.lockPiece();
		chamber.nextPiece();
		optionPanel.update();
		update();
	}
	
	public void togglePause() {
		if(paused)
			unpause();
		else
			pause();
	}
	
	public void pause() {
		paused = true;
		theme.pause();
		repaint();
	}
	
	public void unpause() {
		paused = false;
		theme.unpause();
		repaint();
	}
	
	public void newGame() {
		chamber = new Chamber(6, 6, 12);
		length = chamber.getLength();
		width = chamber.getWidth();
		height = chamber.getHeight();
		vm = new ViewMath(chamber);
		generateCubeFaces();
		theme.restart();
	}
	
	public Piece getNextPiece() {
		return chamber.getNextPiece();
	}
	
	public void setOptionPanel(OptionPanel op) {
		optionPanel = op;
	}
	
	public long getScore() {
		return chamber.getScore();
	}
}