/**
 * 
 */
package IO;

/**
 * @author chenqian
 *
 */
public abstract class RW extends Object{

	public RW() {}
	public abstract void read(byte[] data);
	public abstract byte[] write();
	public abstract String toString();
}
