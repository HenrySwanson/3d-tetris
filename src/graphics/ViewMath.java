package graphics;

import java.awt.geom.Point2D;

import engine.Chamber;
import geom.Matrix;
import geom.Point;

public class ViewMath {
	
	public static final float MIN_PHI = (float) (Math.PI / 100);
	public static final float MAX_PHI = (float) (Math.PI / 2 + 0.1);
	
	public static final float ROTATE = 0.1f;
	
	public static final Matrix BASE_CAMERA = new Matrix(3, 4, 1, 0, 0, 0, 0, 1,
			0, 0, 0, 0, 1, 0);
	
	private Matrix LEFT;
	private Matrix RIGHT;
	private float theta, phi; // polar coordinates
			
	private Matrix transform;
	
	private Matrix transToOrigin;
	private Matrix transToCenter;
	
	public ViewMath(Chamber chamber) {
		int length = chamber.getLength();
		int width = chamber.getWidth();
		int height = chamber.getHeight();
		float diagonal = (float) Math.sqrt(length * length + width * width +
				height * height);
		
		phi = (float) (Math.PI / 2);
		theta = (float) (-Math.PI / 2);
		
		transToOrigin = Matrix.getTranslation(-length / 2.0f + 0.5f,
				-width / 2.0f + 0.5f, -height / 2.0f + 0.5f);
		transToCenter = Matrix.getTranslation(length / 2.0f - 0.5f,
				width / 2.0f - 0.5f, height / 2.0f - 0.5f);
		Matrix rotateTheta = Matrix.getZRotation(ROTATE);
		
		LEFT = transToCenter.multiply(rotateTheta).multiply(transToOrigin);
		RIGHT = transToCenter.multiply(rotateTheta.transpose()).multiply(
				transToOrigin);
		
		transform = Matrix.getXRotation((float) (Math.PI / 2));
		transform = transform.multiply(Matrix.getTranslation(
				-length / 2.0f + 0.5f, diagonal, -height / 2.0f + 0.5f));
	}
	
	public Point2D getProjectedPoint(Point p) {
		Point image = BASE_CAMERA.multiply(transform).multiply(p)
				.unhomogenize();
		return new Point2D.Float(image.get(0), image.get(1));
	}
	
	public Point getViewPoint() {
		return transform.inverse().multiply(new Point(0, 0, 0, 1));
	}
	
	public double getTheta() {
		return theta;
	}
	
	public boolean moveUp() {
		phi -= ROTATE;
		if(phi < MIN_PHI) {
			phi = MIN_PHI;
			return false;
		}
		transform = transform.multiply(transToCenter)
				.multiply(Matrix.getZRotation(theta))
				.multiply(Matrix.getYRotation(ROTATE))
				.multiply(Matrix.getZRotation(-theta)).multiply(transToOrigin);
		return true;
	}
	
	public boolean moveDown() {
		phi += ROTATE;
		if(phi > MAX_PHI) {
			phi = MAX_PHI;
			return false;
		}
		transform = transform.multiply(transToCenter)
				.multiply(Matrix.getZRotation(theta))
				.multiply(Matrix.getYRotation(-ROTATE))
				.multiply(Matrix.getZRotation(-theta)).multiply(transToOrigin);
		return true;
	}

	public void moveLeft() {
		theta -= ROTATE;
		if(theta < -Math.PI) theta += Math.PI * 2; // keeps it between pi and
													// -pi
		transform = transform.multiply(LEFT);
	}
	
	public void moveRight() {
		theta += ROTATE;
		if(theta > Math.PI) theta -= Math.PI * 2; // keeps it between pi and -pi
		transform = transform.multiply(RIGHT);
	}
}
