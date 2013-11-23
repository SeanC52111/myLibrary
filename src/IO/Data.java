/**
 * 
 */
package IO;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * @author chenqian
 *
 */
public class Data extends RW {

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

	@Override
	public void loadBytes(byte[] data) {
		// TODO Auto-generated method stub
		this.data = new String(data);
	}
	
}