/**
 * 
 */
package IO;

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
	
	public void read(byte[] data) {
		// TODO Auto-generated method stub
//		System.out.println("read call me at Data");
		this.data = new String(data);
	}

	public byte[] write() {
		// TODO Auto-generated method stub
//		System.out.println("write call me at Data");
		return data.getBytes();
	}
	
	public String toString() {
//		System.out.println("Call me at Data");
		return data;
	}
	
}