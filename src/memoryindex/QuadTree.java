/**
 * 
 */
package memoryindex;

import io.IO;

import java.util.ArrayList;

import math.MathPoint;
import spatialindex.Point;
import spatialindex.Region;

/**
 * 
 * This implementation is according to http://en.wikipedia.org/wiki/Quadtree.
 * @author chenqian
 *
 */
public class QuadTree {

	private int 				capacity 	= 4;
	private Region 				boundary 	= null;
	private ArrayList<Point> 	points		= null;
	private ArrayList<Integer>	ids			= null;
	private QuadTree[] 			chTree 		= null;
	private int					dim			= 4;
	private int 				cnt			= 0;
	
	/**
	 * 
	 */
	public QuadTree(int capacity, Region boundary) {
		// TODO Auto-generated constructor stub
		this.capacity 	= capacity;
		this.boundary 	= boundary;
		this.points 	= new ArrayList<Point>();
		this.ids 		= new ArrayList<Integer>();
	}
	
	public boolean insert(int id, Point p) {
		if (!boundary.contains(p)) {
			return false;
		}		
		cnt ++;
		if (points != null && points.size() <= capacity) {			
			points.add(p);
			ids.add(id);
			return true;
		} else {			
			if (chTree == null) {
				chTree = new QuadTree[dim];
				Region[] regions = subDivide(boundary);
				for (int i = 0; i < dim; i ++) {
					chTree[i] = new QuadTree(capacity, regions[i]);
				}
			}
			if (points != null) {
				for (int j = 0; j < ids.size(); j ++) {
					boolean found = false;
					for (int i = 0; i < dim; i ++) {
						if (chTree[i].insert(ids.get(j), points.get(j))) {
							found = true;
							break;
						}
					}
					if (!found) return false;
				}
				this.clearPoints();
			} 
			boolean found = false;
			for (int i = 0; i < dim; i ++) {
				if (chTree[i].insert(id, p)) {
					found = true;
					break;
				}
			}
			return found;
		}
	}
	
	public static Region[] subDivide(Region boundary) {
		
		// Get the lower point
		double[] lCoords = new double[(int) boundary.getDimension()];
		for (int i = 0; i < lCoords.length; i ++) {
			lCoords[i] = boundary.getLow(i);
		}
		MathPoint mpL = new MathPoint(lCoords);
		
		// Get the vector
		MathPoint mpC = new MathPoint(boundary.getCenter()).minus(mpL);
		
		
		int tot = 1 << mpL.getDim();
		Region[] regions = new Region[tot];
		for (int i = 0; i < tot; i ++) {
			MathPoint mpS = new MathPoint(mpC);
			for (int j = 0; j < mpL.getDim(); j ++) {
				if (((i >> j) & 1) == 1) {
					mpS.setCoord(j, 0);
				}
			}
			MathPoint l = mpL.add(mpS);
			MathPoint h = l.add(mpC);
			regions[i] = new Region(l.getCoords(), h.getCoords());
		}
		return regions;
	}
	
	/**
	 * Implemented myself.
	 * @param p
	 * @param id
	 * @return
	 */
	public boolean remove(int id, Point p) {
		if (!boundary.contains(p)) {
			return false;
		}
		
		cnt --;
		if (points != null) {
			for (int i = 0; i < points.size(); i ++) {
				if (ids.get(i) == id) {
					points.remove(i);
					ids.remove(i);
					return true;
				}
			}
			return false;
		} else {
			if (chTree == null) {
				return false;
			}
			
			boolean suc = false;
			for (int i = 0; i < dim; i ++) {
				if (chTree[i].remove(id, p)) {
					suc = true;
					break;
				}
			}
			
			if (cnt <= capacity / 2) {
				points = new ArrayList<Point>();
				ids = new ArrayList<Integer>();
				for (int i = 0; i < dim; i ++) {
					points.addAll(chTree[i].points);
					ids.addAll(chTree[i].ids);
					chTree[i].clearPoints();
					chTree[i] = null;
				}
				chTree = null;
			}
			
			return suc;
		}
	}
	
	public void clearPoints() {
		points = null;
		ids = null;
	}
	
	public void queryStrategy(QuadTree tree, final IQueryStrategy qs) {
		QuadTree[] next = new QuadTree[]{tree};
		while (true) {
			QuadTree n = next[0];
			boolean[] hasNext = new boolean[] {false};
			qs.getNextEntry(n, next, hasNext);
			if (hasNext[0] == false) break;
		}
	}
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Region getBoundary() {
		return boundary;
	}

	public void setBoundary(Region boundary) {
		this.boundary = boundary;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}	

	public String toString() {
		return toString(0);
	}
	
	public String toString(int level) {
		StringBuffer sb = new StringBuffer();
		StringBuffer indent = IO.getIndent(level);
		sb.append(indent + "-------------------\n");
		sb.append(indent); 
		sb.append(boundary + "\n");
		if (ids != null) {
			sb.append(indent + "[");
			for (int i = 0; i < ids.size(); i ++) {
				if (i != 0) sb.append(", ");
				sb.append(ids.get(i));
			}
			sb.append("]\n");
		}
//		sb.append(indent + "--\n");
		sb.append(indent + "chTree:");
		if (chTree == null) {
			sb.append(" null\n");
		} else {
			sb.append("\n");
			for (int i = 0; i < dim; i ++) {
				if (chTree[i] != null) {
					sb.append(chTree[i].toString(level + 1));
				} else {
					sb.append(IO.getIndent(level + 1));
					sb.append("null");
				}
				sb.append("\n");
			}
		}
//		sb.append(indent + "--\n");
		sb.append(indent);
		sb.append("cnt: " + cnt);
		return sb.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
