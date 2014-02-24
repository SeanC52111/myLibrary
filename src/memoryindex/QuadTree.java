/**
 * 
 */
package memoryindex;

import io.IO;
import io.RW;

import java.util.ArrayList;

import math.MathPoint;
import spatialindex.IShape;
import spatialindex.Point;
import spatialindex.Region;

/**
 * 
 * This implementation is according to http://en.wikipedia.org/wiki/Quadtree.
 * @author chenqian
 *
 */
public class QuadTree {

	private static long			G_ID		= 0;
	private long				id 			= -1;
	private int 				capacity 	= 4;
	private Region 				boundary 	= null;
	private ArrayList<Point> 	points		= null;
	private ArrayList<RW>		values 		= null;
	private QuadTree[] 			chTree 		= null;
	private int					dim			= 4;
	private int 				cnt			= 0;
	
	/**
	 * 
	 */
	public QuadTree(int capacity, Region boundary) {
		// TODO Auto-generated constructor stub
		this.id			= G_ID ++;
		this.capacity 	= capacity;
		this.boundary 	= boundary;
		this.points 	= new ArrayList<Point>();
		this.values 	= new ArrayList<RW>();
	}
	
	/**
	 * Get tree id.
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Insert a point also record the path.
	 * @param p
	 * @param value
	 * @param path
	 * @return
	 */
	public boolean insert(Point p, RW value, ArrayList<QuadTree> path) {
		if (!boundary.contains(p)) {
			return false;
		}		
		cnt ++;
		if (path != null) {
			path.add(this);
		}
		if (points != null && points.size() <= capacity) {			
			points.add(p);
			values.add(value);
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
				for (int j = 0; j < points.size(); j ++) {
					boolean found = false;
					for (int i = 0; i < dim; i ++) {
						if (chTree[i].insert(points.get(j), values.get(j), path)) {
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
				if (chTree[i].insert(p, value, path)) {
					found = true;
					break;
				}
			}
			return found;
		}
	}
	
	/**
	 * Insert a point without path.
	 * @param p
	 * @param value
	 * @return
	 */
	public boolean insert(Point p, RW value) {
		return insert(p, value, null);
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
		MathPoint epsPoint = MathPoint.getEpsPoint((int) boundary.getDimension());
		for (int i = 0; i < tot; i ++) {
			MathPoint mpS = new MathPoint(mpC);
			for (int j = 0; j < mpL.getDim(); j ++) {
				if (((i >> j) & 1) == 1) {
					mpS.setCoord(j, 0);
				}
			}
			MathPoint l = mpL.add(mpS).minus(epsPoint);
			MathPoint h = l.add(mpC).minus(epsPoint);
			regions[i] = new Region(l.getCoords(), h.getCoords());
		}
		return regions;
	}
	
	public boolean remove(Point p, ArrayList<QuadTree> path) {
		if (!boundary.contains(p)) {
			return false;
		}
		
		cnt --;
		if (path != null) {
			path.add(this);
		}
		if (points != null) {
			for (int i = 0; i < points.size(); i ++) {
				if (points.get(i).equals(p)) {
					points.remove(i);
					values.remove(i);
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
				if (chTree[i].remove(p, path)) {
					suc = true;
					break;
				}
			}
			
			if (cnt <= capacity / 2) {
				points = new ArrayList<Point>();
				values = new ArrayList<RW>();
				for (int i = 0; i < dim; i ++) {
					points.addAll(chTree[i].points);
					values.addAll(chTree[i].values);
					chTree[i].clear();
					chTree[i] = null;
				}
				chTree = null;
			}
			return suc;
		}
	}
	
	/**
	 * Implemented myself.
	 * @param p
	 * @param id
	 * @return
	 */
	public boolean remove(Point p) {
		return remove(p, null);
	}
	
	/**
	 * Clear the tree;
	 */
	public void clear() {
		id = -1;
		boundary = null;
		clearPoints();
		if (chTree != null) {
			for (int i = 0; i < dim; i ++) {
				chTree[i].clear();
				chTree[i] = null;
			}
			chTree = null;
		}
	}
	
	/**
	 * Clear the points and values.
	 * 
	 * */
	public void clearPoints() {
		points = null;
		values = null;
	}
	
	public static void queryStrategy(QuadTree tree, final IQueryStrategyQT qs) {
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

	public int getDim() {
		return dim;
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
		sb.append("@" + id + "\n");
		sb.append(indent); 
		sb.append(boundary + "\n");
		if (points != null) {
			sb.append(indent + "[");
			for (int i = 0; i < points.size(); i ++) {
				if (i != 0) sb.append(", ");
				sb.append(points.get(i));
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
	
	public QuadTree[] getChTrees() {
		return chTree;
	}
	
	public void setChTrees(QuadTree[] chTree) {
		this.chTree = chTree;
	}
	
	public ArrayList<RW> getValues() {
		return values;
	}
	
	public RW getValue(int i) {
		if (i >= values.size()) {
			throw new IllegalStateException("The i is out of array.");
		}
		return values.get(i);
	}
	
	public void setValues(ArrayList<RW> values) {
		this.values = values;
	}
	
	public void setValue(int i, RW value) {
		this.values.set(i, value);
	}
	
	public int getCnt() {
		return cnt;
	}
	
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
