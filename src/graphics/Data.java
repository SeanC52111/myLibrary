package graphics;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;


public class Data {
	/**
	 * 
	 */
	public static boolean isInit = false; 
	public static double sx = Double.MAX_VALUE, sy = Double.MAX_VALUE, mx = Double.MIN_VALUE, my = Double.MIN_VALUE;
	ArrayList<Point> points = new ArrayList<Point>();
	Color color = null;
	int drawType = 0;

	public Data() {
	}
	
	public Data(double[][] points, Color color) {
		for (double[] point : points) {
			Data.updateMargin(point[0], point[1]);
		}
		for (double[] point : points) {
			addPoint(Data.parseFromLatLng(point[0], point[1]));
		}
		this.color = color;
		isInit = true;
		setPointType();
	}
	
	public Data(spatialindex.Point[] points, Color color) {
		for (spatialindex.Point point : points) {
			Data.updateMargin(point.getCoord(0), point.getCoord(1));
		}
		for (spatialindex.Point point : points) {
			addPoint(Data.parseFromLatLng(point.getCoord(0), point.getCoord(1)));
		}
		this.color = color;
		isInit = true;
		setPointType();
	}
	
	public Data(Point[] points, Color color) {
		for (int i = 0; i < points.length; i ++) {
			this.points.add(points[i]);
		}
		this.color = color;
		setPointType();
	}
	
	public Data(ArrayList<Point> points, Color color) {
		this.points = points;
		this.color = color;
		setPointType();
	}
	
	public static void updateMargin(double lat, double lng) {
		if (lat > mx) mx = lat;
		if (lat < sx) sx = lat;
		if (lng > my) my = lng;
		if (lng < sy) sy = lng;
		isInit = true;
	}
	
	public boolean checkMargin() {
		if (sx > mx || sy > my) return false;
		return true;
	}
	
	public void addPoint(Point p) {
		points.add(p);
	}
	
	public static Point parseFromLatLng(double lat, double lng) {
		if (isInit == false) {
			throw new IllegalStateException("The margins may be not initializedl.");
		}
		int x = (int)(((lat - sx) / (mx - sx)) * ShowData.DEFAULT_WIDTH);
		int y = (int)(((lng - sy) / (my - sy)) * ShowData.DEFAULT_HEIGHT);
		return new Point(x, y);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isPointType() {
		return (drawType & 1) == 1 ? true : false;
	} 
	
	public void setPointType() {
		drawType |= 1;
	} 
	
	public boolean isLineType() {
		return ((drawType >> 1) & 1) == 1 ? true : false;
	}
	
	public void setLineType() {
		drawType |= (1 << 1);
	}
}