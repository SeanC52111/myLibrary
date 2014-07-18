/**
 * 
 */
package memoryindex;

import io.IO;

import java.util.ArrayList;
import java.util.PriorityQueue;

import math.MathPoint;
import spatialindex.IShape;
import spatialindex.Region;

/**
 * 
 * This implementation is according to http://en.wikipedia.org/wiki/Quadtree.
 * @author chenqian
 *
 */
public class QuadTree {

	private static long				G_ID		= 0;
	private long					id			= -1;
	private int						capacity	= 4;
	private Region					boundary	= null;
	private ArrayList<QuadEntry>	entries		= null;
	private QuadTree[]				chTree		= null;
	private int						dim			= 4;
	private int						cnt			= 0;
	
	/**
	 * 
	 */
	public QuadTree(int capacity, Region boundary) {
		// TODO Auto-generated constructor stub
		this.id = G_ID++;
		this.capacity = capacity;
		this.boundary = boundary;
		this.entries = new ArrayList<QuadEntry>();
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
	public boolean insert(QuadEntry entry, ArrayList<QuadTree> path) {
		if (!boundary.contains(entry.getShape())) {
			return false;
		}		
		cnt ++;
		if (path != null) {
			path.add(this);
		}
		if (entries != null && entries.size() <= capacity) {			
			entries.add(entry);
			return true;
		} else {			
			if (chTree == null) {
				chTree = new QuadTree[dim];
				Region[] regions = subDivide(boundary);
				for (int i = 0; i < dim; i ++) {
					chTree[i] = new QuadTree(capacity, regions[i]);
				}
			}
			if (entries != null) {
				for (int j = 0; j < entries.size(); j ++) {
					boolean found = false;
					for (int i = 0; i < dim; i ++) {
						if (chTree[i].insert(entries.get(j), path)) {
							found = true;
							break;
						}
					}
					if (!found) return false;
				}
				this.clearEntries();
			} 
			boolean found = false;
			for (int i = 0; i < dim; i ++) {
				if (chTree[i].insert(entry, path)) {
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
	public boolean insert(QuadEntry entry) {
		return insert(entry, null);
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
	
	public boolean remove(QuadEntry entry, ArrayList<QuadTree> path) {
		if (!boundary.contains(entry.getShape())) {
			return false;
		}
		
		cnt --;
		if (path != null) {
			path.add(this);
		}
		if (entries != null) {
			for (int i = 0; i < entries.size(); i ++) {
				if (entries.get(i).getShape().equals(entry.getShape())) {
					entries.remove(i);
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
				if (chTree[i].remove(entry, path)) {
					suc = true;
					break;
				}
			}
			
			if (cnt <= capacity / 2) {
				entries = new ArrayList<QuadEntry>();
				for (int i = 0; i < dim; i ++) {
					entries.addAll(chTree[i].entries);
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
	public boolean remove(QuadEntry entry) {
		return remove(entry, null);
	}
	
	/**
	 * Clear the tree;
	 */
	public void clear() {
		id = -1;
		boundary = null;
		clearEntries();
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
	public void clearEntries() {
		entries = null;
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
	
	public ArrayList<QuadEntry> getEntries() {
		return entries;
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
		if (entries != null) {
			sb.append(indent + "[");
			for (int i = 0; i < entries.size(); i ++) {
				if (i != 0) sb.append(", ");
				sb.append(entries.get(i).getShape());
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
	
	public QuadTree getChTree(int i) {
		return chTree[i];
	}
	
	public QuadTree[] getChTrees() {
		return chTree;
	}
	
	public void setChTrees(QuadTree[] chTree) {
		this.chTree = chTree;
	}
	
	public QuadEntry getEntry(int i) {
		if (i >= entries.size()) {
			throw new IllegalStateException("The i is out of array.");
		}
		return entries.get(i);
	}
	
	public void setEntries(ArrayList<QuadEntry> entries) {
		this.entries = entries;
	}
	
	public void setEntry(int i, QuadEntry entry) {
		this.entries.set(i, entry);
	}
	
	public int getCnt() {
		return cnt;
	}
	
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	
	public void nearestNeighborQuery(int k, final IShape query, final IVisitorQT v) {
		PriorityQueue<NNEntry> queue = new PriorityQueue<NNEntry>();
		queue.add(new NNEntry(this, query));
		
		int count = 0;
		double knearest = 0.0;
		
		while (!queue.isEmpty()) {
			NNEntry first = queue.poll();
			
			if (first.tree != null) {
				QuadTree tree = first.tree;
				if (tree.chTree == null) { // leaf
					for (int i = 0; i < tree.entries.size(); ++i) {
						queue.add(new NNEntry(tree.entries.get(i), query));
					}
				} else {
					for (int i = 0; i < tree.chTree.length; ++i) {
						queue.add(new NNEntry(tree.chTree[i], query));
					}
				}
			} else {
				if (count >= k && first.minDist > knearest) break;
				
				v.visitEntry(first.entry);
				count++;
				knearest = first.minDist;
			}
		}
	}
	
	class NNEntry implements Comparable<NNEntry>{
		QuadTree tree = null;
		QuadEntry entry = null;
		double minDist = 0;
		
		public NNEntry(QuadTree _tree, IShape query) {
			tree = _tree;
			minDist = query.getMinimumDistance(tree.getBoundary());
		}
		
		public NNEntry(QuadEntry _entry, IShape query) {
			entry = _entry;
			minDist = query.getMinimumDistance(entry.getShape());
		}

		@Override
		public int compareTo(NNEntry o) {
			if (minDist < o.minDist) return -1;
			else if (minDist > o.minDist) return 1;
			else return 0;
		}
	}
	
	public boolean checkCount() {
		if (chTree == null) return true;
		else {
			int tmp = 0;
			for (int i = 0; i < getDim(); ++i) {
				if (!chTree[i].checkCount()) {
					System.err.println(chTree[i]);
					return false;
				}
				tmp += chTree[i].getCnt();
			}
			if (tmp != getCnt()) {
				System.err.println(this);
				return false;
			}
			return true;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
