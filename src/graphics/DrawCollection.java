package graphics;

import spatialindex.*;
import spatialindex.Point;

import java.awt.*;
import java.util.ArrayList;


public class DrawCollection {
    /**
     *
     */
    public static boolean isInit = false;
    //	public static double sx = Double.MAX_VALUE, sy = Double.MAX_VALUE, mx = Double.MIN_VALUE, my = Double.MIN_VALUE;
    public static Region  bounds = null;
    ArrayList<Data> datas = new ArrayList<Data>();

    public DrawCollection() {
    }

    public DrawCollection(Data[] datas) {
        for (Data data : datas) {
            DrawCollection.updateMargin(data.getShape());
            this.datas.add(new Data(DrawCollection.parseShape(data.getShape()), data.getColor(), data.getDrawType()));
        }
        isInit = true;
    }

    public DrawCollection(ArrayList<Data> datas) {
        this(datas.toArray(new Data[0]));
    }
	
//	public DrawCollection(Point[] points, Color color) {
//		for (int i = 0; i < points.length; i ++) {
//			this.drawCollection.add((IShape) points[i]);
//		}
//		this.color = color;
//		setPointType();
//	}
	
//	public DrawCollection(ArrayList<IShape> points, Color color) {
//		this.drawCollection = points;
//		this.color = color;
//		setPointType();
//	}
	
	public static void updateMargin(IShape shape) {
        Region tmpRegion = null;
        if (shape instanceof spatialindex.Point) {
            tmpRegion = new Region((spatialindex.Point)shape, (spatialindex.Point)shape);
        } else if (shape instanceof Region) {
            tmpRegion = (Region) shape;
        }
        if (bounds == null) {
            bounds = tmpRegion;
        } else {
            bounds = bounds.combinedRegion(tmpRegion);
        }
        isInit = true;
	}

//
//	public boolean checkMargin() {
//		if (sx > mx || sy > my) return false;
//		return true;
//	}

    private static IShape parsePoint(double[] coords) {
        if (isInit == false) {
            throw new IllegalStateException("The margins may be not initializedl.");
        }
        int x = (int)(((coords[0] - bounds.getLow(0)) / (bounds.getHigh(0) - bounds.getLow(0))) * ShowData.DEFAULT_WIDTH);
        int y = (int)(((coords[1] - bounds.getLow(1)) / (bounds.getHigh(1) - bounds.getLow(1))) * ShowData.DEFAULT_HEIGHT);
        return new Point(new double[]{x, y});
    }

	public static IShape parseShape(IShape shape) {
        if (shape instanceof Point) {
            return parsePoint(((Point) shape).m_pCoords);
        } else {
            double[] low = ((Region)shape).m_pLow;
            double[] high = ((Region)shape).m_pHigh;
            return new Region((Point)parsePoint(low), (Point)parsePoint(high));
        }
	}

    public Class getType() {
        return datas.get(0).getClass();
    }

    public int getSize() {
        return datas.size();
    }

    public Data getData(int i) {
        return datas.get(i);
    }
}