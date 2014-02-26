package graphics;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import spatialindex.IShape;
import spatialindex.Region;


/**
 * 
 */

/**
 * @author chenqian
 *
 *	Refer the example in main.
 *
 */
public class ShowData extends JFrame{

	
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 800;
	public static final int DEFAULT_L_X = 150;
	public static final int DEFAULT_L_Y = 150;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		DrawCollection data = new DrawCollection(new Data[]{
                new Data((IShape)new spatialindex.Point(new double[] {0, 1}), Color.BLACK, Data.DrawType.Point),
                new Data((IShape)new spatialindex.Point(new double[] {0, 0}), Color.BLACK, Data.DrawType.Line),
                new Data((IShape)new spatialindex.Point(new double[] {1, 0}), Color.BLACK, Data.DrawType.Line),
                new Data((IShape)new spatialindex.Point(new double[] {1, 1}), Color.BLACK, Data.DrawType.Line),
                new Data((IShape)new spatialindex.Point(new double[] {0.5, 0.5}), Color.BLACK, Data.DrawType.Line),
                new Data((IShape)new spatialindex.Point(new double[] {0.8, 0.2}), Color.BLACK, Data.DrawType.Line),
                new Data((IShape)new Region(new spatialindex.Point(new double[] {0.2, 0.2}),
                        new spatialindex.Point(new double[] {0.3, 0.3})), Color.RED, Data.DrawType.Region),
                new Data((IShape)new Region(new spatialindex.Point(new double[] {0.5, 0.6}),
                        new spatialindex.Point(new double[] {0.6, 0.7})), Color.RED, Data.DrawType.Region),
				});
        ShowData showData = new ShowData(data);
		ShowData.draw(showData);
	}
	
	public static void draw(final ShowData showData) {
		try{
			EventQueue.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					showData.setVisible(true);
					showData.setResizable(true);
					showData.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ShowData(DrawCollection drawCollection){
		setSize(DEFAULT_WIDTH + 8, DEFAULT_HEIGHT + 30);
		setLocation(DEFAULT_L_X, DEFAULT_L_Y);
		add(new DataPanel(drawCollection));
	}
	
	class DataPanel extends JPanel{
        DrawCollection drawCollection;

        DataPanel(DrawCollection drawCollection) {
            this.drawCollection = drawCollection;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int j = 0; j < drawCollection.getSize(); j ++) {
                Data data = drawCollection.getData(j);
                g.setColor(data.getColor());
                if (data.getDrawType() == Data.DrawType.Point) {
                    spatialindex.Point p = (spatialindex.Point) data.getShape();
                    g.drawRect((int) p.getCoord(0), (int) p.getCoord(1), 5, 5);
                } else if (data.getDrawType() == Data.DrawType.Line) {
                    spatialindex.Point p = (spatialindex.Point) data.getShape();
                    Data dataq = drawCollection.getData(j - 1);
                    spatialindex.Point q = (spatialindex.Point) dataq.getShape();
                    g.drawLine((int) p.getCoord(0), (int) p.getCoord(1), (int) q.getCoord(0), (int) q.getCoord(1));
                } else if (data.getDrawType() == Data.DrawType.Region) {
                    Region region = (Region) data.getShape();
                    g.drawRect((int) region.getLow(0), (int) region.getLow(1),
                            (int) (region.getHigh(0) - region.getLow(0)), (int) (region.getHigh(1) - region.getLow(1)));
                } else {
                    System.out.println("this type is not existed");
                }
            }
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
    }
}
