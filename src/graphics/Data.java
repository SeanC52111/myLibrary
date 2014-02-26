package graphics;

import spatialindex.*;
import spatialindex.Point;

import java.awt.*;

/**
 * Created by chenqian on 26/2/14.
 */
public class Data {

    public static enum DrawType {Point, Region, Line};

    IShape shape;
    Color  color;
    DrawType drawType;



    public Data(IShape shape, Color color, DrawType drawType) {
        this.shape = shape;
        this.color = color;
        this.drawType = drawType;
    }

    public IShape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public Point getPointL() {
        return new Point(((Region)shape).m_pLow);
    }

    public Point getPointH() {
        return new Point(((Region)shape).m_pHigh);
    }

    public DrawType getDrawType() {
        return drawType;
    }

    public void setDrawType(DrawType drawType) {
        this.drawType = drawType;
    }

}
