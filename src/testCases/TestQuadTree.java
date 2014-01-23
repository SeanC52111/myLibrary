package testCases;

import static org.junit.Assert.*;


import java.util.ArrayList;

import memoryindex.BinaryTree;
import memoryindex.IQueryStrategyQT;
import memoryindex.QuadTree;

import org.junit.BeforeClass;
import org.junit.Test;

import spatialindex.Point;
import spatialindex.Region;

public class TestQuadTree {

	static ArrayList<Point> points = new ArrayList<Point>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		int num = 16;
		for (int i = 0; i < num; i ++) {
			points.add(new Point(new double[]{i, i}));
		}
	}

	@Test
	public void testRegionSubdivision() {
		double[] lCoords = new double[]{1, 2}, hCoords = new double[]{3, 4};
		Region region = new Region(lCoords, hCoords);
		Region[] regions = QuadTree.subDivide(region);
		for (int i = 0; i < 4; i ++) {
			System.out.println(regions[i]);
		}
	}
	
	@Test
	public void testInsert() {
		QuadTree root = new QuadTree(4, new Region(new double[] {0, 0}, new double[] {16, 16}));
		for (int i = 0; i < points.size(); i ++) {			
			if (!root.insert(i, points.get(i))) {
				System.out.println(i + " err!");
			} else {
//				System.out.println(i + " inserted!");
			}
		}
//		System.out.println(root);
	}
	
	@Test
	public void testRemove() {
		QuadTree root = new QuadTree(4, new Region(new double[] {0, 0}, new double[] {16, 16}));
		for (int i = 0; i < points.size(); i ++) {			
			if (!root.insert(i, points.get(i))) {
				System.out.println(i + " err!");
			}
		}
		root.remove(6, points.get(6));
		root.remove(5, points.get(5));
		root.remove(4, points.get(4));
		root.remove(3, points.get(3));
		root.remove(2, points.get(2));
		root.remove(1, points.get(1));
//		root.insert(2, points.get(2));
//		System.out.println(root);
	}
	
	@Test 
	public void testRange() {
		QuadTree root = new QuadTree(4, new Region(new double[] {0, 0}, new double[] {16, 16}));
		for (int i = 0; i < points.size(); i ++) {			
			if (!root.insert(i, points.get(i))) {
				System.out.println(i + " err!");
			} else {
//				System.out.println(i + " inserted!");
			}
		}
		RangeQueryStrategy qs = new RangeQueryStrategy(new Region(points.get(2), points.get(5)));
		QuadTree.queryStrategy(root, qs);
		ArrayList<Integer> res = qs.getResult();
		for (int i = 0; i < res.size(); i ++) {
			System.out.println(res.get(i));
		}
	}
	
	class RangeQueryStrategy implements IQueryStrategyQT {

		private ArrayList<QuadTree> toVisit = new ArrayList<QuadTree>();
		private ArrayList<Integer> 	results = new ArrayList<Integer>();
		private Region query = null;
		
		
		public RangeQueryStrategy(Region query) {
			super();
			this.query = query;
		}

		public ArrayList<Integer> getResult() {
			return results;
		}

		@Override
		public void getNextEntry(QuadTree n, QuadTree[] next,
				boolean[] hasNext) {
			// TODO Auto-generated method stub
		
			ArrayList<Point> points = n.getPoints();
			if (points != null) {
				ArrayList<Integer> ids = n.getIds();
				for (int i = 0; i < points.size(); i ++) {
					Point p = points.get(i);
					if (query.contains(p)) {
						results.add(ids.get(i));
					}
				}
			} else {
				QuadTree[] chTrees = n.getChTrees();
				for (int i = 0; i < n.getDim(); i ++) {
					if (chTrees[i].getBoundary().intersects(query)) {
						toVisit.add(chTrees[i]);
					}
				}
			}
			
			if (!toVisit.isEmpty()) {
				next[0] = toVisit.remove(0);
				hasNext[0] = true;
			} else {
				hasNext[0] = false;
			}
		}
	}

}
