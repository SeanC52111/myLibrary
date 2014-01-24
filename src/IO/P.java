/**
 * 
 */
package io;

import java.util.ArrayList;

/**
 * @author chenqian
 *
 */
public class P {

	public static int lineSeq = 50;
	
	public static void Print(ArrayList<Integer> data) {
		for (int i = 0; i < data.size(); i ++) {
			if (i != 0) System.out.print(", ");
			if ((i + 1) % lineSeq == 0) System.out.println();
			System.out.print(data.get(i));
		}
		System.out.println();
	}
	
	public static void Println(ArrayList<Integer> data) {
		for (int i = 0; i < data.size(); i ++) {
			System.out.println(data.get(i));
		}
	}
	/**
	 * 
	 */
	public P() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
