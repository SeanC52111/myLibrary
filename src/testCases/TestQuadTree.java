package testCases;

import java.util.ArrayList;

import memoryindex.IQueryStrategyQT;
import memoryindex.IVisitorQT;
import memoryindex.QuadEntry;
import memoryindex.QuadTree;

import org.junit.BeforeClass;
import org.junit.Test;

import spatialindex.Point;
import spatialindex.Region;

public class TestQuadTree {

	static ArrayList<QuadEntry> entries = new ArrayList<QuadEntry>();
	static ArrayList<QuadEntry> entriesMD = new ArrayList<QuadEntry>();
	static int dim = 6;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		int num = 16;
		for (int i = 0; i < num; i ++) {
			entries.add(new QuadEntry(i, new Point(new double[]{i, i}), null));
		}
		for (int i = 0; i < num; i ++) {
			double[] corrds = new double[dim];
			for (int j = 0; j < dim; ++j) {
				corrds[j] = i;
			}	
			Point point = new Point(corrds);
			entriesMD.add(new QuadEntry(i, point, null));
		}
	}

	@Test
	public void testInsertWithPath() {
		System.out.println("-----insert path-----");
		QuadTree root = new QuadTree(2, 4, new Region(new double[] {0, 0}, new double[] {16, 16}));
		for (int i = 0; i < entries.size(); i ++) {
			ArrayList<QuadTree> path = new ArrayList<QuadTree>();
			if (!root.insert(entries.get(i), path)) {
				System.out.println(i + " err!");
			} else {
				System.out.println(i + ": " + path.size());
//				for (int j = 0; j < path.size(); j ++) {					
//					System.out.println(path.get(j));
//				}
			}
		}
		System.out.println("---------------------");
//		System.out.println(root);
	}
	
	@Test
	public void testRegionSubdivision() {
		double[] lCoords = new double[]{1, 2}, hCoords = new double[]{3, 4};
		Region region = new Region(lCoords, hCoords);
		Region[] regions = QuadTree.subDivide(region);
//		for (int i = 0; i < 4; i ++) {
//			System.out.println(regions[i]);
//		}
	}
	
//	@Test
//	public void testRegionSubdivisionMD() {
//		double[] lCoords = new double[]{0, 0, 0, 0, 0}, hCoords = new double[]{16, 16, 16, 16, 16};
//		Region region = new Region(lCoords, hCoords);
//		Region[] regions = QuadTree.subDivide(region);
//		for (int i = 0; i < 1 << 5; i ++) {
//			System.out.println(regions[i]);
//		}
//	}
	
	@Test
	public void testInsert() {
		QuadTree root = new QuadTree(2, 4, new Region(new double[] {0, 0}, new double[] {16, 16}));
		for (int i = 0; i < entries.size(); i ++) {			
			if (!root.insert(entries.get(i))) {
				System.out.println(i + " err!");
			} else {
//				System.out.println(i + " inserted!");
			}
		}
//		System.out.println(root);
	}
	
	@Test
	public void testRemove() {
		QuadTree root = new QuadTree(2, 4, new Region(new double[] {0, 0}, new double[] {16, 16}));
		for (int i = 0; i < entries.size(); i ++) {			
			if (!root.insert(entries.get(i))) {
				System.out.println(i + " err!");
			}
		}
		root.remove(entries.get(6));
		root.remove(entries.get(5));
		root.remove(entries.get(4));
		root.remove(entries.get(3));
		root.remove(entries.get(2));
		root.remove(entries.get(1));
//		root.insert(2, points.get(2));
//		System.out.println(root);
	}
	
	@Test 
	public void testRange() {
		System.out.println("-----test range-----");
		QuadTree root = new QuadTree(2, 4, new Region(new double[] {0, 0}, new double[] {16, 16}));
		for (int i = 0; i < entries.size(); i ++) {			
			if (!root.insert(entries.get(i))) {
				System.out.println(i + " err!");
			} else {
//				System.out.println(i + " inserted!");
			}
		}
		Region query = new Region((Point)entries.get(2).getShape(), (Point)entries.get(5).getShape());
		RangeQueryStrategy qs = new RangeQueryStrategy(query);
		QuadTree.queryStrategy(root, qs);
		ArrayList<Integer> res = qs.getResult();
		System.out.println("res in range:");
		for (int i = 0; i < res.size(); i ++) {
			System.out.println(res.get(i));
		}
		RQVisitor visitor = new RQVisitor();
		root.rangeQuery(query, visitor);
		System.out.println("-------------------");
	}
	
	@Test 
	public void testRangeMD() {
		System.out.println("-----test rangeMD-----");
		QuadTree root = new QuadTree(dim, 4, new Region(new double[] {0, 0, 0, 0, 0, 0}, new double[] {16, 16, 16, 16, 16, 16}));
		for (int i = 0; i < entriesMD.size(); i ++) {			
			if (!root.insert(entriesMD.get(i))) {
				System.out.println(i + " err!");
			} else {
//				System.out.println(i + " inserted!");
			}
		}
		Region query = new Region((Point)entriesMD.get(2).getShape(), (Point)entriesMD.get(5).getShape());
		RangeQueryStrategy qs = new RangeQueryStrategy(query);
		QuadTree.queryStrategy(root, qs);
		ArrayList<Integer> res = qs.getResult();
		System.out.println("res in range:");
		for (int i = 0; i < res.size(); i ++) {
			System.out.println(res.get(i));
		}
		RQVisitor visitor = new RQVisitor();
		root.rangeQuery(query, visitor);
		System.out.println("-------------------");
	}
	
	@Test
	public void testKNN() {
		QuadTree root = new QuadTree(2, 4, new Region(new double[] {0, 0}, new double[] {16, 16}));
		for (int i = 0; i < entries.size(); i ++) {			
			if (!root.insert(entries.get(i))) {
				System.out.println(i + " err!");
			} else {
//				System.out.println(i + " inserted!");
			}
		}
		Point query = new Point(new double[] {8, 8});
		root.nearestNeighborQuery(3, query, new NNVisitor());
		System.out.println("----");
		root.nearestNeighborQuery(5, query, new NNVisitor());
	}
	
	@Test
	public void testKNNMD() {
		QuadTree root = new QuadTree(dim, 1<<dim, new Region(new double[] {0, 0, 0, 0, 0, 0}, new double[] {16, 16, 16, 16, 16, 16}));
		for (int i = 0; i < entriesMD.size(); i ++) {			
			if (!root.insert(entriesMD.get(i))) {
				System.out.println(i + " err!");
			} else {
//				System.out.println(i + " inserted!");
			}
		}
		Point query = new Point(new double[] {8, 8, 8, 8, 8, 8});
		root.nearestNeighborQuery(3, query, new NNVisitor());
		System.out.println("----");
		root.nearestNeighborQuery(5, query, new NNVisitor());
	}
	
	@Test
	public void testCount() {
		QuadTree root = new QuadTree(2, 4, new Region(new double[] {0, 0}, new double[] {16, 16}));
		for (int i = 0; i < entries.size(); i ++) {			
			if (!root.insert(entries.get(i))) {
				System.out.println(i + " err!");
			} else {
//				System.out.println(i + " inserted!");
			}
		}
		System.out.println(root.checkCount());
	}
	
	@Test
	public void testCountMD() {
		System.out.println("--------test count------");
		QuadTree root = new QuadTree(dim, 1<<dim, new Region(new double[] {0, 0, 0, 0, 0, 0}, new double[] {16, 16, 16, 16, 16, 16}));
		for (int i = 0; i < entriesMD.size(); i ++) {			
			if (!root.insert(entriesMD.get(i))) {
				System.out.println(i + " err!");
			} else {
//				System.out.println(i + " inserted!");
			}
		}
		System.out.println(root.checkCount());
		System.out.println("--------------------------");
	}
	
	
	class NNVisitor implements IVisitorQT {

		@Override
		public void visitEntry(QuadEntry entry) {
			System.out.println(entry.getShape());
		}
		
	}
	
	class RQVisitor implements IVisitorQT {

		@Override
		public void visitEntry(QuadEntry entry) {
			System.out.println(entry.getId() + " : " + entry.getShape());
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
		
			ArrayList<QuadEntry> entries = n.getEntries();
			if (entries != null) {
				for (int i = 0; i < entries.size(); i ++) {
					QuadEntry entry = entries.get(i);
					if (query.contains(entry.getShape())) {
						results.add(entry.getId());
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
