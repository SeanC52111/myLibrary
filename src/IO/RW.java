/**
 * 
 */
package IO;

import java.io.DataOutputStream;

/**
 * @author chenqian
 *
 */
public abstract class RW extends Object{

	public RW() {}
	public abstract void read(byte[] data);
	public abstract void write(DataOutputStream ds);
	public abstract byte[] toBytes();
	public abstract String toString();
}
