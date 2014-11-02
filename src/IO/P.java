/**
 * 
 */
package io;

import java.util.ArrayList;
import java.util.List;


/**
 * @author chenqian
 *
 */
public class P {

	public static int lineSeq = 50;
	
	public static void print(int[] data) {
		if (data == null) {
			System.out.println("null");
			return;
		}
		for (int i = 0; i < data.length; i ++) {
			if (i != 0) System.out.print(", ");
			if ((i + 1) % lineSeq == 0) System.out.println();
			System.out.print(data[i]);
		}
		System.out.println();
	}
	
	public static void println(int[] data) {
		if (data == null) {
			System.out.println("null");
			return;
		}
		for (int i = 0; i < data.length; i ++) {
			System.out.println(data[i]);
		}
	}
	
	public static void print(String[] data) {
		if (data == null) {
			System.out.println("null");
			return;
		}
		for (int i = 0; i < data.length; i ++) {
			if (i != 0) System.out.print(", ");
			if ((i + 1) % lineSeq == 0) System.out.println();
			System.out.print(data[i]);
		}
		System.out.println();
	}
	
	public static void println(String[] data) {
		if (data == null) {
			System.out.println("null");
			return;
		}
		for (int i = 0; i < data.length; i ++) {
			System.out.println(data[i]);
		}
	}
	
	public static void print(List<String> data) {
		int i = 0;
		for (Object d : data) {
			if (i != 0) System.out.print(", ");
			if ((i + 1) % lineSeq == 0) System.out.println();
			System.out.print(d);
			i++;
		}
		System.out.println();
	}
	
	public static void println(List<Object> data) {
		for (Object d : data) {
			System.out.println(d);
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
		ArrayList<String> d = new ArrayList<String>();
		d.add("hello");
		d.add("world");
	}

}
