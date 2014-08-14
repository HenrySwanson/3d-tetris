package graphics;

import geom.Point;

import java.util.Comparator;

//must exist because cube faces are drawn in order of proximity
public class CubeCompare implements Comparator<CubeFace> {
	
	private Point viewpoint;
	
	public CubeCompare(Point point) {
		viewpoint = point;
	}
	
	@Override
	public int compare(CubeFace o1, CubeFace o2) {
		double distance1 = viewpoint.getDistance(o1.getSquareCenter());
		double otherDistance = viewpoint.getDistance(o2.getSquareCenter());
		if(distance1 < otherDistance) return 1; // farthest are fewest for
												// sorting
		if(distance1 > otherDistance) return -1;
		return 0;
	}
	
}