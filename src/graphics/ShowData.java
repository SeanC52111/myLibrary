package graphics;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.MathUtility;


/**
 * 
 */

/**
 * @author chenqian
 *
 *	This file is for showing data visually. 
 *	Function loadMap() loads the map of nodes and roads.
 *	Function loadData() is to load trajectory.
 *	The format for the trajectory is (timestamp, vid, lat, lng)
 *	Function loadRsu() is to load Rsus as the name shows.
 *	The format of the data refers to the one in Converter.java.
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
		
		Data data = new Data(new double[][]{
				{0, 0}, 
				{0, 1},
				{1, 0},
				{1, 1},
				{0, 0},
				{0.5, 0.5},
				{0.8, 0.2}
				}, Color.BLACK);
		data.setLineType();
		ShowData showData = new ShowData(new Data[]{data});
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
	
	public ShowData(Data[] datas){
		setSize(DEFAULT_WIDTH + 8, DEFAULT_HEIGHT + 30);
		setLocation(DEFAULT_L_X, DEFAULT_L_Y);
		add(new DataPanel(datas));
	}
	
	class DataPanel extends JPanel{
		ArrayList<Data> datas = new ArrayList<Data>();
		DataPanel(Data[] datas){
			for (Data data : datas) {
				this.datas.add(data);
			}
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			for (Data data : datas) {
				 g.setColor(data.color);
				 if (data.isPointType()) {
					 for (int j = 0; j < data.points.size(); j ++) {
						 Point p = data.points.get(j);
						g.drawRect(p.x, p.y, 5, 5); 
					 }
				 }
				 if (data.isLineType()) {
					 for (int j = 0; j < data.points.size() - 1; j ++) {
						 Point p = data.points.get(j);
						 Point q = data.points.get(j + 1);
						 g.drawLine(p.x, p.y, q.x, q.y);
					 }
				 }
			}
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		}
	}
}
