/**
 * 
 */
package io;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import io.RW;

/**
 * @author chenqian
 *
 */
public class Data implements RW {

	String data;
	
	public Data() {}
	
	public Data(String data) {
		this.data = data;
	}

	public void write(DataOutputStream ds) {
		// TODO Auto-generated method stub
//		System.out.println("write call me at Data");
		
	}
	
	public byte[] toBytes() {
		return data.getBytes();
	}
	
	public String toString() {
//		System.out.println("Call me at Data");
		return data;
	}

	@Override
	public void read(DataInputStream ds) {
		// TODO Auto-generated method stub
		
	}
	
}