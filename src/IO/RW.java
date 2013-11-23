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
public abstract class RW extends Object{

	public RW() {}
	public abstract void read(DataInputStream ds);
	public abstract void write(DataOutputStream ds);
	public abstract void loadBytes(byte[] data);
	public abstract byte[] toBytes();
	public abstract String toString();
}
